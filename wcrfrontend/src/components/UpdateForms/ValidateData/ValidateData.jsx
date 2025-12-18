import React, {
  useState,
  useEffect,
  useMemo,
  useCallback,
  useContext
} from "react";
import { Outlet, useLocation } from "react-router-dom";
import Select from "react-select";
import styles from "./ValidateData.module.css";
import { API_BASE_URL } from "../../../config";
import api from "../../../api/axiosInstance";
import { RefreshContext } from "../../../context/RefreshContext";
import swal from "sweetalert";


export default function ValidateData() {
	
  const [activeTab, setActiveTab] = useState("pending");
  const [rows, setRows] = useState([]);
  const { refresh } = useContext(RefreshContext);

  const [contractFilter, setContractFilter] = useState("");
  const [structureFilter, setStructureFilter] = useState("");
  const [updatedByFilter, setUpdatedByFilter] = useState("");
  const [searchText, setSearchText] = useState("");
  const location = useLocation();

  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [selectedIds, setSelectedIds] = useState(new Set());



  const [validationList, setValidationList] = useState([]);

  const [listLoading, setListLoading] = useState(false);
  
  const [loading, setLoading] = useState(false);

  const [completedDisabled, setCompletedDisabled] = useState(true);
  const [enableCompleted, setEnableCompleted] = useState(false);


  const [filterOptions, setFilterOptions] = useState({
  	contract: [],
  	structure:[],
  	modifiedBy: [],
  });

  
  
  const [filters, setFilters] = useState({
  	contract: "",
  	structure: "",
  	modifiedBy: "",

  });


   const loadContractOptions = async (selected = "") => {
   	if (filters.contract !== "") return;

	const params = {
		approval_status_fk: "approved"		
	};

   	const res = await api.post(
   		`${API_BASE_URL}/validation/ajax/getContractsInApprovableActivities`,
   		params
   	);

   	const data = res.data || [];

   	setFilterOptions(prev => ({
   		...prev,
   		contract: data.map(val => ({
   			value: val.contract_id_fk,
   			label: val.contract_short_name,
   		})),
   	}));
   };
   
  
  


	const loadStructureOptions = async (selected = "") => {
		if (filters.structure !== "") return;


		const params = {
			contract_id_fk: filters.contract,
			structure: filters.structure,
			updated_by_user_id_fk: filters.modifiedBy,
			approval_status_fk: "Pending",

		};
		const res = await api.post(
			`${API_BASE_URL}/validation/ajax/getStructuresInApprovableActivities`,
			params
		);

		const data = res.data || [];

		setFilterOptions(prev => ({
			...prev,
			structure: data.map(val => ({
				value: val.structure,
				label: val.structure,
			})),
		}));
	};










    const loadUpdatedByOptions = async (selected = "") => {
    	if (filters.modifiedBy !== "") return;

		const params = {
			contract_id_fk: filters.contract,
			structure: filters.structure,
			updated_by_user_id_fk: filters.modifiedBy,
			approval_status_fk: "Pending",

		};

    	const res = await api.post(
			`${API_BASE_URL}/validation/ajax/getUpdatedByListInApprovableActivities`,
    		params
    	);

    	const data = res.data || [];

    	setFilterOptions(prev => ({
    		...prev,
    		modifiedBy: data.map(val => ({
    			value: val.user_id,
    			label: val.user_name,
    		})),
    	}));
    };
   
   
   

   
	useEffect(() => {
		loadContractOptions();
		loadStructureOptions();
		loadUpdatedByOptions();
	}, []);
  
	const filteredData = useMemo(() => {
	  if (!searchText) return validationList;

	  const search = searchText.toLowerCase();

	  return validationList.filter((row) => {
	    return (
	      row.p6_task_code?.toLowerCase().includes(search) ||
	      row.contract_short_name?.toLowerCase().includes(search) ||
	      row.structure?.toLowerCase().includes(search) ||
	      row.component?.toLowerCase().includes(search) ||
	      row.component_id?.toLowerCase().includes(search) ||
	      row.activity_name?.toLowerCase().includes(search) ||
	      row.updated_by?.toLowerCase().includes(search)
	    );
	  });
	}, [validationList, searchText]);

  
  
  
  

  const getToday = () => new Date().toISOString().split("T")[0];

  const handleApprove = async (row) => {
  //  setisLoading(true);

    try {
      const params = new URLSearchParams({
        structure: row.structure,
        progress_id: row.progress_id,
        work_id_fk: row.work_id_fk,
        contract_id_fk: row.contract_id_fk,
      });

      const res = await fetch(
        `${API_BASE_URL}/validation/ajax/approveActivityProgress?${params.toString()}`,
        {
          method: "POST",
          credentials: "include",
        }
      );

      const data = await res.json();

      if (data?.message_flag) {
        swal({
          title: "Success",
          text: data.message,
          icon: "success",
          button: "Okay",
        }).then(() => {
          fetchVaidationList(); // refresh table
        });
      } else {
        swal("Failed", data?.message || "Approval failed", "error");
      }
    } catch (err) {
      console.error(err);
      swal("Error", "Something went wrong", "error");
    } finally {
  //    setLoading(false);
    }
  };


  const handleReject = (row) => {
    swal({
      title: "Are you sure you want to reject progress of activity?",
      icon: "warning",
      buttons: true,
      dangerMode: true,
    }).then((willReject) => {
      if (willReject) {
        confirmReject(row);
      }
    });
  };
  const confirmReject = async (row) => {
  //  setLoading(true);

    try {
      const params = new URLSearchParams({
        structure: row.structure,
        progress_id: row.progress_id,
        work_id_fk: row.work_id_fk,
        contract_id_fk: row.contract_id_fk,
      });

      const res = await fetch(
        `${API_BASE_URL}/validation/ajax/rejectActivityProgress?${params.toString()}`,
        {
          method: "POST",
          credentials: "include",
        }
      );

      const data = await res.json();

      if (data?.message_flag) {
        swal("Rejected", data.message, "success").then(() => {
          fetchVaidationList();
        });
      } else {
        swal("Failed", data?.message || "Rejection failed", "error");
      }
    } catch (err) {
      console.error(err);
      swal("Error", "Something went wrong", "error");
    } finally {
  //    setLoading(false);
    }
  };


  const handleInfo = (taskcode, reporting) => {
 //   const r = rows.find((x) => x.id === id);
    alert(`Info for ${taskcode}\nReporting: ${reporting}`);
  };
  
  
  const fetchVaidationList = useCallback(async () => {
     setListLoading(true);

     try {
       const body = {
         updated_by_user_id_fk: filters.modifiedBy || "",
         contract_id_fk: filters.contract || "",
         structure: filters.structure || "",
         approval_status_fk:
           activeTab === "pending"
             ? "pending"
             : activeTab === "approved"
             ? "approved"
             : "rejected",
       };

       const res = await api.post(
         `${API_BASE_URL}/validation/ajax/getApprovableActivities`,
         body,
         { withCredentials: true }
       );

       setValidationList(Array.isArray(res.data) ? res.data : []);
     } catch (err) {
       console.error("Error fetching validation list:", err);
       setValidationList([]);
     } finally {
       setListLoading(false);
     }
   }, [filters, activeTab]);
  

  

  const total = validationList.length;
  const pages = Math.ceil(total / pageSize);

  const paginatedData = useMemo(() => {
    const start = (page - 1) * pageSize;
    return filteredData.slice(start, start + pageSize);
  }, [filteredData, page, pageSize]);

  // 1️⃣ Reset page when filters / tab / page size change
  useEffect(() => {
    setPage(1);
  }, [filters, activeTab, pageSize]);

  useEffect(() => {
    fetchVaidationList();
  }, [filters, activeTab, fetchVaidationList]);

  useEffect(() => {
    setPage(1);
  }, [searchText]);


 

  
  const handleFilterChange = async (name, value) => {
    let updated = { ...filters, [name]: value };

    if (name === "contract") {
      updated.structure = "";
      updated.modifiedBy = "";
    }

    if (name === "structure") {
      updated.modifiedBy = "";
    }

    setFilters(updated);
  };



  // Select ALL checkboxes in the current visible page
  function toggleSelectAll(checked) {
    if (checked) {
      const ids = new Set(
        validationList
          .filter(
            r =>
              !(r.status === "COMPLETED" && completedDisabled)
          )
          .map(r => r.progress_id)
      );
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

const clearAllFiltersAndSearch = () => {
	setFilters({ contract: "", structure: "", modifiedBy: "" });
	setSearchText("");
	setContractFilter("");
	setUpdatedByFilter("");
	setStructureFilter("");
	setPageSize(10);
};




const renderPageButtons = (page, totalPages, setPageFn) => {
  if (totalPages <= 1) return null;

  const pages = Array.from({ length: totalPages }, (_, i) => i + 1)
    .filter((p) => {
      if (p <= 2 || p > totalPages - 2) return true;
      if (p >= page - 1 && p <= page + 1) return true;
      return false;
    })
    .reduce((acc, p, idx, arr) => {
      if (idx > 0 && p - arr[idx - 1] > 1) acc.push("...");
      acc.push(p);
      return acc;
    }, []);

	return (
	    <div style={{ display: "inline-flex", alignItems: "center", gap: 4 }}>
	      <button
	        disabled={page === 1}
	        onClick={() => setPageFn(page - 1)}
	        className="pageBtn"
	      >
	        ‹
	      </button>

	      {pages.map((item, idx) =>
	        item === "..." ? (
	          <span key={`ellipsis-${idx}`} style={{ padding: "0 6px" }}>
	            ...
	          </span>
	        ) : (
	          <button
	            key={`page-${item}`}
	            onClick={() => setPageFn(item)}
	            className={`pageBtn ${item === page ? "activePage" : ""}`}
	          >
	            {item}
	          </button>
	        )
	      )}

	      <button
	        disabled={page === totalPages}
	        onClick={() => setPageFn(page + 1)}
	        className="pageBtn"
	      >
	        ›
	      </button>
	    </div>
	  );
	};

	const handleBulkApprove = async () => {
	  if (selectedIds.size === 0) {
	    swal("Please select at least one checkbox", "", "error");
	    return;
	  }

//	  setLoading(true);

	  try {
	    // Get selected rows
	    const selectedRows = validationList.filter(row =>
	      selectedIds.has(row.progress_id)
	    );

	    // Distinct values (same logic as JSP)
	    const progressIds = selectedRows.map(r => r.progress_id).join(",");

	    const distinctWorks = [
	      ...new Set(selectedRows.map(r => r.work_id_fk))
	    ].join(",");

	    const distinctContracts = [
	      ...new Set(selectedRows.map(r => r.contract_id_fk))
	    ].join(",");

	    const distinctStructures = [
	      ...new Set(selectedRows.map(r => r.structure))
	    ].join(",");

	    const params = new URLSearchParams({
	      progress_id: progressIds,
	      work_id_fk: distinctWorks,
	      contract_id_fk: distinctContracts,
	      structure: distinctStructures,
	    });

	    const res = await fetch(
	      `${API_BASE_URL}/validation/ajax/approveMultipleActivityProgress?${params.toString()}`,
	      {
	        method: "POST",
	        credentials: "include",
	      }
	    );

	    const data = await res.json();

	    if (data?.message_flag) {
	      swal({
	        title: "Success",
	        text: data.message,
	        icon: "success",
	        button: "Okay",
	      }).then(() => {
	        setSelectedIds(new Set()); // clear selection
	        fetchVaidationList();     // refresh table
	      });
	    } else {
	      swal("Failed", data?.message || "Approval failed", "error");
	    }
	  } catch (err) {
	    console.error(err);
	    swal("Error", "Something went wrong", "error");
	  } finally {
	//    setLoading(false);
	  }
	};
	const confirmBulkReject = async () => {
//	  setLoading(true);

	  try {
	    const progressIds = Array.from(selectedIds).join(",");

	    const params = new URLSearchParams({
	      progress_id: progressIds,
	    });

	    const res = await fetch(
	      `${API_BASE_URL}/validation/ajax/rejectMultipleActivityProgress?${params.toString()}`,
	      {
	        method: "POST",
	        credentials: "include",
	      }
	    );

	    const data = await res.json();

	    if (data?.message_flag) {
	      swal({
	        title: "Success",
	        text: data.message,
	        icon: "success",
	        button: "Okay",
	      }).then(() => {
	        setSelectedIds(new Set());
	        fetchVaidationList();
	      });
	    } else {
	      swal("Failed", data?.message || "Rejection failed", "error");
	    }
	  } catch (err) {
	    console.error(err);
	    swal("Error", "Something went wrong", "error");
	  } finally {
	//    setLoading(false);
	  }
	};

	
	const handleBulkReject = () => {
	  if (selectedIds.size === 0) {
	    swal("Please select at least one checkbox", "", "error");
	    return;
	  }

	  swal({
	    title: "Are you sure you want to reject selected progress of activities?",
	    icon: "warning",
	    buttons: true,
	    dangerMode: true,
	  }).then((isConfirm) => {
	    if (isConfirm) {
	      confirmBulkReject();
	    }
	  });
	};

	
	const toggleDisabledRows = () => {
	  const completedIds = paginatedData
	    .filter(r => r.status === "COMPLETED")
	    .map(r => r.progress_id);

	  setSelectedIds(new Set(completedIds));
	  setCompletedDisabled(false);           
	};




		
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
	  						{Object.keys(filters).map((key) => {
	  							const options = filterOptions[key] || [];
	  							// key mapping to friendly label
	  							const labelMap = {
	  								contract: "Contract",
	  								structure: "Structure",
	  								modifiedBy: "modifiedBy",
	  							};
	  							return (
	  								<div className="filterOptions" key={key} style={{ minWidth: 160 }}>
	  									<Select
	  										options={[{ value: "", label: `Select ${labelMap[key] || key}` }, ...options]}
	  										classNamePrefix="react-select"
	  										value={
	  											options.find((opt) => opt.value === filters[key]) || {
	  												value: "",
	  												label: `Select ${labelMap[key] || key}`,
	  											}
	  										}
	  										onChange={(selectedOption) =>
	  											handleFilterChange(key, selectedOption?.value || "")
	  										}
	  										placeholder={`Select ${labelMap[key] || key}`}
	  										isSearchable
	  										styles={{
	  											control: (base) => ({
	  												...base,
	  												minHeight: "32px",
	  												borderColor: "#ced4da",
	  												boxShadow: "none",
	  												"&:hover": { borderColor: "#86b7fe" },
	  											}),
	  											dropdownIndicator: (base) => ({ ...base, padding: "2px 6px" }),
	  											valueContainer: (base) => ({ ...base, padding: "0 6px" }),
	  											menu: (base) => ({ ...base, zIndex: 9999 }),
	  										}}
	  									/>
	  								</div>
	  							);
	  						})}

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
						
						{activeTab === "pending" && (
						  <div
						    className="row no-mar"
						    style={{
						      marginBottom: 0,
						      display: "flex",
						      justifyContent: "center"
						    }}
						  >
						    <div
						      className="btn-holder"
						      style={{
						        display: "flex",
						        gap: "12px",
						        alignItems: "center",
						        flexWrap: "wrap",
						        justifyContent: "center"
						      }}
						    >
						      {/* APPROVE */}
						      <button
						        className={`btn waves-effect t-c ${
						          selectedIds.size === 0 || loading ? "disabled" : ""
						        }`}
						        onClick={handleBulkApprove}
						        disabled={selectedIds.size === 0 || loading}
						      >
						        <i className="btn-2 btn-green"></i> Approve
						      </button>

						      {/* REJECT */}
						      <button
						        className={`btn waves-effect bg-s t-c ${
						          selectedIds.size === 0 || loading ? "disabled" : ""
						        }`}
						        onClick={handleBulkReject}
						        disabled={selectedIds.size === 0 || loading}
						      >
						        <i className="btn-2 btn-red"></i> Reject
						      </button>

						      {/* ENABLE COMPLETED */}
						      <button
						        className={`btn waves-effect grey darken-1 ${
						          completedDisabled ? "" : "disabled"
						        }`}
						        onClick={toggleDisabledRows}
						        disabled={!completedDisabled}
						      >
						        <i
						          className={`fa ${
						            completedDisabled ? "fa-unlock" : "fa-lock"
						          }`}
						        ></i>{" "}
						        Enable Completed Activities
						      </button>
						    </div>
						  </div>
						)}



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
	      {/* ---------- FIRST HEADER ROW ---------- */}
	      <tr className="top-head">
		  <th rowSpan={2} className="col-check">
		    {activeTab === "pending" && (() => {
		      // rows that are actually selectable right now
		      const selectableRows = validationList.filter(
		        r => !(r.status === "COMPLETED" && completedDisabled)
		      );

		      return (
		        <input
		          type="checkbox"
		          onChange={(e) => toggleSelectAll(e.target.checked)}
		          checked={
		            selectableRows.length > 0 &&
		            selectableRows.every(r => selectedIds.has(r.progress_id))
		          }
		          disabled={selectableRows.length === 0}
		        />
		      );
		    })()}
		  </th>
	        <th rowSpan={2}>Task Code</th>
	        <th rowSpan={2}>Contract</th>
	        <th rowSpan={2}>Structure</th>
	        <th rowSpan={2}>Component</th>
	        <th rowSpan={2}>Element</th>
	        <th rowSpan={2}>Activity Name</th>
	        <th rowSpan={2}>Unit</th>
	        <th rowSpan={2}>Scope</th>

	        <th colSpan={3} className="group">Progress till last update</th>

	        <th rowSpan={2}>Reporting</th>
	        <th rowSpan={2}>Actual Progress Updated / Updated Scope</th>

	        <th colSpan={3} className="group">Results after acceptance</th>

	        <th rowSpan={2}>
	          {activeTab === "pending"
	            ? "Updated On"
	            : activeTab === "approved"
	            ? "Approved On"
	            : "Rejected On"}
	        </th>

	        {activeTab === "pending" && <th rowSpan={2}>Action</th>}
	      </tr>

	      {/* ---------- SECOND HEADER ROW ---------- */}
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
	      {listLoading ? (
	        <tr>
	          <td colSpan={activeTab === "pending" ? 19 : 18} style={{ textAlign: "center" }}>
	            Loading...
	          </td>
	        </tr>
	      ) : paginatedData.length === 0 ? (
	        <tr>
	          <td colSpan={activeTab === "pending" ? 19 : 18} style={{ textAlign: "center" }}>
	            No data available in table
	          </td>
	        </tr>
	      ) : (
			paginatedData.map((row) => {
			  // ===== COMPUTED FIELDS (FROM JSP LOGIC) =====
			  const cumulativeCompleted = Number(row.cumulative_completed) || 0;
			  const actualForDay = Number(row.actual_for_the_day) || 0;

			  const activityLevelAfterAccept = (
			    cumulativeCompleted + actualForDay
			  ).toFixed(1);

			  const reportingText = `${row.updated_by ?? ""} ${row.progress_date ?? ""}`;

			  // ===== JSP BEHAVIOR MATCH =====
			  const isCompleted = row.status === "COMPLETED";
			  const shouldLockCompleted =
			    activeTab === "pending" && isCompleted && completedDisabled;

			  const isCheckboxDisabled =
			    activeTab === "pending" && isCompleted && !enableCompleted;

			  return (
			    <tr
			      key={row.progress_id}
			      className={`data-row ${row.status} ${
			        isCheckboxDisabled ? "disabled-row" : ""
			      }`}
			    >
			      {/* CHECKBOX */}
			      <td className="col-check">
			        {activeTab === "pending" && (
			          <input
			            type="checkbox"
			            disabled={isCheckboxDisabled}
			            checked={selectedIds.has(row.progress_id)}
			            onChange={() => toggleSelect(row.progress_id)}
			          />
			        )}
			      </td>

			      <td>{row.p6_task_code}</td>
			      <td>{row.contract_short_name}</td>
			      <td>{row.structure}</td>
			      <td>{row.component}</td>
			      <td>{row.component_id}</td>
			      <td>{row.activity_name}</td>
			      <td>{row.unit}</td>
			      <td>{row.total_scope}</td>

			      {/* Progress till last update */}
			      <td>{row.cumulative_completed}</td>
			      <td>{row.component_per_prior}</td>
			      <td>{row.structure_per_prior}</td>

			      {/* Reporting */}
			      <td>{reportingText}</td>

			      {/* Actual updated */}
			      <td>
			        {row.actual_for_the_day}
			        {row.updated_scope ? ` / ${row.updated_scope}` : ""}
			      </td>

			      {/* Results after acceptance */}
			      <td>{activityLevelAfterAccept}</td>
			      <td>{row.component_per_post}</td>
			      <td>{row.structure_per_post}</td>

			      {/* Dates */}
			      <td>
			        {activeTab === "pending"
			          ? row.updated_on
			          : activeTab === "approved"
			          ? row.approved_on
			          : row.rejected_on}
			      </td>

			      {/* ACTIONS (PENDING ONLY) */}
			      {activeTab === "pending" && (
			        <td>
			          <div className={styles.tableActionBtns}>
			            <button
			              className="btn-2 btn-green"
			              onClick={() => handleApprove(row)}
			            >
			              ✔
			            </button>

			            <button
			              className="btn-2 btn-red"
			              onClick={() => handleReject(row)}
			            >
			              ✖
			            </button>

			            <button
			              className="btn-2 btn-yellow"
			              onClick={() =>
			                handleInfo(row.p6_task_code, reportingText)
			              }
			            >
			              i
			            </button>
			          </div>
			        </td>
			      )}
			    </tr>
			  );
			})
	      )}
	    </tbody>
	  </table>

	  


      </div>

      {/* Pagination */}
	  <div className="paginationRow">
	    <div className="paginationInfo">
	      Showing {(page - 1) * pageSize + 1} –{" "}
	      {Math.min(page * pageSize, total)} of {total}
	    </div>

	    <div className="paginationControls">
	      {renderPageButtons(page, pages, setPage)}
	    </div>
	  </div>


    </div>
  </div>
  );
}