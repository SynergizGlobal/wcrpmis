import React, { useState, useEffect } from "react";
import { Outlet } from 'react-router-dom';
import { FaTrash } from "react-icons/fa";
import styles from './GeneralStatus.module.css';
import api from "../../../../../api/axiosInstance";
import { API_BASE_URL } from "../../../../../config";

export default function GeneralStatus() {
  const [data, setData] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [search, setSearch] = useState("");
  const [value, setValue] = useState("");
  const [mode, setMode] = useState("add"); // add | edit
  const [editIndex, setEditIndex] = useState(null);
  
  const [generalStatus, setGeneralStatus] = useState("");
  const [contractStatus, setContractStatus] = useState("");

  const filteredData = data.filter(item =>
    item.general_status
      ?.toLowerCase()
      .includes(search.toLowerCase())
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
    setGeneralStatus(item.general_status);
    setContractStatus(item.contract_status);
    setEditIndex(index);
    setShowModal(true);
  };

  
  useEffect(() => {
    api
      .get(`${API_BASE_URL}/api/general-status/list`)
      .then((res) => {
        setData(res.data || []);
      })
      .catch((err) => {
        console.error("Failed to load general status list", err);
      });
  }, []);


  /* ===== SAVE ===== */
  const handleSave = async () => {
    if (!generalStatus.trim()) return;

    try {
      if (mode === "add") {
        await api.post(`${API_BASE_URL}/api/general-status/add`, {
          general_status: generalStatus.trim(),
          contract_status: contractStatus || ""
        });
      } else if (mode === "edit" && editIndex !== null) {
        await api.put(`${API_BASE_URL}/api/general-status/update`, {
          value_old: data[editIndex].general_status,
          value_new: generalStatus.trim(),
          contract_status_new: contractStatus || ""
        });
      }

      // ✅ close modal
      setShowModal(false);
      setGeneralStatus("");
      setContractStatus("");
      setEditIndex(null);

      // ✅ reload table
      const res = await api.get(`${API_BASE_URL}/api/general-status/list`);
      setData(res.data || []);
    } catch (err) {
      console.error("Save failed", err);
      alert("Operation failed. Check console.");
    }
  };
  
  const handleDelete = async (item) => {
    if (!window.confirm("Delete this General Status?")) return;

    try {
      await api.delete(`${API_BASE_URL}/api/general-status/delete`, {
        params: { general_status: item.general_status }
      });

      const res = await api.get(`${API_BASE_URL}/api/general-status/list`);
      setData(res.data || []);
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
          <h2 className="center-align ps-relative">
            General Status
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
              + Add General Status
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
                  <th>General Status</th>
				  <th>Contract Status</th>
                  <th className={styles.actionCol}>Action</th>
                </tr>
              </thead>
              <tbody>
			  {filteredData.map((item, index) => (
			    <tr key={index}>
			      <td>{item.general_status}</td>
			      <td>{item.contract_status}</td>
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
                  ? "Add General Status"
                  : "Edit General Status"}
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
			    placeholder="General Status"
			    value={generalStatus}
			    onChange={(e) => setGeneralStatus(e.target.value)}
			  />

			  <input
			    type="text"
			    placeholder="Contract Status"
			    value={contractStatus}
			    onChange={(e) => setContractStatus(e.target.value)}
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
