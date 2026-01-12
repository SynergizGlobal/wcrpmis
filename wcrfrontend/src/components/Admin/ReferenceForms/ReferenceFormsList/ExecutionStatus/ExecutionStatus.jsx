import React, { useState, useEffect } from "react";
import { FaTrash } from "react-icons/fa";
import styles from './ExecutionStatus.module.css';
import api from "../../../../../api/axiosInstance";
import { API_BASE_URL } from "../../../../../config";

export default function ExecutionStatus() {

  const [showModal, setShowModal] = useState(false);
  const [search, setSearch] = useState("");
  const [value, setValue] = useState("");
  const [mode, setMode] = useState("add"); // add | edit
  const [rows, setRows] = useState([]);
  const [tables, setTables] = useState([]);
  const [oldValue, setOldValue] = useState("");

  /* ================= FILTER ================= */
  const filteredData = rows.filter(row =>
    row.execution_status
      .toLowerCase()
      .includes(search.toLowerCase())
  );

  /* ================= ADD ================= */
  const handleAddClick = () => {
    setMode("add");
    setValue("");
    setOldValue("");
    setShowModal(true);
  };

  /* ================= EDIT ================= */
  const handleEditClick = (item) => {
    setMode("edit");
    setValue(item);
    setOldValue(item); // ✅ IMPORTANT
    setShowModal(true);
  };

  /* ================= LOAD ================= */
  useEffect(() => {
    loadExecutionStatus();
  }, []);

  const loadExecutionStatus = async () => {
    try {
      const res = await api.get(`${API_BASE_URL}/execution-status`);

      const details = res.data.executionStatusDetails;

      const masterList = details.dList1 || [];
      const countList = details.countList || [];
      const tablesList = details.tablesList || [];

      /* ===== BUILD ROWS ===== */
      const mappedRows = masterList.map(es => {
        const counts = {};

        tablesList.forEach(t => {
          counts[t.tName] = 0;
        });

        countList.forEach(c => {
          if (c.execution_status === es.execution_status) {
            counts[c.tName] = c.count;
          }
        });

        return {
          execution_status: es.execution_status,
          counts
        };
      });

      setRows(mappedRows);
      setTables(tablesList);

    } catch (err) {
      console.error("Error loading execution status", err);
    }
  };

  /* ================= SAVE ================= */
  const handleSave = async () => {
    if (!value.trim()) return;

    try {
      const params = new URLSearchParams();

      if (mode === "add") {
        params.append("execution_status", value.trim());

        await fetch(`${API_BASE_URL}/add-execution-status`, {
          method: "POST",
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          },
          body: params.toString()
        });

      } else if (mode === "edit") {
        params.append("value_old", oldValue);
        params.append("value_new", value.trim());

        await fetch(`${API_BASE_URL}/update-execution-status`, {
          method: "POST",
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          },
          body: params.toString()
        });
      }

      await loadExecutionStatus();
      setShowModal(false);
      setValue("");
      setOldValue("");

    } catch (err) {
      console.error("Save failed", err);
    }
  };

  /* ================= DELETE ================= */
  const handleDelete = async (item) => {
    if (!window.confirm(`Delete "${item}" ?`)) return;

    try {
      const params = new URLSearchParams();
      params.append("execution_status", item);

      await fetch(`${API_BASE_URL}/delete-execution-status`, {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded"
        },
        body: params.toString()
      });

      await loadExecutionStatus();

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
            Execution Status
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
                  <th>Execution Status</th>

                  {tables.map(t => (
					<th key={t.tName}>
					  {t.tName.replace(/_/g, " ").toUpperCase()}
					</th>
                  ))}

                  <th className={styles.actionCol}>Action</th>
                </tr>
              </thead>

              <tbody>
                {filteredData.map((row, index) => (
                  <tr key={index}>
                    <td>{row.execution_status}</td>

                    {tables.map(t => (
                      <td key={t.tName} className={styles.countCol}>
                        {row.counts[t.tName] || ""}
                      </td>
                    ))}

                    <td className={styles.actionCol}>
                      <button
                        className={styles.editBtn}
                        onClick={() =>
                          handleEditClick(row.execution_status)
                        }
                      >
                        ✎
                      </button>

                      <button
                        className={styles.deleteBtn}
                        onClick={() =>
                          handleDelete(row.execution_status)
                        }
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
                  ? "Add Execution Status"
                  : "Edit Execution Status"}
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
                placeholder="Execution Status"
                value={value}
                onChange={(e) => setValue(e.target.value)}
              />

              <div className={styles.modalActions}>
                <button
                  type="button"
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