import React, { useState, useEffect } from "react";
import Select from "react-select";
import { CirclePlus, Download } from "lucide-react";
import { LuCloudDownload, LuUpload, LuDownload } from "react-icons/lu";
import api from "../../../api/axiosInstance";
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

  const [showModal, setShowModal] = useState(false);
  const [selectedFile, setSelectedFile] = useState(null);

  const [search, setSearch] = useState("");

  // ✅ Open / Close modal
  const openModal = () => setShowModal(true);
  const closeModal = () => {
    setShowModal(false);
    setSelectedFile(null);
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

    const formData = new FormData();
    formData.append("laUploadFile", selectedFile);

    try {
      setLoading(true);
      const res = await api.post(
          `${API_BASE_URL}/upload-la`,  
        formData,
        {
          headers: { "Content-Type": "multipart/form-data" },
          withCredentials: true,
        }
      );

      if (res.status === 200) {
        alert("✅ File uploaded successfully!");
        closeModal();
      } else {
        alert("⚠️ Upload failed. Please try again.");
      }
    } catch (err) {
      console.error("❌ Upload error:", err);
      alert("❌ Upload failed. Check console for details.");
    } finally {
      setLoading(false);
    }
  };

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
          const typePromise = api.post(
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
            api.post(
              `${API_BASE_URL}/land-acquisition/ajax/getStatussFilterListInLandAcquisition`,
              dependentFilters,
              { withCredentials: true }
            ),
            api.post(
              `${API_BASE_URL}/land-acquisition/ajax/getVillagesFilterListInLandAcquisition`,
              dependentFilters,
              { withCredentials: true }
            ),
            api.post(
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
    const res = await api.post(
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

  // another storgae file Download code

  const handleSampleDownload = () => {
    const fileUrl = "/files/landacquisition/Land_Acquisition_Template.xlsx"; // or any URL
    const fileName = "Land_Acquisition_Template.xlsx";

    const link = document.createElement("a");
    link.href = fileUrl;
    link.setAttribute("download", fileName);
    document.body.appendChild(link);
    link.click();
    link.remove();
  };

  // data export function
    const exportLA = () => {
  const form = document.getElementById("exportLandAcquisitionForm");

  document.getElementById("exportLa_land_status_fk").value = landStatus?.value || "";
  document.getElementById("exportVillage").value = village?.value || "";
  document.getElementById("exportType_of_land").value = typeOfLand?.value || "";
  document.getElementById("exportSub_category_of_land").value = subCategory?.value || "";
  document.getElementById("exportsearchStr").value = search || "";

  form.submit();
};

  return (
    <div className={styles.container}>
      {/* Page Heading */}
      {!isFormPage && (
        <div className="pageHeading">
          <h2>Land Acquisition</h2>
          <div className="rightBtns">
            <button className="btn-2 transparent-btn" onClick={handleSampleDownload}>
              <LuDownload size={16} />
            </button>
            <button className="btn btn-primary" onClick={openModal}>
              <LuUpload size={16} /> Upload
            </button>
            <button className="btn btn-primary" onClick={handleAdd}>
              <CirclePlus size={16} /> Add
            </button>
            <button className="btn btn-primary"
             onClick={(e) => {
                e.preventDefault(); // <-- IMPORTANT
                exportLA();
              }}
             >
              <LuCloudDownload size={16} /> Export
            </button>
          </div>
        </div>
      )}

      {/* Filters */}
      {!isFormPage && (
        <>
        <div className="innerPage">
          {/* export hidden form */}
          <form
            id="exportLandAcquisitionForm"
            action={`${API_BASE_URL}/land-acquisition/export-land`}
            method="POST"
            target="_blank"
            style={{ display: "none" }}
          >
            <input type="hidden" name="la_land_status_fk" id="exportLa_land_status_fk" />
            <input type="hidden" name="village" id="exportVillage" />
            <input type="hidden" name="type_of_land" id="exportType_of_land" />
            <input type="hidden" name="sub_category_of_land" id="exportSub_category_of_land" />
            <input type="hidden" name="searchStr" id="exportsearchStr" />
          </form>

          {/* export hidden form */}
          
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
                          onClick={() => navigate("landacquisitionform", { state: { la_id: row.la_id } })}
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
      {/* Modal */}
      {showModal && (
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
            <h3 className="text-center mb-2">Upload Land Acquisition File</h3>

            <form onSubmit={handleSubmit} encType="multipart/form-data">
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
      <Outlet />
    </div>
  );
}
