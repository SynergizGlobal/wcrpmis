import React, { useEffect, useState } from "react";
import styles from "./AsBuiltStatus.module.css";
import { API_BASE_URL } from "../../../../../config.js";
import { FaEdit, FaTrash, FaTimes } from "react-icons/fa";
 

export default function AsBuiltStatus() {
  const [data, setData] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [search, setSearch] = useState("");
  const [value, setValue] = useState("");
  const [mode, setMode] = useState("add");
  const [editIndex, setEditIndex] = useState(null);

  /* ================= FETCH LIST ================= */
  const fetchStatuses = async () => {
    try {
      const res = await fetch(`${API_BASE_URL}/as-built-status`);
      const list = await res.json();
      setData(list || []);
    } catch (err) {
      console.error("Failed to load As Built Status", err);
    }
  };

  useEffect(() => {
    fetchStatuses();
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
    if (!value.trim()) {
      alert("Status cannot be empty");
      return;
    }

    const formData = new FormData();
    let url = "";
    let successMsg = "";
    let errorMsg = "";

    if (mode === "add") {
      formData.append("as_built_status", value.trim());
      url = `${API_BASE_URL}/add-as-built-status`;
      successMsg = "Status added successfully";
      errorMsg = "Failed to add status";

    } else if (mode === "edit" && editIndex !== null) {
      formData.append("value_old", data[editIndex]);
      formData.append("value_new", value.trim());
      url = `${API_BASE_URL}/update-as-built-status`;
      successMsg = "Status updated successfully";
      errorMsg = "Failed to update status";
    }

    try {
      const res = await fetch(url, {
        method: "POST",
        body: formData
      });

      let message = "";
      try {
        const json = await res.json();
        message = json?.message;
      } catch (e) {
      }

      if (res.ok) {
        alert(message || successMsg);

        setShowModal(false);
        setValue("");
        setEditIndex(null);
        fetchStatuses(); // reload from DB
      } else {
        alert(message || errorMsg);
      }

    } catch (err) {
      console.error(err);
      alert("Server error. Please try again.");
    }
  };


  /* ================= DELETE ================= */
  const handleDelete = async (item) => {
    if (!window.confirm(`Delete "${item}" ?`)) return;

    const formData = new FormData();
    formData.append("as_built_status", item);

    await fetch(`${API_BASE_URL}/delete-as-built-status`, {
      method: "POST",
      body: formData
    });

    fetchStatuses();
  };

  return (
    <div className={styles.container}>
      <div className="card">
        <div className="formHeading">
          <h2 className="center-align">As Built Status</h2>
        </div>

        <div className="innerPage">
          {/* ADD */}
          <div className={styles.topActions}>
            <button className="btn btn-primary" onClick={handleAddClick}>
              + Add As Built Status
            </button>
          </div>

          {/* SEARCH */}
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

          {/* TABLE */}
          <div className={`dataTable ${styles.tableWrapper}`}>
            <table className={styles.table}>
              <thead>
                <tr>
                  <th>As Built Status</th>
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
					        title="Edit"
					      >
					        <FaEdit />
					      </button>

					      <button
					        className={styles.deleteBtn}
					        onClick={() => handleDelete(item)}
					        title="Delete"
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
            Showing {filteredData.length} of {filteredData.length} entries
          </div>
        </div>
      </div>

      {/* MODAL */}
      {showModal && (
        <div className={styles.modalOverlay}>
          <div className={styles.modal}>
            <div className={styles.modalHeader}>
              <span>
                {mode === "add" ? "Add As Built Status" : "Edit As Built Status"}
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
                placeholder="As Built Status"
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
