import React, { useEffect, useState } from "react";
import { API_BASE_URL } from "../../../../../config.js";
import { FaEdit, FaTrash } from "react-icons/fa";
import styles from "./DesignFileType.module.css";

export default function DesignFileType() {
  const [data, setData] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [search, setSearch] = useState("");
  const [value, setValue] = useState("");
  const [mode, setMode] = useState("add");
  const [editIndex, setEditIndex] = useState(null);

  /* ================= FETCH LIST ================= */
  const fetchDesignFileTypes = async () => {
    try {
      const res = await fetch(`${API_BASE_URL}/design-file-type`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({})
      });

      const json = await res.json();

      const list =
        json?.designFileTypeDetails?.dList1
          ?.map(item => item.design_file_type)
          .filter(Boolean) || [];

      setData(list);
    } catch (err) {
      console.error("Failed to load Design File Type", err);
    }
  };

  useEffect(() => {
    fetchDesignFileTypes();
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
      formData.append("design_file_type", value.trim());

      await fetch(`${API_BASE_URL}/add-design-file-type`, {
        method: "POST",
        body: formData
      });

      alert("Design File Type added successfully.");
    } 
    else if (mode === "edit" && editIndex !== null) {
      formData.append("value_old", data[editIndex]);
      formData.append("value_new", value.trim());

      await fetch(`${API_BASE_URL}/update-design-file-type`, {
        method: "POST",
        body: formData
      });

      alert("Design File Type updated successfully.");
    }

    setShowModal(false);
    setValue("");
    setEditIndex(null);
    fetchDesignFileTypes();
  };

  /* ================= DELETE ================= */
  const handleDelete = async (item) => {
    if (!window.confirm(`Delete "${item}" ?`)) return;

    const formData = new FormData();
    formData.append("design_file_type", item);

    await fetch(`${API_BASE_URL}/delete-design-file-type`, {
      method: "POST",
      body: formData
    });

    alert("Design File Type deleted successfully.");
    fetchDesignFileTypes();
  };

  return (
    <div className={styles.container}>
      <div className="card">
        <div className="formHeading">
          <h2 className="center-align">Design File Type</h2>
        </div>

        <div className="innerPage">
          {/* ADD */}
          <div className={styles.topActions}>
            <button className="btn btn-primary" onClick={handleAddClick}>
              + Add Design File Type
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
                  <th>Design File Type</th>
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
                {mode === "add" ? "Add Design File Type" : "Edit Design File Type"}
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
                placeholder="Design File Type"
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
