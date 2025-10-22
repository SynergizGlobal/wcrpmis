import React, { useState, useEffect } from "react";
import Select from "react-select";
import { CirclePlus } from "lucide-react";
import { LuCloudDownload } from "react-icons/lu";
import axios from "axios";
import { Outlet, useNavigate, useLocation } from "react-router-dom";
import styles from "./LandAcquisition.module.css";
import { API_BASE_URL } from "../../../config";

import { FaSearch } from 'react-icons/fa';
import { MdEditNote } from 'react-icons/md';

export default function LandAcquisition() {
  const location = useLocation();
  const navigate = useNavigate();

  // Filters
  const [village, setVillage] = useState(null);
  const [typeOfLand, setTypeOfLand] = useState(null);
  const [subCategory, setSubCategory] = useState(null);
  const [landStatus, setLandStatus] = useState(null);

  const [villageOptions, setVillageOptions] = useState([]);
  const [typeOptions, setTypeOptions] = useState([]);
  const [subOptions, setSubOptions] = useState([]);
  const [statusOptions, setStatusOptions] = useState([]);

  // Data and pagination
  const [data, setData] = useState([]);
  const [totalRows, setTotalRows] = useState(0);
  const [page, setPage] = useState(1);
  const [perPage, setPerPage] = useState(10);
  const [loading, setLoading] = useState(false);

  const [search, setSearch] = useState("");

  // Filters linking
  const filterParams = {
    village: village?.value || "",
    type_of_land: typeOfLand?.value || "",
    sub_category_of_land: subCategory?.value || "",
    la_land_status_fk: landStatus?.value || "",
  };

  //  Fetch filter options
      const fetchFilters = async (filters = {}) => {
        try {
          // Separate type fetch (always all)
          const typePromise = axios.post(
            `${API_BASE_URL}/land-acquisition/ajax/getTypesOfLandsFilterListInLandAcquisition`,
            { village: "", type_of_land: "", sub_category_of_land: "", la_land_status_fk: "" },
            { withCredentials: true }
          );

          // Other filters are dependent on selected values
          const dependentFilters = {
            village: filters.village?.value || village?.value || "",
            type_of_land: filters.typeOfLand?.value || typeOfLand?.value || "",
            sub_category_of_land: filters.subCategory?.value || subCategory?.value || "",
            la_land_status_fk: filters.landStatus?.value || landStatus?.value || "",
          };

          const [statusRes, villageRes, subRes, typeRes] = await Promise.all([
            axios.post(
              `${API_BASE_URL}/land-acquisition/ajax/getStatussFilterListInLandAcquisition`,
              dependentFilters,
              { withCredentials: true }
            ),
            axios.post(
              `${API_BASE_URL}/land-acquisition/ajax/getVillagesFilterListInLandAcquisition`,
              dependentFilters,
              { withCredentials: true }
            ),
            axios.post(
              `${API_BASE_URL}/land-acquisition/ajax/getSubCategoryFilterListInLandAcquisition`,
              dependentFilters,
              { withCredentials: true }
            ),
            typePromise, // always unfiltered types
          ]);

          // ✅ Update dropdown options
          setStatusOptions(
            statusRes.data.map((s) => ({
              label: s.la_land_status_fk,
              value: s.la_land_status_fk,
            }))
          );
          setVillageOptions(
            villageRes.data.map((v) => ({
              label: v.village,
              value: v.village,
            }))
          );
          setTypeOptions(
            typeRes.data.map((t) => ({
              label: t.type_of_land,
              value: t.type_of_land,
            }))
          );
          setSubOptions(
            subRes.data.map((s) => ({
              label: s.sub_category_of_land,
              value: s.sub_category_of_land,
            }))
          );
        } catch (err) {
          console.error("Error fetching filters:", err);
        }
      };




  // Fetch table data
  const fetchData = async (page = 1, length = perPage) => {
  setLoading(true);
  try {
    const res = await axios.post(
      `${API_BASE_URL}/land-acquisition/ajax/get-land-acquisition?iDisplayStart=${(page - 1) * length}&iDisplayLength=${length}&sSearch=${search || ""}`,
      filterParams,
      { withCredentials: true } // important for session cookies
    );

    setData(res.data.aaData || []);
    setTotalRows(res.data.iTotalRecords || 0);
  } catch (err) {
    console.error("Error fetching table data:", err);
  } finally {
    setLoading(false);
  }
};
  // Fetch all filters on first load
    useEffect(() => {
      fetchFilters(); //will use default empty {}
    }, []);

    // Fetch filters again when any selection changes (to get dependent values)
    useEffect(() => {
      fetchFilters({ village, typeOfLand, subCategory, landStatus });
    }, [village, typeOfLand, subCategory, landStatus]);

    // Fetch data when filters or pagination change
    useEffect(() => {
      fetchData(page, perPage);
    }, [village, typeOfLand, subCategory, landStatus, search, page, perPage]);

    // Reset dependent dropdowns
    useEffect(() => {
      setSubCategory(null);
      setLandStatus(null);
    }, [typeOfLand]);


    const clearFilters = () => {
      setVillage(null);
      setTypeOfLand(null);
      setSubCategory(null);
      setLandStatus(null);
      fetchData(1);
    };

  const handleAdd = () => navigate("landacquisitionform");
  const isFormPage = location.pathname.endsWith("/landacquisitionform");

  const totalPages = Math.ceil(totalRows / perPage);

  return (
    <div className={styles.container}>
      {/* Page Heading */}
      {!isFormPage && (
        <div className="pageHeading">
          <h2>Land Acquisition</h2>
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
      {!isFormPage && (
        <>
        <div className="innerPage">
          <div className="filterRow">
            <div className="filterOptions">
              <Select 
                value={village} 
                onChange={setVillage} 
                options={villageOptions} 
                placeholder="Select Village" 
                isClearable 
                isSearchable 
              />
            </div>
            <div className="filterOptions">
              <Select value={typeOfLand} 
                onChange={setTypeOfLand} 
                options={typeOptions} 
                placeholder="Select Type of Land" 
                isClearable 
                isSearchable 
            />
            </div>
            <div className="filterOptions">
              <Select 
                value={subCategory} 
                onChange={setSubCategory} 
                options={subOptions} 
                placeholder="Select Sub Category" 
                isClearable isSearchable 
            />
            </div>
            <div className="filterOptions">
              <Select 
                value={landStatus} 
                onChange={setLandStatus} 
                options={statusOptions} 
                placeholder="Select Land Status" 
                isClearable 
                isSearchable 
              />
            </div>
              <button className="btn btn-2 btn-primary" onClick={clearFilters}>Clear Filters</button>
          </div>
          
          {/* Search Bar */}
          <div className="searchBar">
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
            <div className="searchRow form-field">
            <input
              type="text"
              className="form-control"
              placeholder="Search..."
              value={search}
              onChange={(e) => {
                setSearch(e.target.value);
                const value = e.target.value;
                if (value.length === 0 || value.length > 2) fetchData(1); // triggers after 2+ chars
              }}
              onKeyDown={(e) => {
                if (e.key === "Enter") fetchData(1); // Press Enter to search
              }}
            />
            <button
              className="transparent-btn search-btn"
              onClick={() => fetchData(1)}
            >
              <FaSearch
                size="16"
              />
            </button>
          </div>
            </div>

          {/* Table */}
          <div className={`dataTable ${styles.tableWrapper}`}>
            <table className={styles.projectTable}>
              <thead>
                <tr>
                  <th>Survey No</th>
                  <th>Project</th>
                  <th>Village</th>
                  <th>Type of Land</th>
                  <th>Sub Category</th>
                  <th>Area to be Acquired (Ha)</th>
                  <th>Area Acquired (Ha)</th>
                  <th>Last Update</th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
                {loading ? (
                  <tr>
                    <td colSpan="9" style={{ textAlign: "center" }}>Loading...</td>
                  </tr>
                ) : data.length > 0 ? (
                  data.map((row, i) => (
                    <tr key={i}>
                      <td>{row.survey_number || "-"}</td>
                      <td>{row.project_name || "-"}</td>
                      <td>{row.village || "-"}</td>
                      <td>{row.type_of_land || "-"}</td>
                      <td>{row.sub_category_of_land || "-"}</td>
                      <td>{row.area_to_be_acquired || "-"}</td>
                      <td>{row.area_acquired || "-"}</td>
                      <td>{row.modified_date || "-"}</td>
                      <td>
                        <button
                          className="btn btn-sm btn-outline-primary"
                          onClick={() => navigate("landacquisitionform", { state: { data: row } })}
                        >
                          <MdEditNote
                            size="22"
                          />
                        </button>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="9" style={{ textAlign: "center" }}>No records found</td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>

          {/* Pagination */}
          <div className="paginationRow">
            <div className="entryCount">
              Showing {Math.min((page - 1) * perPage + 1, totalRows)}–
              {Math.min(page * perPage, totalRows)} of {totalRows} entries
            </div>
            <div className="pagination">
              {totalPages > 1 && (
                <>
                  {/* Previous Button */}
                  <button
                    disabled={page === 1}
                    onClick={() => {
                      setPage(page - 1);
                      window.scrollTo({ top: 0, behavior: "smooth" });
                    }}
                    className="pageBtn"
                  >
                    ‹
                  </button>

                  {/* Page Numbers */}
                  {Array.from({ length: totalPages }, (_, i) => i + 1)
                    .filter((pageNumber) => {
                      // Always show first 2 and last 2 pages
                      if (pageNumber <= 2 || pageNumber > totalPages - 2) return true;

                      // Show nearby pages (current -1, current, current +1)
                      if (pageNumber >= page - 1 && pageNumber <= page + 1) return true;

                      return false;
                    })
                    .reduce((acc, pageNumber, index, arr) => {
                      // Insert "..." where gaps exist
                      if (index > 0 && pageNumber - arr[index - 1] > 1) acc.push("...");
                      acc.push(pageNumber);
                      return acc;
                    }, [])
                    .map((item, idx) =>
                      item === "..." ? (
                        <span key={idx} className="ellipsis">
                          ...
                        </span>
                      ) : (
                        <button
                          key={idx}
                          onClick={() => {
                            setPage(item);
                            window.scrollTo({ top: 0, behavior: "smooth" });
                          }}
                          className={`pageBtn ${item === page ? "activePage" : ""}`}
                        >
                          {item}
                        </button>
                      )
                    )}

                  {/* Next Button */}
                  <button
                    disabled={page === totalPages}
                    onClick={() => {
                      setPage(page + 1);
                      window.scrollTo({ top: 0, behavior: "smooth" });
                    }}
                    className="pageBtn"
                  >
                    ›
                  </button>
                </>
              )}
            </div>

          </div>
        </div>
        </>
      )}
      <Outlet />
    </div>
  );
}
