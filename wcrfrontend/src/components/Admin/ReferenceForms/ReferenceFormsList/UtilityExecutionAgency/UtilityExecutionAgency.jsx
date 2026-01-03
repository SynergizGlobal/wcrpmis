import React, { useState, useEffect } from "react";
import { FaEdit, FaTrash, FaTimes } from "react-icons/fa";
import styles from "./UtilityExecutionAgency.module.css";
import { API_BASE_URL } from "../../../../../config";

const API_ENDPOINTS = {
  GET: "/utility-execution-agency",
  ADD: "/add-utility-execution-agency",
  UPDATE: "/update-utility-execution-agency",
  DELETE: "/delete-utility-execution-agency"
};

export default function UtilityExecutionAgency() {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [search, setSearch] = useState("");
  const [value, setValue] = useState("");
  const [mode, setMode] = useState("add");
  const [editIndex, setEditIndex] = useState(null);
  const [saving, setSaving] = useState(false);

  /* ================= FETCH (GET) ================= */
  const fetchData = async () => {
    try {
      setLoading(true);

      const response = await fetch(
        `${API_BASE_URL}${API_ENDPOINTS.GET}`,
        { method: "GET" }
      );

      const json = await response.json();

      if (!json.success) {
        throw new Error("Failed to fetch Utility Execution Agency list");
      }

      const list =
        json?.utilityExecutionAgencyList?.countList?.map(item => ({
          execution_agency: item.execution_agency,
          count: item.count
        })) || [];

      setData(list);
    } catch (error) {
      console.error(error);
      setData([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  /* ================= FILTER ================= */
  const filteredData = data.filter(item =>
    item.execution_agency
      ?.toLowerCase()
      .includes(search.toLowerCase())
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
    setValue(item.execution_agency);
    setEditIndex(index);
    setShowModal(true);
  };

  /* ================= DELETE ================= */
  const handleDeleteClick = async (index) => {
    if (!window.confirm("Are you sure you want to delete this execution agency?")) {
      return;
    }

    try {
      const response = await fetch(
        `${API_BASE_URL}${API_ENDPOINTS.DELETE}/${encodeURIComponent(
          data[index].execution_agency
        )}`,
        { method: "DELETE" }
      );

      const result = await response.json();

      if (!result.success) {
        throw new Error(result.message);
      }

      setData(prev => prev.filter((_, i) => i !== index));
    } catch (error) {
      console.error(error);
      alert("Failed to delete execution agency");
    }
  };

  /* ================= SAVE (ADD / UPDATE) ================= */
  const handleSave = async () => {
    if (!value.trim()) {
      alert("Execution Agency is required");
      return;
    }

    try {
      setSaving(true);
      let response;

      if (mode === "add") {
        // ✅ FIXED: correct JSON key
        response = await fetch(
          `${API_BASE_URL}${API_ENDPOINTS.ADD}`,
          {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
              execution_agency: value.trim()
            })
          }
        );
      } else {
        response = await fetch(
          `${API_BASE_URL}${API_ENDPOINTS.UPDATE}`,
          {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
              value_old: data[editIndex].execution_agency,
              value_new: value.trim()
            })
          }
        );
      }

      const result = await response.json();

      if (!result.success) {
        throw new Error(result.message);
      }

      if (mode === "add") {
        setData(prev => [
          { execution_agency: value.trim(), count: "0" },
          ...prev
        ]);
      } else {
        setData(prev =>
          prev.map((item, index) =>
            index === editIndex
              ? { ...item, execution_agency: value.trim() }
              : item
          )
        );
      }

      setShowModal(false);
      setValue("");
      setEditIndex(null);

    } catch (error) {
      console.error(error);
      alert("Save failed");
    } finally {
      setSaving(false);
    }
  };

  /* ================= UI ================= */
  return (
    <div className={styles.container}>
      <div className="card">
        <div className="formHeading">
          <h2 className="center-align">Utility Execution Agency</h2>
        </div>

        <div className="innerPage">
          <div className={styles.topActions}>
            <button
              className="btn btn-primary"
              onClick={handleAddClick}
              disabled={loading}
            >
              + Add Utility Execution Agency
            </button>
          </div>

          <div className={styles.searchBox}>
            <input
              type="text"
              placeholder="Search execution agency..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              disabled={loading}
            />
            {search && (
              <span className={styles.clear} onClick={() => setSearch("")}>
                <FaTimes />
              </span>
            )}
          </div>

          <div className={`dataTable ${styles.tableWrapper}`}>
            {loading ? (
              <div className={styles.loading}>Loading...</div>
            ) : (
              <table className={styles.table}>
                <thead>
                  <tr>
                    <th>Execution Agency</th>
                    <th>Count</th>
                    <th className={styles.actionCol}>Action</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredData.length ? (
                    filteredData.map((item, index) => (
                      <tr key={index}>
                        <td>{item.execution_agency}</td>
                        <td>{item.count}</td>
                        <td className={styles.actionCol}>
                          <button
                            className={styles.editBtn}
                            onClick={() => handleEditClick(item, index)}
                          >
                            <FaEdit />
                          </button>
                          <button
                            className={styles.deleteBtn}
                            onClick={() => handleDeleteClick(index)}
                          >
                            <FaTrash />
                          </button>
                        </td>
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td colSpan="3" className={styles.noData}>
                        No data available
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            )}
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
                  ? "Add Utility Execution Agency"
                  : "Edit Utility Execution Agency"}
              </span>
              <FaTimes
                className={styles.close}
                onClick={() => !saving && setShowModal(false)}
              />
            </div>

            <div className={styles.modalBody}>
              <input
                type="text"
                placeholder="Execution Agency"
                value={value}
                onChange={(e) => setValue(e.target.value)}
                autoFocus
                disabled={saving}
              />

              <div className={styles.modalActions}>
                <button
                  className="btn btn-primary"
                  onClick={handleSave}
                  disabled={saving}
                >
                  {saving ? "Saving..." : mode === "add" ? "ADD" : "UPDATE"}
                </button>
                <button
                  className="btn btn-white"
                  onClick={() => setShowModal(false)}
                  disabled={saving}
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
