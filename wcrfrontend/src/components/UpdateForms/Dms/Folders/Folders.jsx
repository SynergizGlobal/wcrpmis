import React, { useState, useMemo } from "react";
import { Outlet } from "react-router-dom";
import styles from "./Folders.module.css";
import { dmsData } from "../../../../data/mockDmsData";

import folderIcon from "../../../../assets/images/icons/folder.svg";
import pdfIcon from "../../../../assets/images/icons/pdf.svg";
import fileIcon from "../../../../assets/images/icons/file.svg";

export default function Folders() {
  const [project, setProject] = useState("");
  const [contract, setContract] = useState("");
  const [path, setPath] = useState([]);

  const folders = useMemo(() => {
    let result = [];

    // Nothing selected
    if (!project && !contract) return [];

    // Project selected
    if (project && dmsData[project]?.folders) {
      result = [...dmsData[project].folders];
    }

    // Contract selected
    if (contract) {
      for (const p in dmsData) {
        const contractFolders =
          dmsData[p]?.contracts?.[contract]?.folders;
        if (contractFolders) {
          result = [...result, ...contractFolders];
        }
      }
    }

    return result;
  }, [project, contract]);

  const currentItems =
    path.length === 0
      ? folders
      : path[path.length - 1].children || [];

  const openFolder = (item) => {
    if (item.children) setPath([...path, item]);
  };

  const goToCrumb = (index) => {
    setPath(path.slice(0, index + 1));
  };

  return (
    <div className={styles.container}>

      {/* FILTER BAR */}
      <div className={styles.filterBar}>
        <select
          value={project}
          onChange={(e) => {
            setProject(e.target.value);
            setPath([]);
          }}
        >
          <option value="">Select Project</option>
          <option value="MUTP IIIA">MUTP IIIA</option>
        </select>

        <select
          value={contract}
          onChange={(e) => {
            setContract(e.target.value);
            setPath([]);
          }}
        >
          <option value="">Select Contract</option>
          <option value="224 Construction of Important Bridges">
            224 Construction of Important Bridges
          </option>
        </select>
      </div>

      {/* BREADCRUMB */}
      {(project || contract || path.length > 0) && (
        <div className={styles.breadcrumb}>
          <span onClick={() => setPath([])}>Home</span>
          {path.map((p, i) => (
            <span key={i} onClick={() => goToCrumb(i)}>
              {" > "} {p.name}
            </span>
          ))}
        </div>
      )}

      {/* EMPTY STATE */}
      {folders.length === 0 && (
        <div className={styles.empty}>
          Please select a <b>Project</b> or <b>Contract</b> to view folders
        </div>
      )}

      {/* GRID */}
      {folders.length > 0 && (
        <div className={styles.grid}>
          {currentItems.map((item, i) => (
            <div
              key={i}
              className={styles.card}
              onClick={() => openFolder(item)}
            >
              <img
                src={
                  item.children
                    ? folderIcon
                    : item.fileType === "pdf"
                    ? pdfIcon
                    : fileIcon
                }
                alt=""
              />
              <p title={item.name}>{item.name}</p>
            </div>
          ))}
        </div>
      )}

      <Outlet />
    </div>
  );
}
