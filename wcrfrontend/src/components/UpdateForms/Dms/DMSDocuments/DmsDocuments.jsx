import React, { useState, useEffect } from "react";
import DmsTable from "../DmsTable/DmsTable";
import Drafts from "../Correspondence/Drafts";
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

    const [projects, setProjects] = useState([]);
    const [contracts, setContracts] = useState([]);
    const [departments, setDepartments] = useState([]);
    const [statuses, setStatuses] = useState([]);

  // single upload section

    const [docForm, setDocForm] = useState({
      fileName: "",
      fileNumber: "",
      revisionNo: "R01",
      revisionDate: "",
      projectName: "",
      contractName: "",
      department: "",
      status: ""
    });

    const [documentFile, setDocumentFile] = useState(null);

  /* ---------- Folder Tree State ---------- */
  const [folders, setFolders] = useState([
    {
      id: "1",
      name: "Reports",
      children: [
        {
          id: "2",
          name: "MPR",
          children: []
        }
      ]
    }
  ]);

  const fetchDocuments = async () => {
    try {
      setLoading(true);

      const res = await api.get("/api/documents/list"); 

      const rows = res.data || [];

      const mapped = rows.map((d) => ({
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
        "View / Download": (
          <span
            style={{ color: "#0d6efd", cursor: "pointer" }}
            onClick={() =>
              window.open(`/api/documents/download/${d.id}`, "_blank")
            }
          >
            Download
          </span>
        )
      }));

      setDocuments(mapped);

    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchDocuments();
  }, []);

   const buildPathString = () =>
    selectedPath.map(f => f.name).join("/") + (selectedPath.length ? "/" : "");

  const handleSaveDocument = async () => {
      try {
        const fd = new FormData();

        const dto = {
          ...docForm,
          path: buildPathString(),
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
              ...node.children,
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

  const handleAddFolder = () => {
    if (!newFolderName || selectedPath.length === 0) return;

    if (getDepth(selectedPath) >= MAX_DEPTH) {
      alert("Maximum folder depth (10) reached");
      return;
    }

    setFolders(prev =>
      addFolder(prev, selectedPath, newFolderName)
    );
    setNewFolderName("");
  };

  /* ---------- Recursive Folder UI ---------- */
  const renderFolders = (nodes, path = []) =>
    nodes.map(folder => {
      const currentPath = [...path, folder];
      const isExpanded = expandedFolders.has(folder.id);
      const isSelected = selectedPath.at(-1)?.id === folder.id;

      return (
        <div key={folder.id} className={styles.folderNode}>
          <div className={styles.folderRow}>
            {folder.children.length > 0 && (
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
                  setFolders(prev => renameFolder(prev, folder.id, renameValue));
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

    const renameFolder = (nodes, id, name) =>
    nodes.map(n => {
      if (n.id === id) return { ...n, name };
      return { ...n, children: renameFolder(n.children || [], id, name) };
    });

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


  return (
    <>
      {/* ACTION BAR */}
      <div className={styles.actionBar}>
        <button className="btn btn-primary" onClick={() => setShowUpload(true)}>Upload</button>
        <button className="btn btn-secondary" onClick={() => setShowDrafts(true)}>Drafts</button>
      </div>

      {/* DOCUMENT TABLE */}
      {!showDrafts && (
        <DmsTable
          loading={loading}
          columns={[
            "File Type","File Number","File Name","Revision No",
            "Status","Project Name","Contract Name","Path",
            "Created By","Date Uploaded","Revision Date",
            "Department","View / Download"
          ]}
          mockData={documents}
        />
      )}

      {/* DRAFTS */}
      {showDrafts && <Drafts onBack={() => setShowDrafts(false)} />}

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
              <button className="btn btn-primary">Save</button>
              <button className="btn btn-white" onClick={() => setShowUpload(false)}>Cancel</button>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
