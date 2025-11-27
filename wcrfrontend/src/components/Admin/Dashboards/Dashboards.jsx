import React, { useContext, useState, useEffect } from "react";
import { RefreshContext } from "../../../context/RefreshContext";
import Select from "react-select";
import { CirclePlus } from "lucide-react";
import { LuUpload, LuDownload } from "react-icons/lu";
import { Outlet, useNavigate, useLocation } from "react-router-dom"; 
import api from "../../../api/axiosInstance";
import styles from './Dashboards.module.css';
import { API_BASE_URL } from "../../../config";

export default function Dashboards() {
	const location = useLocation();
	  const navigate = useNavigate();
	   const [search, setSearch] = useState("");
	  const [page, setPage] = useState(1);
	  const [perPage, setPerPage] = useState(10);
	  const { refresh } = useContext(RefreshContext);
	  
	  const [dashboards, setDashBoard] = useState([]);
	  const [showModal, setShowModal] = useState(false);
	  
	  const [filters, setFilters] = useState({
	     module: "",
	     dashboardType: "",
	     status: "",
	   });
	   
	   const openModal = () => setShowModal(true);
	      
	  /* useEffect(() => {
	      fetchProjects();
	    }, []);

	    const fetchProjects = async () => {
	      try {
	        const res = await api.get(`${API_BASE_URL}/projects`, { withCredentials: true });
	        setProjects(res.data || []);
	      } catch (err) {
	        console.error("Error fetching projects:", err);
	      }
	    };
	 */
 const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setFilters({ ...filters, [name]: value });
  };

  const handleAdd = () => navigate("dashboardform");
  const handleEdit = (project) => navigate("dashboardform", { state: { project } });

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
            {Object.keys(filters).map((key) => {
              const options = [
                { value: "", label: `Select ${key}` },
                { value: "demo1", label: `${key} 1` },
                { value: "demo2", label: `${key} 2` },
              ];

              return (
                <div className={styles.filterOptions} key={key}>
                  <Select
                    options={options}
                    value={options.find((opt) => opt.value === filters[key])}
                    onChange={(selectedOption) =>
                      handleFilterChange({
                        target: { name: key, value: selectedOption.value },
                      })
                    }
                    placeholder={`Select ${key}`}
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
                        zIndex: 9999, // prevents clipping inside modals or cards
                      }),
                    }}
                  />
                </div>
              );
            })}
            <button className="btn btn-2 btn-primary">Clear Filters</button>
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
                {dashboards.length > 0 ? (
                  dashboards.map((dash, index) => (
                    <tr key={index}>
                      <td>{dash.name}</td>
                      <td>{dash.module}</td>
                      <td>{dash.folder}</td>
                      <td>{dash.status}</td>
                      <td>{dash.userRoleAccess}</td>
                      <td>{dash.userTypeAccess}</td>
                      <td>{dash.action}</td>
                      <td>
                        <button className="btn btn-sm btn-outline-primary" onClick={() => handleEdit(dash)}>
                          Edit
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
        </div>
      )}
        <Outlet />
    </div>
  );
}
