import React, { useState, useEffect } from "react";
import Select from "react-select";
import styles from "./Project.module.css";
import { CirclePlus } from "lucide-react";
import { LuCloudDownload } from "react-icons/lu";
import { MdEditNote, MdDelete } from "react-icons/md";
import api from "../../../api/axiosInstance";
import { Outlet, useNavigate, useLocation } from "react-router-dom";
import { API_BASE_URL } from "../../../config";
import * as XLSX from "xlsx";
import jsPDF from "jspdf";
import autoTable from "jspdf-autotable";

export default function Project() {
  const location = useLocation();
  const navigate = useNavigate();

  const [projects, setProjects] = useState([]);
  const [filteredProjects, setFilteredProjects] = useState([]);

  const [selectedType, setSelectedType] = useState(null);
  const [selectedStatus, setSelectedStatus] = useState(null);
  const [globalSearch, setGlobalSearch] = useState("");

  const [designPerPage, setDesignPerPage] = useState(10);
  const [designPage, setDesignPage] = useState(1);

  const isProjectForm = location.pathname.endsWith("/projectform");

  /* ✅ Fetch Projects */
  useEffect(() => {
    fetchProjects();
  }, []);

  const fetchProjects = async () => {
    try {
      const res = await api.get(
        `${API_BASE_URL}/projects/api/getProjectList`,
        { withCredentials: true }
      );
      const data = res.data || [];
      setProjects(data);
    } catch (err) {
      console.error("Error fetching projects:", err);
    }
  };

  /* ✅ Single Filtering Logic */
  useEffect(() => {
    let filtered = [...projects];

    if (selectedType) {
      filtered = filtered.filter(
        p => p.project_type_name === selectedType.value
      );
    }

    if (selectedStatus) {
      filtered = filtered.filter(
        p => p.project_status === selectedStatus.value
      );
    }

    if (globalSearch.trim()) {
      const term = globalSearch.toLowerCase();

      filtered = filtered.filter(p =>
        Object.values(p).some(v =>
          v && String(v).toLowerCase().includes(term)
        )
      );
    }

    setFilteredProjects(filtered);
    setDesignPage(1);

  }, [projects, selectedType, selectedStatus, globalSearch]);

  /* ✅ Pagination */
  const indexOfLast = designPage * designPerPage;
  const indexOfFirst = indexOfLast - designPerPage;

  const currentProjects = filteredProjects.slice(indexOfFirst, indexOfLast);
  const totalPages = Math.ceil(filteredProjects.length / designPerPage);

  /* ✅ Options */
  const projectTypeOptions = [
    ...new Set(projects.map(p => p.project_type_name).filter(Boolean))
  ].map(t => ({ label: t, value: t }));

  const projectStatusOptions = [
    ...new Set(projects.map(p => p.project_status).filter(Boolean))
  ].map(s => ({ label: s, value: s }));

  /* ✅ Actions */
  const handleAdd = () => navigate("projectform");

  const handleEdit = (project) =>
    navigate("projectform", { state: { project } });

  const handleDelete = async (proj) => {
    if (!window.confirm(`Delete project "${proj.project_name}"?`)) return;

    try {
      const response = await fetch(
        `${API_BASE_URL}/projects/api/deleteProject/${proj.project_id}`,
        { method: "DELETE", credentials: "include" }
      );

      if (response.ok) {
        alert("Project deleted successfully");
        setProjects(prev =>
          prev.filter(p => p.project_id !== proj.project_id)
        );
      } else {
        const errText = await response.text();
        alert(errText);
      }
    } catch (err) {
      console.error(err);
    }
  };

  /* ✅ Export Excel */
  const handleExportExcel = () => {
    const ws = XLSX.utils.json_to_sheet(filteredProjects);
    const wb = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, "Projects");
    XLSX.writeFile(wb, "Projects.xlsx");
  };

  /* ✅ Export PDF */
  const handleExportPDF = () => {
    const doc = new jsPDF();

    const columns = [
      "ID", "Name", "Status", "Type", "Zone",
      "Plan Head", "Year", "Amount", "Commission Date",
      "Division", "Sections", "Remarks"
    ];

    const rows = filteredProjects.map(p => [
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
      head: [columns],
      body: rows,
      startY: 20,
      styles: { fontSize: 8 }
    });

    doc.save("Projects.pdf");
  };

  const handleClearFilters = () => {
    setSelectedType(null);
    setSelectedStatus(null);
    setGlobalSearch("");
  };

  return (
    <div className={styles.container}>
      {!isProjectForm && (
        <>
          <div className="pageHeading">
            <h2>Project</h2>
            <div className="rightBtns">
              <button className="btn btn-primary" onClick={handleAdd}>
                <CirclePlus size={16} /> Add
              </button>

              <button className="btn btn-primary" onClick={handleExportExcel}>
                <LuCloudDownload size={16} /> Excel
              </button>

              <button className="btn btn-primary" onClick={handleExportPDF}>
                <LuCloudDownload size={16} /> PDF
              </button>
            </div>
          </div>

          <div className="innerPage">
		  {/* FILTER ROW */}
		  <div className={styles.filterRow}>

		    <div className="filterOptions">
		      <label> Project Status</label>
		      <Select
		        classNamePrefix="react-select"
		        options={projectStatusOptions}
		        value={selectedStatus}
		        onChange={setSelectedStatus}
		        isClearable
		        placeholder="Select Project Status"
		      />
		    </div>

		    <div className="filterOptions">
		      <label>Project Type</label>
		      <Select
		        classNamePrefix="react-select"
		        options={projectTypeOptions}
		        value={selectedType}
		        onChange={setSelectedType}
		        isClearable
		        placeholder="Select Project Type"
		      />
		    </div>

		    <button
		      className="btn btn-2 btn-primary"
		      type="button"
		      onClick={handleClearFilters}
		    >
		      Clear Filter
		    </button>

		  </div>

		  {/* CONTROL ROW */}
		  <div className={styles.controlRow}>

		    {/* LEFT SIDE */}
		    <div className="showEntriesCount">
		      <label>Show</label>
		      <select
		        value={designPerPage}
		        onChange={(e) => {
		          setDesignPerPage(Number(e.target.value));
		          setDesignPage(1);
		        }}
		      >
		        {[5, 10, 20, 50, 100].map(size => (
		          <option key={size} value={size}>{size}</option>
		        ))}
		      </select>
		      <span>entries</span>
		    </div>

		    {/* RIGHT SIDE */}
		    <div className="searchRow">
		      <input
		        type="text"
		        className="form-control"
		        placeholder="Search..."
		        value={globalSearch}
		        onChange={e => setGlobalSearch(e.target.value)}
		        onKeyDown={(e) => {
		          if (e.key === "Enter") setDesignPage(1);
		        }}
		      />
		    </div>

		  </div>
			 

            <div className={`dataTable ${styles.tableWrapper}`}>
              <table className={styles.projectTable}>
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Status</th>
                    <th>Type</th>
                    <th>Zone</th>
                    <th>Amount</th>
                    <th>Division</th>
                    <th>Action</th>
                  </tr>
                </thead>

                <tbody>
                  {currentProjects.length ? (
                    currentProjects.map((proj, i) => (
                      <tr key={i}>
                        <td>{proj.project_id}</td>
                        <td>{proj.project_name}</td>
                        <td>{proj.project_status}</td>
                        <td>{proj.project_type_name}</td>
                        <td>{proj.railway_zone}</td>
                        <td>{proj.sanctioned_amount}</td>
                        <td>{proj.division}</td>

                        <td className="d-flex gap-2">
                          <button onClick={() => handleEdit(proj)}>
                            <MdEditNote size={18} />
                          </button>

                          <button onClick={() => handleDelete(proj)}>
                            <MdDelete size={18} />
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

            {/* ✅ Pagination */}
            {totalPages > 1 && (
              <div className={styles.paginationBar}>
                <div className={styles.paginationInfo}>
                  Showing {filteredProjects.length ? indexOfFirst + 1 : 0}
                  {" "}to{" "}
                  {Math.min(indexOfLast, filteredProjects.length)}
                  {" "}of {filteredProjects.length}
                </div>

                <div className={styles.paginationButtons}>
                  <button
                    disabled={designPage === 1}
                    onClick={() => setDesignPage(p => p - 1)}
                  >
                    Prev
                  </button>

                  {Array.from({ length: totalPages }, (_, i) => (
                    <button
                      key={i + 1}
                      className={designPage === i + 1 ? styles.activePage : ""}
                      onClick={() => setDesignPage(i + 1)}
                    >
                      {i + 1}
                    </button>
                  ))}

                  <button
                    disabled={designPage === totalPages}
                    onClick={() => setDesignPage(p => p + 1)}
                  >
                    Next
                  </button>
                </div>
              </div>
            )}
          </div>
        </>
      )}

      <Outlet />
    </div>
  );
}