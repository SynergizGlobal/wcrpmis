import React, { useContext, useState, useEffect } from "react";
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

export default function Issues() {
    const location = useLocation();
    const navigate = useNavigate();
    const [search, setSearch] = useState("");
    const [Issues, setIssues] = useState([]);
    const [page, setPage] = useState(1);
    const [perPage, setPerPage] = useState(10);
    const { refresh } = useContext(RefreshContext);

      const [filters, setFilters] = useState({
        work: "",
        hod: "",
        dyhod: "",
        Issue: "",
        contractStatus: "",
        statusOfWork: "",
      });
  
      useEffect(() => {
    fetchIssues();
  }, [refresh, location]);

  const fetchIssues = async () => {
    try {
      const res = await api.get(`${API_BASE_URL}/Issues`, { withCredentials: true });
      setIssues(res.data || []);
    } catch (err) {
      console.error("Error fetching projects:", err);
    }
  };

  const filteredIssues = Issues.filter((c) => {
      const text = search.toLowerCase();
  
      return (
        c.IssueId?.toLowerCase().includes(text) ||
        c.IssueName?.toLowerCase().includes(text) ||
        c.panNumber?.toLowerCase().includes(text) ||
        c.specilaization?.toLowerCase().includes(text) ||
        c.phoneNumber?.toLowerCase().includes(text) ||
        c.email?.toLowerCase().includes(text) ||
        c.address?.toLowerCase().includes(text)
      );
    });
    
    // Pagination logic
     const totalRecords = filteredIssues.length;
     const totalPages = Math.ceil(totalRecords / perPage);
  
     // Slice data according to page & perPage
     const paginatedIssues = filteredIssues.slice(
       (page - 1) * perPage,
       page * perPage
     );
  
    const handleAdd = () => navigate("issuesform");
    const handleEdit = (Issue) =>
      navigate("issuesform", { state: { Issue } });
  
    const isIssueForm = location.pathname.endsWith("/issuesform");
    
    const handleExport = () => {
      if (Issues.length === 0) {
        alert("No data to export");
        return;
      }
      // Export as Excel sheet
      const worksheet = XLSX.utils.json_to_sheet(Issues);
      const workbook = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(workbook, worksheet, "Issues");
  
      // File name
      XLSX.writeFile(workbook, "Issues_List.xlsx");
    };

  return (
    <div className={styles.container}>
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
                        <th>Issue ID</th>
                        <th>Issue Name</th>
                        <th>PAN Number</th>
                        <th>Specialization</th>
                        <th>Address</th>
                        <th>Primary Contact</th>
                        <th>Phone Number</th>
                        <th>Email</th>
                        <th>Actions</th>
                      </tr>
                    </thead>
              <tbody>
              {paginatedIssues.length > 0 ? (
                paginatedIssues.map((cntrs, index) => (
                    <tr key={index}>
                      <td>{cntrs.IssueId}</td>
                      <td>{cntrs.IssueName}</td>
                      <td>{cntrs.panNumber}</td>
                      <td>{cntrs.specilaization}</td>
                      <td>{cntrs.address}</td>
                      <td>{cntrs.primaryContact}</td>
                      <td>{cntrs.phoneNumber}</td>
                      <td>{cntrs.email}</td>
      
                      <td>
                        <button
                          className="btn btn-sm btn-outline-primary"
                          onClick={() => handleEdit(cntrs)}
                        >
                  <MdEditNote size={22} />
                        </button>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="9" style={{ textAlign: "center" }}>
                      No matching records found
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