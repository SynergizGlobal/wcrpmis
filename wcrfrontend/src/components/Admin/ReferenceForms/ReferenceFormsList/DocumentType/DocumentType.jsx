import React, { useEffect, useState } from "react";
import { API_BASE_URL } from "../../../../../config.js";
import { FaEdit, FaTrash } from "react-icons/fa";
import styles from "./DocumentType.module.css";
import Swal from "sweetalert2";

export default function DocumentType() {
  const [rows, setRows] = useState([]);
  const [columns, setColumns] = useState([]);
  const [search, setSearch] = useState("");

  const [showModal, setShowModal] = useState(false);
  const [mode, setMode] = useState("add");

  const [documentType, setDocumentType] = useState("");
  const [documentTypeOld, setDocumentTypeOld] = useState("");

  /* ================= FETCH ================= */
  const fetchData = async () => {
    try {
      const res = await fetch(`${API_BASE_URL}/document-type`, {
        method: "GET",
        credentials: "include"
      });

      const json = await res.json();
      if (json.message !== "success") return;

      const documentTypeList = json.documentTypeList || [];
      const tablesList = json.documentTypeDetails?.tablesList || [];
      const countList = json.documentTypeDetails?.countList || [];

      /* ---- DISTINCT FK TABLE NAMES ---- */
      const columnNames = [
        ...new Set(
          tablesList
            .map(t => t?.tName)
            .filter(Boolean)
        )
      ];

      setColumns(columnNames);

      /* ---- document_type | table -> count ---- */
      const countMap = {};
      countList.forEach(c => {
        if (c?.document_type && c?.tName) {
          countMap[`${c.document_type}|${c.tName}`] = Number(c.count || 0);
        }
      });

      /* ---- MERGE DATA ---- */
      const merged = documentTypeList.map((d, idx) => {
        const counts = {};
        columnNames.forEach(col => {
          counts[col] = countMap[`${d.document_type}|${col}`] || 0;
        });

        return {
          id: idx + 1,
          documentType: d.document_type ?? "",
          counts
        };
      });

      setRows(merged);
    } catch (err) {
      console.error("Fetch failed", err);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);
  



  /* ================= SEARCH ================= */
  const filteredRows = rows.filter(r =>
    `${r.documentType || ""}`
      .toLowerCase()
      .includes((search || "").toLowerCase())
  );
  const startIndex = filteredRows.length ? 1 : 0;
  const endIndex = filteredRows.length;
  const total = filteredRows.length;

  /* ================= ADD ================= */
  const handleAdd = () => {
    setMode("add");
    setDocumentType("");
    setDocumentTypeOld("");
    setShowModal(true);
  };

  /* ================= EDIT ================= */
  const handleEdit = row => {
    setMode("edit");
    setDocumentType(row.documentType);
    setDocumentTypeOld(row.documentType);
    setShowModal(true);
  };

  /* ================= SAVE ================= */
  const handleSave = async () => {
    if (!documentType.trim()) {
      Swal.fire("Validation", "Document Type is required", "warning");
      return;
    }

    const formData = new FormData();

    try {
      if (mode === "add") {
        formData.append("document_type", documentType);

        await fetch(`${API_BASE_URL}/add-document-type`, {
          method: "POST",
          body: formData
        });
      } else {
        formData.append("document_type_old", documentTypeOld);
        formData.append("document_type_new", documentType);

        await fetch(`${API_BASE_URL}/update-document-type`, {
          method: "POST",
          body: formData
        });
      }

      setShowModal(false);
      fetchData();
      Swal.fire("Success", "Saved successfully", "success");
    } catch (err) {
      Swal.fire("Error", "Something went wrong", "error");
    }
  };

  /* ================= DELETE ================= */
  const handleDelete = row => {
    const hasRefs = Object.values(row.counts).some(v => v > 0);
    if (hasRefs) return;

    Swal.fire({
      title: "Delete Document Type?",
      text: "This action cannot be undone",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Yes, Delete"
    }).then(async result => {
      if (result.isConfirmed) {
        const formData = new FormData();
        formData.append("document_type", row.documentType);

        await fetch(`${API_BASE_URL}/delete-document-type`, {
          method: "POST",
          body: formData
        });

        fetchData();
        Swal.fire("Deleted", "Document Type deleted successfully", "success");
      }
    });
  };

  return (
    <div className={styles.container}>
      <div className="card">
        <div className="formHeading">
          <h2 className="center-align">Document Type</h2>
        </div>

        <div className="innerPage">
          <div className={styles.topActions}>
            <button className="btn btn-primary" onClick={handleAdd}>
              + Add Document Type
            </button>
          </div>

          <div className={styles.searchBox}>
            <input
              placeholder="Search Document Type"
              value={search}
              onChange={e => setSearch(e.target.value)}
            />
          </div>

          <div className={`dataTable ${styles.tableWrapper}`}>
            <table className={styles.table}>
              <thead>
                <tr>
                  <th>Document Type</th>
                  {columns.map(c => (
                    <th key={c}>{c.toUpperCase()}</th>
                  ))}
                  <th>Action</th>
                </tr>
              </thead>

              <tbody>
                {filteredRows.map(r => (
                  <tr key={r.id}>
                    <td>{r.documentType}</td>

                    {columns.map(c => (
                      <td key={c}>
                        {r.counts[c] ? `(${r.counts[c]})` : ""}
                      </td>
                    ))}

                    <td className={styles.actionCol}>
					<div className={styles.actionButtons}>
                        <button
                          className={styles.editBtn}
                          onClick={() => handleEdit(r)}
                          title="Edit"
                        >
                          <FaEdit />
                        </button>

                        {!Object.values(r.counts).some(v => v > 0) && (
                          <button
                            className={styles.deleteBtn}
                            onClick={() => handleDelete(r)}
                            title="Delete"
                          >
                            <FaTrash />
                          </button>
                        )}
                      </div>
                    </td>
                  </tr>
                ))}

                {filteredRows.length === 0 && (
                  <tr>
                    <td colSpan={columns.length + 2} className="center-align">
                      No records found
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
		  <div className={styles.footerText}>
		    Showing {startIndex} to {endIndex} of {total}
		  </div>

        </div>
      </div>

      {/* ================= MODAL ================= */}
      {showModal && (
        <div className={styles.modalOverlay}>
          <div className={styles.modal}>
            <div className={styles.modalHeader}>
              <span>{mode === "add" ? "Add" : "Update"} Document Type</span>
              <span
                className={styles.close}
                onClick={() => setShowModal(false)}
              >
                ✕
              </span>
            </div>

            <div className={styles.modalBody}>
              <label>Document Type</label>
              <input
                value={documentType}
                onChange={e => setDocumentType(e.target.value)}
              />

              <div className={styles.modalActions}>
                <button className="btn btn-primary" onClick={handleSave}>
                  {mode === "add" ? "ADD" : "UPDATE"}
                </button>
                <button
                  className="btn btn-white"
                  onClick={() => setShowModal(false)}
                >
                  CANCEL
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
