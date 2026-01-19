import React, { useState, useEffect, useRef, useContext } from "react";
import Select from "react-select";
import styles from './UtilityShifting.module.css';
import { CirclePlus } from "lucide-react";
import { LuCloudDownload, LuUpload, LuDownload, LuSearch, LuChevronLeft, LuChevronRight } from "react-icons/lu";
import { MdEditNote } from "react-icons/md";
import api from "../../../api/axiosInstance";
import { Outlet, useNavigate, useLocation } from "react-router-dom"; 
import { API_BASE_URL } from "../../../config";
import { RefreshContext } from "../../../context/RefreshContext";

export default function UtilityShifting() {
  const location = useLocation();
  const navigate = useNavigate();
  const fileInputRef = useRef(null);
  const { refresh } = useContext(RefreshContext);
  const [utilityShiftingList, setUtilityShiftingList] = useState([]);
  const [uploadedFilesList, setUploadedFilesList] = useState([]);
  const [filteredUploadedFiles, setFilteredUploadedFiles] = useState([]);
  const [uploadLoading, setUploadLoading] = useState(false);
  const [uploadMessage, setUploadMessage] = useState({ type: '', text: '' });
  const [loading, setLoading] = useState(false);
  const [filterOptions, setFilterOptions] = useState({
    location: [],
    category: [],
    utilityType: [],
    status: []
  });
  const [filtersLoading, setFiltersLoading] = useState({
    location: false,
    category: false,
    utilityType: false,
    status: false
  });
  const [pagination, setPagination] = useState({
    pageNumber: 1,
    pageDisplayLength: 10,
    totalRecords: 0,
    totalDisplayRecords: 0
  });
  const [uploadPagination, setUploadPagination] = useState({
    pageNumber: 1,
    pageDisplayLength: 10,
    totalRecords: 0
  });
  const [filters, setFilters] = useState({
    location: "",
    category: "",
    utilityType: "",
    status: "",
  });
  const [searchParameter, setSearchParameter] = useState("");
  const [uploadSearchParameter, setUploadSearchParameter] = useState("");

  useEffect(() => {
    fetchFilterOptions();
    fetchUtilityShiftingList();
    fetchUploadedFilesList();
  }, []);

  useEffect(() => {
    if (refresh) {
      console.log("🔄 Refresh triggered, refetching data...");
      fetchUtilityShiftingList();
      fetchUploadedFilesList();
    }
  }, [refresh]);

  useEffect(() => {
    setPagination(prev => ({ ...prev, pageNumber: 1 }));
  }, [filters.location, filters.category, filters.utilityType, filters.status]);

  useEffect(() => {
    fetchUtilityShiftingList();
  }, [pagination.pageNumber, pagination.pageDisplayLength, filters]);

  useEffect(() => {
    const timeoutId = setTimeout(() => {
      if (pagination.pageNumber === 1) {
        fetchUtilityShiftingList();
      } else {
        setPagination(prev => ({ ...prev, pageNumber: 1 }));
      }
    }, 500);
    
    return () => clearTimeout(timeoutId);
  }, [searchParameter]);

  useEffect(() => {
    filterUploadedFiles();
  }, [uploadSearchParameter, uploadedFilesList, uploadPagination.pageNumber, uploadPagination.pageDisplayLength]);

  const filterUploadedFiles = () => {
    let filtered = uploadedFilesList;
    
    if (uploadSearchParameter) {
      const searchLower = uploadSearchParameter.toLowerCase();
      filtered = filtered.filter(file => 
        (file.filePath && file.filePath.toLowerCase().includes(searchLower)) ||
        (file.status && file.status.toLowerCase().includes(searchLower)) ||
        (file.remarks && file.remarks.toLowerCase().includes(searchLower)) ||
        (file.uploaded_by_user_id_fk && file.uploaded_by_user_id_fk.toLowerCase().includes(searchLower)) ||
        (file.uploaded_on && file.uploaded_on.toLowerCase().includes(searchLower)) ||
        (file.uploaded_file && file.uploaded_file.toLowerCase().includes(searchLower))
      );
    }
    
    setUploadPagination(prev => ({
      ...prev,
      totalRecords: filtered.length
    }));
    
    const startIndex = (uploadPagination.pageNumber - 1) * uploadPagination.pageDisplayLength;
    const endIndex = startIndex + uploadPagination.pageDisplayLength;
    setFilteredUploadedFiles(filtered.slice(startIndex, endIndex));
  };

  // Generate page numbers for pagination
  const generatePageNumbers = (currentPage, totalPages) => {
    const delta = 2; // Number of pages to show before and after current page
    const range = [];
    const rangeWithDots = [];
    let l;

    for (let i = 1; i <= totalPages; i++) {
      if (i === 1 || i === totalPages || (i >= currentPage - delta && i <= currentPage + delta)) {
        range.push(i);
      }
    }

    range.forEach((i, index) => {
      if (index > 0 && i - range[index - 1] > 1) {
        rangeWithDots.push('...');
      }
      rangeWithDots.push(i);
    });

    return rangeWithDots;
  };

  const fetchFilterOptions = async () => {
    try {
      setFiltersLoading({
        location: true,
        category: true,
        utilityType: true,
        status: true
      });

      const filterEndpoints = [
        { key: 'location', url: '/ajax/getLocationListFilter', backendField: 'location_name' },
        { key: 'category', url: '/ajax/getUtilityCategoryListFilter', backendField: 'utility_category_fk' },
        { key: 'utilityType', url: '/ajax/getUtilityTypeListFilter', backendField: 'utility_type_fk' },
        { key: 'status', url: '/ajax/getStatusListFilter', backendField: 'shifting_status_fk' }
      ];

      const requests = filterEndpoints.map(async (endpoint) => {
        try {
          const response = await api.post(
            `${API_BASE_URL}/utility-shifting${endpoint.url}`, 
            {},
            { 
              withCredentials: true,
              headers: {
                'Content-Type': 'application/json',
              }
            }
          );

          if (response.data && Array.isArray(response.data)) {
            const options = response.data.map(item => {
              let label = '';
              let value = '';

              switch (endpoint.key) {
                case 'location':
                  label = item.location_name || item.name || item.location_fk || 'Unknown Location';
                  value = item.location_id || item.location_name || item.location_fk || '';
                  break;
                case 'category':
                  label = item.utility_category_name || item.category_name || item.name || item.utility_category_fk || 'Unknown Category';
                  value = item.utility_category_id || item.id || item.utility_category_fk || '';
                  break;
                case 'utilityType':
                  label = item.utility_type_name || item.type_name || item.name || item.utility_type_fk || 'Unknown Type';
                  value = item.utility_type_id || item.id || item.utility_type_fk || '';
                  break;
                case 'status':
                  label = item.shifting_status_name || item.status_name || item.name || item.shifting_status_fk || 'Unknown Status';
                  value = item.shifting_status_id || item.id || item.shifting_status_fk || '';
                  break;
                default:
                  label = item.name || item.label || item.description || 'Unknown';
                  value = item.id || item.value || item.code || '';
              }

              if (!value && value !== 0) {
                console.warn(`Invalid value for ${endpoint.key}:`, item);
                return null;
              }

              return { 
                label: label.toString(), 
                value: value.toString() 
              };
            }).filter(option => option !== null);

            const optionsWithEmpty = [
              { value: "", label: `Select ${endpoint.key}` },
              ...options
            ];

            return { key: endpoint.key, options: optionsWithEmpty };
          } else {
            console.warn(`⚠️ ${endpoint.key} response is not an array:`, response.data);
            return { 
              key: endpoint.key, 
              options: [{ value: "", label: `Select ${endpoint.key}` }] 
            };
          }
        } catch (error) {
          console.error(`❌ Error fetching ${endpoint.key} options:`, error);
          return { 
            key: endpoint.key, 
            options: [{ value: "", label: `Select ${endpoint.key}` }] 
          };
        }
      });

      const results = await Promise.all(requests);
      
      const newFilterOptions = { ...filterOptions };
      results.forEach(result => {
        newFilterOptions[result.key] = result.options;
      });
      setFilterOptions(newFilterOptions);

    } catch (error) {
      console.error("❌ Error fetching filter options:", error);
    } finally {
      setFiltersLoading({
        location: false,
        category: false,
        utilityType: false,
        status: false
      });
    }
  };

  const fetchUtilityShiftingList = async () => {
    try {
      setLoading(true);
      
      const startIndex = (pagination.pageNumber - 1) * pagination.pageDisplayLength;
      
      const requestBody = {
        location_name: filters.location || '',
        utility_category_fk: filters.category || '', 
        utility_type_fk: filters.utilityType || '',
        shifting_status_fk: filters.status || ''
      };

      const queryParams = new URLSearchParams({
        iDisplayStart: startIndex.toString(),
        iDisplayLength: pagination.pageDisplayLength.toString(),
        sSearch: searchParameter || ''
      });

      const response = await api.post(
        `${API_BASE_URL}/utility-shifting/ajax/getUtilityShiftingList?${queryParams.toString()}`, 
        requestBody,
        { 
          withCredentials: true,
          headers: {
            'Content-Type': 'application/json',
          }
        }
      );

      if (response.data) {
        setUtilityShiftingList(response.data.aaData || []);
        setPagination(prev => ({
          ...prev,
          totalRecords: response.data.iTotalRecords || 0,
          totalDisplayRecords: response.data.iTotalDisplayRecords || 0
        }));
      }
    } catch (err) {
      console.error("❌ Error fetching utility shifting list:", err);
      if (err.response) {
        console.error("Response error:", err.response.data);
        console.error("Response status:", err.response.status);
      }
    } finally {
      setLoading(false);
    }
  };

  const fetchUploadedFilesList = async () => {
    try {
      const response = await api.post(
        `${API_BASE_URL}/utility-shifting/ajax/getUtilityShiftingUploadsList`, 
        {}, 
        { 
          withCredentials: true,
          headers: {
            'Content-Type': 'application/json',
          }
        }
      );

      if (response.data && Array.isArray(response.data)) {
        setUploadedFilesList(response.data);
      }
    } catch (err) {
      console.error("❌ Error fetching uploaded files list:", err);
      if (err.response) {
        console.error("Response error:", err.response.data);
      }
    }
  };

  const handleSampleDownload = () => {
    const fileUrl = "/files/utilityshifting/Utility_Shifting_Template.xlsx";
    const fileName = "Utility_Shifting_Template.xlsx";

    const link = document.createElement("a");
    link.href = fileUrl;
    link.setAttribute("download", fileName);
    document.body.appendChild(link);
    link.click();
    link.remove();
  };

  const handleUploadClick = () => {
    fileInputRef.current?.click();
  };

  const handleFileUpload = async (event) => {
    const file = event.target.files[0];
    if (!file) return;

    if (!file.name.endsWith('.xlsx') && !file.name.endsWith('.xls')) {
      setUploadMessage({
        type: 'error',
        text: 'Please upload an Excel file (.xlsx or .xls)'
      });
      return;
    }

    if (file.size > 10 * 1024 * 1024) {
      setUploadMessage({
        type: 'error',
        text: 'File size should be less than 10MB'
      });
      return;
    }

    await uploadUtilityShiftingFile(file);
  };

  const uploadUtilityShiftingFile = async (file) => {
    setUploadLoading(true);
    setUploadMessage({ type: '', text: '' });

    try {
      const formData = new FormData();
      formData.append("file", file);

      const response = await api.post(
        `${API_BASE_URL}/utility-shifting/upload-utility-shifting`,
        formData,
        {
          withCredentials: true,
          headers: {
            'Content-Type': 'multipart/form-data',
          },
          timeout: 60000
        }
      );

      if (response.data) {
        const responseData = response.data;
        
        if (responseData.success) {
          setUploadMessage({
            type: 'success',
            text: responseData.success
          });
          fetchUtilityShiftingList();
          fetchUploadedFilesList();
        } 
        else if (responseData.error) {
          setUploadMessage({
            type: 'error',
            text: responseData.error
          });
        }
        else {
          const keys = Object.keys(responseData);
          let foundMessage = false;
          
          for (const key of keys) {
            const value = responseData[key];
            if (value && typeof value === 'string') {
              if (value.includes('success') || value.includes('uploaded') || value.includes('records')) {
                setUploadMessage({
                  type: 'success',
                  text: value
                });
                fetchUtilityShiftingList();
                fetchUploadedFilesList();
                foundMessage = true;
                break;
              } else if (value.includes('error') || value.includes('fail') || value.includes('wrong')) {
                setUploadMessage({
                  type: 'error',
                  text: value
                });
                foundMessage = true;
                break;
              }
            }
          }
          
          if (!foundMessage) {
            const firstKey = keys[0];
            if (firstKey) {
              setUploadMessage({
                type: 'info',
                text: responseData[firstKey]
              });
            }
          }
        }
      }

    } catch (error) {
      console.error('Upload error:', error);
      
      let errorMessage = 'Upload failed! Please try again.';
      
      if (error.response) {
        const serverError = error.response.data;
        
        if (typeof serverError === 'object') {
          if (serverError.error) {
            errorMessage = serverError.error;
          } else if (serverError.message) {
            errorMessage = serverError.message;
          } else {
            const keys = Object.keys(serverError);
            if (keys.length > 0) {
              errorMessage = serverError[keys[0]];
            }
          }
        } else if (typeof serverError === 'string') {
          errorMessage = serverError;
        }
      } else if (error.request) {
        errorMessage = 'No response from server. Please check your connection.';
      } else {
        errorMessage = error.message;
      }
      
      setUploadMessage({
        type: 'error',
        text: errorMessage
      });
    } finally {
      setUploadLoading(false);
      if (fileInputRef.current) {
        fileInputRef.current.value = '';
      }
    }
  };

  const handleFilterChange = (name, value) => {
    setFilters(prev => ({ ...prev, [name]: value }));
  };

  const handleSearchChange = (e) => {
    setSearchParameter(e.target.value);
  };

  const handleUploadSearchChange = (e) => {
    setUploadSearchParameter(e.target.value);
    setUploadPagination(prev => ({ ...prev, pageNumber: 1 }));
  };

  const handlePageChange = (newPage) => {
    setPagination(prev => ({ ...prev, pageNumber: newPage }));
  };

  const handleUploadPageChange = (newPage) => {
    setUploadPagination(prev => ({ ...prev, pageNumber: newPage }));
  };

  const handlePageSizeChange = (e) => {
    const newSize = parseInt(e.target.value);
    setPagination(prev => ({ ...prev, pageDisplayLength: newSize, pageNumber: 1 }));
  };

  const handleUploadPageSizeChange = (e) => {
    const newSize = parseInt(e.target.value);
    setUploadPagination(prev => ({ ...prev, pageDisplayLength: newSize, pageNumber: 1 }));
  };

  const handleAdd = () => navigate("add-utility-shifting");
  
  const handleEdit = (utilityShifting) => navigate("add-utility-shifting", { 
    state: { 
      utility_shifting_id: utilityShifting.utility_shifting_id,
      design: utilityShifting 
    } 
  });

  const handleClearFilters = () => {
    setFilters({
      location: "",
      category: "",
      utilityType: "",
      status: "",
    });
    setSearchParameter("");
    setPagination(prev => ({ ...prev, pageNumber: 1 }));
  };

  const handleClearUploadSearch = () => {
    setUploadSearchParameter("");
    setUploadPagination(prev => ({ ...prev, pageNumber: 1 }));
  };

  const isUtilityShiftingForm = location.pathname.endsWith("/add-utility-shifting");

  const totalPages = Math.ceil(pagination.totalRecords / pagination.pageDisplayLength);
  const startRecord = (pagination.pageNumber - 1) * pagination.pageDisplayLength + 1;
  const endRecord = Math.min(pagination.pageNumber * pagination.pageDisplayLength, pagination.totalRecords);

  const uploadTotalPages = Math.ceil(uploadPagination.totalRecords / uploadPagination.pageDisplayLength);
  const uploadStartRecord = (uploadPagination.pageNumber - 1) * uploadPagination.pageDisplayLength + 1;
  const uploadEndRecord = Math.min(uploadPagination.pageNumber * uploadPagination.pageDisplayLength, uploadPagination.totalRecords);

  const exportUtilityShifting = () => {
    const {
      location,     
      category,   
      utilityType,  
      status       
    } = filters;
   
    document.getElementById("exportLocation_name").value = location || "";
    document.getElementById("exportUtility_category_fk").value = category || "";
    document.getElementById("exportUtility_type_fk").value = utilityType || "";
    document.getElementById("exportShifting_status_fk").value = status || "";

    document.getElementById("exportUtilityShiftingForm").submit();
  };

  return (
    <div className={styles.container}>
      <form
        id="exportUtilityShiftingForm"
        action={`${API_BASE_URL}/utility-shifting/export-utility-shifting`}
        method="POST"
        style={{ display: "none" }}
      >
        <input type="hidden" id="exportLocation_name" name="location_name" />
        <input type="hidden" id="exportUtility_category_fk" name="utility_category_fk" />
        <input type="hidden" id="exportUtility_type_fk" name="utility_type_fk" />
        <input type="hidden" id="exportShifting_status_fk" name="shifting_status_fk" />
      </form>

      {!isUtilityShiftingForm && (
        <div className="pageHeading">
          <h2>Utility Shifting</h2>
          <div className="rightBtns">
            <button className="btn-2 transparent-btn" onClick={handleSampleDownload}>
              <LuDownload size={16} />
            </button>
            
            <button 
              className="btn btn-primary" 
              onClick={handleUploadClick}
              disabled={uploadLoading}
            >
              {uploadLoading ? (
                <>
                  <div className="spinner-border spinner-border-sm me-2" role="status">
                    <span className="visually-hidden">Loading...</span>
                  </div>
                  Uploading...
                </>
              ) : (
                <>
                  <LuUpload size={16} /> Upload
                </>
              )}
            </button>
            
            <input
              type="file"
              ref={fileInputRef}
              onChange={handleFileUpload}
              accept=".xlsx,.xls"
              style={{ display: 'none' }}
            />
            
            <button className="btn btn-primary" onClick={handleAdd}>
              <CirclePlus size={16} /> Add
            </button>
            <button className="btn btn-primary" onClick={exportUtilityShifting}>
              <LuCloudDownload size={16} /> Export
            </button>
          </div>
        </div>
      )}
      
      {uploadMessage.text && (
        <div className={`alert ${uploadMessage.type === 'success' ? 'alert-success' : uploadMessage.type === 'error' ? 'alert-danger' : 'alert-info'} mt-3`}>
          <div dangerouslySetInnerHTML={{ __html: uploadMessage.text }} />
        </div>
      )}
      
      {!isUtilityShiftingForm && (
        <div className="innerPage">
          <div className={styles.filterRow}>
            <div className={styles.filterControls}>
              {Object.keys(filters).map((key) => (
                <div className={`filterOptions ${styles.filterOption}`} key={key}>
                  <Select
                    options={filterOptions[key]}
                    classNamePrefix="react-select"
                    value={filterOptions[key].find((opt) => opt.value === filters[key])}
                    onChange={(selectedOption) =>
                      handleFilterChange(key, selectedOption?.value || "")
                    }
                    placeholder={
                      filtersLoading[key] 
                        ? `Loading ${key}...` 
                        : `Select ${key}`
                    }
                    isDisabled={filtersLoading[key]}
                    isSearchable
                    isLoading={filtersLoading[key]}
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
              ))}
              
              <button className={`btn btn-outline-secondary ${styles.clearFilterBtn}`} onClick={handleClearFilters}>
                <span className={styles.clearFilterIcon}>×</span>
                Clear Filters
              </button>
            </div>

            <div className={styles.searchContainer}>
              <div className={styles.searchBox}>
                <LuSearch className={styles.searchIcon} size={18} />
                <input
                  type="text"
                  placeholder="Search utility shifting..."
                  value={searchParameter}
                  onChange={handleSearchChange}
                  className={`form-control ${styles.searchInput}`}
                />
              </div>
            </div>
          </div>
          
          {loading && (
            <div className="text-center p-3">
              <div className="spinner-border text-primary" role="status">
                <span className="visually-hidden">Loading...</span>
              </div>
              <p className="mt-2">Loading utility shifting data...</p>
            </div>
          )}
          
          {!loading && (
            <>
              <div className={`dataTable ${styles.tableWrapper}`}>
                <table className={styles.designTable}>
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>Description</th>
                      <th>Utility Type</th>
                      <th>Custodian</th>
                      <th>HOD</th>
                      <th>Execution Agency</th>
                      <th>Status</th>
                      <th>Last Update</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {utilityShiftingList.length > 0 ? (
                      utilityShiftingList.map((us, index) => (
                        <tr key={index}>
                          <td>{us.utility_shifting_id}</td>
                          <td>{us.utility_description}</td>
                          <td>{us.utility_type_fk}</td>
                          <td>{us.custodian}</td>
                          <td>{us.user_name}</td>
                          <td>{us.execution_agency_fk}</td>
                          <td>{us.shifting_status_fk}</td>
                          <td>{us.modified_date}</td>
                          <td>
                            <button
                              className="btn btn-sm btn-outline-primary"
                              onClick={() => handleEdit(us)}
                            >
                              <MdEditNote size="22" />
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

              {utilityShiftingList.length > 0 && (
                <div className={styles.pagination}>
                  <div className={styles.paginationInfo}>
                    Showing {startRecord} to {endRecord} of {pagination.totalRecords} entries
                    {searchParameter && ` (filtered by: "${searchParameter}")`}
                  </div>
                  <div className={styles.paginationControls}>
                    <button 
                      className="btn btn-sm btn-outline-primary d-flex align-items-center"
                      disabled={pagination.pageNumber === 1}
                      onClick={() => handlePageChange(pagination.pageNumber - 1)}
                      title="Previous Page"
                    >
                      <LuChevronLeft size={16} />
                    </button>
                    
                    <div className="d-flex align-items-center">
                      {generatePageNumbers(pagination.pageNumber, totalPages).map((page, index) => (
                        page === '...' ? (
                          <span key={`ellipsis-${index}`} className="mx-1">...</span>
                        ) : (
                          <button
                            key={page}
                            className={`btn btn-sm mx-1 ${pagination.pageNumber === page ? 'btn-primary' : 'btn-outline-primary'}`}
                            onClick={() => handlePageChange(page)}
                          >
                            {page}
                          </button>
                        )
                      ))}
                    </div>
                    
                    <button 
                      className="btn btn-sm btn-outline-primary d-flex align-items-center"
                      disabled={pagination.pageNumber === totalPages}
                      onClick={() => handlePageChange(pagination.pageNumber + 1)}
                      title="Next Page"
                    >
                      <LuChevronRight size={16} />
                    </button>
                  </div>
                  <div className={styles.pageSize}>
                    <span className="me-2">Show:</span>
                    <select 
                      value={pagination.pageDisplayLength} 
                      onChange={handlePageSizeChange}
                      className="form-select form-select-sm"
                    >
                      <option value="10">10</option>
                      <option value="25">25</option>
                      <option value="50">50</option>
                      <option value="100">100</option>
                    </select>
                  </div>
                </div>
              )}

              <br />
              <br />
              
              <div className="pageHeading">
                <h2>Uploaded Utility Shifting Data</h2>
                <div className="rightBtns">
                  <button className="btn-2 transparent-btn hidden">
                    <LuDownload size={16} />
                  </button>
                </div>
              </div>
              <br />
              
              <div className={styles.uploadSearchSection}>
                <div className={styles.uploadSearchContainer}>
                  <div className={styles.uploadSearchBox}>
                    <LuSearch className={styles.uploadSearchIcon} size={18} />
                    <input
                      type="text"
                      placeholder="Search uploaded files"
                      value={uploadSearchParameter}
                      onChange={handleUploadSearchChange}
                      className={`form-control ${styles.uploadSearchInput}`}
                    />
                  </div>
                </div>
              </div>

              <div className={`dataTable ${styles.tableWrapper}`}>
                <table className={styles.uploadedDesignTable}>
                  <thead>
                    <tr>
                      <th>Uploaded File</th>
                      <th>Status</th>
                      <th>Remarks</th>
                      <th>Uploaded By</th>
                      <th>Uploaded On</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredUploadedFiles.length > 0 ? (
                      filteredUploadedFiles.map((uf, index) => (
                        <tr key={index}>
                          <td>
                            {uf.uploaded_file ? (
                              <a
                                href={`/UTILITY_UPLOADED_FILES/${uf.uploaded_file}`}
                                download
                                style={{ color: "blue", textDecoration: "underline" }}
                              >
                                {uf.uploaded_file}
                              </a>
                            ) : (
                              ""
                            )}
                          </td>
                          <td>{uf.status}</td>
                          <td>{uf.remarks}</td>
                          <td>{uf.uploaded_by_user_id_fk}</td>
                          <td>{uf.uploaded_on}</td>
                        </tr>
                      ))
                    ) : (
                      <tr>
                        <td colSpan="5" style={{ textAlign: "center" }}>
                          {uploadedFilesList.length === 0 ? 'No uploaded files found' : 'No files match your search'}
                        </td>
                      </tr>
                    )}
                  </tbody>
                </table>
              </div>

              {uploadedFilesList.length > 0 && (
                <div className={styles.pagination}>
                  <div className={styles.paginationInfo}>
                    Showing {uploadStartRecord} to {uploadEndRecord} of {uploadPagination.totalRecords} entries
                    {uploadSearchParameter && ` (filtered from ${uploadedFilesList.length} total entries)`}
                  </div>
                  <div className={styles.paginationControls}>
                    <button 
                      className="btn btn-sm btn-outline-primary d-flex align-items-center"
                      disabled={uploadPagination.pageNumber === 1}
                      onClick={() => handleUploadPageChange(uploadPagination.pageNumber - 1)}
                      title="Previous Page"
                    >
                      <LuChevronLeft size={16} />
                    </button>
                    
                    <div className="d-flex align-items-center">
                      {generatePageNumbers(uploadPagination.pageNumber, uploadTotalPages).map((page, index) => (
                        page === '...' ? (
                          <span key={`ellipsis-${index}`} className="mx-1">...</span>
                        ) : (
                          <button
                            key={page}
                            className={`btn btn-sm mx-1 ${uploadPagination.pageNumber === page ? 'btn-primary' : 'btn-outline-primary'}`}
                            onClick={() => handleUploadPageChange(page)}
                          >
                            {page}
                          </button>
                        )
                      ))}
                    </div>
                    
                    <button 
                      className="btn btn-sm btn-outline-primary d-flex align-items-center"
                      disabled={uploadPagination.pageNumber === uploadTotalPages}
                      onClick={() => handleUploadPageChange(uploadPagination.pageNumber + 1)}
                      title="Next Page"
                    >
                      <LuChevronRight size={16} />
                    </button>
                  </div>
                  <div className={styles.pageSize}>
                    <span className="me-2">Show:</span>
                    <select 
                      value={uploadPagination.pageDisplayLength} 
                      onChange={handleUploadPageSizeChange}
                      className="form-select form-select-sm"
                    >
                      <option value="10">10</option>
                      <option value="25">25</option>
                      <option value="50">50</option>
                      <option value="100">100</option>
                    </select>
                  </div>
                </div>
              )}
            </>
          )}
        </div>
      )}
      <Outlet />
    </div>
  );
}