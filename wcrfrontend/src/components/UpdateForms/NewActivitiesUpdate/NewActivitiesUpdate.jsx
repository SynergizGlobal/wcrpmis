import React, { useEffect, useState } from "react";
import Select from "react-select";
import styles from "./NewActivitiesUpdate.module.css";

export default function NewActivitiesUpdate() {
  // ---------------- SAMPLE DATA ---------------- //
  const sampleProjects = [
    { label: "Petlad-Bhadran", value: 1 },
    { label: "Nadiad–Petlad (37 km)", value: 2 },
  ];

  const sampleContracts = {
    1: [
      { label: "All civil engineering works between Petlad-Bhadran", value: 10 },
    ],
    2: [
      { label: "All civil engineering works between Nadiad–Petlad", value: 20 },
    ],
  };

  const sampleStructureTypes = [
    { label: "RUB", value: "RUB" },
    { label: "Minor Bridge", value: "MB" },
    { label: "Track Laying", value: "TL" },
  ];

  const sampleStructures = {
    MB: [
      { label: "Br. No.61 (RCC Box)", value: 61 },
      { label: "Br. No.62 (RCC Box)", value: 62 },
      { label: "Br. No.63 (RCC Box)", value: 63 },
    ],
    RUB: [
      { label: "Br. No.52B (RCC Box)", value: 52 },
      { label: "Br. No.53A (RCC Box)", value: 53 },
    ],
  };

  const sampleComponents = {
    61: [
      { label: "Bottom Slab", value: "BS" },
      { label: "Finishes", value: "FIN" },
    ],
    52: [{ label: "Bottom Slab", value: "BS" }],
  };

  const sampleElements = {
    BS: [
      { label: "Steel Fixing", value: "STF" },
      { label: "Concreting", value: "CON" },
    ],
    FIN: [{ label: "Painting", value: "PNT" }],
  };

  const sampleTable = [
    {
      task_code: "PT_MNB_000140",
      activity: "Base Coat (LS)",
      base_start: "2025-08-29",
      base_finish: "2025-08-29",
      exp_start: "2025-08-29",
      exp_finish: "2025-08-29",
      scope: 559.29,
      pending: 0.0,
      completed: 0,
      actual: 0,
    },
    {
      task_code: "PT_MNB_000150",
      activity: "Final Coat (LS)",
      base_start: "2025-08-29",
      base_finish: "2025-08-29",
      exp_start: "2025-08-29",
      exp_finish: "2025-08-29",
      scope: 559.29,
      pending: 0.0,
      completed: 0,
      actual: 0,
    },
  ];

  const sampleLatestUpdates = [
    { structure: "Br. No.15A (RCC Box)", component: "Bottom Slab" },
    { structure: "Br. No.52B (RCC Box)", component: "Bottom Slab" },
    { structure: "Br. No.53A (RCC Box)", component: "Bottom Slab" },
  ];

  // ---------------- STATES ---------------- //
  const [selectedProject, setSelectedProject] = useState(null);
  const [selectedContract, setSelectedContract] = useState(null);
  const [selectedStructureType, setSelectedStructureType] = useState(null);
  const [selectedStructure, setSelectedStructure] = useState(null);
  const [selectedComponent, setSelectedComponent] = useState(null);
  const [selectedElement, setSelectedElement] = useState(null);

  const [tableRows, setTableRows] = useState([]);

  // ---------------- EDIT TABLE ---------------- //
  const updateCell = (index, field, value) => {
    const updated = [...tableRows];
    updated[index][field] = value;
    setTableRows(updated);
  };

 useEffect(() => {
  if (selectedComponent) {
    setTableRows(sampleTable);
  } else {
    setTableRows([]); // clear when deselected
  }
}, [selectedComponent]);

  return (
    <div className={styles.container}>
        <div className="pageHeading">
        <h2>New Activities Update</h2>
        <div  className="rightBtns md-none">
          {/* <button className="btn btn-primary" onClick={handleAdd}>
            <CirclePlus size={16} /> Add
          </button>
          <button className="btn btn-primary">
            <LuCloudDownload size={16} /> Export
          </button> */}
          <span>&nbsp;</span>
        </div>
      </div>

      <div className={styles.layoutWrapper}>
        {/* ---------------- LEFT PANEL ---------------- */}
        <div className={styles.leftSection}>
          {/* ---------------- ROW 1 ---------------- */}
          <div className="form-row">
            <div className="form-field">
                <Select
                    placeholder="Project"
                    value={selectedProject}
                    options={sampleProjects}
                    onChange={(v) => {
                        setSelectedProject(v);
                        setSelectedContract(null);
                    }}
                    />
            </div>
            <div className="form-field">
                <Select
              placeholder="Contract"
              value={selectedContract}
              options={sampleContracts[selectedProject?.value] || []}
              onChange={(v) => {
                setSelectedContract(v);
              }}
              isDisabled={!selectedProject}
            />
            </div>
          </div>

          {/* ---------------- ROW 2 ---------------- */}
          <div className="form-row flex-4">
            <div className="form-field">
              <Select
                placeholder="Structure Type"
                value={selectedStructureType}
                options={sampleStructureTypes}
                onChange={(v) => {
                    setSelectedStructureType(v);
                    setSelectedStructure(null);
                }}
                isDisabled={!selectedContract}
                />
            </div>
            <div className="form-field">
               <Select
                placeholder="Structure"
                value={selectedStructure}
                options={sampleStructures[selectedStructureType?.value] || []}
                onChange={(v) => {
                setSelectedStructure(v);
                }}
                isDisabled={!selectedStructureType}
            /> 
            </div>
            <div className="form-field">
                <Select
                placeholder="Component"
                value={selectedComponent}
                options={sampleComponents[selectedStructure?.value] || []}
                onChange={(v) => {
                    setSelectedComponent(v);
                }}
                isDisabled={!selectedStructure}
                />
            </div>
            <div className="form-field">
                <Select
                    placeholder="Element"
                    value={selectedElement}
                    options={sampleElements[selectedComponent?.value] || []}
                    onChange={(v) => {
                        setSelectedElement(v);
                    }}
                    isDisabled={!selectedComponent}
                    />   
            </div>
            <div className="form-field">
                <label>Progress Date <span className="red">*</span></label>
                <input name="progress_date" type="date" placeholder="Select Date" />
              </div>
              <div className="form-field">
                <label>Remarks <span className="red">*</span></label>
                <input name="remarks" type="text" placeholder="Enter Value" />
              </div>
              <button className="btn btn-primary">Attach Photo</button>
              <button className="btn btn-secondary">Update</button>
          </div>

          {/* ---------------- ACTION BUTTONS ---------------- */}
          <div className="form-post-buttons">

            <button className="btn btn-primary">Update</button>
            <button className="btn btn-secondary">Reset</button>
            <button className="btn btn-primary">Export</button>
            <button className="btn btn-secondary">Upload</button>
          </div>

          
        </div>

        {/* ---------------- RIGHT PANEL ---------------- */}
        <div className={styles.rightSection}>
          <h4>Latest Updated Structure → Component</h4>
          <div className={styles.latestBox}>
            {sampleLatestUpdates.map((item, i) => (
              <p key={i}>
                <a href="#">
                  {item.structure} → {item.component}
                </a>
              </p>
            ))}
          </div>
        </div>
      </div>
      {/* ---------------- TABLE ---------------- */}
          {tableRows.length > 0 && (
            <div className={`dataTable ${styles.tableWrapper}`}>
            <table className={styles.projectTable}>
              <thead>
                <tr>
                  <th>Task Code</th>
                  <th>Activity</th>
                  <th>Baseline Start</th>
                  <th>Baseline Finish</th>
                  <th>Expected Start</th>
                  <th>Expected Finish</th>
                  <th>Scope</th>
                  <th>Pending</th>
                  <th>Completed</th>
                  <th>Actual</th>
                </tr>
              </thead>

              <tbody>
                {tableRows.map((row, i) => (
                  <tr key={i}>
                    <td>{row.task_code}</td>
                    <td>{row.activity}</td>

                    <td>
                      <input
                        type="date"
                        value={row.base_start}
                        onChange={(e) => updateCell(i, "base_start", e.target.value)}
                      />
                    </td>

                    <td>
                      <input
                        type="date"
                        value={row.base_finish}
                        onChange={(e) => updateCell(i, "base_finish", e.target.value)}
                      />
                    </td>

                    <td>
                      <input
                        type="date"
                        value={row.exp_start}
                        onChange={(e) => updateCell(i, "exp_start", e.target.value)}
                      />
                    </td>

                    <td>
                      <input
                        type="date"
                        value={row.exp_finish}
                        onChange={(e) => updateCell(i, "exp_finish", e.target.value)}
                      />
                    </td>

                    <td>{row.scope}</td>
                    <td className="red">{row.pending}</td>
                    <td>{row.completed}</td>

                    <td>
                      <input
                        type="number"
                        step="0.01"
                        value={row.actual}
                        onChange={(e) => updateCell(i, "actual", e.target.value)}
                      />
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
        </div>
          )}
    </div>
  );
}
