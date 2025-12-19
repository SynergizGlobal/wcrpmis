import React, { useState, useEffect } from "react";
import Select from "react-select";
import styles from './Contract.module.css';
import { CirclePlus } from "lucide-react";
import { LuCloudDownload } from "react-icons/lu";
import api from "../../../api/axiosInstance";
import { Outlet, useNavigate, useLocation } from "react-router-dom"; 
import { API_BASE_URL } from "../../../config";

export default function Contract() {
  const location = useLocation();
  const navigate = useNavigate();
  const [contracts, setContracts] = useState([]);
  const [loadingContracts, setLoadingContracts] = useState(false);
  
  // State for filter options
  
  const [hodOptions, setHodOptions] = useState([]);
  const [dyHodOptions, setDyHodOptions] = useState([]);
  const [contractorOptions, setContractorOptions] = useState([]);
  const [contractStatusOptions, setContractStatusOptions] = useState([]);
  const [statusOfWorkOptions, setStatusOfWorkOptions] = useState([]);
  
  const [filters, setFilters] = useState({
    hod: "",
    dyHod: "",
    contractor: "",
    contractStatus: "",
    statusOfWork: "",
  });

  const [searchQuery, setSearchQuery] = useState("");

  const [loading, setLoading] = useState({
    hod: false,
    dyHod: false,
    contractor: false,
    contractStatus: false,
    statusOfWork: false
  });

  useEffect(() => {
    fetchFilterOptions();
    fetchContracts();
  }, []);

  // Apply filters when they change
  useEffect(() => {
    applyFilters();
  }, [filters, contracts, searchQuery]);

  const fetchContracts = async () => {
    setLoadingContracts(true);
    try {
      const res = await api.get(`${API_BASE_URL}/contract/ajax/getContracts`, { withCredentials: true });
      console.log("API Response:", res.data);
      if (res.data && Array.isArray(res.data)) {
        setContracts(res.data);
      } else {
        console.warn("Unexpected response format:", res.data);
        setContracts([]);
      }
    } catch (err) {
      console.error("Error fetching contracts:", err);
      setContracts([]);
    } finally {
      setLoadingContracts(false);
    }
  };

  const applyFilters = () => {
    // If no filters are set, show all contracts
    const allFiltersEmpty = Object.values(filters).every(value => !value) && !searchQuery;
    if (allFiltersEmpty) {
      // We're already showing all contracts in the contracts state
      return;
    }

    console.log("Applying filters:", filters, "Search query:", searchQuery);
  };

  const fetchFilterOptions = async () => {
    try {
      // Fetch HOD options
      setLoading(prev => ({...prev, hod: true}));
      const hodRes = await api.get(`${API_BASE_URL}/contract/ajax/getDesignationsFilterListInContract`, { withCredentials: true });
      if (hodRes.data && Array.isArray(hodRes.data)) {
        const hodFormatted = hodRes.data.map(item => ({
          value: item.designation,
          label: item.designation
        }));
        setHodOptions([{ value: "", label: "Select HOD" }, ...hodFormatted]);
      }
      setLoading(prev => ({...prev, hod: false}));

      // Fetch Dy HOD options
      setLoading(prev => ({...prev, dyHod: true}));
      const dyHodRes = await api.get(`${API_BASE_URL}/contract/ajax/getDyHODDesignationsFilterListInContract`, { withCredentials: true });
      if (dyHodRes.data && Array.isArray(dyHodRes.data)) {
        const dyHodFormatted = dyHodRes.data.map(item => ({
          value: item.dy_hod_designation,
          label: item.dy_hod_designation
        }));
        setDyHodOptions([{ value: "", label: "Select Dy HOD" }, ...dyHodFormatted]);
      }
      setLoading(prev => ({...prev, dyHod: false}));

      // Fetch Contractor options
      setLoading(prev => ({...prev, contractor: true}));
      const contractorRes = await api.get(`${API_BASE_URL}/contract/ajax/getContractorsFilterListInContract`, { withCredentials: true });
      if (contractorRes.data && Array.isArray(contractorRes.data)) {
        const contractorFormatted = contractorRes.data.map(item => ({
          value: `${item.contractor_id_fk}-${item.contractor_name}`,
          label: `${item.contractor_id_fk}-${item.contractor_name}`
        }));
        setContractorOptions([{ value: "", label: "Select Contractor" }, ...contractorFormatted]);
      }
      setLoading(prev => ({...prev, contractor: false}));

      // Fetch Contract Status options
      setLoading(prev => ({...prev, contractStatus: true}));
      const contractStatusRes = await api.get(`${API_BASE_URL}/contract/ajax/getContractStatusFilterListInContract`, { withCredentials: true });
      if (contractStatusRes.data && Array.isArray(contractStatusRes.data)) {
        const contractStatusFormatted = contractStatusRes.data.map(item => ({
          value: item.contract_status,
          label: item.contract_status
        }));
        setContractStatusOptions([{ value: "", label: "Select Contract Status" }, ...contractStatusFormatted]);
      }
      setLoading(prev => ({...prev, contractStatus: false}));

      // Fetch Status of Work options
      setLoading(prev => ({...prev, statusOfWork: true}));
      const statusOfWorkRes = await api.get(`${API_BASE_URL}/contract/ajax/getStatusFilterListInContract`, { withCredentials: true });
      if (statusOfWorkRes.data && Array.isArray(statusOfWorkRes.data)) {
        const statusOfWorkFormatted = statusOfWorkRes.data.map(item => ({
          value: item.contract_status_fk,
          label: item.contract_status_fk
        }));
        setStatusOfWorkOptions([{ value: "", label: "Select Status of Work" }, ...statusOfWorkFormatted]);
      }
      setLoading(prev => ({...prev, statusOfWork: false}));

    } catch (err) {
      console.error("Error fetching filter options:", err);
      Object.keys(loading).forEach(key => {
        setLoading(prev => ({...prev, [key]: false}));
      });
    }
  };

  const handleFilterChange = (name, value) => {
    setFilters(prev => ({ ...prev, [name]: value }));
  };

  const handleSearchChange = (e) => {
    setSearchQuery(e.target.value);
  };

  const handleClearFilters = () => {
    setFilters({
      hod: "",
      dyHod: "",
      contractor: "",
      contractStatus: "",
      statusOfWork: "",
    });
    setSearchQuery("");
  };

  const handleAdd = () => navigate("add-contract-form");
  const handleEdit = (contract) => navigate("get-contract", { state: { contract } });

  const isDesignDrawingForm = location.pathname.endsWith("/add-contract-form");

  // Get options for each filter
  const getOptionsForFilter = (key) => {
    switch (key) {
      case "hod":
        return hodOptions;
      case "dyHod":
        return dyHodOptions;
      case "contractor":
        return contractorOptions;
      case "contractStatus":
        return contractStatusOptions;
      case "statusOfWork":
        return statusOfWorkOptions;
      default:
        return [{ value: "", label: `Select ${key}` }];
    }
  };

  // Get loading state for each filter
  const getLoadingForFilter = (key) => {
    switch (key) {
      case "hod":
        return loading.hod;
      case "dyHod":
        return loading.dyHod;
      case "contractor":
        return loading.contractor;
      case "contractStatus":
        return loading.contractStatus;
      case "statusOfWork":
        return loading.statusOfWork;
      default:
        return false;
    }
  };

  // Filter contracts based on selected filters and search query
  const filterContracts = () => {
    if (!contracts || contracts.length === 0) return [];

    let filtered = contracts;

    // Apply dropdown filters
    if (filters.hod) {
      filtered = filtered.filter(contract => contract.designation === filters.hod);
    }
    
    if (filters.dyHod) {
      filtered = filtered.filter(contract => contract.dy_hod_designation === filters.dyHod);
    }
    
    if (filters.contractor) {
      filtered = filtered.filter(contract => {
        const contractorValue = `${contract.contractor_id_fk}-${contract.contractor_name}`;
        return contractorValue === filters.contractor;
      });
    }
    
    if (filters.contractStatus) {
      filtered = filtered.filter(contract => contract.contract_status === filters.contractStatus);
    }
    
    if (filters.statusOfWork) {
      filtered = filtered.filter(contract => contract.contract_status_fk === filters.statusOfWork);
    }

    // Apply search query if present
    if (searchQuery.trim()) {
      const query = searchQuery.toLowerCase().trim();
      filtered = filtered.filter(contract => {
        return (
          (contract.project_name && contract.project_name.toLowerCase().includes(query)) ||
          (contract.contract_id && contract.contract_id.toString().toLowerCase().includes(query)) ||
          (contract.contract_short_name && contract.contract_short_name.toLowerCase().includes(query)) ||
          (contract.contractor_name && contract.contractor_name.toLowerCase().includes(query)) ||
          (contract.department_name && contract.department_name.toLowerCase().includes(query)) ||
          (contract.designation && contract.designation.toLowerCase().includes(query)) ||
          (contract.dy_hod_designation && contract.dy_hod_designation.toLowerCase().includes(query)) ||
          (contract.modified_date && contract.modified_date.toLowerCase().includes(query))
        );
      });
    }

    return filtered;
  };

  const filteredContracts = filterContracts();

  return (
    <div className={styles.container}>
      {!isDesignDrawingForm && (
        <div className="pageHeading">
          <h2>Contract</h2>
          <div className="rightBtns">
            <button className="btn btn-primary" onClick={handleAdd}>
              <CirclePlus size={16} /> Add
            </button>
            <button className="btn btn-primary">
              <LuCloudDownload size={16} /> Export
            </button>
          </div>
        </div>
      )}
      
      {/* Filters */}
      {!isDesignDrawingForm && (
        <div className="innerPage">
          <div className={styles.filterRow}>
            {Object.keys(filters).map((key) => {
              const options = getOptionsForFilter(key);
              const isLoading = getLoadingForFilter(key);
              const placeholderText = key === "contractor" ? "Select Contractor" : 
                                    key === "hod" ? "Select HOD" :
                                    key === "dyHod" ? "Select Dy HOD" :
                                    key === "contractStatus" ? "Select Contract Status" :
                                    key === "statusOfWork" ? "Select Status of Work" : 
                                    `Select ${key}`;

              return (
                <div className="filterOptions" key={key}>
                  <Select
                    options={options}
                    classNamePrefix="react-select"
                    value={options.find((opt) => opt.value === filters[key]) || null}
                    onChange={(selectedOption) =>
                      handleFilterChange(key, selectedOption ? selectedOption.value : "")
                    }
                    placeholder={placeholderText}
                    isSearchable
                    isLoading={isLoading}
                    loadingMessage={() => "Loading options..."}
                    noOptionsMessage={() => "No options available"}
                    isClearable={true}
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
              );
            })}
            <button className="btn btn-2 btn-primary" onClick={handleClearFilters}>
              Clear Filters
            </button>
          </div>
          
          <div className={styles.tableControls}>
            <div className="showEntriesCount">
              <label>Show </label>
              <select
                value={10}
                onChange={(e) => console.log("Per page changed:", e.target.value)}
              >
                {[1,5, 10, 20, 50, 100].map((size) => (
                  <option key={size} value={size}>{size}</option>
                ))}
              </select>
              <span> entries</span>
            </div>
            
            <div className={styles.searchWrapper}>
              <input
                type="text"
                placeholder="Search contracts..."
                value={searchQuery}
                onChange={handleSearchChange}
                className={styles.searchInput}
              />
              <span className={styles.searchIcon}>🔍</span>
            </div>
          </div>
          
          <div className={`dataTable ${styles.tableWrapper}`}>
            {loadingContracts ? (
              <div style={{ textAlign: "center", padding: "20px" }}>
                Loading contracts...
              </div>
            ) : (
              <>
                <table className={styles.designTable}>
                  <thead>
                    <tr>
                      <th>Project</th>
                      <th>Contract ID</th>
                      <th>Contract Name</th>
                      <th>Contractor Name</th>
                      <th>Department</th>
                      <th>HOD</th>
                      <th>Dy HOD</th>
                      <th>Last Update</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {filteredContracts.length > 0 ? (
                      filteredContracts.map((contract, index) => (
                        <tr key={contract.contract_id || index}>
                          <td>{contract.project_name}</td>
                          <td>{contract.contract_id}</td>
                          <td>{contract.contract_short_name}</td>
                          <td>{contract.contractor_name}</td>
                          <td>{contract.department_name}</td>
                          <td>{contract.designation}</td>
                          <td>{contract.dy_hod_designation}</td>
                          <td>{contract.modified_date}</td>
                          <td>
                            <button 
                              className="btn btn-sm btn-outline-primary" 
                              onClick={() => handleEdit(contract)}
                            >
                              Edit
                            </button>
                          </td>
                        </tr>
                      ))
                    ) : (
                      <tr>
                        <td colSpan="9" style={{ textAlign: "center", padding: "20px" }}>
                          {contracts.length === 0 
                            ? "No contracts found" 
                            : searchQuery 
                              ? `No contracts found matching "${searchQuery}"`
                              : "No contracts match the selected filters"}
                        </td>
                      </tr>
                    )}
                  </tbody>
                </table>
                {searchQuery && filteredContracts.length > 0 && (
                  <div className={styles.searchResultsInfo}>
                    Showing {filteredContracts.length} result(s) for "{searchQuery}"
                  </div>
                )}
              </>
            )}
          </div>
        </div>
      )}
      <Outlet />
    </div>
  );
}