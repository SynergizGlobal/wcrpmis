import React, { useEffect, useState } from "react";
import styles from "./Folders.module.css";
import api from "../../../../api/axiosInstance";

import folderIcon from "../../../../assets/images/icons/folder.svg";
import pdfIcon from "../../../../assets/images/icons/pdf.svg";
import fileIcon from "../../../../assets/images/icons/file.svg";

// Static Correspondence folder — always injected when a filter is selected
const CORRESPONDENCE_FOLDER = {
  id: "__correspondence__",
  name: "Correspondence",
  isCorrespondence: true,
  subFolders: [
    { id: "__incoming__", name: "Incoming", isCorrespondenceType: "Incoming" },
    { id: "__outgoing__", name: "Outgoing", isCorrespondenceType: "Outgoing" },
  ],
};

export default function Folders() {
  const [project, setProject] = useState("");
  const [contract, setContract] = useState("");

  const [allFolders, setAllFolders] = useState([]);
  const [displayData, setDisplayData] = useState([]);

  const [view, setView] = useState("folders"); // folders | subfolders | files
  const [breadcrumb, setBreadcrumb] = useState([]);
  const [currentFolderId, setCurrentFolderId] = useState(null);
  const [currentSubFolderId, setCurrentSubFolderId] = useState(null);

  const [loading, setLoading] = useState(false);

  /* ================= LOAD FOLDERS ================= */
  const loadFolders = async () => {
    setLoading(true);
    try {
      const res = await api.get("/api/folders/get", { withCredentials: true });
      const data = Array.isArray(res.data) ? res.data : [];
      const rootFolders = data.filter(f => f.parentId == null);
      setAllFolders(rootFolders);
      // Don't show folders until project or contract is selected
      setDisplayData([]);
    } catch (err) {
      console.error("Error loading folders:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadFolders();
  }, []);

  /* ================= INJECT CORRESPONDENCE WHEN FILTER SELECTED ================= */
  useEffect(() => {
    // Reset view whenever filter changes
    setView("folders");
    setBreadcrumb([]);
    setCurrentFolderId(null);
    setCurrentSubFolderId(null);

    if (project && contract) {
      // Both selected — show all DMS folders + Correspondence
      setDisplayData([...allFolders.filter(f => f.id !== "__correspondence__"), CORRESPONDENCE_FOLDER]);
    } else if (project || contract) {
      // Only one selected — show Correspondence folder only
      setDisplayData([CORRESPONDENCE_FOLDER]);
    } else {
      // Nothing selected — hide everything
      setDisplayData([]);
    }
  }, [project, contract, allFolders]);

  /* ================= LOAD SUBFOLDERS GRID ================= */
  const loadSubFoldersGrid = async (folderId, folderName) => {
    setLoading(true);
    setCurrentFolderId(folderId);
    try {
      const requestBody = {
        projects:  project  ? [project]  : [],
        contracts: contract ? [contract] : [],
      };
      const res = await api.post(`/api/subfolders/grid/${folderId}`, requestBody, {
        withCredentials: true,
      });
      const subfolders = Array.isArray(res.data) ? res.data : [];
      setDisplayData(subfolders);
      setView("subfolders");
      setBreadcrumb([{ id: folderId, name: folderName, type: "folder" }]);
    } catch (err) {
      console.error("Subfolders load error:", err);
    } finally {
      setLoading(false);
    }
  };

  /* ================= LOAD CORRESPONDENCE FILES ================= */
  // POST /api/correspondence/getFolderFiles?type=Incoming|Outgoing
  const loadCorrespondenceFiles = async (type, subFolderName) => {
    setLoading(true);
    try {
      const res = await api.post(
        `/api/correspondence/getFolderFiles?type=${type}`,
        {
          projects:  project  ? [project]  : [],
          contracts: contract ? [contract] : [],
        },
        { withCredentials: true }
      );
      const files = Array.isArray(res.data) ? res.data : [];
      setDisplayData(files);
      setView("files");
      setBreadcrumb(prev => [...prev, { name: subFolderName, type: "subfolder" }]);
    } catch (err) {
      console.error("Correspondence files error:", err);
      setDisplayData([]);
      setView("files");
      setBreadcrumb(prev => [...prev, { name: subFolderName, type: "subfolder" }]);
    } finally {
      setLoading(false);
    }
  };

  /* ================= LOAD DMS FILES ================= */
  const loadFiles = async (subFolderId, subFolderName) => {
    setLoading(true);
    setCurrentSubFolderId(subFolderId);
    try {
      const requestBody = {
        projects:  project  ? [project]  : [],
        contracts: contract ? [contract] : [],
      };
      const res = await api.post(
        `/api/documents/grid/subfolder/${subFolderId}`,
        requestBody,
        { withCredentials: true }
      );
      const files = Array.isArray(res.data) ? res.data : [];
      const formattedFiles = files.map(file => ({
        id: file.fileName + (file.revisionNo || ""),
        name: file.fileName,
        fileName: file.fileName,
        fileType: file.fileType,
        filePath: file.filePath,
        revisionNo: file.revisionNo,
      }));
      setDisplayData(formattedFiles);
      setView("files");
      setBreadcrumb(prev => [...prev, { id: subFolderId, name: subFolderName, type: "subfolder" }]);
    } catch (err) {
      console.error("Files load error:", err);
      if (err.response?.status === 404) {
        setDisplayData([]);
        setView("files");
        setBreadcrumb(prev => [...prev, { id: subFolderId, name: subFolderName, type: "subfolder" }]);
      }
    } finally {
      setLoading(false);
    }
  };

  /* ================= GET ICON ================= */
  const getFileIcon = (fileType) => {
    if (!fileType) return fileIcon;
    const type = fileType.toLowerCase();
    return type.includes("pdf") ? pdfIcon : fileIcon;
  };

  /* ================= CLICK HANDLER ================= */
  const handleClick = (item) => {
    if (view === "folders") {
      if (item.isCorrespondence) {
        // Correspondence folder — show Incoming / Outgoing subfolders
        setCurrentFolderId(item.id);
        setDisplayData(item.subFolders);
        setView("subfolders");
        setBreadcrumb([{ id: item.id, name: item.name, type: "folder" }]);
      } else if (item.id) {
        loadSubFoldersGrid(item.id, item.name);
      }
    } else if (view === "subfolders") {
      if (item.isCorrespondenceType) {
        // Incoming / Outgoing
        loadCorrespondenceFiles(item.isCorrespondenceType, item.name);
      } else if (item.id) {
        loadFiles(item.id, item.name);
      }
    } else if (view === "files") {
      console.log("File clicked:", item);
    }
  };

  /* ================= GO BACK ================= */
  const goBack = () => {
    if (view === "files") {
      setView("subfolders");
      setBreadcrumb(prev => prev.slice(0, -1));
      setCurrentSubFolderId(null);
      if (currentFolderId === "__correspondence__") {
        setDisplayData(CORRESPONDENCE_FOLDER.subFolders);
      } else if (currentFolderId) {
        loadSubFoldersGrid(currentFolderId, breadcrumb[0]?.name);
      }
    } else if (view === "subfolders") {
      setView("folders");
      setBreadcrumb([]);
      setCurrentFolderId(null);
      setCurrentSubFolderId(null);
      setDisplayData(allFolders);
    }
  };

  /* ================= RENDER ================= */
  return (
    <div className={styles.container}>
      <div className={styles.filterBar}>
        <select
          value={project}
          onChange={e => setProject(e.target.value)}
          className={styles.select}
        >
          <option value="">Select Project</option>
          <option value="MUTP IIIA">MUTP IIIA</option>
        </select>

        <select
          value={contract}
          onChange={e => setContract(e.target.value)}
          className={styles.select}
        >
          <option value="">Select Contract</option>
          <option value="224 Construction of Important Bridges (Br. No. 73 & 75) in Vasai Creek, Minor Bridge (Br. No.74)">
            224 Construction of Important Bridges (Br. No. 73 & 75) in Vasai Creek, Minor Bridge (Br. No.74)
          </option>
        </select>

        {(view === "subfolders" || view === "files") && (
          <button onClick={goBack} className={styles.backButton}>
            ← Back to {view === "files" ? "Subfolders" : "Folders"}
          </button>
        )}
      </div>

      <div className={styles.breadcrumb}>
        <span
          className={styles.breadcrumbLink}
          onClick={() => {
            setView("folders");
            setBreadcrumb([]);
            setCurrentFolderId(null);
            setCurrentSubFolderId(null);
            setDisplayData(allFolders);
          }}
        >
          Folders
        </span>
        {breadcrumb.map((b, i) => (
          <span key={i}> › <span>{b.name}</span></span>
        ))}
      </div>

      {loading ? (
        <div className={styles.loading}>Loading...</div>
      ) : !project && !contract ? (
        <div className={styles.messageBox} style={{ padding: '40px', textAlign: 'center', color: '#9ca3af', fontSize: '14px' }}>
          Please select a <strong>Project</strong> or <strong>Contract</strong> to view folders.
        </div>
      ) : displayData.length === 0 ? (
        <div className={styles.empty}>
          No {view === "files" ? "files" : "folders"} found
        </div>
      ) : (
        <div className={styles.grid}>
          {displayData.map((item, i) => (
            <div
              key={item.id || i}
              className={styles.card}
              onClick={() => handleClick(item)}
            >
              <img
                src={view === "files" ? getFileIcon(item.fileType) : folderIcon}
                alt={view === "files" ? "file" : "folder"}
              />
              <p title={item.name || item.fileName}>
                {item.name || item.fileName}
              </p>
              {view === "files" && item.revisionNo && (
                <small className={styles.revision}>Rev: {item.revisionNo}</small>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}