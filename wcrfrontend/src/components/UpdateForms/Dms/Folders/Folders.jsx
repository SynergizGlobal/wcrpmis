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

  const [view, setView] = useState("folders");       // "folders" | "subfolders" | "files"
  const [breadcrumb, setBreadcrumb] = useState([]);

  const [loading, setLoading] = useState(false);

  // ─── fetch projects / contracts for dropdowns ───────────────────────────────
  const [projects, setProjects] = useState([]);
  const [contracts, setContracts] = useState([]);

  useEffect(() => {
    api.get("/projects/get-project-name")
      .then(r => setProjects(r.data || []))
      .catch(() => {});

    api.get("/contract/get-contract-name")
      .then(r => setContracts(r.data || []))
      .catch(() => {});
  }, []);

  // ─── load folders only when BOTH project & contract are chosen ───────────────
  useEffect(() => {
    // reset view whenever filters change
    setDisplayData([]);
    setAllFolders([]);
    setView("folders");
    setBreadcrumb([]);

    if (!project || !contract) return;   // ← do nothing until both are set

    loadFolders(project, contract);
  }, [project, contract]);

  const loadFolders = async (selectedProject, selectedContract) => {
    setLoading(true);
    try {
      const res = await api.post(
        "/api/folders/grid",
        {
          projects:  [selectedProject],
          contracts: [selectedContract],
        },
        { withCredentials: true }
      );

      const data = Array.isArray(res.data) ? res.data : [];
      setAllFolders(data);
      setDisplayData(data);
    } catch (err) {
      console.error("Folder load error:", err);
    } finally {
      setLoading(false);
    }
  };

  // ─── load files inside a sub-folder ─────────────────────────────────────────
  const loadFiles = async (subFolderId, subFolderName) => {
    setLoading(true);
    try {
      const res = await api.post(
        `/api/subfolders/grid/${subFolderId}`,
        {
          projects:  project  ? [project]  : [],
          contracts: contract ? [contract] : [],
        },
        { withCredentials: true }
      );

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

  // ─── card click ─────────────────────────────────────────────────────────────
  const handleClick = (item) => {
    if (view === "folders") {
      // folder has subFolders → drill into them
      if (item.subFolders?.length > 0) {
        setDisplayData(item.subFolders);
        setView("subfolders");
        setBreadcrumb([{ name: item.name }]);
      }
    } else if (view === "subfolders") {
      // sub-folder clicked → load its files
      loadFiles(item.id, item.name);
    }
  };

  // ─── back button ────────────────────────────────────────────────────────────
  const goBack = () => {
    if (view === "files") {
      // go back to sub-folders of current folder
      const folderName = breadcrumb[0]?.name;
      const parentFolder = allFolders.find(f => f.name === folderName);
      setDisplayData(parentFolder?.subFolders || []);
      setView("subfolders");
      setBreadcrumb(prev => prev.slice(0, -1));
    } else {
      // go back to root folder list
      setDisplayData(allFolders);
      setView("folders");
      setBreadcrumb([]);
    }
  };

  // ─── helpers ────────────────────────────────────────────────────────────────
  const getIcon = (item) => {
    if (view === "files") {
      return item.fileType === "pdf" ? pdfIcon : fileIcon;
    }
    return folderIcon;
  };

  const getLabel = (item) => item.name || item.fileName || "—";

  const showEmpty = !loading && !project && !contract;
  const showSelectContract = !loading && project && !contract;
  const showNoData = !loading && project && contract && displayData.length === 0;

  return (
    <div className={styles.container}>

      {/* ── Filter Bar ── */}
      <div className={styles.filterBar}>
        <select value={project} onChange={e => setProject(e.target.value)}>
          <option value="">— Select Project —</option>
          {projects.map(p => (
            <option key={p.id || p.name} value={p.name}>{p.name}</option>
          ))}
        </select>

        <select
          value={contract}
          onChange={e => setContract(e.target.value)}
          disabled={!project}
        >
          <option value="">— Select Contract —</option>
          {contracts.map(c => (
            <option key={c.id || c.name} value={c.name}>{c.name}</option>
          ))}
        </select>

        {breadcrumb.length > 0 && (
          <button onClick={goBack}>← Back</button>
        )}
      </div>

      {/* ── Breadcrumb ── */}
      {breadcrumb.length > 0 && (
        <div className={styles.breadcrumb}>
          <span
            style={{ cursor: "pointer", color: "#4f6ef7" }}
            onClick={() => { setDisplayData(allFolders); setView("folders"); setBreadcrumb([]); }}
          >
            Home
          </span>
          {breadcrumb.map((b, i) => (
            <span key={i}> › {b.name}</span>
          ))}
        </div>
      )}

      {/* ── States ── */}
      {showEmpty && (
        <div className={styles.emptyState}>
          <p>📁 Please select a <strong>Project</strong> and <strong>Contract</strong> to view folders.</p>
        </div>
      )}

      {showSelectContract && (
        <div className={styles.emptyState}>
          <p>📋 Now select a <strong>Contract</strong> to load folders.</p>
        </div>
      )}

      {loading && <div className={styles.loading}>Loading...</div>}

      {showNoData && (
        <div className={styles.emptyState}>
          <p>No folders found for the selected project and contract.</p>
        </div>
      )}

      {/* ── Grid ── */}
      {!loading && displayData.length > 0 && (
        <div className={styles.grid}>
          {displayData.map((item, i) => (
            <div
              key={item.id || i}
              className={styles.card}
              onClick={() => handleClick(item)}
            >
              <img src={getIcon(item)} alt="" />
              <p>{getLabel(item)}</p>
            </div>
          ))}
        </div>
      )}

    </div>
  );
}