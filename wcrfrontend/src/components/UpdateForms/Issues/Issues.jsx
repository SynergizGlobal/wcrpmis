import React, { useContext, useState, useEffect, useCallback  } from "react";
import { RefreshContext } from "../../../context/RefreshContext";
import Select from "react-select";
import styles from './Issues.module.css';
import { CirclePlus } from "lucide-react";
import { LuCloudDownload } from "react-icons/lu";
import api from "../../../api/axiosInstance";
import { Outlet, useNavigate, useLocation } from "react-router-dom"; 
import { API_BASE_URL } from "../../../config";

import * as XLSX from "xlsx";

import { MdEditNote } from "react-icons/md";

export default function Issues({}) {
	
	
    const location = useLocation();
    const navigate = useNavigate();
    const { refresh } = useContext(RefreshContext);
	const [issues, setIssues] = useState([]); // raw issues from backend
	const [loading, setLoading] = useState(false);

	// UI state: search, pagination
	const [search, setSearch] = useState("");
	const [page, setPage] = useState(Number(localStorage.getItem("issuesPageNo")) || 1);
	const [perPage, setPerPage] = useState(10);
	
	const isIssueForm = location.pathname.endsWith("/issuesform");




	
	// Filters (single-select strings to match JSP)
	const [filters, setFilters] = useState({
	  contract: "",
	  hod: "",
	  department: "",
	  category: "",
	  status: "",
	});

	// Options for selects
	const [filterOptions, setFilterOptions] = useState({
	  contract: [],
	  hod: [],
	  department: [],
	  category: [],
	  status: [],
	});



	// Helper to build the localStorage filter string same as JSP:
	// "key=value^key=value^"
	const saveFiltersToLocalStorage = useCallback((map) => {
	  let filterString = "";
	  Object.keys(map).forEach((k) => {
	    const v = map[k] ?? "";
	    filterString += `${k}=${v}^`;
	  });
	  localStorage.setItem("issueFilters", filterString);
	}, []);

	// parse issueFilters stored in localStorage (JSP format)
	const restoreFiltersFromLocalStorage = useCallback(() => {
	  const raw = localStorage.getItem("issueFilters") || "";
	  if (!raw.trim()) return null;
	  const parts = raw.split("^");
	  const restored = {};
	  parts.forEach((p) => {
	    if (!p) return;
	    const kv = p.split("=");
	    if (kv.length >= 2) {
	      restored[kv[0].trim()] = kv.slice(1).join("=").trim();
	    }
	  });
	  return restored;
	}, []);

	// Utility to ensure we always send strings
	const sanitizeParam = (v) => {
	  if (v === null || typeof v === "undefined") return "";
	  if (Array.isArray(v)) return v.join(",");
	  return String(v);
	};

	// LOADERS: each loads options and optionally marks the given selected value
	// they mirror the JSP endpoints used earlier

	const loadHodOptions = async () => {
	  const params = {
	    contract_id_fk: sanitizeParam(filters.contract),
	    department_fk: sanitizeParam(filters.department),
	    category_fk: sanitizeParam(filters.category),
	    status_fk: sanitizeParam(filters.status),
	    hod: "",
	  };

	  try {
	    const res = await api.post(`${API_BASE_URL}/issue/ajax/getHODListFilterInIssue`, params);
	    const data = res.data || [];

	    // just set options — NO AUTO SELECTION
	    setFilterOptions(prev => ({
	      ...prev,
	      hod: data.map(v => ({
	        value: v.designation,
	        label: v.designation,
	      })),
	    }));

	  } catch (e) {
	    console.error("loadHodOptions error", e);
	  }
	};


	const loadContractOptions = async (selectedValue = "") => {
	  const params = {
	    contract_id_fk: "",
	    department_fk: sanitizeParam(filters.department),
	    category_fk: sanitizeParam(filters.category),
	    status_fk: sanitizeParam(filters.status),
	    hod: sanitizeParam(filters.hod),
	  };
	  try {
	    const res = await api.post(`${API_BASE_URL}/issue/ajax/getContractsListFilterInIssue`, params);
	    const data = res.data || [];
	    const options = data.map((v) => ({
	      value: v.contract_id_fk,
	      label: v.contract_short_name?.trim() || v.contract_id_fk,
	    }));
	    setFilterOptions((prev) => ({ ...prev, contract: options }));

	    if (selectedValue && options.some((o) => o.value === selectedValue)) {
	      setFilters((prev) => ({ ...prev, contract: selectedValue }));
	    }
	  } catch (e) {
	    console.error("loadContractOptions error", e);
	  }
	};

	const loadDepartmentOptions = async (selectedValue = "") => {
	  const params = {
	    contract_id_fk: sanitizeParam(filters.contract),
	    department_fk: "",
	    category_fk: sanitizeParam(filters.category),
	    status_fk: sanitizeParam(filters.status),
	    hod: sanitizeParam(filters.hod),
	  };
	  try {
	    const res = await api.post(`${API_BASE_URL}/issue/ajax/getDepartmentsListFilterInIssue`, params);
	    const data = res.data || [];
	    const options = data.map((v) => ({
	      value: v.department_fk,
	      label: v.department_name,
	    }));
	    setFilterOptions((prev) => ({ ...prev, department: options }));

	    if (selectedValue && options.some((o) => o.value === selectedValue)) {
	      setFilters((prev) => ({ ...prev, department: selectedValue }));
	    }
	  } catch (e) {
	    console.error("loadDepartmentOptions error", e);
	  }
	};

	const loadCategoryOptions = async (selectedValue = "") => {
	  const params = {
	    contract_id_fk: sanitizeParam(filters.contract),
	    department_fk: sanitizeParam(filters.department),
	    category_fk: "",
	    status_fk: sanitizeParam(filters.status),
	    hod: sanitizeParam(filters.hod),
	  };
	  try {
	    const res = await api.post(`${API_BASE_URL}/issue/ajax/getCategoryListFilterInIssue`, params);
	    const data = res.data || [];
	    const options = data.map((v) => ({
	      value: v.category_fk,
	      label: v.category_fk,
	    }));
	    setFilterOptions((prev) => ({ ...prev, category: options }));

	    if (selectedValue && options.some((o) => o.value === selectedValue)) {
	      setFilters((prev) => ({ ...prev, category: selectedValue }));
	    }
	  } catch (e) {
	    console.error("loadCategoryOptions error", e);
	  }
	};

	const loadStatusOptions = async (selectedValue = "") => {
	  const params = {
	    contract_id_fk: sanitizeParam(filters.contract),
	    department_fk: sanitizeParam(filters.department),
	    category_fk: sanitizeParam(filters.category),
	    status_fk: "",
	    hod: sanitizeParam(filters.hod),
	  };
	  try {
	    const res = await api.post(`${API_BASE_URL}/issue/ajax/getStatusListFilterInIssue`, params);
	    const data = res.data || [];
	    const options = data.map((v) => ({
	      value: v.status_fk,
	      label: v.status_fk,
	    }));
	    setFilterOptions((prev) => ({ ...prev, status: options }));

	    if (selectedValue && options.some((o) => o.value === selectedValue)) {
	      setFilters((prev) => ({ ...prev, status: selectedValue }));
	    }
	  } catch (e) {
	    console.error("loadStatusOptions error", e);
	  }
	};

	// Load all filter lists in sequence (like JSP did)
	const loadAllFilterOptions = useCallback(
	  async (restoredFilters = null) => {
	    // restoredFilters may contain keys like 'hod', 'contract_id_fk' etc.
	    // call loaders and pass restored selected value to auto-select
	    await loadContractOptions(restoredFilters?.contract_id_fk || "");
	    await loadHodOptions(restoredFilters?.hod || "");
	    await loadDepartmentOptions(restoredFilters?.department_fk || "");
	    await loadCategoryOptions(restoredFilters?.category_fk || "");
	    await loadStatusOptions(restoredFilters?.status_fk || "");
	  },
	  [filters.contract, filters.department, filters.category, filters.status, filters.hod]
	);

	// Fetch issues from backend (send strings only)
	const fetchIssues = useCallback(
	  async (keepPage = true) => {
	    setLoading(true);
	    try {
	      const params = {
	        contract_id_fk: sanitizeParam(filters.contract),
	        department_fk: sanitizeParam(filters.department),
	        category_fk: sanitizeParam(filters.category),
	        status_fk: sanitizeParam(filters.status),
	        hod: sanitizeParam(filters.hod),
	      };

	      // Save filters to localStorage in JSP format
	      const mapForLs = {
	        contract_id_fk: params.contract_id_fk,
	        department_fk: params.department_fk,
	        category_fk: params.category_fk,
	        status_fk: params.status_fk,
	        hod: params.hod,
	      };
	      saveFiltersToLocalStorage(mapForLs);

	      const res = await api.post(`${API_BASE_URL}/issue/ajax/getIssuesList`, params, {
	        withCredentials: true,
	      });

	      const data = res.data || [];

	      // Keep raw backend objects — render fields directly in JSX
	      setIssues(data);

	      // restore saved page if required
	      if (keepPage) {
	        const pageNo = Number(localStorage.getItem("issuesPageNo") || 0);
	        setPage(pageNo > 0 ? pageNo + 1 : 1); // JSP stored zero-based page; convert to 1-based
	      }
	    } catch (err) {
	      console.error("fetchIssues error", err);
	    } finally {
	      setLoading(false);
	    }
	  },
	  [filters, saveFiltersToLocalStorage]
	);

	// When component mounts: restore filters then load everything
	useEffect(() => {
	  const restored = restoreFiltersFromLocalStorage();
	  // restored keys: e.g. hod, contract_id_fk, department_fk...
	  if (restored) {
	    // Map restored keys to local filters shape
	    setFilters((prev) => ({
	      ...prev,
	      contract: restored.contract_id_fk || "",
	      hod: restored.hod || "",
	      department: restored.department_fk || "",
	      category: restored.category_fk || "",
	      status: restored.status_fk || "",
	    }));
	    // load options based on restored and then fetchIssues
	    loadAllFilterOptions(restored).then(() => fetchIssues());
	  } else {
	    // first time load
	    loadAllFilterOptions(null).then(() => fetchIssues());
	  }
	  // eslint-disable-next-line react-hooks/exhaustive-deps
	}, [location.key]);

	// also re-fetch when filters change (the JSP triggered getIssues after filter changes)
	useEffect(() => {
	  // Whenever filters change due to user action, fetch issues (do not overwrite page)
	  fetchIssues(false);
	}, [filters.contract, filters.hod, filters.department, filters.category, filters.status]);

	// SEARCH + PAGINATION derived lists
	const filteredIssues = issues.filter((c) => {
	  const text = search.trim().toLowerCase();
	  if (!text) return true;

	  // try matching several fields similar to your earlier code
	  return (
	    (c.issue_id || "").toString().toLowerCase().includes(text) ||
	    (c.title || "").toLowerCase().includes(text) ||
	    (c.other_org_resposible_person_name || "").toLowerCase().includes(text) ||
	    (c.other_organization || "").toLowerCase().includes(text) ||
	    (c.location || "").toLowerCase().includes(text) ||
	    (c.status_fk || "").toLowerCase().includes(text)
	  );
	});

	// pagination (page is 1-based)
	const totalRecords = filteredIssues.length;
	const totalPages = Math.max(1, Math.ceil(totalRecords / perPage));
	const currentPage = Math.min(Math.max(1, page), totalPages);
	const paginatedIssues = filteredIssues.slice((currentPage - 1) * perPage, currentPage * perPage);

	// UI handlers
	const handleFilterChange = async (name, value) => {
	  // update single-select fields
	  setFilters((prev) => ({ ...prev, [name]: value }));
	  // when contract changes in JSP they reloaded dependent filters; we mirror that:
	  if (name === "contract") {
	    // clear dependent filters
	    setFilters((prev) => ({ ...prev, hod: "", department: "", category: "", status: "" }));
	    // reload options
	    await loadAllFilterOptions();
	  }
	};

	const clearAllFiltersAndSearch = () => {
	  setFilters({ contract: "", hod: "", department: "", category: "", status: "" });
	  setSearch("");
	  localStorage.setItem("issueFilters", "");
	  localStorage.setItem("issuesPageNo", "0");
	  fetchIssues();
	};

	const getIssue = (issueId) => {
	  navigate(`/issuesform`, { state: { issueId } });
	};
  

	  

	const handleExport = () => {
	  if (!issues || issues.length === 0) {
	    alert("No data to export");
	    return;
	  }

	  const form = document.getElementById("exportIssueForm");

	  document.getElementById("exportContract").value   = filters.contract   || "";
	  document.getElementById("exportHod").value        = filters.hod        || "";
	  document.getElementById("exportDepartment").value = filters.department || "";
	  document.getElementById("exportCategory").value   = filters.category   || "";
	  document.getElementById("exportStatus").value     = filters.status     || "";
	  document.getElementById("exportSearch").value     = search             || "";

	  form.submit();
	};


	  const handleAdd = () => navigate("issuesform");
	  const handleEdit = (issueId) => {
	    navigate("issuesform", { state: { issue_id: issueId } });
	  };



 
	

	
  return (
    <div className={styles.container}>
	<form
	  id="exportIssueForm"
	  action={`${API_BASE_URL}/issue/export-issues`}
	  method="POST"
	  style={{ display: "none" }}
	>
	  <input type="hidden" id="exportContract"   name="contract" />
	  <input type="hidden" id="exportHod"        name="hod" />
	  <input type="hidden" id="exportDepartment" name="department_fk" />
	  <input type="hidden" id="exportCategory"   name="category_fk" />
	  <input type="hidden" id="exportStatus"     name="status_fk" />
	  <input type="hidden" id="exportSearch"     name="search" />
	</form>


      { !isIssueForm &&(
            <div className="pageHeading">
              <h2>Issue</h2>
              <div  className="rightBtns">
                <button className="btn btn-primary" onClick={handleAdd}>
                  <CirclePlus size={16} /> Add
                </button>
            <button className="btn btn-primary" onClick={handleExport}>
              <LuCloudDownload size={16} /> Export
            </button>
              </div>
            </div>
            )}
			
			
			{!isIssueForm && (
				<div className={styles.filterRow}>
							  {Object.keys(filters).map((key) => {
							    // fetch dropdown options dynamically
							    const options = filterOptions[key] || [];

							    // Friendly labels for UI
							    const labelMap = {
							      contract: "Contract",
							      hod: "HOD",
							      department: "Department",
							      category: "Category",
							      status: "Status",
							    };

							    // React-select needs the selected value object
							    const selectedOption =
							      options.find((opt) => opt.value === filters[key]) ||
							      null; // allow placeholder to show

							    return (
							      <div className="filterOptions" key={key} style={{ minWidth: 160 }}>
							        <Select
							          options={[
							            { value: "", label: `Select ${labelMap[key]}` },
							            ...options,
							          ]}
							          classNamePrefix="react-select"
							          value={selectedOption}
							          onChange={(opt) => handleFilterChange(key, opt?.value || "")}
							          placeholder={`Select ${labelMap[key]}`}
							          isSearchable
							          styles={{
							            control: (base) => ({
							              ...base,
							              minHeight: "32px",
							              borderColor: "#ced4da",
							              boxShadow: "none",
							              "&:hover": { borderColor: "#86b7fe" },
							            }),
							            dropdownIndicator: (base) => ({
							              ...base,
							              padding: "2px 6px",
							            }),
							            valueContainer: (base) => ({
							              ...base,
							              padding: "0 6px",
							            }),
							            menu: (base) => ({
							              ...base,
							              zIndex: 9999,
							            }),
							          }}
							        />
							      </div>
							    );
							  })}

							  {/* Clear filter button */}
							  <div style={{ display: "flex", alignItems: "center", gap: 8 }}>
							    <button
							      className="btn btn-2 btn-primary"
							      type="button"
							      onClick={clearAllFiltersAndSearch}
							    >
							      Clear Filter
							    </button>
							  </div>
							</div>
			)
				
			}
			
			

            
            {/* Filters */}
            {!isIssueForm && (
              
              <div className="innerPage">
                 {/* Search + Entries Row */}
              <div className={styles.tableTopRow}>
          <div className="showEntriesCount">
                        <label>Show </label>
                        <select
                          value={perPage}
                          onChange={(e) => { setPerPage(Number(e.target.value)); setPage(1); }}
                        >
                          {[5, 10, 20, 50, 100].map((size) => (
                            <option key={size} value={size}>{size}</option>
                          ))}
                        </select>
                        <span> entries</span>
                      </div>
                <div className={styles.searchWrapper}>
                  <input
                    type="text"
                    placeholder="Search"
                    value={search}
                    onChange={(e) => setSearch(e.target.value)}
                    className={styles.searchInput}
                  />
                  <span className={styles.searchIcon}>🔍</span>
                </div>
              </div>
                
                <div className={`dataTable ${styles.tableWrapper}`}>
				<table className={styles.projectTable}>
				  <thead>
				    <tr>
				      <th>Contract</th>
				      <th>Short Description</th>
				      <th>Location</th>
				      <th>Responsible Person</th>
				      <th>Department</th>
				      <th>Issue Status</th>
				      <th>Last Update</th>
				      <th>Actions</th>
				    </tr>
				  </thead>

				  <tbody>
				    {paginatedIssues.length > 0 ? (
				      paginatedIssues.map((issue, index) => (
				        <tr key={index}>
				          <td>
				            {issue.contract_short_name?.trim()
				              ? issue.contract_short_name
				              : issue.contract_id_fk}
				          </td>

				          <td>{issue.title}</td>
				          <td>{issue.location}</td>
				          <td>{issue.other_org_resposible_person_name}</td>
				          <td>{issue.other_organization}</td>
				          <td>{issue.status_fk}</td>
				          <td>{issue.modified_date}</td>
						  <td>
						    <button
						      className="btn btn-sm btn-outline-primary"
						      onClick={() => handleEdit(issue.issue_id)}
						    >
						      <MdEditNote size={22} />
						    </button>
						  </td>

				        </tr>
				      ))
				    ) : (
				      <tr>
				        <td colSpan="9" style={{ textAlign: "center" }}>
				          No records found
				        </td>
				      </tr>
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
				       onClick={() => page > 1 && setPage(page - 1)}
				       disabled={page === 1}
				     >
				       {"<"}
				     </button>

				     <button className={styles.activePage}>{page}</button>

				     <button
				       onClick={() => page < totalPages && setPage(page + 1)}
				       disabled={page === totalPages}
				     >
				       {">"}
				     </button>
				   </div>
				 </div>

              </div>
            )}
      <Outlet />
    </div>
  );
}