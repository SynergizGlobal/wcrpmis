import React, { useState, useEffect} from "react";
import { Outlet } from 'react-router-dom';
import { FaTrash } from "react-icons/fa";
import styles from './StructureType.module.css';
import api from "../../../../../api/axiosInstance";
import { API_BASE_URL } from "../../../../../config";

const INITIAL_DATA = [

];

export default function StructureType() {
  const [data, setData] = useState(INITIAL_DATA);
  const [showModal, setShowModal] = useState(false);
  const [search, setSearch] = useState("");
  const [value, setValue] = useState("");
  const [mode, setMode] = useState("add"); // add | edit
  const [editIndex, setEditIndex] = useState(null);

  const filteredData = data.filter(item =>
    item.toLowerCase().includes(search.toLowerCase())
  );

  /* ===== ADD ===== */
  const handleAddClick = () => {
    setMode("add");
    setValue("");
    setEditIndex(null);
    setShowModal(true);
  };

  /* ===== EDIT ===== */
  const handleEditClick = (item, index) => {
    setMode("edit");
    setValue(item);
    setEditIndex(index);
    setShowModal(true);
  };
  
  useEffect(() => {
    loadStructureTypes();
  }, []);

  const loadStructureTypes = async () => {
    try {
      const res = await api.get(`${API_BASE_URL}/api/structure-type/list`);
      setData(
        res.data
          .map(item => item.structure_type)
          .filter(Boolean)
      );
    } catch (err) {
      console.error("Error loading structure types", err);
    }
  };


  /* ===== SAVE ===== */
  const handleSave = async () => {
    if (!value.trim()) return;

    try {
      if (mode === "add") {
        await api.post(`${API_BASE_URL}/api/structure-type/add`, {
          structure_type: value.trim()
        });
      } else if (mode === "edit" && editIndex !== null) {
        await api.put(`${API_BASE_URL}/api/structure-type/update`, {
          value_old: data[editIndex],
          value_new: value.trim()
        });
      }

      await loadStructureTypes();
      setShowModal(false);
      setValue("");
      setEditIndex(null);
    } catch (err) {
      console.error("Save failed", err);
    }
  };

  const handleDelete = async (item) => {
    if (!window.confirm(`Delete "${item}" ?`)) return;

    try {
      await api.delete(`${API_BASE_URL}/api/structure-type/delete`, {
        data: { structure_type: item }
      });

      await loadStructureTypes();
    } catch (err) {
      console.error("Delete failed", err);
    }
  };


  return (
    <div className={styles.container}>
      <div className="card">
        {/* HEADER */}
        <div className="formHeading">
          <h2 className="center-align ps-relative">
            Structure Type
          </h2>
        </div>

        {/* INNER PAGE */}
        <div className="innerPage">
          {/* ADD BUTTON */}
          <div className={styles.topActions}>
            <button
              className="btn btn-primary"
              onClick={handleAddClick}
            >
              + Add Structure Type
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
              <span
                className={styles.clear}
                onClick={() => setSearch("")}
              >
                ✕
              </span>
            )}
          </div>

          {/* TABLE */}
          <div className={`dataTable ${styles.tableWrapper}`}>
            <table className={styles.table}>
              <thead>
                <tr>
                  <th>Structure Type</th>
                  <th className={styles.actionCol}>Action</th>
                </tr>
              </thead>
              <tbody>
                {filteredData.map((item, index) => (
                  <tr key={index}>
                    <td>{item}</td>
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
              <span>
                {mode === "add"
                  ? "Add Structure Type"
                  : "Edit Structure Type"}
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
                placeholder="Structure Type"
                value={value}
                onChange={(e) => setValue(e.target.value)}
              />

              <div className={styles.modalActions}>
                <button
                  className="btn btn-primary"
                  onClick={handleSave}
                >
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
