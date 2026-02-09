import React, { useEffect, useMemo, useState } from "react";
import Select from "react-select";
import styles from "./ReportsAccess.module.css";
import { MdEditNote } from "react-icons/md";
import { CircleArrowRight, Search } from "lucide-react";
import api from "../../../api/axiosInstance";
import { useNavigate } from "react-router-dom";

export default function Reports() {
  const navigate = useNavigate();

  /* ================= STATES ================= */
  const [reports, setReports] = useState([]);
  const [modules, setModules] = useState([]);
  const [statuses, setStatuses] = useState([]);
  const [module, setModule] = useState(null);
  const [status, setStatus] = useState(null);
  const [searchText, setSearchText] = useState("");
  const [search, setSearch] = useState("");
  const [perPage, setPerPage] = useState(10);
  const [page, setPage] = useState(1);
  const [loading, setLoading] = useState(false);
  const [loadingFilters, setLoadingFilters] = useState(false);

  const userTypeAccess = "MRVC";

  /* ================= LOAD FILTERS ================= */
  useEffect(() => {
    loadFilters();
  }, []);

  /* ================= LOAD REPORTS ================= */
  useEffect(() => {
    loadReports();
    setPage(1);
  }, [module, status]);

  /* ================= API ================= */
  const loadReports = async () => {
    try {
      setLoading(true);
      const res = await api.post("/report-access/ajax/get-reports-list", {
        module_name_fk: module?.value || "",
        soft_delete_status_fk: status?.value || "",
        user_type_access: userTypeAccess,
      });
      setReports(res.data || []);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const loadFilters = async () => {
    try {
      setLoadingFilters(true);
      const [moduleRes, statusRes] = await Promise.all([
        api.post("/report-access/ajax/getModulesFilterListInReport", {}),
        api.post("/report-access/ajax/getStatusFilterListInReport", {}),
      ]);
      setModules(moduleRes.data || []);
      setStatuses(statusRes.data || []);
    } catch (err) {
      console.error(err);
    } finally {
      setLoadingFilters(false);
    }
  };

  /* ================= OPTIONS ================= */
  const moduleOptions = useMemo(
    () =>
      modules.map((m) => ({
        label: m.module_name_fk,
        value: m.module_name_fk,
      })),
    [modules]
  );

  const statusOptions = useMemo(
    () =>
      statuses.map((s) => ({
        label: s.soft_delete_status_fk,
        value: s.soft_delete_status_fk,
      })),
    [statuses]
  );

  /* ================= SEARCH ================= */
  const filteredData = useMemo(() => {
    const text = search.toLowerCase();
    return reports.filter((row) =>
      Object.values(row).join(" ").toLowerCase().includes(text)
    );
  }, [reports, search]);

  const clearFilters = () => {
    setModule(null);
    setStatus(null);
    setSearch("");
    setSearchText("");
    setPage(1);
    loadReports();
  };

  /* ================= PAGINATION ================= */
  const totalPages = Math.ceil(filteredData.length / perPage);
  const totalRecords = filteredData.length;
  const startIndex = (page - 1) * perPage;
  const endIndex = Math.min(startIndex + perPage, totalRecords);
  const showingText = totalRecords > 0 
    ? `Showing ${startIndex + 1} to ${endIndex} of ${totalRecords} entries`
    : `Showing 0 to 0 of 0 entries`;

  const paginatedRows = useMemo(
    () => filteredData.slice(startIndex, endIndex),
    [filteredData, startIndex, endIndex]
  );

  const paginationRange = useMemo(() => {
    const maxVisible = 5;
    let start = Math.max(1, page - Math.floor(maxVisible / 2));
    let end = Math.min(totalPages, start + maxVisible - 1);

    if (end - start < maxVisible - 1) {
      start = Math.max(1, end - maxVisible + 1);
    }

    return Array.from({ length: end - start + 1 }, (_, i) => start + i);
  }, [page, totalPages]);

  /* ================= ACTION HANDLERS ================= */
 
  const openEdit = (row) => {
	  navigate(`/admin/get-report`, {
	    state: { formId: row.form_id }
	  });
    };

  const openReport = (row) => {
      console.log("URL TYPE:", row.url_type);
      console.log("FORM URL:", row.web_form_url);

      if (!row.url_type || !row.web_form_url) {
        alert("Form navigation not configured");
        return;
      }

      const typePath = row.url_type
        .toLowerCase()
        .replace(/\s+/g, "");

      const finalUrl = `${process.env.PUBLIC_URL}/${typePath}/${row.web_form_url}`;

      console.log("FINAL NAV URL:", finalUrl);

      window.location.href = finalUrl;
    };
  /* ================= UI ================= */
  return (
    <div className={styles.container} style={{ padding: "0 20px" }}>
      <div className={styles.titleBar}>Reports</div>

      {/* FILTERS */}
      <div className={styles.filters} style={{ gap: 12 }}>
        <Select
          options={moduleOptions}
          value={module}
          onChange={setModule}
          isClearable
          placeholder="Select Module"
          isLoading={loadingFilters}
        />

        <Select
          options={statusOptions}
          value={status}
          onChange={setStatus}
          isClearable
          placeholder="Select Status"
          isLoading={loadingFilters}
        />

        <button
          onClick={clearFilters}
          style={{
            height: 38,
            padding: "0 14px",
            background: "#3f4f85",
            color: "#fff",
            borderRadius: 6,
            border: "none",
            cursor: "pointer",
          }}
        >
          Clear Filters
        </button>
      </div>

      {/* ================= SEARCH + ENTRIES ================= */}
      <div className={styles.tableTop}>
        <div>
          Show{" "}
          <select
            value={perPage}
            onChange={(e) => {
              setPerPage(Number(e.target.value));
              setPage(1);
            }}
          >
            {[5, 10, 20, 50].map((n) => (
              <option key={n}>{n}</option>
            ))}
          </select>{" "}
          entries
        </div>

        {/* 🔥 LIVE SEARCH INPUT */}
        <div style={{ position: "relative" }}>
          <input
            className={styles.searchInput}
            placeholder="Search..."
            value={search}
            onChange={(e) => {
              setSearch(e.target.value);  // 🔥 live search happens here
              setPage(1);
            }}
          />

          <span className={styles.searchIcon}>🔍</span>
        </div>
      </div>

      {/* TABLE */}
      <table className={styles.table}>
        <thead>
          <tr>
            <th>Module</th>
            <th>Report</th>
            <th>Folder</th>
            <th>Status</th>
            <th>User Role Access</th>
            <th>User Type Access</th>
            <th>User Access</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {paginatedRows.length === 0 ? (
            <tr>
              <td colSpan="8" align="center">
                No records found
              </td>
            </tr>
          ) : (
            paginatedRows.map((row) => (
              <tr key={row.form_id} style={{ fontSize: "20px" }}>
                <td>{row.module_name_fk}</td>
                <td>{row.form_name}</td>
                <td>{row.folder_name}</td>
                <td>{row.soft_delete_status_fk}</td>
                <td>{row.user_role_access || "-"}</td>
                <td>{row.user_type_access || "-"}</td>
                <td>{row.user_access || "-"}</td>
                <td className={styles.actions}>
                  <button
                    onClick={() => openEdit(row)}
                    style={{
                      background: "transparent",
                      border: "none",
                      cursor: "pointer",
                      color: "#000",
                    }}
                  >
                    <MdEditNote size={22} />
                  </button>

              
				  {/* Always show the button */}
				  <button
				    onClick={() => openReport(row)}
				    style={{
				      background: "transparent",
				      border: "none",
				      cursor: "pointer",
				      color: "#000",
				    }}
				  >
				    <CircleArrowRight size={22} />
				  </button>
                 
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>

      {/* ENTRIES INFO + PAGINATION */}
      <div style={{ 
        display: "flex", 
        justifyContent: "space-between", 
        alignItems: "center", 
        marginTop: "20px",
        flexWrap: "wrap" 
      }}>
        {/* Showing X to Y of Z entries */}
        <div style={{ 
          color: "#666", 
          fontSize: "14px",
          marginBottom: "10px"
        }}>
          {showingText}
        </div>

        {/* PAGINATION */}
        {totalPages > 1 && (
          <div
            className={styles.pagination}
            style={{
              display: "flex",
              gap: 6,
              marginBottom: "10px"
            }}
          >
            <button
              disabled={page === 1}
              onClick={() => setPage(page - 1)}
              style={{ 
                color: "#3f4f85",
                background: "transparent",
                border: "1px solid #ccc",
                padding: "4px 10px",
                borderRadius: 4,
                cursor: page === 1 ? "not-allowed" : "pointer",
                opacity: page === 1 ? 0.5 : 1
              }}
            >
              Pre
            </button>

            {paginationRange.map((p) => (
              <button
                key={p}
                onClick={() => setPage(p)}
                style={{
                  background: page === p ? "#3f4f85" : "#fff",
                  color: page === p ? "#fff" : "#3f4f85",
                  border: "1px solid #3f4f85",
                  padding: "4px 10px",
                  borderRadius: 4,
                  fontWeight: page === p ? "bold" : "normal",
                  cursor: "pointer"
                }}
              >
                {p}
              </button>
            ))}

            <button
              disabled={page === totalPages}
              onClick={() => setPage(page + 1)}
              style={{ 
                color: "#3f4f85",
                background: "transparent",
                border: "1px solid #ccc",
                padding: "4px 10px",
                borderRadius: 4,
                cursor: page === totalPages ? "not-allowed" : "pointer",
                opacity: page === totalPages ? 0.5 : 1
              }}
            >
              Next
            </button>
          </div>
        )}
      </div>
    </div>
  );
}
