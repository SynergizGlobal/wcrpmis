import React, { useState, useEffect, useCallback } from "react";
import Select from "react-select";
import styles from './UpdateStructure.module.css';
import { CirclePlus } from "lucide-react";
import { LuCloudDownload } from "react-icons/lu";
import { MdEditNote } from "react-icons/md";
import api from "../../../api/axiosInstance";
import { Outlet, useNavigate, useLocation } from "react-router-dom"; 
import { API_BASE_URL } from "../../../config";
import ReactPaginate from "react-paginate";
import { debounce } from "lodash";

export default function UpdateStructure() {
  const location = useLocation();
  const navigate = useNavigate();
  const [structureFormGrid, setStructureFormGrid] = useState([]);
  const [loading, setLoading] = useState(false);
  const [pagination, setPagination] = useState({
    currentPage: 0,
    totalRecords: 0,
    totalPages: 0,
    pageSize: 10
  });
  
  const [filters, setFilters] = useState({
    contract: "",
    structureType: "",
    workStatus: "",
    search: ""
  });
  
  const [options, setOptions] = useState({
    contracts: [],
    structureTypes: [],
    workStatuses: []
  });

  // Debug location state
  useEffect(() => {
    console.log("📍 UpdateStructure - Location state:", location.state);
  }, [location.state]);

  // Fetch filter options on component mount
  useEffect(() => {
    fetchFilterOptions();
  }, []);

  // Fetch data when filters or pagination change
  useEffect(() => {
    fetchStructureFormGrid();
  }, [filters.contract, filters.structureType, filters.workStatus, filters.search, pagination.currentPage]);

  const fetchFilterOptions = async () => {
    try {
      const [contractsRes, structureTypesRes, workStatusesRes] = await Promise.all([
        api.get(`${API_BASE_URL}/ajax/getContractsFilterListInStructure`, { withCredentials: true }),
        api.get(`${API_BASE_URL}/ajax/getStructureTypeListForFilter`, { withCredentials: true }),
        api.get(`${API_BASE_URL}/ajax/getWorkStatusListInStructure`, { withCredentials: true })
      ]);

      setOptions({
        contracts: contractsRes.data?.map(item => ({
          value: item.contract_id || item.value,
          label: item.contract_short_name 
            })) || [],
        structureTypes: structureTypesRes.data?.map(item => ({
          value: item.structure_type_id || item.value,
          label:  item.structure_type 
        })) || [],
        workStatuses: workStatusesRes.data?.map(item => ({
          value: item.work_status_id || item.value,
          label:  item.work_status_fk
        })) || []
      });
    } catch (err) {
      console.error("Error fetching filter options:", err);
    }
  };

  const fetchStructureFormGrid = async () => {
    setLoading(true);
    try {
      const params = {
        iDisplayStart: pagination.currentPage * pagination.pageSize,
        iDisplayLength: pagination.pageSize,
        sSearch: filters.search || ""
      };

      // Add filter params if selected
      if (filters.contract) params.contract_id_fk = filters.contract;
      if (filters.structureType) params.structure_type_fk = filters.structureType;
      if (filters.workStatus) params.work_status_fk = filters.workStatus;

      const res = await api.get(`${API_BASE_URL}/ajax/getStructuresList`, {
        params,
        withCredentials: true
      });

      const data = res.data;
      setStructureFormGrid(data.aaData || []);
      
      setPagination(prev => ({
        ...prev,
        totalRecords: data.iTotalRecords || 0,
        totalPages: Math.ceil((data.iTotalRecords || 0) / pagination.pageSize)
      }));
    } catch (err) {
      console.error("Error fetching structure list:", err);
      setStructureFormGrid([]);
    } finally {
      setLoading(false);
    }
  };

  const handleFilterChange = (name, value) => {
    setFilters(prev => ({ ...prev, [name]: value }));
    // Reset to first page when filter changes
    if (name !== "search") {
      setPagination(prev => ({ ...prev, currentPage: 0 }));
    }
  };

  // Debounced search handler
  const handleSearchChange = useCallback(
    debounce((value) => {
      handleFilterChange("search", value);
      setPagination(prev => ({ ...prev, currentPage: 0 }));
    }, 500),
    []
  );

  const handlePageChange = (selectedItem) => {
    setPagination(prev => ({ ...prev, currentPage: selectedItem.selected }));
  };

  const clearFilters = () => {
    setFilters({
      contract: "",
      structureType: "",
      workStatus: "",
      search: ""
    });
    setPagination(prev => ({ ...prev, currentPage: 0 }));
  };

  const handleAdd = () => navigate("get-structure-form");

  // FIXED: Updated handleEdit to pass correct state properties
  const handleEdit = (structure) => {
    console.log("📤 Navigating to edit with structure:", structure);
    console.log("📤 Structure ID:", structure.structure_id);
    console.log("📤 Passing state with:", { 
      structure_id: structure.structure_id,
      updateStructureform: structure 
    });
    
    navigate("get-structure-form", { 
      state: { 
        structure_id: structure.structure_id,  // This is what UpdateStructureForm checks for
        updateStructureform: structure         // This is also what UpdateStructureForm checks for
      } 
    });
  };

  const isStructureForm = location.pathname.endsWith("/get-structure-form");

  return (
    <div className={styles.container}>
      {/* Top Bar */}
      {!isStructureForm && (
        <div className="pageHeading">
          <h2>Structure Form</h2>
          <div className="rightBtns">
            <button 
              className="btn btn-primary d-flex align-items-center gap-1" 
              onClick={handleAdd}
            >
              <CirclePlus size={20} />
              Add Structure
            </button>
          </div>
        </div>
      )}
      
      {/* Filters */}
      {!isStructureForm && (
        <div className="innerPage">
          {/* Search Bar */}
          <div className={styles.searchBar}>
            <input
              type="text"
              placeholder="Search structures..."
              className="form-control"
              onChange={(e) => handleSearchChange(e.target.value)}
              style={{ maxWidth: "300px" }}
            />
          </div>

          <div className="filterRow">
            {/* Contract Filter */}
            <div className="filterOptions">
              <Select
                options={[{ value: "", label: "Select Contract" }, ...options.contracts]}
                value={options.contracts.find(opt => opt.value === filters.contract) || { value: "", label: "Select Contract" }}
                onChange={(selectedOption) => handleFilterChange("contract", selectedOption.value)}
                placeholder="Select Contract"
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
                    zIndex: 9999,
                  }),
                }}
              />
            </div>

            {/* Structure Type Filter */}
            <div className="filterOptions">
              <Select
                options={[{ value: "", label: "Select Structure Type" }, ...options.structureTypes]}
                value={options.structureTypes.find(opt => opt.value === filters.structureType) || { value: "", label: "Select Structure Type" }}
                onChange={(selectedOption) => handleFilterChange("structureType", selectedOption.value)}
                placeholder="Select Structure Type"
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
                    zIndex: 9999,
                  }),
                }}
              />
            </div>

            {/* Work Status Filter */}
            <div className="filterOptions">
              <Select
                options={[{ value: "", label: "Select Work Status" }, ...options.workStatuses]}
                value={options.workStatuses.find(opt => opt.value === filters.workStatus) || { value: "", label: "Select Work Status" }}
                onChange={(selectedOption) => handleFilterChange("workStatus", selectedOption.value)}
                placeholder="Select Work Status"
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
                    zIndex: 9999,
                  }),
                }}
              />
            </div>

            <button className="btn btn-2 btn-primary" onClick={clearFilters}>
              Clear Filters
            </button>
          </div>

          {/* Data Table */}
          <div className={`dataTable ${styles.tableWrapper}`}>
            {loading ? (
              <div className="text-center py-4">Loading...</div>
            ) : (
              <>
                <table className={styles.projectTable}>
                  <thead>
                    <tr>
                      <th>Structure ID</th>
                      <th>Project</th>
                      <th>Structure Type</th>
                      <th>Structure</th>
                      <th>Contract</th>
                      <th>Work Status</th>
                      <th>Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    {structureFormGrid.length > 0 ? (
                      structureFormGrid.map((sf, index) => (
                        <tr key={sf.structure_id || index}>
                          <td>{sf.structure_id}</td>
                          <td>{sf.project_id_fk}</td>
                          <td>{sf.structure_type_fk}</td>
                          <td>{sf.structure_name || sf.structure}</td>
                          <td>{sf.contract_id_fk}</td>
                          <td>{sf.work_status_fk}</td>
                          <td>
                            <button 
                              className="btn btn-sm btn-outline-primary d-flex align-items-center gap-1" 
                              onClick={() => handleEdit(sf)}
                              title="Edit"
                            >
                              <MdEditNote size={18} />
                            </button>
                          </td>
                        </tr>
                      ))
                    ) : (
                      <tr>
                        <td colSpan="7" style={{ textAlign: "center" }}>
                          No records found
                        </td>
                      </tr>
                    )}
                  </tbody>
                </table>

                {/* Pagination */}
                {pagination.totalPages > 1 && (
                  <div className={styles.paginationWrapper}>
                    <ReactPaginate
                      previousLabel={"← Previous"}
                      nextLabel={"Next →"}
                      pageCount={pagination.totalPages}
                      onPageChange={handlePageChange}
                      forcePage={pagination.currentPage}
                      containerClassName={styles.pagination}
                      previousLinkClassName={styles.pageLink}
                      nextLinkClassName={styles.pageLink}
                      disabledClassName={styles.disabled}
                      activeClassName={styles.active}
                      pageRangeDisplayed={3}
                      marginPagesDisplayed={2}
                    />
                    <div className={styles.paginationInfo}>
                      Showing {structureFormGrid.length} of {pagination.totalRecords} records
                    </div>
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