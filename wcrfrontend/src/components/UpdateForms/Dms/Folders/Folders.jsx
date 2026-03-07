import React, { useEffect, useState } from "react";
import styles from "./Folders.module.css";
import api from "../../../../api/axiosInstance";

import folderIcon from "../../../../assets/images/icons/folder.svg";
import pdfIcon from "../../../../assets/images/icons/pdf.svg";
import fileIcon from "../../../../assets/images/icons/file.svg";

export default function Folders() {
  const [project, setProject] = useState("");
  const [contract, setContract] = useState("");

  const [allFolders, setAllFolders] = useState([]);
  const [displayData, setDisplayData] = useState([]);

  const [view, setView] = useState("folders");
  const [breadcrumb, setBreadcrumb] = useState([]);
  const [currentSubFolderId, setCurrentSubFolderId] = useState(null);

  const [loading, setLoading] = useState(false);

  /* ================= LOAD FOLDERS ================= */
  const loadFolders = async () => {
    setLoading(true);
    try {
      const res = await api.get("/api/folders/get", {
        withCredentials: true,
      });

      const data = Array.isArray(res.data) ? res.data : [];
      setAllFolders(data);
      setDisplayData(data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadFolders();
  }, []);

  /* ================= FILTER ================= */
  useEffect(() => {
    let filtered = allFolders;

    if (project) {
      filtered = filtered.filter(f => f.projectName === project);
    }

    if (contract) {
      filtered = filtered.filter(f => f.contractName === contract);
    }

    setDisplayData(filtered);
    setView("folders");
    setBreadcrumb([]);
  }, [project, contract, allFolders]);

  /* ================= LOAD FILES ================= */
  const loadFiles = async (subFolderId, subFolderName) => {
  setLoading(true);

  try {
    const res = await api.get(
      `/api/subfolders/grid/${subFolderId}`,   // ✅ FIXED URL
      {
        projects: project ? [project] : [],
        contracts: contract ? [contract] : [],
      },
      { withCredentials: true }
    );

    console.log("Files response:", res.data);

    const files = Array.isArray(res.data) ? res.data : [];

    setDisplayData(files);
    setView("files");
    setBreadcrumb(prev => [...prev, { name: subFolderName }]);

  } catch (err) {
    console.error("File load error:", err);
  } finally {
    setLoading(false);
  }
};

  /* ================= CLICK ================= */
  const handleClick = (item) => {
    if (view === "folders") {
      if (item.subFolders?.length > 0) {
        setDisplayData(item.subFolders);
        setView("subfolders");
        setBreadcrumb([{ name: item.name }]);
      }
    } 
    else if (view === "subfolders") {
      loadFiles(item.id, item.name);
    }
  };

  const goBack = () => {
    if (view === "files") {
      setView("subfolders");
      setBreadcrumb(prev => prev.slice(0, -1));
      loadFolders(); // reload fresh
    } else {
      setView("folders");
      setBreadcrumb([]);
      loadFolders();
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.filterBar}>
        <select value={project} onChange={e => setProject(e.target.value)}>
          <option value="">Select Project</option>
          <option value="MUTP IIIA">MUTP IIIA</option>
        </select>

        <select value={contract} onChange={e => setContract(e.target.value)}>
          <option value="">Select Contract</option>
          <option value="224 Construction of Important Bridges">
            224 Construction of Important Bridges
          </option>
        </select>

        {breadcrumb.length > 0 && (
          <button onClick={goBack}>Back</button>
        )}
      </div>

      <div className={styles.breadcrumb}>
        {breadcrumb.map((b, i) => (
          <span key={i}> › {b.name}</span>
        ))}
      </div>

      {loading && <div>Loading...</div>}

      <div className={styles.grid}>
        {displayData.map((item, i) => (
          <div
            key={i}
            className={styles.card}
            onClick={() => handleClick(item)}
          >
            <img
              src={
                view === "files"
                  ? item.fileType === "pdf"
                    ? pdfIcon
                    : fileIcon
                  : folderIcon
              }
              alt=""
            />
            <p>{item.name || item.fileName}</p>
          </div>
        ))}
      </div>
    </div>
  );
}

