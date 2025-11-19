import React, { useState, useEffect, useMemo } from "react";
import { Outlet } from 'react-router-dom';
import styles from './ValidateData.module.css';

const sampleData = [
  {
    id: 1,
    taskCode: "NP_E_02140",
    structure: "ND-PTD",
    component: "CH. 17000 - CH. 18000",
    element: "CH. 17280 - CH. 18000",
    activityName: "Earthwork Filling",
    unit: "CuM",
    scope: 35121.12,
    progressLast: { activityLevel: 3.2, componentLevel: 3.2, structureLevel: 2.7 },
    reporting: "Nihal 2025-01-25",
    actualUpdated: 26,
    resultsAfterAccept: { activityLevel: 1567, componentLevel: 6.5, structureLevel: 5.5 },
    updatedOn: "2025-01-25",
    status: "pending",
  },
  {
    id: 2,
    taskCode: "NP_E_02140",
    structure: "ND-PTD",
    component: "CH. 17000 - CH. 18000",
    element: "CH. 17280 - CH. 18000",
    activityName: "Earthwork Filling",
    unit: "CuM",
    scope: 35121.12,
    progressLast: { activityLevel: 3.2, componentLevel: 3.2, structureLevel: 2.7 },
    reporting: "Nihal 2025-01-26",
    actualUpdated: 24,
    resultsAfterAccept: { activityLevel: 1565, componentLevel: 6.5, structureLevel: 5.5 },
    updatedOn: "2025-01-26",
    status: "pending",
  },
  {
    id: 3,
    taskCode: "NP_E_04410",
    structure: "ND-PTD",
    component: "CH. 35000 - CH. 36000",
    element: "CH. 35760 - CH. 36000",
    activityName: "Railway Earth Filling",
    unit: "CuM",
    scope: 800,
    progressLast: { activityLevel: 799, componentLevel: 0.2, structureLevel: 0.2 },
    reporting: "Nihal 2024-12-21",
    actualUpdated: 17,
    resultsAfterAccept: { activityLevel: 816, componentLevel: 0.2, structureLevel: 0.2 },
    updatedOn: "2024-12-21",
    status: "approved",
    approvedOn: "2025-11-19",
  },
  {
    id: 4,
    taskCode: "NP_E_04470",
    structure: "ND-PTD",
    component: "CH. 35760 - CH. 36000",
    element: "CH. 35760 - CH. 36000",
    activityName: "Railway Earth Filling",
    unit: "CuM",
    scope: 552.75,
    progressLast: { activityLevel: 549, componentLevel: 0.2, structureLevel: 0.2 },
    reporting: "Nihal 2024-12-16",
    actualUpdated: 12,
    resultsAfterAccept: { activityLevel: 561, componentLevel: 0.2, structureLevel: 0.2 },
    updatedOn: "2024-12-16",
    status: "rejected",
    rejectedOn: "2025-11-10",
  },
];

