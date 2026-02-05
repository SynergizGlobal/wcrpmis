import React, { useContext, useEffect, useState } from "react";
import { RefreshContext } from "../../../context/RefreshContext";
import Select from "react-select";
import styles from './TemplateUpload.module.css';
import { MdEditNote } from "react-icons/md";
import { FaUpload, FaDownload, FaTrash, FaHistory } from "react-icons/fa";
import api from "../../../api/axiosInstance";
import { Outlet, useNavigate, useLocation } from "react-router-dom";
import { API_BASE_URL } from "../../../config";

export default function TempalteUpload() {
  const { refresh } = useContext(RefreshContext);
  const navigate = useNavigate();
  const location = useLocation();

  const [forms, setForms] = useState([]);
  const [search, setSearch] = useState("");
  const [page, setPage] = useState(1);
  const [perPage, setPerPage] = useState(10);
  
  const [selectedFile, setSelectedFile] = useState(null);
  const [loading, setLoading] = useState(false);
  
  const [showUploadModal, setShowUploadModal] = useState(false);
  const [showHistoryModal, setShowHistoryModal] = useState(false);
  const [selectedRow, setSelectedRow] = useState(null);
  
  // ✅ Open / Close modal
 // const openModal = () => setShowUploadModal(true);
  const closeModal = () => {
    setShowUploadModal(false);
    setSelectedFile(null);
  };

  /* -------------------- FILTERING -------------------- */
  const filteredData = forms.filter((f) => {
    const text = search.toLowerCase();

    return (
      (f.template_name || "").toLowerCase().includes(text) ||
      (f.user_name || f.uploaded_by || "").toLowerCase().includes(text) ||
      (f.status || "").toLowerCase().includes(text) ||
      (f.uploaded_on || "").toLowerCase().includes(text)
    );
  });
  
  useEffect(() => {
    fetchTemplates();
  }, [refresh]);

  const fetchTemplates = async () => {
    try {
      const res = await api.get("/api/templates");
      setForms(res.data || []);
    } catch (error) {
      console.error("Error fetching templates", error);
    }
  };
  
  // ✅ Handle file selection
    const handleFileChange = (e) => {
      setSelectedFile(e.target.files[0]);
    };
  
  // ✅ Submit upload form
   const handleSubmit = async (e) => {
     e.preventDefault();
     if (!selectedFile) {
       alert("Please select a file first!");
       return;
     }
	 }
	 

  /* -------------------- PAGINATION -------------------- */
  const totalRecords = filteredData.length;
  const totalPages = Math.ceil(totalRecords / perPage);

  const paginatedRows = filteredData.slice(
    (page - 1) * perPage,
    page * perPage
  );

  const handleUploadClick = (row) => {
    setSelectedRow(row);
    setShowUploadModal(true);
  };
  
  const handleUploadSubmit = async () => {
    if (!selectedFile) {
      alert("Please attach a file");
      return;
    }

    try {
      const formData = new FormData();
      formData.append("template_name", selectedRow.template_name);
      formData.append("templateFile", selectedFile);

	  await api.post("/api/templates/upload", formData, {
	    headers: { "Content-Type": "multipart/form-data" },
	    withCredentials: true   // 🔴 REQUIRED
	  });


      alert("Template uploaded successfully");
      setShowUploadModal(false);
      setSelectedFile(null);
      fetchTemplates();
    } catch (error) {
      alert("Upload failed");
      console.error(error);
    }
  };


  const handleHistoryClick = (row) => {
    setSelectedRow(row);
    setShowHistoryModal(true);
  };

  const handleDownload = (row) => {
    const link = document.createElement("a");
    link.href = `${API_BASE_URL}/TEMPLATE_FILES/${row.attachment}`;
    link.download = "";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };


  const handleDelete = async (row) => {
    if (!window.confirm("Are you sure you want to delete this template?")) return;

    try {
      await api.delete(`/api/templates/${row.id}`);
      alert("Template deleted successfully");
      fetchTemplates(); // refresh table
    } catch (error) {
      console.error("Delete failed", error);
      alert("Delete failed");
    }
  };


  const isTemplateUploadPage = location.pathname.includes("formsform");
  
  return (
    <div className={styles.container}>
	{/* Top Bar */}
      {!isTemplateUploadPage && (
        <>
          <div className="pageHeading">
            <h2>Template Upload Form</h2>
              <div  className="rightBtns">
                &nbsp;
              </div>
          </div>


            {/* SEARCH + ENTRIES */}
            <div className="innerPage">
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
                      <th>Template</th>
                      <th>Upload By</th>
                      <th>Upload On</th>
                      <th>Status</th>
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
              <tr key={row.id}>
                <td>{row.template_name}</td>
                <td>{row.user_name || row.uploaded_by}</td>
                <td>{row.uploaded_on}</td>
                <td>{row.status}</td>
              <td>
                <div className={styles.actionBtns}>
                  {/* UPLOAD */}
                  <button
                    className={`btn btn-2 btn-transparent {styles.iconBtn}`}
                    title="Upload Template"
                    onClick={() => handleUploadClick(row)}
                  >
                    <FaUpload />
                  </button>

                  {/* DOWNLOAD */}
                  <button
                type="button"  
                    className={`btn btn-2 btn-transparent {styles.iconBtn}`}
                    title="Download Template"
                    onClick={() => handleDownload(row)}
                download
                  >
                    <FaDownload />
                  </button>

                  {/* DELETE */}
                  <button
                    className={`btn btn-2 btn-transparent {styles.iconBtn}`}
                    title="Delete Template"
                    onClick={() => handleDelete(row)}
                  >
                    <FaTrash />
                  </button>

                  {/* HISTORY */}
                  <button
                    className={`btn btn-2 btn-transparent {styles.iconBtn}`}
                    title="Upload History"
                    onClick={() => handleHistoryClick(row)}
                  >
                    <FaHistory />
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
	  
	  {/* ================= UPLOAD TEMPLATE MODAL ================= */}
	  {showUploadModal && (
	    <div
	      className="modal-overlay"
	      style={{
	        position: "fixed",
	        inset: 0,
	        background: "rgba(0,0,0,0.5)",
	        display: "flex",
	        alignItems: "center",
	        justifyContent: "center",
	        zIndex: 9999,
	      }}
	    >
	      <div
	        className="modal-content"
	        style={{
	          background: "#fff",
	          borderRadius: "10px",
	          width: "420px",
	          maxWidth: "90%",
	          padding: "1.5rem",
	          boxShadow: "0 5px 15px rgba(0,0,0,0.3)",
	        }}
	      >
	        <h3 className="text-center mb-2">Upload Template File</h3>

	        <form onSubmit={handleUploadSubmit} encType="multipart/form-data">
	          <div className="form-group mb-3 center-align">
	            <label className="form-label fw-bold mb-2">Attachment</label>
	            <input
	              type="file"
	              id="laUploadFile"
	              name="laUploadFile"
	              onChange={handleFileChange}
	              required
	              className="form-control"
	            />
	            {selectedFile && (
	              <p style={{ marginTop: "10px", color: "#475569" }}>
	                Selected: {selectedFile.name}
	              </p>
	            )}
	          </div>

	          <div
	            className="modal-actions"
	            style={{
	              display: "flex",
	              justifyContent: "space-evenly",
	              marginTop: "1rem",
	            }}
	          >
	            <button
	              type="submit"
	              className="btn btn-primary"
	              style={{ width: "48%" }}
	              disabled={loading}
	            >
	              {loading ? "Uploading..." : "Update"}
	            </button>

	            <button
	              type="button"
	              className="btn btn-white"
	              style={{ width: "48%" }}
	              onClick={closeModal}
	            >
	              Cancel
	            </button>
	          </div>
	        </form>
	      </div>
	    </div>
	  )}
    
	  {/* ================= TEMPLATE UPLOAD HISTORY MODAL ================= */}
	  {showHistoryModal && (
		<div
	      className="modal-overlay"
	      style={{
	        position: "fixed",
	        inset: 0,
	        background: "rgba(0,0,0,0.5)",
	        display: "flex",
	        alignItems: "center",
	        justifyContent: "center",
	        zIndex: 9999,
	      }}
	    >
	      <div
	        style={{
	          background: "#fff",
	          width: "80%",
	          maxWidth: "1000px",
	          borderRadius: "6px",
	          overflow: "hidden",
	        }}
	      >
	        {/* HEADER */}
			<div
			  style={{
			    background: "#3f4a7f",
			    color: "white",
			    padding: "12px 16px",
			    display: "flex",
			    justifyContent: "space-between",
			    alignItems: "center",
			  }}
			>
	          <h4 style={{ margin: 0 }}>Template Upload History</h4>
	          <button
	            onClick={() => setShowHistoryModal(false)}
	            style={{
	              background: "transparent",
	              border: "none",
	              color: "#fff",
	              fontSize: "18px",
	              cursor: "pointer",
	            }}
	          >
	            ✕
	          </button>
	        </div>

	        {/* BODY */}
	        <div style={{ padding: "16px" }}>
			<table className={styles.historyTable}>
			  <thead>
			    <tr>
			      <th>Template</th>
			      <th>Uploaded By</th>
			      <th>Uploaded On</th>
			      <th>Status</th>
			    </tr>
			  </thead>
			  <tbody>
			    {selectedRow?.tableHistoryList?.length > 0 ? (
			      selectedRow.tableHistoryList.map((h, index) => (
			        <tr key={index}>
			        <td>
					  <a
					    href={`${API_BASE_URL}/TEMPLATES_OLD/${h.attachment}`}
					    download
					    style={{ color: "#0d6efd", textDecoration: "none" }}
					    title="Download old template"
					  >
					    {h.template_name}
					  </a>
					</td>

			          <td>{h.user_name || h.uploaded_by}</td>
			          <td>{h.uploaded_on}</td>
			          <td>{h.status}</td>
			        </tr>
			      ))
			    ) : (
			      <tr>
			        <td colSpan="4" style={{ textAlign: "center", padding: "12px" }}>
			          No history found
			        </td>
			      </tr>
			    )}
			  </tbody>
			</table>
	        </div>
	      </div>
	    </div>
	  )}
      <Outlet />
    </div>
  );
}