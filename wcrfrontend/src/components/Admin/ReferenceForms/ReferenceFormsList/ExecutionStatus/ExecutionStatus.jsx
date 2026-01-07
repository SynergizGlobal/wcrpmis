import React, { useState, useEffect } from "react";
import { FaTrash } from "react-icons/fa";
import styles from './ExecutionStatus.module.css';
import api from "../../../../../api/axiosInstance";
import { API_BASE_URL } from "../../../../../config";

export default function ExecutionStatus() {
  const [data, setData] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [search, setSearch] = useState("");
  const [executionStatus, setExecutionStatus] = useState("");
  const [mode, setMode] = useState("add"); // add | edit
  const [editIndex, setEditIndex] = useState(null);

  /* ================= LOAD DATA ================= */
  useEffect(() => {
    loadExecutionStatus();
  }, []);

  const loadExecutionStatus = async () => {
    try {
      const res = await api.get(`${API_BASE_URL}/api/execution-status/list`);
      setData(res.data || []);
    } catch (err) {
      console.error("Failed to load execution status", err);
    }
  };

  /* ================= SEARCH ================= */
  const filteredData = data.filter(item =>
    item.execution_status
      ?.toLowerCase()
      .includes(search.toLowerCase())
  );

  /* ================= ADD ================= */
  const handleAddClick = () => {
    setMode("add");
    setExecutionStatus("");
    setEditIndex(null);
    setShowModal(true);
  };

  /* ================= EDIT ================= */
  const handleEditClick = (item, index) => {
    setMode("edit");
    setExecutionStatus(item.execution_status);
    setEditIndex(index);
    setShowModal(true);
  };

  /* ================= SAVE (ADD / UPDATE) ================= */
  const handleSave = async () => {
    if (!executionStatus.trim()) return;

    try {
      if (mode === "add") {
        await api.post(`${API_BASE_URL}/api/execution-status/add`, {
          execution_status: executionStatus.trim()
        });
      } else if (mode === "edit" && editIndex !== null) {
        await api.put(`${API_BASE_URL}/api/execution-status/update`, {
          value_old: data[editIndex].execution_status,
          value_new: executionStatus.trim()
        });
      }

      setShowModal(false);
      setExecutionStatus("");
      setEditIndex(null);

      loadExecutionStatus(); // reload from DB
    } catch (err) {
      console.error("Save failed", err);
      alert("Operation failed. Check console.");
    }
  };

  /* ================= DELETE ================= */
  const handleDelete = async (item) => {
    if (!window.confirm("Delete this Execution Status?")) return;

    try {
      await api.delete(`${API_BASE_URL}/api/execution-status/delete`, {
        params: { execution_status: item.execution_status }
      });
      loadExecutionStatus();
    } catch (err) {
      console.error("Delete failed", err);
      alert("Delete failed. Check console.");
    }
  };

  return (
    <div className={styles.container}>
      <div className="card">

        {/* HEADER */}
        <div className="formHeading">
          <h2 className="center-align ps-relative">Execution Status</h2>
        </div>

        <div className="innerPage">

          {/* ADD BUTTON */}
          <div className={styles.topActions}>
            <button className="btn btn-primary" onClick={handleAddClick}>
              + Add Execution Status
            </button>
          </div>

          {/* SEARCH */}
          <div className={styles.searchBox}>
            <input
              type="text"
              placeholder="Search"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />
            {search && (
              <span className={styles.clear} onClick={() => setSearch("")}>
                ✕
              </span>
            )}
          </div>

          {/* TABLE */}
          <div className={`dataTable ${styles.tableWrapper}`}>
            <table className={styles.table}>
              <thead>
                <tr>
                  <th>Execution Status</th>
                  <th className={styles.actionCol}>Action</th>
                </tr>
              </thead>
              <tbody>
                {filteredData.map((item, index) => (
                  <tr key={index}>
                    <td>{item.execution_status}</td>
                    <td className={styles.actionCol}>
                      <button
                        className={styles.editBtn}
                        onClick={() => handleEditClick(item, index)}
                      >
                        ✎
                      </button>
                      <button
                        className={styles.deleteBtn}
                        onClick={() => handleDelete(item)}
                      >
                        <FaTrash />
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <div className={styles.footerText}>
            Showing 1 to {filteredData.length} of {filteredData.length} entries
          </div>
        </div>
      </div>

      {/* MODAL */}
      {showModal && (
        <div className={styles.modalOverlay}>
          <div className={styles.modal}>
            <div className={styles.modalHeader}>
              <span>{mode === "add" ? "Add Execution Status" : "Edit Execution Status"}</span>
              <span className={styles.close} onClick={() => setShowModal(false)}>✕</span>
            </div>

            <div className={styles.modalBody}>
              <input
                type="text"
                placeholder="Execution Status"
                value={executionStatus}
                onChange={(e) => setExecutionStatus(e.target.value)}
              />

              <div className={styles.modalActions}>
                <button className="btn btn-primary" onClick={handleSave}>
                  {mode === "add" ? "ADD" : "UPDATE"}
                </button>
                <button className="btn btn-white" onClick={() => setShowModal(false)}>
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