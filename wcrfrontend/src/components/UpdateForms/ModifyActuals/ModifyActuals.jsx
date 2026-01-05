import React, { useState } from "react";
import Select from "react-select";
import styles from "./ModifyActuals.module.css";

export default function ModifyActuals() {
  // RADIO OPTIONS
  const actions = [
    { label: "Completed Scope / Actual Zero by Task Code", value: "completed" },
    { label: "Delete activities by task code", value: "deleteTask" },
    { label: "Delete activities by contract", value: "deleteContract" },
  ];

  // SAMPLE DROPDOWNS
  const sampleContracts = [
    { label: "Nadiad–Petlad (37km)", value: 1 },
    { label: "Petlad–Bhadran", value: 2 },
  ];

  const sampleStructures = [
    { label: "Bridge No. 61", value: 61 },
    { label: "Bridge No. 52B", value: 52 },
  ];

    const sampleDeleteReasons = [
    { label: "Invalid Work", value: "inv" },
    { label: "Duplicate Entry", value: "dup" },
  ];

  // SAMPLE TABLE DATA
    const sampleTable = [
      { id: 1, task: "NP_E_00010", activity: "Earthwork Cutting", scope: 1347.95, completed: 0 },
      { id: 2, task: "NP_E_00020", activity: "Blanketing (CuM)", scope: 787.6, completed: 0 },
      { id: 3, task: "NP_E_00030", activity: "Ballast Spreading", scope: 1563, completed: 0 },
    ];



  // STATES
   const [selectedAction, setSelectedAction] = useState(null);
  const [selectedContract, setSelectedContract] = useState(null);
  const [selectedStructure, setSelectedStructure] = useState(null);
  const [deleteReason, setDeleteReason] = useState(null);
  const [search, setSearch] = useState("");
  const [checkedItems, setCheckedItems] = useState([]);

  // TABLE VISIBILITY LOGIC
const isShowTable =
  (selectedAction === "completed" && selectedContract) ||
  (selectedAction === "deleteTask" && selectedContract && selectedStructure) ||
  (selectedAction === "deleteContract" && selectedContract && selectedStructure);


  // SEARCHED DATA
  const filteredTable = sampleTable.filter((row) =>
    row.activity.toLowerCase().includes(search.toLowerCase())
  );

   const handleCheckboxChange = (id) => {
    setCheckedItems((prev) =>
      prev.includes(id) ? prev.filter((x) => x !== id) : [...prev, id]
    );
  };

  return (
    <div className={styles.container}>
      <div className="pageHeading">
        <h2>Modify Actuals</h2>
        <div className="rightBtns">
         &nbsp;
        </div>
      </div>
      

      {/* RADIO OPTIONS */}
      <div className={styles.radioRow}>
        <label>Action</label>
        {actions.map((a, i) => (
          <label key={i} className={styles.radioLabel}>
            <input
              type="radio"
              name="action"
              value={a.value}
              onChange={() => {
                setSelectedAction(a.value);
                setSelectedContract(null);
                setSelectedStructure(null);
                setDeleteReason(null);
                setCheckedItems([]);
              }}
            />
            {a.label}
          </label>
        ))}
      </div>

      {/* CONDITIONAL FIELDS */}
      <div className={styles.fieldsSection}>

        <div className="form-row">
          {/* Contract Field for all three options */}
            {selectedAction && (
              <div className="form-field">
                <label>Contract *</label>
                <Select
                  placeholder="Select"
                  value={selectedContract}
                  options={sampleContracts}
                  onChange={(v) => setSelectedContract(v)}
                />
              </div>
            )}

            {selectedAction === "deleteContract" &&  (
              <div className="form-field">
                <label>Structure *</label>
                <Select
                  placeholder="Select"
                  value={selectedStructure}
                  options={sampleStructures}
                  onChange={(v) => setSelectedStructure(v)}
                />
              </div>
            )}
        </div>
      
        <div className={styles.searchBtns}>
          {/* Buttons */}
            {selectedAction && (
              <div className="form-post-buttons">
                <button
                  className="btn btn-primary"
                  disabled={checkedItems.length === 0 || !isShowTable}
                  style={{
                    backgroundColor:
                      checkedItems.length === 0 ? "#bcbcbc" : "var(--primary-color)",
                    cursor: checkedItems.length === 0 ? "not-allowed" : "pointer",
                  }}
                >
                  UPDATE
                </button>
                <button className="btn btn-secondary">RESET</button>
              </div>
            )}

            {isShowTable && (
              <div className={styles.searchRow}>
                <label>Search</label>
                <input
                  type="text"
                  placeholder="Search activity..."
                  value={search}
                  onChange={(e) => setSearch(e.target.value)}
                />
              </div>
            )}
        </div>
        
      </div>

  
      {isShowTable && (
        <div className={styles.tableWrapper}>
          {/* Search Box */}
          

          {/* TABLE */}
          <div className={`dataTable ${styles.tableWrapper}`}>
            <table className={styles.dataTable}>
              <thead>
                <tr>
                  <th></th>
                  <th>Task Code</th>
                  <th>Activity</th>
                  <th>Scope</th>
                  <th>Completed</th>
                </tr>
              </thead>

              <tbody>
                {filteredTable.map((row, i) => (
                  <tr key={i}>
                    <td>
                      <input
                        type="checkbox"
                        checked={checkedItems.includes(row.id)}
                        onChange={() => handleCheckboxChange(row.id)}
                      />
                    </td>
                    <td>{row.task}</td>
                    <td>{row.activity}</td>
                    <td>{row.scope}</td>
                    <td>{row.completed}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
}
