import React, { useEffect, useState } from "react";
import styles from "./IssuePriority.module.css";
import { API_BASE_URL } from "../../../../../config.js";
import { FaEdit, FaTrash } from "react-icons/fa";

export default function IssuePriority() {
  const [data, setData] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [search, setSearch] = useState("");
  const [value, setValue] = useState("");
  const [mode, setMode] = useState("add");
  const [editIndex, setEditIndex] = useState(null);

  /* ================= FETCH LIST ================= */
  const fetchIssuePriorities = async () => {
    try {
      const res = await fetch(`${API_BASE_URL}/issue-priority`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({})
      });

      const json = await res.json();

      const list =
        (json.issuePriority || [])
          .map(item => item.priority)
          .filter(Boolean);

      setData(list);
    } catch (err) {
      console.error("Failed to load Issue Priority", err);
    }
  };

  useEffect(() => {
    fetchIssuePriorities();
  }, []);

  /* ================= FILTER ================= */
  const filteredData = data.filter(item =>
    item.toLowerCase().includes(search.toLowerCase())
  );

  /* ================= ADD ================= */
  const handleAddClick = () => {
    setMode("add");
    setValue("");
    setEditIndex(null);
    setShowModal(true);
  };

  /* ================= EDIT ================= */
  const handleEditClick = (item, index) => {
    setMode("edit");
    setValue(item);
    setEditIndex(index);
    setShowModal(true);
  };

  /* ================= SAVE ================= */
  const handleSave = async () => {
    if (!value.trim()) return;

    const formData = new FormData();

    if (mode === "add") {
      formData.append("priority", value.trim());

      await fetch(`${API_BASE_URL}/add-issue-priority`, {
        method: "POST",
        body: formData
      });

      alert("Issue Priority added successfully.");
    } 
    else if (mode === "edit" && editIndex !== null) {
      formData.append("value_old", data[editIndex]);
      formData.append("value_new", value.trim());

      await fetch(`${API_BASE_URL}/update-issue-priority`, {
        method: "POST",
        body: formData
      });

      alert("Issue Priority updated successfully.");
    }

    setShowModal(false);
    setValue("");
    setEditIndex(null);
    fetchIssuePriorities();
  };

  /* ================= DELETE ================= */
  const handleDelete = async (item) => {
    if (!window.confirm(`Delete "${item}" ?`)) return;

    const formData = new FormData();
    formData.append("priority", item);

    await fetch(`${API_BASE_URL}/delete-issue-priority`, {
      method: "POST",
      body: formData
    });

    alert("Issue Priority deleted successfully.");
    fetchIssuePriorities();
  };

  return (
    <div className={styles.container}>
      <div className="card">
        <div className="formHeading">
          <h2 className="center-align">Issue Priority</h2>
        </div>

        <div className="innerPage">
          <div className={styles.topActions}>
            <button className="btn btn-primary" onClick={handleAddClick}>
              + Add Issue Priority
            </button>
          </div>

          <div className={styles.searchBox}>
            <input
              type="text"
              placeholder="Search"
              value={search}
              onChange={e => setSearch(e.target.value)}
            />
            {search && (
              <span className={styles.clear} onClick={() => setSearch("")}>
                ✕
              </span>
            )}
          </div>

          <div className={`dataTable ${styles.tableWrapper}`}>
            <table className={styles.table}>
              <thead>
                <tr>
                  <th>Issue Priority</th>
                  <th className={styles.actionCol}>Action</th>
                </tr>
              </thead>
              <tbody>
                {filteredData.map((item, index) => (
                  <tr key={index}>
                    <td>{item}</td>
                    <td className={styles.actionCol}>
                      <div className={styles.actionButtons}>
                        <button
                          className={styles.editBtn}
                          onClick={() => handleEditClick(item, index)}
                        >
                          <FaEdit />
                        </button>
                        <button
                          className={styles.deleteBtn}
                          onClick={() => handleDelete(item)}
                        >
                          <FaTrash />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          <div className={styles.footerText}>
            Showing {filteredData.length} entries
          </div>
        </div>
      </div>

      {showModal && (
        <div className={styles.modalOverlay}>
          <div className={styles.modal}>
            <div className={styles.modalHeader}>
              <span>
                {mode === "add" ? "Add Issue Priority" : "Edit Issue Priority"}
              </span>
              <span
                className={styles.close}
                onClick={() => setShowModal(false)}
              >
                ✕
              </span>
            </div>

            <div className={styles.modalBody}>
              <input
                type="text"
                placeholder="Issue Priority"
                value={value}
                onChange={e => setValue(e.target.value)}
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
