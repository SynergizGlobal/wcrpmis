import React, { useContext, useState, useEffect } from "react";
import { RefreshContext } from "../../../context/RefreshContext";
import Select from "react-select";
import styles from './Contractor.module.css';
import { CirclePlus } from "lucide-react";
import { LuCloudDownload } from "react-icons/lu";
import api from "../../../api/axiosInstance";
import { Outlet, useNavigate, useLocation } from "react-router-dom"; 
import { API_BASE_URL } from "../../../config";
import * as XLSX from "xlsx";

import { MdEditNote } from "react-icons/md";


export default function Contractor() {
 const location = useLocation();
  const navigate = useNavigate();
   const [search, setSearch] = useState("");
  const [contractors, setContractors] = useState([]);
  const [page, setPage] = useState(1);
  const [perPage, setPerPage] = useState(10);
  const { refresh } = useContext(RefreshContext);
 

  useEffect(() => {
    fetchContractors();
  }, [refresh, location]);

  const fetchContractors = async () => {
    try {
      const res = await api.get(`${API_BASE_URL}/contractors`, { withCredentials: true });
      setContractors(res.data || []);
    } catch (err) {
      console.error("Error fetching projects:", err);
    }
  };

  const filteredContractors = contractors.filter((c) => {
    const text = search.toLowerCase();

    return (
      c.contractorId?.toLowerCase().includes(text) ||
      c.contractorName?.toLowerCase().includes(text) ||
      c.panNumber?.toLowerCase().includes(text) ||
      c.specilaization?.toLowerCase().includes(text) ||
      c.phoneNumber?.toLowerCase().includes(text) ||
      c.email?.toLowerCase().includes(text) ||
      c.address?.toLowerCase().includes(text)
    );
  });
  
  // Pagination logic
   const totalRecords = filteredContractors.length;
   const totalPages = Math.ceil(totalRecords / perPage);

   // Slice data according to page & perPage
   const paginatedContractors = filteredContractors.slice(
     (page - 1) * perPage,
     page * perPage
   );

  const handleAdd = () => navigate("add-contractor-form");
  const handleEdit = (contractor) =>
    navigate("add-contractor-form", { state: { contractor } });

  const isContractorForm = location.pathname.endsWith("/add-contractor-form");
  
  const handleExport = () => {
    if (contractors.length === 0) {
      alert("No data to export");
      return;
    }
    // Export as Excel sheet
    const worksheet = XLSX.utils.json_to_sheet(contractors);
    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, "Contractors");

    // File name
    XLSX.writeFile(workbook, "Contractors_List.xlsx");
  };

  return (
    <div className={styles.container}>
      {/* Top Bar */}
      { !isContractorForm &&(
      <div className="pageHeading">
        <h2>Contractor</h2>
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
      {!isContractorForm && (
        
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
                  <th>Contractor ID</th>
                  <th>Contractor Name</th>
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
			  {paginatedContractors.length > 0 ? (
			    paginatedContractors.map((cntrs, index) => (
			        <tr key={index}>
			          <td>{cntrs.contractorId}</td>
			          <td>{cntrs.contractorName}</td>
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