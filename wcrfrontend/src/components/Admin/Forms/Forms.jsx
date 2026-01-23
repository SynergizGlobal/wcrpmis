import React, { useContext, useEffect, useState } from "react";
import { RefreshContext } from "../../../context/RefreshContext";
import Select from "react-select";
import styles from "./Forms.module.css";
import { MdEditNote } from "react-icons/md";
import { CircleArrowRight } from 'lucide-react';
import api from "../../../api/axiosInstance";
import { Outlet, useNavigate, useLocation } from "react-router-dom";
import { API_BASE_URL } from "../../../config";

export default function Forms() {
  const { refresh } = useContext(RefreshContext);
  const navigate = useNavigate();
  const location = useLocation();

  const [forms, setForms] = useState([]);
  const [search, setSearch] = useState("");
  const [page, setPage] = useState(1);
  const [perPage, setPerPage] = useState(10);
  
  const [moduleOptions, setModuleOptions] = useState([]);
  const [statusOptions, setStatusOptions] = useState([]);

  const [filters, setFilters] = useState({
    module: "",
    status: "",
  });

  /* -------------------- LOAD FORMS -------------------- */
  useEffect(() => {
    loadForms();
  }, [refresh, location, filters]);

  const loadForms = async () => {
    try {
      const payload = {
        module_name_fk: filters.module || null,
        soft_delete_status_fk: filters.status || null,
        user_type_access: "MRVC"
      };

      const res = await api.post(
        "/form-access/ajax/get-forms-list",
        payload
      );

      setForms(res.data || []);
      setPage(1);
    } catch (err) {
      console.error("Error loading forms", err);
    }
  };

  /* -------------------- FILTERING -------------------- */
  const filteredData = forms.filter((f) => {
    const text = search.toLowerCase();

    return (
      (f.module_name_fk || "").toLowerCase().includes(text) ||
      (f.form_name || "").toLowerCase().includes(text) ||
      (f.folder_name || "").toLowerCase().includes(text)
    );
  });

  /* -------------------- PAGINATION -------------------- */
  const totalRecords = filteredData.length;
  const totalPages = Math.ceil(totalRecords / perPage);

  const paginatedRows = filteredData.slice(
    (page - 1) * perPage,
    page * perPage
  );

  /* -------------------- HANDLERS -------------------- */
  const handleClearFilters = () => {
    setFilters({ module: "", status: "" });
    setSearch("");
    setPage(1);
  };

  const handleEdit = (row) => {
    navigate("formsform", {
      state: {
        formId: row.form_id
      }
    });
  };


  const isFormsPage = location.pathname.includes("formsform");
  
  const handleForward = (row) => {
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


  /* -------------------- DROPDOWN OPTIONS -------------------- */
  useEffect(() => {
    loadFilters();
  }, []);

  const loadFilters = async () => {
    try {
      const moduleRes = await api.post(
        "/form-access/ajax/getModulesFilterListInForm",
        {}
      );

      setModuleOptions([
        { value: "", label: "Select Module" },
        ...(moduleRes.data || []).map((m) => ({
          value: m.module_name_fk,
          label: m.module_name_fk
        }))
      ]);

      const statusRes = await api.post(
        "/form-access/ajax/getStatusFilterListInForm",
        {}
      );

      setStatusOptions([
        { value: "", label: "Select Status" },
        ...(statusRes.data || []).map((s) => ({
          value: s.soft_delete_status_fk,
          label: s.soft_delete_status_fk
        }))
      ]);
    } catch (err) {
      console.error("Error loading filters", err);
    }
  };

  return (
    <div className={styles.container}>
	{/* Top Bar */}
      {!isFormsPage && (
        <>
          <div className="pageHeading">
            <h2>Forms</h2>
              <div  className="rightBtns">
                &nbsp;
              </div>
          </div>

          {/* FILTERS */}
          <div className="innerPage">
            <div className={styles.filterRow}>
              <Select
                options={moduleOptions}
                value={moduleOptions.find(
                  (o) => o.value === filters.module
                )}
                onChange={(opt) =>
                  setFilters({ ...filters, module: opt.value })
                }
                placeholder="Select Module"
                className={styles.filterOptions}
              />

              <Select
                options={statusOptions}
                value={statusOptions.find(
                  (o) => o.value === filters.status
                )}
                onChange={(opt) =>
                  setFilters({ ...filters, status: opt.value })
                }
                placeholder="Select Status"
                className={styles.filterOptions}
              />

              <button
                className="btn btn-2 btn-primary"
                onClick={handleClearFilters}
              >
                Clear Filters
              </button>
            </div>

            {/* SEARCH + ENTRIES */}
            <div className={styles.tableTopRow}>
              <div className="showEntriesCount">
                <label>Show </label>
                <select
                  value={perPage}
                  onChange={(e) => {
                    setPerPage(Number(e.target.value));
                    setPage(1);
                  }}
                >
                  {[5, 10, 20, 50].map((n) => (
                    <option key={n} value={n}>
                      {n}
                    </option>
                  ))}
                </select>
                <span> entries</span>
              </div>

              <div className={styles.searchWrapper}>
                <input
                  type="text"
                  placeholder="Search"
				  className={styles.searchInput}
                  value={search}
                  onChange={(e) => {
                    setSearch(e.target.value);
                    setPage(1);
                  }}
                />
				<span className={styles.searchIcon}>🔍</span>
              </div>
            </div>

            {/* TABLE */}
            <div className={`dataTable ${styles.tableWrapper}`}>
              <table className={styles.projectTable}>
                <thead>
                  <tr>
                    <th>Module</th>
                    <th>Form</th>
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
                    paginatedRows.map((row, index) => (
                      <tr key={index}>
					  <td>{row.module_name_fk}</td>
					  <td>{row.form_name}</td>
					  <td>{row.folder_name}</td>
					  <td>{row.soft_delete_status_fk}</td>
					  <td>{row.user_role_access || "-"}</td>
					  <td>{row.user_type_access || "-"}</td>
					  <td>{row.user_access || "-"}</td>
					  <td>
					    <div className={styles.actionBtns}>
					      {/* EDIT */}
					      <button
					        className="btn btn-2 btn-transparent"
					        title="Edit Form"
					        onClick={() => handleEdit(row)}
					      >
					        <MdEditNote size={22} />
					      </button>

					      {/* FORWARD */}
					      <button
					        className="btn btn-2 btn-transparent"
					        title="Open Form"
					        onClick={() => handleForward(row)}
					      >
					        <CircleArrowRight size={22} />
					      </button>
					    </div>
					  </td>
                      </tr>
                    ))
                  )}
                </tbody>
              </table>
            </div>

            {/* PAGINATION */}
            <div className={styles.paginationBar}>
              <span>
                {totalRecords === 0
                  ? "0 - 0"
                  : `${(page - 1) * perPage + 1} - ${Math.min(
                      page * perPage,
                      totalRecords
                    )}`}{" "}
                of {totalRecords}
              </span>

              <div className={styles.paginationBtns}>
                <button
                  disabled={page === 1}
                  onClick={() => setPage(page - 1)}
                >
                  {"<"}
                </button>
                <button className={styles.activePage}>{page}</button>
                <button
                  disabled={page === totalPages}
                  onClick={() => setPage(page + 1)}
                >
                  {">"}
                </button>
              </div>
            </div>
          </div>
        </>
      )}

      <Outlet />
    </div>
  );
}
