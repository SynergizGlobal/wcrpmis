import React, { useContext, useState, useEffect } from "react";
import { RefreshContext } from "../../../context/RefreshContext";
import Select from "react-select";
import { CirclePlus } from "lucide-react";
import { LuUpload, LuDownload } from "react-icons/lu";
import { Outlet, useNavigate, useLocation } from "react-router-dom"; 
import api from "../../../api/axiosInstance";
import styles from './Dashboards.module.css';
import { API_BASE_URL } from "../../../config";
import { MdEditNote } from "react-icons/md";

export default function Dashboards() {
	const location = useLocation();
	  const navigate = useNavigate();
	   const [search, setSearch] = useState("");
	  const [page, setPage] = useState(1);
	  const [perPage, setPerPage] = useState(10);
	  const { refresh } = useContext(RefreshContext);
	  
	  const [dashboards, setDashBoards] = useState([]);
	  const [showModal, setShowModal] = useState(false);
	  
	  const openModal = () => setShowModal(true);
	  
	  const [filters, setFilters] = useState({
	     module: "",
	     dashboardType: "",
	     status: "",
	   });
	   
	   const [moduleOptions, setModuleOptions] = useState([]);
	   const [dashboardTypeOptions, setDashboardTypeOptions] = useState([]);
	   const [statusOptions, setStatusOptions] = useState([]);
	      
	   useEffect(() => {
	     // MODULE FILTER (from backend)
	     api.get(`${API_BASE_URL}/api/dashboard/modules`)
	       .then((res) => {
	         setModuleOptions(
	           res.data.map(m => ({
	             value: m,
	             label: m
	           }))
	         );
	       })
	       .catch(err => console.error("Module fetch error:", err));

	     // DASHBOARD TYPE FILTER (static)
	     setDashboardTypeOptions([
	       { value: "module", label: "Module" },
	       { value: "project", label: "Project" }
	     ]);

	     // STATUS FILTER (static)
	     setStatusOptions([
	       { value: "Active", label: "Active" },
	       { value: "Inactive", label: "Inactive" }
	     ]);

	   }, [refresh, location]);
	   
	   // FILTER CHANGE HANDLER
	   const handleFilterChange = (name, value) => {
	     setFilters(prev => ({ ...prev, [name]: value }));
	     setPage(1);
	   };

	   // CLEAR FILTERS
	   const handleClearFilters = () => {
	     setFilters({
	       module: "",
	       dashboardType: "",
	       status: "",
	     });

	     setPage(1);
	   };

	   // SEARCH 
	   const filteredDashboards = dashboards.filter(d => {
	       const txt = search.toLowerCase();
	       return (
	         d.dashboard_name?.toLowerCase().includes(txt) ||
	         d.module_name_fk?.toLowerCase().includes(txt) ||
	         d.folder?.toLowerCase().includes(txt) ||
	         d.user_role_access?.toLowerCase().includes(txt) ||
	         d.user_type_access?.toLowerCase().includes(txt) ||
	         d.user_access?.toLowerCase().includes(txt)
	       );
	   });

	   // PAGINATION
	   const totalRecords = filteredDashboards.length;
	   const totalPages = Math.ceil(totalRecords / perPage);

	   const paginatedRows = filteredDashboards.slice(
	     (page - 1) * perPage,
	     page * perPage
	   );
	   
	   // INITIAL LOAD ONLY
	   useEffect(() => {
	     fetchDashboards();  
	   }, [refresh, location]);

	   // WHEN FILTERS CHANGE → always POST
	   useEffect(() => {
	     fetchDashboardList();
	   }, [filters, refresh, location]);

	   const fetchDashboards = () => {
	     api.get(`${API_BASE_URL}/api/dashboard/list`)
	       .then(res => setDashBoards(res.data))
	       .catch(err => console.error("Dashboard list error:", err));
	   };

	   const fetchDashboardList = () => {
	     api.post(`${API_BASE_URL}/api/dashboard/list`, {
	       module_name_fk: filters.module,
	       dashboard_type_fk: filters.dashboardType,
	       soft_delete_status_fk: filters.status
	     })
	     .then(res => setDashBoards(res.data))
	     .catch(err => console.error("Dashboard List Error:", err));
	   };

  const handleAdd = () => navigate("dashboardform");
  const handleEdit = async (dash) => {
    try {
      const res = await api.get(`${API_BASE_URL}/api/dashboard/get/${dash.dashboard_id}`);
      navigate("dashboardform", { state: { dashboardData: res.data } });
    } catch (err) {
      console.error("Get dashboard error:", err);
      alert("Failed to load dashboard: " + (err?.response?.data?.message || err.message));
    }
  };

  const isDashboardForm = location.pathname.endsWith("/dashboardform");

  return (
    <div className={styles.container}>
      {/* Top Bar */}
      { !isDashboardForm &&(
      <div className="pageHeading">
		<h2>Dashboards Access</h2>
        <div  className="rightBtns">
		<button className="btn-2 transparent-btn">
			<LuDownload size={16} />
		</button>
		<button className="btn btn-primary" onClick={openModal}>
			<LuUpload size={16} /> Upload
		</button>
          <button className="btn btn-primary" onClick={handleAdd}>
            <CirclePlus size={16} /> Add
          </button>
        </div>
      </div>
      )}
      
      {/* Filters */}
      {!isDashboardForm && (
        
        <div className="innerPage">
          <div className={styles.filterRow}>
		  <div className={styles.filterRow}>

		    {/* MODULE FILTER */}
		    <Select
		      options={[{ value: "", label: "Select Module" }, ...moduleOptions]}
		      value={moduleOptions.find(o => o.value === filters.module) || null}
			  onChange={opt => handleFilterChange("module", opt ? opt.value : null)}
		      placeholder="Select Module"
		    />

		    {/* DASHBOARD TYPE FILTER */}
		    <Select
		      options={[{ value: "", label: "Select Dashboard Type" }, ...dashboardTypeOptions]}
		      value={dashboardTypeOptions.find(o => o.value === filters.dashboardType) || null}
		      onChange={opt => handleFilterChange("dashboardType", opt? opt.value : null)}
		      placeholder="Select Dashboard Type"
		    />

		    {/* STATUS FILTER */}
		    <Select
		      options={[{ value: "", label: "Select Status" }, ...statusOptions]}
		      value={statusOptions.find(o => o.value === filters.status) || null}
		      onChange={opt => handleFilterChange("status", opt?opt.value : null)}
		      placeholder="Select Status"
		    />

		    {/* CLEAR BUTTON */}
		    <button className="btn btn-2 btn-primary" onClick={handleClearFilters}>
		      Clear Filters
		    </button>

		  </div>
          </div>
		  
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
				<th>Name</th>
				<th>Module</th>
	              <th>Folder</th>
	              <th>Status</th>
	              <th>User Role Access</th>
	              <th>User Type Access</th>
	              <th>User Access</th>
	              <th>Action</th>
                </tr>
              </thead>
			  <tbody>
			  {paginatedRows.length > 0 ? (
			      paginatedRows.map((dash, i) => (
			        <tr key={i}>
			          <td>{dash.dashboard_name}</td>
			          <td>{dash.module_name_fk}</td>
			          <td>{dash.folder || "-"}</td>
			          <td>{dash.soft_delete_status_fk}</td>
			          <td>{dash.user_role_access}</td>
			          <td>{dash.user_type_access}</td>
			          <td>{dash.user_access}</td>

			          <td>
			            <button 
			              className="btn btn-sm btn-outline-primary"
			              onClick={() => handleEdit(dash)}
			            >
			              <MdEditNote size={22} />
			            </button>
			          </td>
			        </tr>
			      ))
			    ) : (
			      <tr>
			        <td colSpan="8" style={{ textAlign: "center" }}>
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
		  	        : `${(page - 1) * perPage + 1} - ${Math.min(page * perPage, totalRecords)}`}
		  	      {" "}of {totalRecords}
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
