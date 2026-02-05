import React, { useEffect, useMemo, useState } from "react";
import Select from "react-select";
import styles from "./ReportsAccess.module.css";
import { MdEditNote } from "react-icons/md";
import { CircleArrowRight } from "lucide-react";
import api from "../../../api/axiosInstance";
import { useNavigate } from "react-router-dom";

export default function Reports() {
  const navigate = useNavigate();

  /* =================================================
     STATES
  ================================================= */
  const [reports, setReports] = useState([]);
  const [modules, setModules] = useState([]);
  const [statuses, setStatuses] = useState([]);

  const [module, setModule] = useState(null);
  const [status, setStatus] = useState(null);

  const [search, setSearch] = useState("");
  const [perPage, setPerPage] = useState(10);
  const [page, setPage] = useState(1);

  const [loading, setLoading] = useState(false);
  const [loadingFilters, setLoadingFilters] = useState(false);

  const userTypeAccess = "MRVC";

  /* =================================================
     LOAD FILTERS + RESTORE SAVED FILTERS
  ================================================= */
  useEffect(() => {
    loadFilters();

    const saved = localStorage.getItem("reportFilters");

    if (saved) {
      const obj = JSON.parse(saved);

      if (obj.module_name_fk) {
        setModule({
          label: obj.module_name_fk,
          value: obj.module_name_fk,
        });
      }

      if (obj.soft_delete_status_fk) {
        setStatus({
          label: obj.soft_delete_status_fk,
          value: obj.soft_delete_status_fk,
        });
      }
    }
  }, []);

  /* =================================================
     AUTO LOAD REPORTS WHEN FILTER CHANGES
  ================================================= */
  useEffect(() => {
    loadReports();

    localStorage.setItem(
      "reportFilters",
      JSON.stringify({
        module_name_fk: module?.value || "",
        soft_delete_status_fk: status?.value || "",
      })
    );

    setPage(1);
  }, [module, status]);

  /* =================================================
     API CALLS
  ================================================= */

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
      console.error("Reports load error:", err);
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
      console.error("Filter load error:", err);
    } finally {
      setLoadingFilters(false);
    }
  };

  /* =================================================
     DROPDOWN OPTIONS
  ================================================= */

  const moduleOptions = useMemo(
    () => [
      { label: "Select Module", value: "" },
      ...modules.map((m) => ({
        label: m.module_name_fk,
        value: m.module_name_fk,
      })),
    ],
    [modules]
  );

  const statusOptions = useMemo(
    () => [
      { label: "Select Status", value: "" },
      ...statuses.map((s) => ({
        label: s.soft_delete_status_fk,
        value: s.soft_delete_status_fk,
      })),
    ],
    [statuses]
  );

  /* =================================================
     SEARCH
  ================================================= */

  const filteredData = useMemo(() => {
    const text = search.toLowerCase();

    return reports.filter((row) =>
      Object.values(row)
        .join(" ")
        .toLowerCase()
        .includes(text)
    );
  }, [reports, search]);

  /* =================================================
     PAGINATION
  ================================================= */

  const totalRecords = filteredData.length;
  const totalPages = Math.ceil(totalRecords / perPage);

  const paginatedRows = filteredData.slice(
    (page - 1) * perPage,
    page * perPage
  );

  /* =================================================
     ACTIONS
  ================================================= */

  const clearFilters = () => {
    setModule(null);
    setStatus(null);
    setSearch("");
    setPage(1);
    localStorage.removeItem("reportFilters");
  };

  const openEdit = (formId) => {
    navigate("/get-access-reports", {
      state: { formId },
    });
  };

  const openReport = (row) => {
    if (!row.web_form_url) return;

    window.open(`/${row.web_form_url}`, "_blank");
  };

  /* =================================================
     SELECT STYLE
  ================================================= */

  const selectStyles = {
    container: (base) => ({ ...base, width: 200 }),
    control: (base) => ({
      ...base,
      minHeight: 38,
      borderRadius: 6,
      boxShadow: "none",
    }),
    indicatorSeparator: () => ({ display: "none" }),
  };

  /* =================================================
     UI
  ================================================= */

  return (
    <div className={styles.container}>
      <div className={styles.titleBar}>Reports</div>

      {/* ================= FILTERS ================= */}
      <div className={styles.filters}>
        <Select
          options={moduleOptions}
          value={module}
          onChange={setModule}
          styles={selectStyles}
          isLoading={loadingFilters}
          isClearable
          placeholder="Select Module"
        />

        <Select
          options={statusOptions}
          value={status}
          onChange={setStatus}
          styles={selectStyles}
          isLoading={loadingFilters}
          isClearable
          placeholder="Select Status"
        />

        <button className={styles.clearBtn} onClick={clearFilters}>
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

        <input
          placeholder="Search"
          value={search}
          onChange={(e) => {
            setSearch(e.target.value);
            setPage(1);
          }}
        />
      </div>

      {/* ================= LOADER ================= */}
      {loading && <div className={styles.loader}>Loading...</div>}

      {/* ================= TABLE ================= */}
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
              <tr key={row.form_id}>
                <td>{row.module_name_fk}</td>
                <td>{row.form_name}</td>
                <td>{row.folder_name}</td>
                <td>{row.soft_delete_status_fk}</td>
                <td>{row.user_role_access || "-"}</td>
                <td>{row.user_type_access || "-"}</td>
                <td>{row.user_access || "-"}</td>

                <td className={styles.actions}>
                  <button onClick={() => openEdit(row.form_id)}>
                    <MdEditNote size={18} />
                  </button>

                  {row.web_form_url && (
                    <button onClick={() => openReport(row)}>
                      <CircleArrowRight size={18} />
                    </button>
                  )}
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>

      {/* ================= PAGINATION ================= */}
      <div className={styles.pagination}>
        <span>
          {totalRecords === 0
            ? "0 - 0"
            : `${(page - 1) * perPage + 1} - ${Math.min(
                page * perPage,
                totalRecords
              )}`}{" "}
          of {totalRecords}
        </span>

        <div>
          <button disabled={page === 1} onClick={() => setPage(page - 1)}>
            {"<"}
          </button>
          <button>{page}</button>
          <button
            disabled={page === totalPages}
            onClick={() => setPage(page + 1)}
          >
            {">"}
          </button>
        </div>
      </div>
    </div>
  );
}
