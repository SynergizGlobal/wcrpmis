import React, { useEffect, useState } from "react";
import styles from "./Folders.module.css";
import api from "../../../../api/axiosInstance";

import folderIcon from "../../../../assets/images/icons/folder.svg";
import pdfIcon from "../../../../assets/images/icons/pdf.svg";
import fileIcon from "../../../../assets/images/icons/file.svg";

export default function Folders() {
  const [project, setProject] = useState("");
  const [contract, setContract] = useState("");

  const [projects, setProjects] = useState([]);
  const [contracts, setContracts] = useState([]);

  const [allFolders, setAllFolders] = useState([]);
  const [displayData, setDisplayData] = useState([]);

  const [view, setView] = useState("folders"); // folders, subfolders, files
  const [breadcrumb, setBreadcrumb] = useState([]);
  const [currentFolderId, setCurrentFolderId] = useState(null);
  const [currentSubFolderId, setCurrentSubFolderId] = useState(null);

  const [loading, setLoading] = useState(false);

  /* ================= LOAD PROJECTS AND CONTRACTS ================= */
  const loadProjectsAndContracts = async () => {
    try {
      // You might need to create these endpoints or use existing ones
      // For now, using the ones from your screenshot
      setProjects(["MUTP IIIA"]);
      setContracts(["224 Construction of Important Bridges (Br. No. 73 & 75) in Vasai Creek, Minor Bridge (Br. No.74)"]);
    } catch (err) {
      console.error("Error loading projects/contracts:", err);
    }
  };

  useEffect(() => {
    loadProjectsAndContracts();
  }, []);

  /* ================= LOAD FOLDERS ================= */
  const loadFolders = async () => {
    setLoading(true);
    try {
      // If project and contract are selected, use the filtered endpoint
      if (project && contract) {
        const requestBody = {
          projects: [project],
          contracts: [contract]
        };
        
        const res = await api.post("/api/folders/grid", requestBody, {
          withCredentials: true,
        });
        
        const data = Array.isArray(res.data) ? res.data : [];
        setAllFolders(data);
        setDisplayData(data);
      } else {
        // Load all folders if no filter selected
        const res = await api.get("/api/folders/get", {
          withCredentials: true,
        });
        
        const data = Array.isArray(res.data) ? res.data : [];
        setAllFolders(data);
        setDisplayData(data);
      }
    } catch (err) {
      console.error("Error loading folders:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (project && contract) {
      loadFolders();
    } else {
      loadFolders();
    }
  }, [project, contract]);

  /* ================= LOAD SUBFOLDERS GRID ================= */
  const loadSubFoldersGrid = async (folderId, folderName) => {
    setLoading(true);
    setCurrentFolderId(folderId);

    try {
      const requestBody = {
        projects: project ? [project] : [],
        contracts: contract ? [contract] : []
      };

      const res = await api.post(`/api/subfolders/grid/${folderId}`, requestBody, {
        withCredentials: true
      });

      console.log("Subfolders response:", res.data);

      const subfolders = Array.isArray(res.data) ? res.data : [];
      
      setDisplayData(subfolders);
      setView("subfolders");
      setBreadcrumb([{ id: folderId, name: folderName, type: 'folder' }]);

    } catch (err) {
      console.error("Subfolders load error:", err);
    } finally {
      setLoading(false);
    }
  };

  /* ================= LOAD FILES ================= */
  const loadFiles = async (subFolderId, subFolderName) => {
    setLoading(true);
    setCurrentSubFolderId(subFolderId);

    try {
      const requestBody = {
        projects: project ? [project] : [],
        contracts: contract ? [contract] : []
      };

      // Using the document repository query from your backend
      const res = await api.post(`/api/documents/grid/subfolder/${subFolderId}`, requestBody, {
        withCredentials: true
      });

      console.log("Files response:", res.data);

      const files = Array.isArray(res.data) ? res.data : [];
      
      // Transform the data for display
      const formattedFiles = files.map(file => ({
        id: file.fileName + (file.revisionNo || ''),
        name: file.fileName,
        fileName: file.fileName,
        fileType: file.fileType,
        filePath: file.filePath,
        revisionNo: file.revisionNo
      }));

      setDisplayData(formattedFiles);
      setView("files");
      setBreadcrumb(prev => [
        ...prev, 
        { id: subFolderId, name: subFolderName, type: 'subfolder' }
      ]);

    } catch (err) {
      console.error("Files load error:", err);
      // If endpoint doesn't exist, show mock data for now
      if (err.response?.status === 404) {
        console.log("Files endpoint not configured yet");
        setDisplayData([]);
        setView("files");
        setBreadcrumb(prev => [
          ...prev, 
          { id: subFolderId, name: subFolderName, type: 'subfolder' }
        ]);
      }
    } finally {
      setLoading(false);
    }
  };

  /* ================= GET ICON BASED ON FILE TYPE ================= */
  const getFileIcon = (fileType) => {
    if (!fileType) return fileIcon;
    
    const type = fileType.toLowerCase();
    if (type.includes('pdf') || type === 'pdf' || type === 'application/pdf') {
      return pdfIcon;
    }
    return fileIcon;
  };

  /* ================= CLICK HANDLER ================= */
  const handleClick = (item) => {
    if (view === "folders") {
      // Clicked on a main folder - load subfolders grid
      if (item.id) {
        loadSubFoldersGrid(item.id, item.name);
      }
    } 
    else if (view === "subfolders") {
      // Clicked on a subfolder - load files
      if (item.id) {
        loadFiles(item.id, item.name);
      }
    }
    else if (view === "files") {
      // Clicked on a file - you could add preview/download here
      console.log("File clicked:", item);
    }
  };

  /* ================= GO BACK ================= */
  const goBack = () => {
    if (view === "files") {
      // Go back to subfolders
      setView("subfolders");
      setBreadcrumb(prev => prev.slice(0, -1));
      setCurrentSubFolderId(null);
      
      // Reload subfolders for the current folder
      if (currentFolderId) {
        loadSubFoldersGrid(currentFolderId, breadcrumb[0]?.name);
      }
    } else if (view === "subfolders") {
      // Go back to main folders
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
          {projects.map((p, index) => (
            <option key={index} value={p}>{p}</option>
          ))}
        </select>

        <select 
          value={contract} 
          onChange={e => setContract(e.target.value)}
          className={styles.select}
          disabled={!project}
        >
          <option value="">Select Contract</option>
          {contracts.map((c, index) => (
            <option key={index} value={c}>{c}</option>
          ))}
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
          <span key={i}>
            {' › '}
            <span>{b.name}</span>
          </span>
        ))}
      </div>

      {!project || !contract ? (
        <div className={styles.messageBox}>
          <p>Please select a Project and Contract to view folders</p>
        </div>
      ) : loading ? (
        <div className={styles.loading}>Loading...</div>
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