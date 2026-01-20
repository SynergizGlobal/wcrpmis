import React, { useContext, useState, useEffect } from "react";
import { RefreshContext } from "../../../context/RefreshContext";
import Select from "react-select";
import styles from './Users.module.css';
import { CirclePlus } from "lucide-react";
import { LuCloudDownload } from "react-icons/lu";
import { MdEditNote } from "react-icons/md";
import api from "../../../api/axiosInstance";
import { Outlet, useNavigate, useLocation } from "react-router-dom"; 
import { API_BASE_URL } from "../../../config";


export default function Users() {
  const location = useLocation();
  const navigate = useNavigate();
  const [search, setSearch] = useState("");
  const [users, setUsers] = useState([]);
  const [page, setPage] = useState(1);
  const [perPage, setPerPage] = useState(10);
  const { refresh } = useContext(RefreshContext);
  
  // Filter states
  const [userTypes, setUserTypes] = useState([]);
  const [userRoles, setUserRoles] = useState([]);
  const [departments, setDepartments] = useState([]);
  const [reportingToList, setReportingToList] = useState([]);
  
  const [selectedUserType, setSelectedUserType] = useState("");
  const [selectedUserRole, setSelectedUserRole] = useState("");
  const [selectedDepartment, setSelectedDepartment] = useState("");
  const [selectedReportingTo, setSelectedReportingTo] = useState("");
  
  const [isLoadingFilters, setIsLoadingFilters] = useState(false);
  const [isLoadingUsers, setIsLoadingUsers] = useState(false);
  const [isExporting, setIsExporting] = useState(false);

  // Load saved filters from localStorage on mount
  useEffect(() => {
    const savedFilters = JSON.parse(localStorage.getItem("usersFilters")) || {};
    setSelectedUserType(savedFilters.user_type_fk || "");
    setSelectedUserRole(savedFilters.user_role_name_fk || "");
    setSelectedDepartment(savedFilters.department_fk || "");
    setSelectedReportingTo(savedFilters.reporting_to_id_srfk || "");
  }, []);

  useEffect(() => {
    fetchUsers();
    fetchFilterData();
  }, [refresh, location, selectedUserType, selectedUserRole, selectedDepartment, selectedReportingTo]);

  const fetchUsers = async () => {
    setIsLoadingUsers(true);
    try {
      const params = {
        user_type_fk: selectedUserType,
        user_role_name_fk: selectedUserRole,
        department_fk: selectedDepartment,
        reporting_to_id_srfk: selectedReportingTo
      };
      
      const res = await api.post(`${API_BASE_URL}/users/getUsersList`, params, { 
        withCredentials: true 
      });
      setUsers(res.data || []);
    } catch (err) {
      console.error("Error fetching users:", err);
      // Try alternative endpoint if the above fails
      try {
        const params = {
          user_type_fk: selectedUserType,
          user_role_name_fk: selectedUserRole,
          department_fk: selectedDepartment,
          reporting_to_id_srfk: selectedReportingTo
        };
        const res = await api.post(`${API_BASE_URL}/users/ajax/getUsersList`, params, { 
          withCredentials: true 
        });
        setUsers(res.data || []);
      } catch (err2) {
        console.error("Error with alternative endpoint:", err2);
      }
    } finally {
      setIsLoadingUsers(false);
    }
  };

  const fetchFilterData = async () => {
    setIsLoadingFilters(true);
    try {
      const filterParams = {
        user_type_fk: selectedUserType,
        user_role_name_fk: selectedUserRole,
        department_fk: selectedDepartment,
        reporting_to_id_srfk: selectedReportingTo
      };
      
      // Fetch all filter data in parallel
      const endpoints = [
        { url: `${API_BASE_URL}/users/getUserTypesFilterInUser`, setter: setUserTypes },
        { url: `${API_BASE_URL}/users/getUserRolesFilterInUser`, setter: setUserRoles },
        { url: `${API_BASE_URL}/users/getUserDepartmentsFilterInUser`, setter: setDepartments },
        { url: `${API_BASE_URL}/users/getUserReportingToListFilterInUser`, setter: setReportingToList }
      ];

      // Try with /users prefix first
      const promises = endpoints.map(endpoint => 
        api.post(endpoint.url, filterParams, { withCredentials: true })
          .catch(err => {
            console.error(`Error fetching ${endpoint.url}:`, err);
            // Try with /ajax prefix if first attempt fails
            const altUrl = endpoint.url.replace('/users/', '/users/ajax/');
            return api.post(altUrl, filterParams, { withCredentials: true })
              .catch(err2 => {
                console.error(`Error with alternative ${altUrl}:`, err2);
                return { data: [] }; // Return empty array if both fail
              });
          })
      );

      const results = await Promise.all(promises);
      
      // Set the data from results
      endpoints.forEach((endpoint, index) => {
        if (results[index] && results[index].data) {
          endpoint.setter(results[index].data);
        }
      });
      
    } catch (err) {
      console.error("Error fetching filter data:", err);
    } finally {
      setIsLoadingFilters(false);
    }
  };

  const handleFilterChange = (type, value) => {
    // Update filters in localStorage
    const savedFilters = JSON.parse(localStorage.getItem("usersFilters")) || {};
    
    switch(type) {
      case 'user_type_fk':
        setSelectedUserType(value);
        if (value) savedFilters.user_type_fk = value;
        else delete savedFilters.user_type_fk;
        break;
      case 'user_role_name_fk':
        setSelectedUserRole(value);
        if (value) savedFilters.user_role_name_fk = value;
        else delete savedFilters.user_role_name_fk;
        break;
      case 'department_fk':
        setSelectedDepartment(value);
        if (value) savedFilters.department_fk = value;
        else delete savedFilters.department_fk;
        break;
      case 'reporting_to_id_srfk':
        setSelectedReportingTo(value);
        if (value) savedFilters.reporting_to_id_srfk = value;
        else delete savedFilters.reporting_to_id_srfk;
        break;
    }
    
    localStorage.setItem("usersFilters", JSON.stringify(savedFilters));
    setPage(1); // Reset to first page when filters change
  };

  const clearFilters = () => {
    setSelectedUserType("");
    setSelectedUserRole("");
    setSelectedDepartment("");
    setSelectedReportingTo("");
    localStorage.removeItem("usersFilters");
    setPage(1);
    fetchUsers();
    fetchFilterData();
  };

  const filteredUsers = users.filter((user) => {
    const text = search.toLowerCase();
    return (
      user.user_id?.toLowerCase().includes(text) ||
      user.user_name?.toLowerCase().includes(text) ||
      user.designation?.toLowerCase().includes(text) ||
      user.department_name?.toLowerCase().includes(text) ||
      user.reporting_to_name?.toLowerCase().includes(text) ||
      user.user_type_fk?.toLowerCase().includes(text) ||
      user.user_role_name_fk?.toLowerCase().includes(text)
    );
  });

  // Pagination logic
  const totalRecords = filteredUsers.length;
  const totalPages = Math.ceil(totalRecords / perPage);
  const paginatedUsers = filteredUsers.slice(
    (page - 1) * perPage,
    page * perPage
  );

  const handleAdd = () => navigate("userform");
  
  const handleEdit = (user) => {
    // Since controller is @RequestMapping("/users"), edit endpoint might be /users/get-user
    // Create a form submission like in JSP
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = `${API_BASE_URL}/users/get-user`;
    
    const input = document.createElement('input');
    input.type = 'hidden';
    input.name = 'user_id';
    input.value = user.user_id;
    
    form.appendChild(input);
    document.body.appendChild(form);
    form.submit();
  };

  const isUserForm = location.pathname.endsWith("/userform");

  const handleExport = async () => {
    if (users.length === 0) {
      alert("No data to export");
      return;
    }
    
    setIsExporting(true);
    
    try {
      const exportData = {
        user_type_fk: selectedUserType || null,
        user_role_name_fk: selectedUserRole || null,
        department_fk: selectedDepartment || null,
        reporting_to_id_srfk: selectedReportingTo || null
      };
      
      // Use fetch API
      const response = await fetch(`${API_BASE_URL}/users/export-users`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(exportData),
        credentials: 'include'
      });
      
      // Check content type
      const contentType = response.headers.get('content-type');
      
      if (contentType && contentType.includes('application/vnd.openxmlformats-officedocument.spreadsheetml.sheet') ||
          contentType && contentType.includes('application/vnd.ms-excel')) {
        // It's an Excel file
        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        
        // Get filename from headers
        const contentDisposition = response.headers.get('content-disposition');
        let filename = 'Users_Export.xlsx';
        if (contentDisposition) {
          const filenameMatch = contentDisposition.match(/filename="?([^"]+)"?/);
          if (filenameMatch && filenameMatch[1]) {
            filename = filenameMatch[1];
          }
        }
        
        a.href = url;
        a.download = filename;
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
        document.body.removeChild(a);
      } else {
        // Might be a redirect or error page - try to read as text
        const text = await response.text();
        
        // Check if it's an HTML page (redirect)
        if (text.includes('<html') || text.includes('<!DOCTYPE')) {
          // Server redirected - reload the page to show error message
          alert('Export completed with a redirect. Page will reload.');
          window.location.reload();
        } else {
          // Some other response
          console.log('Unexpected response:', text);
          alert('Export completed but with unexpected response format.');
        }
      }
    } catch (error) {
      console.error('Error during export:', error);
      alert('Error during export: ' + error.message);
    } finally {
      setIsExporting(false);
    }
  };

  // Alternative export method using form submission
  const handleExportAlternative = () => {
    if (users.length === 0) {
      alert("No data to export");
      return;
    }
    
    // Method 2: Using form submission (like JSP does)
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = `${API_BASE_URL}/users/export-users`;
    form.style.display = 'none';
    
    // Create a hidden textarea with JSON data
    const dataInput = document.createElement('textarea');
    dataInput.name = 'data';
    dataInput.style.display = 'none';
    dataInput.value = JSON.stringify({
      user_type_fk: selectedUserType || '',
      user_role_name_fk: selectedUserRole || '',
      department_fk: selectedDepartment || '',
      reporting_to_id_srfk: selectedReportingTo || ''
    });
    
    form.appendChild(dataInput);
    document.body.appendChild(form);
    form.submit();
    
    // Clean up after submission
    setTimeout(() => {
      document.body.removeChild(form);
    }, 1000);
  };

 // Generate page numbers for pagination
  const getPageNumbers = () => {
    const pageNumbers = [];
    const maxPagesToShow = 5;
    
    if (totalPages <= maxPagesToShow) {
      // Show all pages
      for (let i = 1; i <= totalPages; i++) {
        pageNumbers.push(i);
      }
    } else {
      // Show limited pages with ellipsis
      if (page <= 3) {
        // Show first 4 pages and last page
        for (let i = 1; i <= 4; i++) {
          pageNumbers.push(i);
        }
        pageNumbers.push('...');
        pageNumbers.push(totalPages);
      } else if (page >= totalPages - 2) {
        // Show first page and last 4 pages
        pageNumbers.push(1);
        pageNumbers.push('...');
        for (let i = totalPages - 3; i <= totalPages; i++) {
          pageNumbers.push(i);
        }
      } else {
        // Show first page, current-1, current, current+1, and last page
        pageNumbers.push(1);
        pageNumbers.push('...');
        pageNumbers.push(page - 1);
        pageNumbers.push(page);
        pageNumbers.push(page + 1);
        pageNumbers.push('...');
        pageNumbers.push(totalPages);
      }
    }
    
    return pageNumbers;
 
  };

  return (
    <div className={styles.usersContainer}>
      {/* Top Bar */}
      {!isUserForm && (
        <div className="pageHeading">
          <h2>Users</h2>
          <div className="rightBtns">
            <button className="btn btn-primary" onClick={handleAdd}>
              <CirclePlus size={16} /> Add User
            </button>
            <button 
              className="btn btn-primary" 
              onClick={handleExport}
              disabled={isExporting}
            >
              <LuCloudDownload size={16} /> {isExporting ? 'Exporting...' : 'Export'}
            </button>
          </div>
        </div>
      )}

      {/* Loader for users */}
      {isLoadingUsers && (
        <div className={styles.loaderOverlay}>
          <div className={styles.spinner}></div>
        </div>
      )}

      {/* Filters Section */}
      {!isUserForm && (
        <div className="innerPage">
          {/* Filter Row */}
          <div className={styles.filterSection}>
            <div className={styles.filterRow}>
              <div className={styles.filterColumn}>
                <label>User Type</label>
                <select
                  value={selectedUserType}
                  onChange={(e) => handleFilterChange('user_type_fk', e.target.value)}
                  className={styles.filterSelect}
                >
                  <option value="">Select</option>
                  {userTypes.map((type) => (
                    <option key={type.user_type_fk} value={type.user_type_fk}>
                      {type.user_type_fk}
                    </option>
                  ))}
                </select>
              </div>

              <div className={styles.filterColumn}>
                <label>User Role</label>
                <select
                  value={selectedUserRole}
                  onChange={(e) => handleFilterChange('user_role_name_fk', e.target.value)}
                  className={styles.filterSelect}
                >
                  <option value="">Select</option>
                  {userRoles.map((role) => (
                    <option key={role.user_role_name_fk} value={role.user_role_name_fk}>
                      {role.user_role_name_fk}
                    </option>
                  ))}
                </select>
              </div>

              <div className={styles.filterColumn}>
                <label>Department</label>
                <select
                  value={selectedDepartment}
                  onChange={(e) => handleFilterChange('department_fk', e.target.value)}
                  className={styles.filterSelect}
                >
                  <option value="">Select</option>
                  {departments.map((dept) => (
                    <option key={dept.department_fk} value={dept.department_fk}>
                      {dept.department_name}
                    </option>
                  ))}
                </select>
              </div>

              <div className={styles.filterColumn}>
                <label>Reporting To</label>
                <select
                  value={selectedReportingTo}
                  onChange={(e) => handleFilterChange('reporting_to_id_srfk', e.target.value)}
                  className={styles.filterSelect}
                >
                  <option value="">Select</option>
                  {reportingToList.map((report) => (
                    <option key={report.user_id} value={report.user_id}>
                      {report.designation ? `${report.designation} - ` : ''}{report.user_name}
                    </option>
                  ))}
                </select>
              </div>

              <div className={styles.filterColumn}>
                <button
				className={styles.clearFilterBtn}
                  onClick={clearFilters}
                >
                  Clear Filters
                </button>
              </div>
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

          {/* Users Table */}
          <div className={`dataTable ${styles.tableWrapper}`}>
            <table className={styles.usersTable}>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Name</th>
                  <th>Designation</th>
                  <th>Department</th>
                  <th>Reporting To</th>
                  <th>User Type</th>
                  <th>User Role</th>
                  <th>Last Login</th>
                  <th>Last 7 days</th>
                  <th>Last 30 days</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {paginatedUsers.length > 0 ? (
                  paginatedUsers.map((user, index) => (
                    <tr key={index}>
                      <td>{user.user_id}</td>
                      <td>{user.user_name}</td>
                      <td>{user.designation}</td>
                      <td>{user.department_name}</td>
                      <td>{user.reporting_to_name}</td>
                      <td>{user.user_type_fk}</td>
                      <td>{user.user_role_name_fk}</td>
                      <td>{user.last_login}</td>
                      <td>{user.last7DaysLogins}</td>
                      <td>{user.last30DaysLogins}</td>
                      <td>
                        <button
                          className="btn btn-sm btn-outline-primary"
                          onClick={() => handleEdit(user)}
                          title="Edit"
                        >
                          <MdEditNote size={22} />
                        </button>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="11" style={{ textAlign: "center" }}>
                      {isLoadingUsers ? "Loading..." : "No matching records found"}
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>

          {/* PAGINATION with Numbers */}
          {totalRecords > 0 && (
            <div className={styles.paginationBar}>
              <span>
                Showing {(page - 1) * perPage + 1} to {Math.min(page * perPage, totalRecords)} of {totalRecords} entries
              </span>

              <div className={styles.paginationBtns}>
                <button
                  onClick={() => page > 1 && setPage(page - 1)}
                  disabled={page === 1}
                  className={styles.paginationBtn}
                >
                  &laquo; Prev
                </button>

                {getPageNumbers().map((pageNum, index) => (
                  pageNum === '...' ? (
                    <span key={`ellipsis-${index}`} className={styles.pageEllipsis}>...</span>
                  ) : (
                    <button
                      key={pageNum}
                      onClick={() => setPage(pageNum)}
                      className={`${styles.pageNumber} ${page === pageNum ? styles.activePage : ''}`}
                    >
                      {pageNum}
                    </button>
                  )
                ))}

                <button
                  onClick={() => page < totalPages && setPage(page + 1)}
                  disabled={page === totalPages}
                  className={styles.paginationBtn}
                >
                  Next &raquo;
                </button>
              </div>
            </div>
          )}
        </div>
      )}

      {/* Loader for filters */}
      {isLoadingFilters && (
        <div className={styles.loaderOverlay}>
          <div className={styles.spinner}></div>
        </div>
      )}

      <Outlet />
    </div>
  );
}