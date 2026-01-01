import React, { useState } from "react";
import styles from "./BankGuaranteeType.module.css";

const INITIAL_DATA = [
  "Acceleration Advance",
  "Advance Guarantee",
  "ESHS Performance BG",
  "Mobilisation Advance",
  "Others",
  "Performance Guarantee",
  "Retention Money",
  "Security Deposit",
  "Test Bank guarantee",
];

export default function BankGuaranteeType() {
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

  /* ===== SAVE ===== */
  const handleSave = () => {
    if (!value.trim()) return;

    if (mode === "add") {
      setData(prev => [...prev, value.trim()]);
    } else if (mode === "edit" && editIndex !== null) {
      const updated = [...data];
      updated[editIndex] = value.trim();
      setData(updated);
    }

    setShowModal(false);
    setValue("");
    setEditIndex(null);
  };

  return (
    <div className={styles.container}>
      <div className="card">
        {/* HEADER */}
        <div className="formHeading">
          <h2 className="center-align ps-relative">
            Bank Guarantee Type
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
              + Add Bank Guarantee Type
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
                  <th>Bank Guarantee Type</th>
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
                  ? "Add Bank Guarantee Type"
                  : "Edit Bank Guarantee Type"}
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
                placeholder="Bank Guarantee Type"
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
