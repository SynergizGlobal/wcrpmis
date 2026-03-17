import React, { useState, useEffect } from "react";
import DmsTable from "../DmsTable/DmsTable";
import Drafts from "./Drafts";
import styles from "./DmsDocuments.module.css";
import api from "../../../../api/axiosInstance";
import AsyncSelect from "react-select/async";
import Select from "react-select";
import { API_BASE_URL } from "../../../../config";

const MAX_DEPTH = 10;

export default function DmsDocuments() {
  const [showUpload, setShowUpload] = useState(false);
  const [showDrafts, setShowDrafts] = useState(false);
  const [activeTab, setActiveTab] = useState("single");
  const [expandedFolders, setExpandedFolders] = useState(new Set());
  const [folderSearch, setFolderSearch] = useState("");
  const [renamingId, setRenamingId] = useState(null);
  const [renameValue, setRenameValue] = useState("");
  const [fileName, setFileName] = useState("");
  const [documents, setDocuments] = useState([]);
  const [loading, setLoading] = useState(false);
  const [selectedPath, setSelectedPath] = useState([]);
  const [newFolderName, setNewFolderName] = useState("");
  const [openMenu, setOpenMenu] = useState(null);
  const [selectedDoc, setSelectedDoc] = useState(null);
  const [showSendPopup, setShowSendPopup] = useState(false);
  const [showUpdatePopup, setShowUpdatePopup] = useState(false);
  const [showVersionsPopup, setShowVersionsPopup] = useState(false);
  const [pendingFolderId, setPendingFolderId] = useState(null);

    const [projects, setProjects] = useState([]);
    const [contracts, setContracts] = useState([]);
    const [departments, setDepartments] = useState([]);
    const [statuses, setStatuses] = useState([]);

    const [toUser, setToUser] = useState(null);
  // single upload section

    const [docForm, setDocForm] = useState({
      fileName: "",
      fileNumber: "",
      revisionNo: "R01",
      revisionDate: "",
      projectName: "",
      contractName: "",
      department: "",
      currentStatus: ""
    });

    const [sendForm, setSendForm] = useState({
      toUser: null,
      // sendTo: "",
      // sendToUserId: "",
      sendSubject: "",
      sendReason: "",
      responseExpected: "Yes",
      targetResponseDate: ""
    });

    const [documentFile, setDocumentFile] = useState(null);

  /* ---------- Folder Tree State ---------- */
  const [folders, setFolders] = useState([]);

    const toggleMenu = (e, id) => {
      e.stopPropagation();
      setOpenMenu(prev => (prev === id ? null : id));
    }

    const handleSend = (doc) => {
      console.log("CLICKED DOC:", doc);
      if (!doc?.id) {
        alert("Document ID missing. Cannot send.");
        console.error("INVALID DOC:", doc);
        return;
      }
      setSelectedDoc(doc);
      setSendForm({
        toUser: null,
        sendSubject: doc.subject || "",
        sendReason: "",
        responseExpected: "Yes",
        targetResponseDate: ""
      });
      setShowSendPopup(true);
      setOpenMenu(null);
    };

    const findFolderPathByNames = (nodes, names, index = 0, path = []) => {

    for (const node of nodes) {

      if (node.name === names[index]) {

        const newPath = [...path, node];

        if (index === names.length - 1) {
          return newPath;
        }

        if (node.children?.length) {

          const result = findFolderPathByNames(
            node.children,
            names,
            index + 1,
            newPath
          );

          if (result) return result;

        }

      }

    }

    return null;

  };

    const handleUpdate = (doc) => {

      console.log("DOCUMENT OBJECT:", doc);
      console.log("DOCUMENT PATH:", doc.Path);

      setSelectedDoc(doc);

      setDocForm({
        fileName: doc["File Name"] || "",
        fileNumber: doc["File Number"] || "",
        revisionNo: doc["Revision No"] || "",
        revisionDate: doc["Revision Date"]?.split("T")[0] || "",
        projectName: doc["Project Name"] || "",
        contractName: doc["Contract Name"] || "",
        department: doc["Department"] || "",
        currentStatus: doc["Status"] || "",
        updateReason: ""
      });

      if (doc.Path) {

        const names = doc.Path
          .split("/")
          .map(s => s.trim())
          .filter(Boolean);

        console.log("PATH NAMES:", names);

        const path = findFolderPathByNames(folders, names);

        console.log("FOUND PATH:", path);

        if (path) {

          setSelectedPath(path);

          setExpandedFolders(new Set(path.map(f => f.id)));

        }

      }

      setShowUpdatePopup(true);
    };

    const handleVersions = (doc) => {
      setSelectedDoc(doc);
      setShowVersionsPopup(true);
      setOpenMenu(null);
    };

    const handleNotRequired = async (doc) => {
      if (!window.confirm("Mark this document as Not Required?")) return;

      try {
        const payload = {
          documentId: doc.id,
          path: doc.Path
        };

        await api.post("/api/documents/not-required", payload);

        fetchDocuments();
      } catch (err) {
        console.error(err);
      }
    };

    const handleSaveDraft = async () => {
      try {

        const payload = {
          id: "",
          docId: selectedDoc.id,
          sendTo: sendForm.toUser?.email || "",
          sendToUserId: sendForm.toUser?.value || "",
          sendSubject: sendForm.sendSubject,
          sendReason: sendForm.sendReason,
          responseExpected: sendForm.responseExpected,
          targetResponseDate: sendForm.targetResponseDate,
          attachmentName: selectedDoc["File Name"],
          status: "Draft"
        };

        await api.post("/api/documents/send-document", payload);

        alert("Draft saved successfully");

        setShowSendPopup(false);

      } catch (err) {
        console.error(err);

        const msg = err?.response?.data?.message || err?.response?.data || "Failed to save draft";
        
        alert(msg);
      }
    };

    const handleSendDocument = async () => {

      if(!sendForm.toUser) {
        alert("Recipient is required");
        return;
      }
      if (!selectedDoc?.id) {
        alert("Document ID missing");
        console.error("FINAL SELECTED DOC:", selectedDoc);
        return;
      }
      console.log("SELECTED USER:", sendForm.toUser);
      try {

        const payload = {
          id: selectedDoc.id, 
          docId: selectedDoc.id,
          sendTo: sendForm.toUser?.email || "",
          sendToUserId: sendForm.toUser?.value || "",
          sendSubject: sendForm.sendSubject,
          sendReason: sendForm.sendReason,
          responseExpected: sendForm.responseExpected,
          targetResponseDate: sendForm.targetResponseDate,
          attachmentName: selectedDoc["File Name"],
          status: "Send"
        };

        await api.post("/api/documents/send-document", payload);

        alert("Document sent successfully");

        setShowSendPopup(false);

      } catch (err) {
        console.error(err);
      }
    };

  const fetchDocuments = async () => {
    try {
      setLoading(true);

      const res = await api.get("/api/documents/list"); 

      const rows = res.data || [];

      const mapped = rows.map((d) => ({
        id: d.id,
        folderId: d.folderId,
        "File Type": d.fileType || "",
        "File Number": d.fileNumber || "",
        "File Name": d.fileName || "",
        "Revision No": d.revisionNumber || "",
        Status: d.status || "",
        "Project Name": d.projectName || "",
        "Contract Name": d.contractName || "",
        Path: d.path || "",
        "Created By": d.createdBy || "",
        "Date Uploaded": d.dateUploaded || "",
        "Revision Date": d.revisionDate || "",
        Department: d.department || "",
      //   Actions: (
      //   <div className={styles.actionMenu}>
      //     <span
      //       className={styles.menuIcon}
      //       onClick={(e) => toggleMenu(e, d.id)}
      //     >
      //       ⋮
      //     </span>

      //     {openMenu === d.id && (
      //       <div className={styles.menuDropdown}>
      //         <div onClick={() => handleSend(d)}>Send</div>
      //         <div onClick={() => handleUpdate(d)}>Update</div>
      //         <div onClick={() => handleVersions(d)}>View old versions</div>
      //         <div onClick={() => handleNotRequired(d)}>Not required</div>
      //         <div onClick={() =>
      //           window.open(`/api/documents/download/${d.id}`, "_blank")
      //         }>
      //           Download
      //         </div>
      //         <div onClick={() => window.print()}>Print</div>
      //       </div>
      //     )}
      //   </div>
      // )
      }));

      setDocuments(mapped);

    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    const closeMenu = () => setOpenMenu(null);
    document.addEventListener("click", closeMenu);
    return () => document.removeEventListener("click", closeMenu);
  }, []);

  // const buildTree = (list) => {

  //   const map = {};
  //   const roots = [];

  //   list.forEach(f => {
  //     map[f.id] = { ...f, children: [] };
  //   });

  //   list.forEach(f => {

  //     if (f.parent && f.parent.id) {
  //       map[f.parent.id].children.push(map[f.id]);
  //     } else {
  //       roots.push(map[f.id]);
  //     }

  //   });

  //   return roots;
  // };

  const convertTree = (list) => {

    const map = {};
    const roots = [];

    list.forEach(f => {
      map[f.id] = { ...f, children: [] };
    });

    list.forEach(f => {
      if (f.parentId) {
        map[f.parentId]?.children.push(map[f.id]);
      } else {
        roots.push(map[f.id]);
      }
    });

    return roots;
  };

const fetchFolders = async () => {
  try {
    const res = await api.get("/api/folders/tree");

    const data = convertTree(res.data || []);

    setFolders(data);

  } catch (err) {
    console.error("Folders fetch error", err);
  }
};



  useEffect(() => {
    fetchDocuments();
    fetchFolders();
  }, []);

  useEffect(() => {

    if (!pendingFolderId || folders.length === 0) return;

    console.log("FOLDER ID:", pendingFolderId);

    const path = findFolderPath(folders, pendingFolderId);

    console.log("FOUND PATH:", path);

    if (path) {

      setSelectedPath(path);

      setExpandedFolders(prev => {
        const next = new Set(prev);
        path.forEach(p => next.add(p.id));
        return next;
      });

    }

    setPendingFolderId(null);

  }, [pendingFolderId, folders]);

   const buildPathString = () =>
    selectedPath.map(f => f.name).join("/") + (selectedPath.length ? "/" : "");

  const handleSaveDocument = async () => {
      try {
        const fd = new FormData();
        const last = selectedPath[selectedPath.length - 1];

        const dto = {
          ...docForm,
          // path: buildPathString(),
          folderId: last?.id
        };

        fd.append("dto", JSON.stringify(dto));

        if (documentFile) {
          fd.append("file", documentFile);
        }

        await api.post("/api/documents/upload", fd);

        alert("Document uploaded successfully");
        setShowUpload(false);
        fetchDocuments();

      } catch (err) {
        console.error(err);
        alert("Upload failed");
         const msg = err?.response?.data?.message || err?.response?.data || "Failed to send document";
        
        alert(msg);
      }
    };

    



  const addFolder = (nodes, path, name, level = 1) => {
    return nodes.map(node => {
      if (path[0]?.id === node.id) {
        if (path.length === 1) {
          if (level >= MAX_DEPTH) return node;
          return {
            ...node,
            children: [
              ...(node.children || []),
              { id: Date.now().toString(), name, children: [] }
            ]
          };
        }
        return {
          ...node,
          children: addFolder(node.children, path.slice(1), name, level + 1)
        };
      }
      return node;
    });
  };

  const getDepth = (path) => path.length;

  const handleAddFolder = async () => {

    if (!newFolderName) return;

    try {

      const parent = selectedPath[selectedPath.length - 1];

      const payload = {
        name: newFolderName,
        parentId: parent ? parent.id : null
      };

      await api.post("/api/folders/create", payload);

      setNewFolderName("");

      await fetchFolders(); // reload tree

    } catch (err) {
      console.error("Folder creation failed", err);
      alert("Failed to create folder");
    }
  };

  /* ---------- Recursive Folder UI ---------- */
  const renderFolders = (nodes, path = []) =>
    nodes.map(folder => {
      const currentPath = [...path, folder];
      const isExpanded = expandedFolders.has(folder.id);
      const isSelected = String(selectedPath[selectedPath.length - 1]?.id) === String(folder.id);


      return (
        <div key={folder.id} className={styles.folderNode}>
          <div className={styles.folderRow}>
            {folder.children?.length > 0 && (
              <span
                className={styles.arrow}
                onClick={(e) => {
                  e.stopPropagation();
                  toggleFolder(folder.id);
                }}
              >
                {isExpanded ? "▼" : "▶"}
              </span>
            )}

            {renamingId === folder.id ? (
              <input
                value={renameValue}
                onChange={e => setRenameValue(e.target.value)}
                onBlur={() => {
                  handleRename(folder.id, renameValue);
                  setRenamingId(null);
                }}
                autoFocus
              />
            ) : (
              <div
                className={`${styles.folderItem} ${isSelected ? styles.activeFolder : ""}`}
                onClick={() => setSelectedPath(currentPath)}
                onDoubleClick={() => {
                  setRenamingId(folder.id);
                  setRenameValue(folder.name);
                }}
              >
                📁 {folder.name}
              </div>
            )}
          </div>

          {isExpanded && (
            <div className={styles.folderChildren}>
              {renderFolders(folder.children || [], currentPath)}
            </div>
          )}
        </div>
      );
    });


    const toggleFolder = (id) => {
      setExpandedFolders(prev => {
        const next = new Set(prev);
        next.has(id) ? next.delete(id) : next.add(id);
        return next;
      });
    };


    const filterFolders = (nodes, query) => {
      if (!query) return nodes;

      const q = query.toLowerCase();

      return nodes
        .map(node => {
          const children = filterFolders(node.children || [], query);
          const match = node.name.toLowerCase().includes(q);

          if (match || children.length) {
            // auto expand matched parents
            expandedFolders.add(node.id);
            return { ...node, children };
          }
          return null;
        })
        .filter(Boolean);
    };

    const handleRename = async (id, name) => {

      try {

        await api.put(`/api/folders/rename/${id}`, null, {
          params: { name }
        });

        fetchFolders();

      } catch (err) {
        console.error(err);
      }

    };

    const livePreviewPath = () => {
      if (!selectedPath.length) return "";

      const base = buildPathString();
      const folder = newFolderName ? `${newFolderName}/` : "";
      const file = fileName ? fileName : "";

      return `${base}${folder}${file}`;
    };

      useEffect(() => {
        api.get("/projects/get-project-name")
          .then(r => setProjects(r.data || []));

        api.get("/contract/get-contract-name")
          .then(r => setContracts(r.data || []));

        api.get("/api/departments/get")
          .then(r => setDepartments(r.data || []));

        api.get("/api/statuses/get")
          .then(r => setStatuses(r.data || []));

       }, []);

        const projectOptions = projects.map(p => ({
          value: p.name,
          label: p.name
        }));

        const contractOptions = contracts.map(c => ({
          value: c.name,
          label: c.name
        }));

         const departmentOptions = departments.map(d => ({
          value: d.id,
          label: d.name
        }));
    
        const handleSelectChange = (name, option) => {
          setDocForm(prev => ({
            ...prev,
            [name]: option ? option.value : ""
          }));
        };

        const searchUsers = async (inputValue) => {
          try {

            const res = await api.get(`/users/search?query=${inputValue}`);

            console.log("USER API RESPONSE:", res.data);

            return (res.data || []).map(user => ({
              label: `${user.userName} (${user.emailId})`,
              value: user.userId,
              email: user.emailId
            }));

          } catch (err) {
            console.error(err);
            return [];
          }
        };

        const handleDraftClick = (draft) => {

          console.log("DRAFT CLICKED:", draft);

          setSelectedDoc({
            id: draft.id,
            docId: draft.docId,
            "File Name": draft.attachment,
          });

          setSendForm({
            toUser: draft.sendTo,
            sendSubject: draft.subject,
            sendReason: draft.reason,
            responseExpected: draft.responseExpected,
            targetResponseDate: draft.targetDate
          });

          setShowDrafts(false);
          setShowSendPopup(true);
        };

        const handleUpdateDocument = async () => {

          try{

          const fd = new FormData();

          const lastFolder = selectedPath[selectedPath.length - 1];

            const dto = {
              revisionNo: docForm.revisionNo,
              revisionDate: docForm.revisionDate,
              reasonForUpdate: docForm.updateReason,
              folderId: lastFolder?.id || null
            };

          fd.append("dto",JSON.stringify(dto));

          if(documentFile){
          fd.append("file",documentFile);
          }

          await api.put(`/api/documents/update/${selectedDoc.id}`, fd);

          alert("Document updated successfully");

          setShowUpdatePopup(false);

          fetchDocuments();

          }catch(err){
          console.error(err);
          alert("Update failed");
          }

          };

          const findFolderPath = (nodes, targetId, path = []) => {

            for (const node of nodes) {

              const newPath = [...path, node];

              if (String(node.id) === String(targetId)) {
                return newPath;
              }

              if (node.children?.length) {
                const result = findFolderPath(node.children, targetId, newPath);
                if (result) return result;
              }

            }

            return null;
          };

          

  return (
    <>
      {/* ACTION BAR */}
      <div className={styles.actionBar}>

        {!showDrafts && (
          <>
            <button className="btn btn-primary" onClick={() => setShowUpload(true)}>
              Upload
            </button>

            <button
              className="btn btn-secondary"
              onClick={() => setShowDrafts(true)}
            >
              Drafts
            </button>
          </>
        )}

        {showDrafts && (
          <button
            className="btn btn-secondary"
            onClick={() => setShowDrafts(false)}
          >
            Documents
          </button>
        )}

      </div>

      {/* DOCUMENT TABLE */}
      {showDrafts ? (
        <Drafts key="drafts" onDraftClick={handleDraftClick} />
      ) : (
        <DmsTable
          loading={loading}
          columns={[
            "File Type","File Number","File Name","Revision No",
            "Status","Project Name","Contract Name","Path",
            "Created By","Date Uploaded","Revision Date",
            "Department","Actions"
          ]}
          mockData={documents}
          renderActions={(row) => (
            <div className={styles.actionMenu}>
              <span
                className={styles.menuIcon}
                onClick={(e) => toggleMenu(e, row.id)}
              >
                ⋮
              </span>

              {openMenu === row.id && (
                <div className={styles.menuDropdown}>
                  <div onClick={() => handleSend(row)}>Send</div>
                  <div onClick={() => handleUpdate(row)}>Update</div>
                  <div onClick={() => handleVersions(row)}>View old versions</div>
                  <div onClick={() => handleNotRequired(row)}>Not required</div>
                  <div onClick={() =>
                    window.open(`/api/documents/download/${row.id}`, "_blank")
                  }>
                    Download
                  </div>
                  <div onClick={() => window.print()}>Print</div>
                </div>
              )}
            </div>
          )}
        />
      )}

      {/* ================= UPLOAD MODAL ================= */}
      {showUpload && (
        <div className={styles.overlay}>
          <div className={styles.modal}>
            {/* HEADER */}
            <div className={styles.header}>
              <h3>Upload Documents</h3>
              <span onClick={() => setShowUpload(false)}>×</span>
            </div>

            {/* TABS */}
            <div className={styles.tabs}>
              <button
                className={activeTab === "single" ? styles.active : ""}
                onClick={() => setActiveTab("single")}
              >
                Single Upload
              </button>
              <button
                className={activeTab === "bulk" ? styles.active : ""}
                onClick={() => setActiveTab("bulk")}
              >
                Bulk Upload
              </button>
            </div>

            {/* BODY */}
            <div className={styles.body}>
              {activeTab === "single" && (
                <>
                  <div className={styles.formGrid}>
                    <div className="form-row">
                      <div className="form-field">
                        <label>File Name *</label>
                        <input
                          value={docForm.fileName}
                          onChange={e =>
                            setDocForm({ ...docForm, fileName: e.target.value })
                          }
                        />
                      </div>
                      <div className="form-field">
                        <label>File Number *</label>
                        <input
                          value={docForm.fileNumber}
                          onChange={e =>
                            setDocForm({ ...docForm, fileNumber: e.target.value })
                          }
                        />
                      </div>
                      <div className="form-field">
                        <label>Revision No *</label>
                        {/* <input placeholder="Revision No" defaultValue="R01" /> */}
                        <input
                          value={docForm.revisionNo}
                          onChange={e =>
                            setDocForm({ ...docForm, revisionNo: e.target.value })
                          }
                        />
                      </div>
                      <div className="form-field">
                        <label>Revision Date *</label>
                        <input
                          type="date"
                          value={docForm.revisionDate}
                          onChange={e =>
                            setDocForm({ ...docForm, revisionDate: e.target.value })
                          }
                        />
                      </div>
                      <div className="form-field">
                        <label>Project Name *</label>
                        <Select
                          options={projectOptions}
                          value={
                            docForm.projectName ? { value: docForm.projectName, label: docForm.projectName} : null
                          }
                          placeholder="Search Project..."
                          isClearable
                          onChange={(opt) => handleSelectChange("projectName", opt)}
                        />
                      </div>
                      
                      <div className="form-field">
                        <label>Contract Name *</label>
                        <Select
                          options={contractOptions}
                          value={
                            docForm.contractName ? { value: docForm.contractName, label: docForm.contractName} : null
                          }
                          placeholder="Search Contract..."
                          isClearable
                          onChange={(opt) => handleSelectChange("contractName", opt)}
                        />
                      </div>
                    </div>
                  
                    
                  </div>

                  {/* FOLDER TREE */}
                  <div className={styles.folderSection}>
                    <h4>Select Folder</h4>

                    <input
                      className={styles.search}
                      placeholder="Search folders..."
                      value={folderSearch}
                      onChange={(e) => setFolderSearch(e.target.value)}
                    />
                    <div className={styles.breadcrumb}>
                      {selectedPath.map((p, i) => (
                        <span
                          key={p.id}
                          onClick={() => {
                            setSelectedPath(selectedPath.slice(0, i + 1));
                            setExpandedFolders(new Set(selectedPath.slice(0, i + 1).map(x => x.id)));
                          }}
                        >
                          {p.name} /
                        </span>
                      ))}
                    </div>
                    <div style={{marginBottom:"10px",fontWeight:"500"}}>
                      Selected Folder: {selectedPath.map(p=>p.name).join(" / ") || "None"}
                    </div>
                    <div className={styles.folderTree}>
                      {renderFolders(filterFolders(folders, folderSearch))}
                    </div>

                    
                      <div className="form-row">
                        <div className="form-field">
                          <input
                            placeholder="New Folder Name"
                            value={newFolderName}
                            onChange={(e) => setNewFolderName(e.target.value)}
                          />
                        </div>
                          <button className="btn btn-2 btn-primary" onClick={handleAddFolder}>+ Add Folder</button>
                      </div>

                    <div className={styles.pathPreview}>
                      <strong>Live Preview:</strong>{" "}
                      <span>
                        {livePreviewPath() || "—"}
                      </span>
                    </div>


                  </div>

                  <div className={styles.formGrid}>
                    <div className="form-row">
                      <div className="form-field">
                        <label>Department</label>
                        <Select
                          options={departmentOptions}
                          value={
                            departmentOptions.find(
                              option => option.value === docForm.department
                            ) || null
                          }
                          placeholder="Search Department..."
                          isClearable
                          onChange={(opt) => handleSelectChange("department", opt)}
                        />
                      </div>
                      <div className="form-field">
                        <label>Status</label>
                        <Select
                          options={statuses.map(s => ({
                            value: s.id,
                            label: s.name
                          }))}
                          value={
                              statuses
                                .map(s => ({ value: s.id, label: s.name }))
                                .find(option => option.value === docForm.currentStatus) || null
                            }
                          placeholder="Select Status"
                          isClearable
                          onChange={(opt) =>
                            setDocForm(prev => ({
                              ...prev,
                              currentStatus: opt ? opt.value : ""
                            }))
                          }
                        />
                      </div>
                      <div className="form-field">
                        <label>Document File</label>
                        <input
                          type="file"
                          onChange={(e) => setDocumentFile(e.target.files[0])}
                        />
                      </div>
                    </div>
                    
                    
                  </div>
                </>
              )}

              {activeTab === "bulk" && (
                <>
                <div className="form-row">
                  <button className="btn btn-2 btn-primary">Download Excel Template</button>
                </div>

                <div className="form-row">
                  <div className="form-field">
                    <label>Excel File</label>
                    <input type="file" />
                  </div>
                  <div className="form-field">
                    <label>Associated Documents (zip)</label>
                    <input type="file" />
                  </div>
                </div>
                  <button className="btn btn-2 btn-primary">Preview Metadata</button>
                </>
              )}
            </div>

            {/* FOOTER */}
            <div className={styles.footer}>
              <button className="btn btn-primary" onClick={handleSaveDocument}>Save</button>
              <button className="btn btn-white" onClick={() => setShowUpload(false)}>Cancel</button>
            </div>
          </div>
        </div>
      )}

      {showSendPopup && (
        <div className={styles.overlay}>
        <div className={styles.modal}>
          <div className={styles.header}>
            <h3>Send Document</h3>
            <span onClick={() => setShowSendPopup(false)}>×</span>
          </div>
        <div className={styles.body}>
          <div className="form-row">

            <div className="form-field">
              <label>To</label>
              <AsyncSelect
                placeholder="Search recipient..."
                loadOptions={searchUsers}
                value={sendForm.toUser}
                onChange={(selected) => {

                  console.log("USER SELECTED:", selected);

                  setToUser(selected);

                  setSendForm(prev => ({
                    ...prev,
                    toUser: selected
                  }));

                }}
                isClearable
                cacheOptions
                defaultOptions
              />
            </div>

            <div className="form-field">
              <label>Subject</label>
              <input
                type="text"
                value={sendForm.sendSubject}
                onChange={(e) =>
                  setSendForm({ ...sendForm, sendSubject: e.target.value })
                }
                placeholder="Enter email subject"
              />
            </div>

            <div className="form-field">
              <label>Reason for Sending</label>
              <textarea
                value={sendForm.sendReason}
                onChange={(e) =>
                  setSendForm({ ...sendForm, sendReason: e.target.value })
                }
                placeholder="Enter reason for sending the document"
              />
            </div>

            <div className="form-field">
              <label>Response Expected</label>
              <select
                value={sendForm.responseExpected}
                onChange={(e) =>
                  setSendForm({ ...sendForm, responseExpected: e.target.value })
                }
              >
                <option value="Yes">Yes</option>
                <option value="No">No</option>
              </select>
            </div>

            <div className="form-field">
              <label>Target Response Date</label>
              <input
                type="date"
                value={sendForm.targetResponseDate}
                onChange={(e) =>
                  setSendForm({ ...sendForm, targetResponseDate: e.target.value })
                }
              />
            </div>

            <div className="form-field">
              <label>Attachments</label>

              <div className={styles.attachmentsList}>
                {selectedDoc && (
                  <div className={styles.attachmentItem}>
                    <a href={`/api/documents/download/${selectedDoc.id}`} target="_blank">
                      📎 {selectedDoc["File Name"]}
                    </a>
                  </div>
                )}
              </div>

            </div>

          </div>
        </div>

        <div className={styles.footer}>
        <button className="btn btn-primary" onClick={handleSendDocument}>Send</button>
        <button className="btn btn-white" onClick={handleSaveDraft}>Save Draft</button>
        <button className="btn btn-red" onClick={()=>setShowSendPopup(false)}>Cancel</button>
        </div>

        </div>
        </div>
        )}

      {showUpdatePopup && (
        <div className={styles.overlay}>
        <div className={styles.modal}>

        <div className={styles.header}>
        <h3>Update Document</h3>
        <span onClick={()=>setShowUpdatePopup(false)}>×</span>
        </div>

        <div className={styles.body}>

        <div className="form-row">

        <div className="form-field">
        <label>File Name</label>
        <input
        value={selectedDoc?.["File Name"] || ""}
        disabled
        />
        </div>

        <div className="form-field">
        <label>File Number</label>
        <input
        value={selectedDoc?.["File Number"] || ""}
        disabled
        />
        </div>

        <div className="form-field">
        <label>Revision No</label>
        <input
        value={docForm.revisionNo}
        onChange={(e)=>setDocForm({
        ...docForm,
        revisionNo:e.target.value
        })}
        />
        </div>

        <div className="form-field">
        <label>Revision Date</label>
        <input
        type="date"
        value={docForm.revisionDate}
        onChange={(e)=>setDocForm({
        ...docForm,
        revisionDate:e.target.value
        })}
        />
        </div>

        <div className="form-field">
        <label>Project Name</label>
        <Select
        options={projectOptions}
        value={
        docForm.projectName
        ? {value:docForm.projectName,label:docForm.projectName}
        : null
        }
        onChange={(opt)=>handleSelectChange("projectName",opt)}
        />
        </div>

        <div className="form-field">
        <label>Contract Name</label>
        <Select
        options={contractOptions}
        value={
        docForm.contractName
        ? {value:docForm.contractName,label:docForm.contractName}
        : null
        }
        onChange={(opt)=>handleSelectChange("contractName",opt)}
        />
        </div>

        <div className="form-field">
        <label>Department</label>
        <Select
        options={departmentOptions}
        value={
          departmentOptions.find(
          o=>o.label === docForm.department
          ) || null
          }
        onChange={(opt)=>handleSelectChange("department",opt)}
        />
        </div>

        <div className="form-field">
        <label>Status</label>
        <Select
        options={statuses.map(s=>({
        value:s.id,
        label:s.name
        }))}
        value={
        statuses
        .map(s=>({value:s.id,label:s.name}))
        .find(o=>o.value===docForm.currentStatus) || null
        }
        onChange={(opt)=>
        setDocForm({
        ...docForm,
        currentStatus:opt?.value || ""
        })
        }
        />
        </div>

        <div className="form-field">
        <label>Reason for Update</label>
        <textarea
        value={docForm.updateReason || ""}
        onChange={(e)=>setDocForm({
        ...docForm,
        updateReason:e.target.value
        })}
        />
        </div>

        <div className="form-field">
        <label>Upload New Version</label>
        <input
        type="file"
        onChange={(e)=>setDocumentFile(e.target.files[0])}
        />
        </div>

      </div>
      
      <div className="form-row">
        <div className={styles.folderSection}>
          <h4>Select Folder</h4>

          <input
            className={styles.search}
            placeholder="Search folders..."
            value={folderSearch}
            onChange={(e)=>setFolderSearch(e.target.value)}
          />

          <div className={styles.breadcrumb}>
            {selectedPath.map((p,i)=>(
              <span key={p.id}>
                {p.name} /
              </span>
            ))}
          </div>

          <div className={styles.folderTree}>
            {renderFolders(filterFolders(folders,folderSearch))}
          </div>

        </div>
      </div>

      <div className="form-row">
        <div className="form-field">
          <label>Current Document</label>

          {selectedDoc && (
          <a
          href={`/api/documents/download/${selectedDoc.id}`}
          target="_blank"
          >
          📎 {selectedDoc["File Name"]}
          </a>
          )}

        </div>

        </div>

        </div>

        <div className={styles.footer}>
        <button
        className="btn btn-primary"
        onClick={handleUpdateDocument}
        >
        Save
        </button>

        <button
        className="btn btn-red"
        onClick={()=>setShowUpdatePopup(false)}
        >
        Cancel
        </button>
        </div>

        </div>
        </div>
        )}

        {showVersionsPopup && (
          <div className={styles.overlay}>
            <div className={styles.modal}>
              <div className={styles.header}>
                <h3>Older Versions</h3>
              </div>
              <div className={styles.body}>
                <div className="dataTable">
                  <table className={styles.versionsTable}>
                    <thead>
                      <tr>
                        <th>File Name</th>
                        <th>File Number</th>
                        <th>Revision</th>
                        <th>Download</th>
                      </tr>
                    </thead>

                    <tbody>
                      <tr>
                        <td>{selectedDoc?.["File Name"]}</td>
                        <td>{selectedDoc?.["File Number"]}</td>
                        <td>{selectedDoc?.["Revision No"]}</td>
                        <td>
                        <a href={`/api/documents/download/${selectedDoc?.id}`}>Download</a>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
                <div className={styles.footer}>
                  <button className="btn btn-2 btn-white" onClick={()=>setShowVersionsPopup(false)}>Close</button>
                </div>

          </div>
          </div>
          )}
    </>
  );
}
