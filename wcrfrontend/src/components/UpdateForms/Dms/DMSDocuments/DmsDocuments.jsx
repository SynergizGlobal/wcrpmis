import React, { useState } from "react";
import DmsTable from "../DmsTable/DmsTable";
import Drafts from "../Correspondence/Drafts";
import styles from "./DmsDocuments.module.css";

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

  const [selectedPath, setSelectedPath] = useState([]);
  const [newFolderName, setNewFolderName] = useState("");

  /* ---------- Helpers ---------- */
  const getDepth = (path) => path.length;

  const buildPathString = () =>
    selectedPath.map(f => f.name).join("/") + "/";

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
          columns={[
            "File Type","File Number","File Name","Revision No",
            "Status","Project Name","Contract Name","Path",
            "Created By","Date Uploaded","Revision Date",
            "Department","View / Download"
          ]}
          mockData={[
            {
              "File Type": "PDF",
              "File Number": "MPR/006",
              "File Name": "MPR - Dec'25",
              "Revision No": "R01",
              Status: "Closed",
              "Project Name": "MUTP IIIA",
              "Contract Name": "224 Construction of Important Bridges",
              Path: "Reports/MPR/2025/",
              "Created By": "PMIS_CO_001",
              "Date Uploaded": "2026-01-30",
              "Revision Date": "2025-12-29",
              Department: "Engineering",
              "View / Download": "Download"
            }
          ]}
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
                        <input placeholder="File Name" />
                      </div>
                      <div className="form-field">
                        <label>File Number *</label>
                        <input placeholder="File Number" />
                      </div>
                      <div className="form-field">
                        <label>Revision No *</label>
                        <input placeholder="Revision No" defaultValue="R01" />                       
                      </div>
                      <div className="form-field">
                        <label>Revision Date *</label>
                        <input type="date" />
                      </div>
                      <div className="form-field">
                        <label>Project</label>
                        <select>
                          <option>Select Project</option>
                        </select>
                      </div>
                      <div className="form-field">
                        <label>Contract</label>
                        <select>
                          <option>Select Contract</option>
                        </select>
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
                            placeholder="File Name"
                            value={fileName}
                            onChange={(e) => setFileName(e.target.value)}
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
                        <select>
                          <option>Select Department</option>
                        </select>
                      </div>
                      <div className="form-field">
                        <label>Status</label>
                        <select>
                          <option>Select Status</option>
                        </select>
                      </div>
                      <div className="form-field">
                        <label>Document File</label>
                        <input type="file" />
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
