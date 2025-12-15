import React, { useState, useEffect, useCallback, useContext, useRef } from "react";
import { RefreshContext } from "../../../context/RefreshContext";
import Select from "react-select";
import styles from "./UpdateStructure.module.css";
import { MdEditNote } from "react-icons/md";
import api from "../../../api/axiosInstance";
import { Outlet, useNavigate, useLocation } from "react-router-dom";
import { API_BASE_URL } from "../../../config";
import ReactPaginate from "react-paginate";
import { debounce } from "lodash";

export default function UpdateStructure() {
  const location = useLocation();
  const navigate = useNavigate();
  const { refresh } = useContext(RefreshContext);

  const [structureFormGrid, setStructureFormGrid] = useState([]);
  const [loading, setLoading] = useState(false);

  const [pagination, setPagination] = useState({
    currentPage: 0,
    totalRecords: 0,
    totalPages: 0,
    pageSize: 10,
  });

  const [filters, setFilters] = useState({
    contract: null,
    structureType: null,
    workStatus: null,
    search: "",
  });

  const [options, setOptions] = useState({
    contracts: [],
    structureTypes: [],
    workStatuses: [],
  });

  // Create a ref for the search input to clear its value
  const searchInputRef = useRef(null);

  useEffect(() => {
    fetchFilterOptions();
  }, []);

  useEffect(() => {
    fetchStructureFormGrid();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [
    filters.contract,
    filters.structureType,
    filters.workStatus,
    filters.search,
    pagination.currentPage,
    pagination.pageSize,
    location.key,
  ]);

  const fetchFilterOptions = async () => {
    try {
      const [contractsRes, structureTypesRes, workStatusesRes] =
        await Promise.all([
          api.get(`${API_BASE_URL}/ajax/getContractsFilterListInStructure`, {
            withCredentials: true,
          }),
          api.get(`${API_BASE_URL}/ajax/getStructureTypeListForFilter`, {
            withCredentials: true,
          }),
          api.get(`${API_BASE_URL}/ajax/getWorkStatusListInStructure`, {
            withCredentials: true,
          }),
        ]);

      // Debug logging to see actual response structure
      console.log("Structure Types API Response:", structureTypesRes.data);

      setOptions({
        contracts:
          contractsRes.data?.map((item) => ({
            value: item.contract_id_fk || item.contractIdFk,
            label: item.contract_short_name || item.contractShortName,
          })) || [],
        structureTypes:
          Array.isArray(structureTypesRes.data)
            ? structureTypesRes.data.map((item) => {
                // Extract value from possible property names
                const value = 
                  item.structure_type_id || 
                  item.id ||
                  item.value ||
                  item.structure_type || 
                  item.structureType;
                
                // Extract label from possible property names
                const label = 
                  item.structure_type || 
                  item.label ||
                  item.name ||
                  item.structure_type_name ||
                  item.structureTypeName ||
                  (value ? `Structure Type ${value}` : 'Unknown');
                
                return {
                  value: value,
                  label: label,
                };
              })
            : [],
        workStatuses:
          workStatusesRes.data?.map((item) => ({
            value: item.work_status_fk || item.workStatusFk,
            label: item.work_status_fk || item.workStatusFk,
          })) || [],
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
        sSearch: filters.search || "",
      };

      if (filters.contract) {
        params.contract_id_fk = filters.contract.value;
      }
      
      if (filters.structureType) {
        // Try multiple possible parameter names for structure type
        params.structure_type = filters.structureType.value;
        params.structure_type_fk = filters.structureType.value;
        params.structureTypeId = filters.structureType.value;
      }
      
      if (filters.workStatus) {
        params.work_status_fk = filters.workStatus.value;
      }

      console.log("Fetching structures with params:", params);

      const res = await api.get(`${API_BASE_URL}/ajax/getStructuresList`, {
        params,
        withCredentials: true,
      });

      const data = res.data || {};
      console.log("Structures API Response:", data);
      setStructureFormGrid(data.aaData ?? data.data ?? []);

      setPagination((prev) => {
        const totalRecords = data.iTotalRecords ?? data.iTotalDisplayRecords ?? 0;
        const totalPages =
          totalRecords > 0 ? Math.ceil(totalRecords / prev.pageSize) : 0;
        return {
          ...prev,
          totalRecords,
          totalPages,
        };
      });
    } catch (err) {
      console.error("Error fetching structure list:", err);
      setStructureFormGrid([]);
      setPagination((prev) => ({ ...prev, totalRecords: 0, totalPages: 0 }));
    } finally {
      setLoading(false);
    }
  };

  const handleFilterChange = (name, selectedOption) => {
    console.log(`Filter ${name} changed to:`, selectedOption);
    setFilters((prev) => ({
      ...prev,
      [name]: selectedOption || null,
    }));
    setPagination((prev) => ({ ...prev, currentPage: 0 }));
  };

  const handleSearchChange = useCallback(
    debounce((value) => {
      setFilters((prev) => ({ ...prev, search: value }));
      setPagination((prev) => ({ ...prev, currentPage: 0 }));
    }, 500),
    []
  );

  const handlePageChange = (selectedItem) => {
    setPagination((prev) => ({
      ...prev,
      currentPage: selectedItem.selected,
    }));
  };

  const handlePageSizeChange = (e) => {
    const newSize = Number(e.target.value) || 10;
    setPagination((prev) => ({
      ...prev,
      pageSize: newSize,
      currentPage: 0,
      totalPages:
        prev.totalRecords > 0 ? Math.ceil(prev.totalRecords / newSize) : prev.totalPages,
    }));
  };

  const clearFilters = () => {
    // Clear all filters
    setFilters({
      contract: null,
      structureType: null,
      workStatus: null,
      search: "",
    });
    
    // Clear the search input field
    if (searchInputRef.current) {
      searchInputRef.current.value = "";
    }
    
    setPagination((prev) => ({ ...prev, currentPage: 0 }));
  };

  const handleEdit = (structure) => {
    navigate("get-structure-form", {
      state: {
        structure_id: structure.structure_id ?? structure.structureId,
        updateStructureform: structure,
      },
    });
  };

  const isStructureForm = location.pathname.endsWith("/get-structure-form");

  const startRecord =
    pagination.totalRecords === 0
      ? 0
      : pagination.currentPage * pagination.pageSize + 1;
  const endRecord =
    startRecord === 0 ? 0 : startRecord + structureFormGrid.length - 1;

  return (
    <div className={styles.container}>
      {!isStructureForm && (
        <div className="pageHeading">
          <h2>Structure Form</h2>
        </div>
      )}

      {!isStructureForm && (
        <div className="innerPage">
          <div>
            <div className={styles.filterSection}>
              <div className={styles.filterRow}>
                <div className={styles.filterOptions}>
                  <Select
                    classNamePrefix="react-select"
                    options={options.contracts}
                    value={filters.contract}
                    onChange={(selectedOption) =>
                      handleFilterChange("contract", selectedOption)
                    }
                    placeholder="Select Contract"
                    isSearchable
                    isClearable
                  />
                </div>

                <div className={styles.filterOptions}>
                  <Select
                    classNamePrefix="react-select"
                    options={options.structureTypes}
                    value={filters.structureType}
                    onChange={(selectedOption) =>
                      handleFilterChange("structureType", selectedOption)
                    }
                    placeholder="Select Structure Type"
                    isSearchable
                    isClearable
                  />
                </div>

                <div className={styles.filterOptions}>
                  <Select
                    classNamePrefix="react-select"
                    options={options.workStatuses}
                    value={filters.workStatus}
                    onChange={(selectedOption) =>
                      handleFilterChange("workStatus", selectedOption)
                    }
                    placeholder="Select Work Status"
                    isSearchable
                    isClearable
                  />
                </div>

                <button
                  className={`btn btn-2 btn-primary ${styles.clearButton}`}
                  type="button"
                  onClick={clearFilters}
                >
                  Clear Filters
                </button>
              </div>
              
              {/* Search bar moved to right side */}
              <div className={styles.searchBarContainer}>
                <div className={styles.searchBar}>
                  <input
                    ref={searchInputRef}
                    type="text"
                    placeholder="Search structures..."
                    className="form-control"
                    onChange={(e) => handleSearchChange(e.target.value)}
                    defaultValue={filters.search}
                  />
                </div>
              </div>
            </div>
          </div>

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
                        <tr key={sf.structure_id ?? sf.structureId ?? index}>
                          <td>{sf.structure_id ?? sf.structureId}</td>
                          <td>{sf.project_id_fk ?? sf.projectIdFk}</td>
                          <td>{sf.structure_type_fk ?? sf.structureTypeFk}</td>
                          <td>{sf.structure_name ?? sf.structure}</td>
                          <td>{sf.contract_short_name ?? sf.contractShortName}</td>
                          <td>{sf.work_status_fk ?? sf.workStatusFk}</td>
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

                {pagination.totalPages > 1 && (
                  <div className={styles.paginationWrapper}>
                    <div className={styles.pageSizeSelector}>
                      <span>Rows per page:</span>
                      <select
                        value={pagination.pageSize}
                        onChange={handlePageSizeChange}
                      >
                        <option value={5}>5</option>
                        <option value={10}>10</option>
                        <option value={20}>20</option>
                        <option value={50}>50</option>
                      </select>
                    </div>

                    <ReactPaginate
                      breakLabel="..."
                      previousLabel="‹"
                      nextLabel="›"
                      pageCount={pagination.totalPages}
                      onPageChange={handlePageChange}
                      forcePage={pagination.currentPage}
                      containerClassName={styles.pagination}
                      pageClassName={styles.pageItem}
                      pageLinkClassName={styles.pageLink}
                      previousClassName={styles.pageItem}
                      nextClassName={styles.pageItem}
                      previousLinkClassName={styles.pageLink}
                      nextLinkClassName={styles.pageLink}
                      breakClassName={styles.pageItem}
                      breakLinkClassName={styles.pageLink}
                      disabledClassName={styles.disabled}
                      activeClassName={styles.active}
                      pageRangeDisplayed={3}
                      marginPagesDisplayed={1}
                    />

                    <div className={styles.paginationInfo}>
                      {pagination.totalRecords > 0 ? (
                        <>
                          Showing <strong>{startRecord}</strong>–
                          <strong>{endRecord}</strong> of{" "}
                          <strong>{pagination.totalRecords}</strong>
                        </>
                      ) : (
                        "No records"
                      )}
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