export default function ValidateData() {
  const [activeTab, setActiveTab] = useState("pending");
  const [rows, setRows] = useState([]);

  const [contractFilter, setContractFilter] = useState("");
  const [structureFilter, setStructureFilter] = useState("");
  const [updatedByFilter, setUpdatedByFilter] = useState("");
  const [searchText, setSearchText] = useState("");

  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [selectedIds, setSelectedIds] = useState(new Set());

  useEffect(() => {
    setTimeout(() => setRows(sampleData), 200);
  }, []);

  const getToday = () => new Date().toISOString().split("T")[0];

  const handleApprove = (id) => {
    setRows((prev) =>
      prev.map((r) =>
        r.id === id ? { ...r, status: "approved", approvedOn: getToday() } : r
      )
    );
  };

  const handleReject = (id) => {
    setRows((prev) =>
      prev.map((r) =>
        r.id === id ? { ...r, status: "rejected", rejectedOn: getToday() } : r
      )
    );
  };

  const handleInfo = (id) => {
    const r = rows.find((x) => x.id === id);
    alert(`Info for ${r.taskCode}\nReporting: ${r.reporting}`);
  };

  const filtered = useMemo(() => {
    return rows.filter((r) => {
      if (activeTab !== r.status) return false;

      if (contractFilter && !r.component.includes(contractFilter)) return false;
      if (structureFilter && !r.structure.includes(structureFilter)) return false;
      if (updatedByFilter && !r.reporting.includes(updatedByFilter)) return false;

      if (searchText) {
        const s = searchText.toLowerCase();
        const hay = `${r.taskCode} ${r.structure} ${r.component} ${r.element} ${r.activityName}`.toLowerCase();
        if (!hay.includes(s)) return false;
      }

      return true;
    });
  }, [rows, activeTab, contractFilter, structureFilter, updatedByFilter, searchText]);

  const total = filtered.length;
  const pages = Math.ceil(total / pageSize);
  const visible = filtered.slice((page - 1) * pageSize, page * pageSize);

  // Select ALL checkboxes in the current visible page
function toggleSelectAll(checked) {
  if (checked) {
    const ids = new Set(visible.map(r => r.id));
    setSelectedIds(ids);
  } else {
    setSelectedIds(new Set());
  }
}

// Select INDIVIDUAL checkbox
function toggleSelect(id) {
  setSelectedIds(prev => {
    const next = new Set(prev);
    if (next.has(id)) next.delete(id);
    else next.add(id);
    return next;
  });
}


  return (
    <div className={styles.container}>
      <div className="pageHeading">
        <h2>Approve Activity Progress - Validate data</h2>
        <div  className="rightBtns md-none">
          {/* <button className="btn btn-primary" onClick={handleAdd}>
            <CirclePlus size={16} /> Add
          </button>
          <button className="btn btn-primary">
            <LuCloudDownload size={16} /> Export
          </button>*/}
           &nbsp;
        </div> 
       
      </div>
    <div className="innerPage">
      {/* Tabs */}
      <div className={styles.tabBtns}>
        {["pending", "approved", "rejected"].map((t) => (
          <button
            key={t}
            onClick={() => setActiveTab(t)}
            style={{
              background: activeTab === t ? "#CAEEFB" : "#fff",
              color: activeTab === t ? "#000" : "#000",
              borderBottom: activeTab === t ? "1px solid #3C467B" : "1px solid #ccc"
            }}
          >
            {t.charAt(0).toUpperCase() + t.slice(1)}
          </button>
        ))}
      </div>

      {/* Filters */}
    <div className={styles.filterRow}>
      <div  className="filterOptions">
        <select value={contractFilter} onChange={(e) => setContractFilter(e.target.value)}>
          <option value="">Select Contract</option>
          <option value="17000">CH. 17000</option>
          <option value="35000">CH. 35000</option>
        </select>
      </div>
      <div  className="filterOptions">
        <select value={structureFilter} onChange={(e) => setStructureFilter(e.target.value)}>
          <option value="">Select Structure</option>
          <option value="ND-PTD">ND-PTD</option>
          <option value="SD-PTD">SD-PTD</option>
        </select>
        </div>
      <div className="filterOptions">
        <select value={updatedByFilter} onChange={(e) => setUpdatedByFilter(e.target.value)}>
          <option value="">Select Updated By</option>
          <option value="Nihal">Nihal</option>
          <option value="Venkatesh">Venkatesh</option>
        </select>

        {/* <input
          placeholder="Search..."
          style={{ padding: 6 }}
          value={searchText}
          onChange={(e) => setSearchText(e.target.value)}
        /> */}
      </div>
    </div>

    {/* Show Entries + Search Row */}
      <div className={styles.tableTopRow} style={{ display: "flex", justifyContent: "space-between", marginTop: "10px" }}>
        
        {/* Show Entries */}
        <div className="showEntriesCount">
          <label>Show </label>
          <select
            value={pageSize}
            onChange={(e) => {
              setPageSize(Number(e.target.value));
              setPage(1);  // reset to first page
            }}
          >
            {[5, 10, 20, 50, 100].map((size) => (
              <option key={size} value={size}>{size}</option>
            ))}
          </select>
          <span> entries</span>
        </div>

        {/* Search */}
        <div className={styles.searchWrapper}>
          <input
            type="text"
            placeholder="Search"
            value={searchText}
            onChange={(e) => {
              setSearchText(e.target.value);
              setPage(1);
            }}
            className={styles.searchInput}
          />
          <span className={styles.searchIcon}>🔍</span>
        </div>
      </div>


      {/* Table */}
      <div className={`dataTable ${styles.tableWrapper}`}>
        <table className="main-table">
          <thead>
            {/* ---------- FIRST HEADER ROW (GROUP HEADERS) ---------- */}
            <tr className="top-head">
              <th rowSpan={2} className="col-check">
                {activeTab === "pending" && (
                  <input
                    type="checkbox"
                    onChange={(e) => toggleSelectAll(e.target.checked)}
                    checked={
                      visible.length > 0 &&
                      visible.every((r) => selectedIds.has(r.id))
                    }
                  />
                )}
              </th>

              <th rowSpan={2}>Task Code</th>
              <th rowSpan={2}>Structure</th>
              <th rowSpan={2}>Component</th>
              <th rowSpan={2}>Element</th>
              <th rowSpan={2}>Activity Name</th>
              <th rowSpan={2}>Unit</th>
              <th rowSpan={2}>Scope</th>

              {/* Group 1 */}
              <th colSpan={3} className="group">Progress till last update</th>

              <th rowSpan={2}>Reporting</th>
              <th rowSpan={2}>Actual Progress Updated / Updated Scope</th>

              {/* Group 2 */}
              <th colSpan={3} className="group">
                Results shown after accepting in the validation form
              </th>

              {/* Last column changes based on tab */}
              <th rowSpan={2}>
                {activeTab === "pending"
                  ? "Updated ON"
                  : activeTab === "approved"
                  ? "Approved ON"
                  : "Rejected ON"}
              </th>

              {/* ACTION column only for Pending */}
              {activeTab === "pending" && <th rowSpan={2}>Action</th>}
            </tr>

            {/* ---------- SECOND HEADER ROW (SUB-HEADERS) ---------- */}
            <tr className="sub-head">
              <th>Activity Level</th>
              <th>Component Level</th>
              <th>Structure Level</th>

              <th>Activity Level</th>
              <th>Component Level</th>
              <th>Structure Level</th>
            </tr>
          </thead>

          <tbody>
            {visible.length === 0 ? (
              <tr>
                <td colSpan={activeTab === "pending" ? 18 : 17} className="no-data">
                  No data available in table
                </td>
              </tr>
            ) : (
              visible.map((row) => (
                <tr key={row.id} className={`data-row ${row.status}`}>
                  {/* checkbox only for pending */}
                  <td className="col-check">
                    {activeTab === "pending" && (
                      <input
                        type="checkbox"
                        checked={selectedIds.has(row.id)}
                        onChange={() => toggleSelect(row.id)}
                      />
                    )}
                  </td>

                  <td>{row.taskCode}</td>
                  <td>{row.structure}</td>
                  <td>{row.component}</td>
                  <td>{row.element}</td>
                  <td>{row.activityName}</td>
                  <td>{row.unit}</td>
                  <td>{row.scope}</td>

                  <td>{row.progressLast.activityLevel}</td>
                  <td>{row.progressLast.componentLevel}</td>
                  <td>{row.progressLast.structureLevel}</td>

                  <td>{row.reporting}</td>
                  <td>{row.actualUpdated}</td>

                  <td>{row.resultsAfterAccept.activityLevel}</td>
                  <td>{row.resultsAfterAccept.componentLevel}</td>
                  <td>{row.resultsAfterAccept.structureLevel}</td>

                  <td>
                    {activeTab === "pending"
                      ? row.updatedOn
                      : activeTab === "approved"
                      ? row.approvedOn
                      : row.rejectedOn}
                  </td>

                  {/* ACTION BUTTONS only for pending */}
                  {activeTab === "pending" && (
                    <td>
                      <div className={styles.tableActionBtns}>
                        <button
                          className="btn-2 btn-green"
                          title="Approve"
                          onClick={() => handleApprove(row.id)}
                        >
                          ✔
                        </button>

                        <button
                          className="btn-2 btn-red"
                          title="Reject"
                          onClick={() => handleReject(row.id)}
                        >
                          ✖
                        </button>

                        <button
                          className="btn-2 btn-yellow"
                          title="Info"
                          onClick={() => handleInfo(row.id)}
                        >
                          i
                        </button>
                      </div>
                    </td>
                  )}
                </tr>
              ))
            )}
          </tbody>
        </table>

      </div>

      {/* Pagination */}
      <div style={{ marginTop: 20, display: "flex", justifyContent: "space-between" }}>
        <div>
          Showing {visible.length} of {total}
        </div>
        <div>
          <button disabled={page === 1} onClick={() => setPage(page - 1)}>
            Prev
          </button>
          <span style={{ margin: "0 10px" }}>{page}</span>
          <button disabled={page === pages} onClick={() => setPage(page + 1)}>
            Next
          </button>
        </div>
      </div>
    </div>
  </div>
  );
}