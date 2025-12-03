	import React, { useState, useEffect } from "react";
	import Select from "react-select";
	import styles from "./Project.module.css";
	import { CirclePlus } from "lucide-react";
	import { LuCloudDownload } from "react-icons/lu";
	import api from "../../../api/axiosInstance";
	import { Outlet, useNavigate, useLocation } from "react-router-dom";
	import { API_BASE_URL } from "../../../config";
	import * as XLSX from "xlsx";
	
	import jsPDF from "jspdf";
	import autoTable from "jspdf-autotable";  
	import { MdEditNote, MdDelete  } from 'react-icons/md';
	
	export default function Project() {
	  const location = useLocation();
	  const navigate = useNavigate();
	  const [projects, setProjects] = useState([]);
	  const [filteredProjects, setFilteredProjects] = useState([]);
	
	  // Filters
	  const [selectedType, setSelectedType] = useState(null);
	  const [selectedStatus, setSelectedStatus] = useState(null);
	
	  // Global search
	  const [globalSearch, setGlobalSearch] = useState("");
	
	  // Pagination
	  const [currentPage, setCurrentPage] = useState(1);
	  const itemsPerPage = 5;
	
	  useEffect(() => {
	    fetchProjects();
	  }, []);
	
	  const fetchProjects = async () => {
	    try {
	      const res = await api.get(`${API_BASE_URL}/projects/api/getProjectList`, { withCredentials: true });
	      const data = res.data || [];
	      setProjects(data);
	      setFilteredProjects(data);
	    } catch (err) {
	      console.error("Error fetching projects:", err);
	    }
	  };
	
	  // Filters + Global search
	  useEffect(() => {
	    let filtered = [...projects];
	    if (selectedType) filtered = filtered.filter(p => p.project_type_name === selectedType.value);
	    if (selectedStatus) filtered = filtered.filter(p => p.project_status === selectedStatus.value);
	    if (globalSearch.trim() !== "") {
	      const term = globalSearch.toLowerCase();
	      filtered = filtered.filter(p => Object.values(p).some(v => v && String(v).toLowerCase().includes(term)));
	    }
	    setFilteredProjects(filtered);
	    setCurrentPage(1);
	  }, [selectedType, selectedStatus, globalSearch, projects]);
	
	  const handleAdd = () => navigate("projectform");
	  const handleEdit = (project) => navigate("projectform", { state: { project } });
	  const handleDelete = async (proj) => {
	    if (window.confirm(`Are you sure you want to delete project "${proj.project_name}"?`)) {
	      try {
			const response = await fetch(
			  `${API_BASE_URL}/projects/api/deleteProject/${proj.project_id}`,
			  {
			    method: "DELETE",
			    credentials: "include",
			    headers: {
			      "Content-Type": "application/json",
			    },
			  }
			);
	
	        if (response.ok) {
	          alert("Project deleted successfully!");
	          // Optional: remove deleted project from UI
	          setProjects((prev) => prev.filter(p => p.project_id !== proj.project_id));
	        } else {
	          const errorText = await response.text();
	          alert("Failed to delete project: " + errorText);
	        }
	      } catch (error) {
	        console.error("Error deleting project:", error);
	        alert("An error occurred while deleting the project.");
	      }
	    }
	  };
	
	  const isProjectForm = location.pathname.endsWith("/projectform");
	
	  const projectTypeOptions = [...new Set(projects.map(p => p.project_type_name).filter(Boolean))].map(t => ({ label: t, value: t }));
	  const projectStatusOptions = [...new Set(projects.map(p => p.project_status).filter(Boolean))].map(s => ({ label: s, value: s }));
	
	  const handleClearFilters = () => {
	    setSelectedType(null);
	    setSelectedStatus(null);
	    setGlobalSearch("");
	  };
	
	  // Pagination
	  const totalPages = Math.ceil(filteredProjects.length / itemsPerPage);
	  const startIndex = (currentPage - 1) * itemsPerPage;
	  const paginatedProjects = filteredProjects.slice(startIndex, startIndex + itemsPerPage);
	  const handlePrevPage = () => setCurrentPage(prev => Math.max(prev - 1, 1));
	  const handleNextPage = () => setCurrentPage(prev => Math.min(prev + 1, totalPages));
	
	  // Export Excel
	  const handleExportExcel = () => {
	    const ws = XLSX.utils.json_to_sheet(filteredProjects);
	    const wb = XLSX.utils.book_new();
	    XLSX.utils.book_append_sheet(wb, ws, "Projects");
	    XLSX.writeFile(wb, "Projects.xlsx");
	  };
	
	
	  const handleExportPDF = () => {
	    const doc = new jsPDF();
	
	    const tableColumn = [
	      "ID",
	      "Name",
	      "Status",
	      "Type",
	      "Zone",
	      "Plan Head",
	      "Year",
	      "Amount",
	      "Commission Date",
	      "Division",
	      "Sections",
	      "Remarks"
	    ];
	
	    const tableRows = filteredProjects.map(p => [
	      p.project_id,
	      p.project_name,
	      p.project_status,
	      p.project_type_name,
	      p.railway_zone,
	      p.plan_head_number,
	      p.sanctioned_year,
	      p.sanctioned_amount,
	      p.sanctioned_commissioning_date,
	      p.division,
	      p.sections,
	      p.remarks
	    ]);
	
	    doc.text("Project List", 14, 15);
	
	    autoTable(doc, {
	      head: [tableColumn],
	      body: tableRows,
	      startY: 20,
	      styles: {
	        fontSize: 8,
	        cellPadding: 3,
	        overflow: 'linebreak',   // Allows wrapping in cells (optional)
	        valign: 'middle',
	        halign: 'left',
	      },
	      headStyles: {
	        fillColor: [22, 160, 133],  // Green background
	        textColor: 255,
	        fontStyle: 'bold',
	        halign: 'center',
	        cellPadding: 4,
	        minCellWidth: 15,
	      },
	      columnStyles: {
	        0: { cellWidth: 15, halign: 'center' }, // ID
	        1: { cellWidth: 50, halign: 'left' },   // Name wider
	        2: { cellWidth: 20, halign: 'center' }, // Status
	        3: { cellWidth: 30, halign: 'left' },   // Type
	        4: { cellWidth: 15, halign: 'center' }, // Zone
	        5: { cellWidth: 20, halign: 'center' }, // Plan Head
	        6: { cellWidth: 15, halign: 'center' }, // Year
	        7: { cellWidth: 25, halign: 'right' },  // Amount, right aligned
	        8: { cellWidth: 30, halign: 'center' }, // Commission Date
	        9: { cellWidth: 20, halign: 'center' }, // Division
	        10: { cellWidth: 20, halign: 'center' },// Sections
	        11: { cellWidth: 40, halign: 'left' },  // Remarks wider
	      }
	    });
	
	    doc.save("Projects.pdf");
	  };
	
	
	
	
	
	  return (
	    <div className={styles.container}>
	      {!isProjectForm && (
	        <div className="pageHeading">
	          <h2>Project</h2>
	          <div className="rightBtns">
	            <button className="btn btn-primary" onClick={handleAdd}><CirclePlus size={16} /> Add</button>
	            <button className="btn btn-primary" onClick={handleExportExcel}><LuCloudDownload size={16} /> Export Excel</button>
	            <button className="btn btn-primary" onClick={handleExportPDF}><LuCloudDownload size={16} /> Export PDF</button>
	          </div>
	        </div>
	      )}
	
	      {!isProjectForm && (
	        <div className="innerPage">
	          {/* Filters row */}
	          <div className={styles.filterRow}>
	            <div className={styles.leftFilters}>
	              <div className={styles.filterGroup}>
	                <label>Project Status:</label>
	                <Select options={projectStatusOptions} value={selectedStatus} onChange={setSelectedStatus} isClearable placeholder="Select status"/>
	              </div>
	              <div className={styles.filterGroup}>
	                <label>Project Type:</label>
	                <Select options={projectTypeOptions} value={selectedType} onChange={setSelectedType} isClearable placeholder="Select type"/>
	              </div>
	            </div>
				<button className="btn btn-primary" onClick={handleClearFilters}>Clear Filters</button>
	            <div className={styles.rightFilters}>
	              <input type="text" className={styles.globalSearch} placeholder="Search all columns..." value={globalSearch} onChange={e => setGlobalSearch(e.target.value)}/>
	            </div>
	          </div>
	
	          {/* Table */}
	          <div className={`dataTable ${styles.tableWrapper}`}>
	            <table className={styles.projectTable}>
	              <thead>
	                <tr>
	                  <th>ID</th><th>Name</th><th>Status</th><th>Type</th><th>Zone</th>
	                  <th>Plan Head</th><th>Year</th><th>Amount</th><th>Commission Date</th>
	                  <th>Division</th><th>Sections</th><th>Remarks</th><th>Action</th>
	                </tr>
	              </thead>
	              <tbody>
	                {paginatedProjects.length > 0 ? (
	                  paginatedProjects.map((proj, i) => (
	                    <tr key={i}>
	                      <td>{proj.project_id}</td><td>{proj.project_name}</td><td>{proj.project_status}</td>
	                      <td>{proj.project_type_name}</td><td>{proj.railway_zone}</td><td>{proj.plan_head_number}</td>
	                      <td>{proj.sanctioned_year}</td><td>{proj.sanctioned_amount}</td><td>{proj.sanctioned_commissioning_date}</td>
	                      <td>{proj.division}</td><td>{proj.sections}</td><td>{proj.remarks}</td>
						  <td className="d-flex justify-content-center align-items-center gap-2">
						    <button
						      type="button"
						      className="btn btn-sm btn-outline-primary"
						      onClick={() => handleEdit(proj)}
						      title="Edit"
						    >
						      <MdEditNote size={20} />
						    </button>
	
						    <button
						      type="button"
						      className="btn btn-sm btn-outline-danger"
						      onClick={() => handleDelete(proj)}
						      title="Delete"
						    >
						      <MdDelete size={20} />
						    </button>
						  </td>
	
	                    </tr>
	                  ))
	                ) : (
	                  <tr><td colSpan="13" style={{ textAlign: "center" }}>No records found</td></tr>
	                )}
	              </tbody>
	            </table>
	          </div>
	
	          {/* Pagination */}
	          {filteredProjects.length > 0 && (
	            <div className={styles.paginationBar}>
	              <div className={styles.paginationInfo}>
	                Showing {startIndex + 1} to {Math.min(startIndex + itemsPerPage, filteredProjects.length)} of {filteredProjects.length} entries
	              </div>
	              <div className={styles.paginationButtons}>
	                <button className="btn btn-sm btn-outline-primary" onClick={handlePrevPage} disabled={currentPage===1}>Previous</button>
	                {Array.from({ length: totalPages }, (_, i) => (
	                  <button key={i+1} className={`btn btn-sm ${currentPage===i+1?"btn-primary":"btn-outline-primary"}`} onClick={()=>setCurrentPage(i+1)}>{i+1}</button>
	                ))}
	                <button className="btn btn-sm btn-outline-primary" onClick={handleNextPage} disabled={currentPage===totalPages}>Next</button>
	                <span className={styles.totalPageText}>&nbsp;of {totalPages} pages</span>
	              </div>
	            </div>
	          )}
	        </div>
	      )}
	      <Outlet/>
	    </div>
	  );
	}
