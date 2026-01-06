import React, { useEffect, useState } from "react";
import styles from "./IssueStatus.module.css";
import { API_BASE_URL } from "../../../../../config.js";
import { FaEdit, FaTrash } from "react-icons/fa";

export default function IssueStatus() {
  const [data, setData] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [search, setSearch] = useState("");
  const [value, setValue] = useState("");
  const [mode, setMode] = useState("add");
  const [editIndex, setEditIndex] = useState(null);

  /* ================= FETCH STATUS + COUNT ================= */
  const fetchIssueStatus = async () => {
    try {
      const res = await fetch(`${API_BASE_URL}/issue-status`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({})
      });

      const json = await res.json();

      const statusList = json.issueStatusList || [];
      const countList = json.issueStatusDetails?.countList || [];

      // Convert countList → Map { status: count }
      const countMap = {};
      countList.forEach(c => {
        countMap[c.status] = Number(c.count || 0);
      });

      // Merge status + count
      const merged = statusList.map(s => ({
        status: s.status,
        count: countMap[s.status] || 0
      }));

      setData(merged);
    } catch (err) {
      console.error("Failed to load Issue Status", err);
    }
  };

  useEffect(() => {
    fetchIssueStatus();
  }, []);

  /* ================= FILTER ================= */
  const filteredData = data.filter(item =>
    item.status.toLowerCase().includes(search.toLowerCase())
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
    setValue(item.status);
    setEditIndex(index);
    setShowModal(true);
  };

  /* ================= SAVE ================= */
  const handleSave = async () => {
    if (!value.trim()) return;

    const formData = new FormData();

    if (mode === "add") {
      formData.append("status", value.trim());

      await fetch(`${API_BASE_URL}/add-issue-status`, {
        method: "POST",
        body: formData
      });

      alert("Status added successfully.");
    } 
    else if (mode === "edit" && editIndex !== null) {
      formData.append("status_old", data[editIndex].status);
      formData.append("status_new", value.trim());

      await fetch(`${API_BASE_URL}/update-issue-status`, {
        method: "POST",
        body: formData
      });

      alert("Status updated successfully.");
    }

    setShowModal(false);
    setValue("");
    setEditIndex(null);
    fetchIssueStatus();
  };

  /* ================= DELETE ================= */
  const handleDelete = async (item) => {
    if (!window.confirm(`Delete "${item.status}" ?`)) return;

    const formData = new FormData();
    formData.append("status", item.status);

    await fetch(`${API_BASE_URL}/delete-issue-status`, {
      method: "POST",
      body: formData
    });

    alert("Status deleted successfully.");
    fetchIssueStatus();
  };

  return (
    <div className={styles.container}>
      <div className="card">
        <div className="formHeading">
          <h2 className="center-align">Issue Status</h2>
        </div>

        <div className="innerPage">
          <div className={styles.topActions}>
            <button className="btn btn-primary" onClick={handleAddClick}>
              + Add Status
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
                  <th>Status</th>
                  <th>Issue</th>
                  <th className={styles.actionCol}>Action</th>
                </tr>
              </thead>
			  <tbody>
			    {filteredData.map((item, index) => {
			      const canDelete = !item.count || item.count === 0;

			      return (
			        <tr key={index}>
			          <td>{item.status}</td>
			          <td>{item.count}</td>

			          <td className={styles.actionCol}>
			            <div className={styles.actionButtons}>
			              {/* EDIT → always visible */}
			              <button
			                className={styles.editBtn}
			                onClick={() => handleEditClick(item, index)}
			                title="Edit"
			              >
			                <FaEdit />
			              </button>

			              {/* DELETE → only if no dependency */}
			              {canDelete && (
			                <button
			                  className={styles.deleteBtn}
			                  onClick={() => handleDelete(item)}
			                  title="Delete"
			                >
			                  <FaTrash />
			                </button>
			              )}
			            </div>
			          </td>
			        </tr>
			      );
			    })}
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
              <span>{mode === "add" ? "Add Status" : "Edit Status"}</span>
              <span className={styles.close} onClick={() => setShowModal(false)}>
                ✕
              </span>
            </div>

            <div className={styles.modalBody}>
              <input
                type="text"
                placeholder="Status"
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
