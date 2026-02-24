import React, { useEffect, useState, useContext } from "react";
import { RefreshContext } from "../../../context/RefreshContext";
import Select from "react-select";
import { Outlet, useLocation } from 'react-router-dom';
import styles from './IssuesDetailsReport.module.css';
import api from "../../../api/axiosInstance";
import { API_BASE_URL } from "../../../config";

export default function IssuesDetailsReport() {
	 const { refresh } = useContext(RefreshContext);
	  const location = useLocation();
	  
	  const [search, setSearch] = useState("");
	  const [page, setPage] = useState(1);
	  
	  const [hodOptions, setHodOptions] = useState([]);
	  const [contractOptions, setContractOptions] = useState([]);
	  const [statusOptions, setStatusOptions] = useState([]);
	  const [locationOptions, setLocationOptions] = useState([]);
	  const[categoryOptions, setCategoryOptions] = useState([]);
	  const[locationAndDescriptionOptions, setLocationAndDescriptionOptions] = useState([]);

	  const [filters, setFilters] = useState({
	    hod: "",
	    contract: "",
	    status: "",
	    location: "",
	    category: "",
	    locationAndDescription: "",
	  });
	  
	  const handleClearFilters = () => {
	    setFilters({
	      hod: "",
	      contract: "",
	      status: "",
	      location: "",
	      category: "",
	      locationAndDescription: "",
	    });
	    setSearch("");
	    setPage(1);
	  };

	  /* -------------------- DROPDOWN OPTIONS -------------------- */
	  useEffect(() => {
	    loadFilters();
	  }, []);

	  const loadFilters = async () => {
	    try {
	      const [
	        hodRes,
	        contractRes,
	        statusRes,
	        locationRes,
	        categoryRes,
	        titleRes
	      ] = await Promise.all([
	        api.post(`${API_BASE_URL}/api/issues-report/getHodList`, {}, { withCredentials: true }),
	        api.post(`${API_BASE_URL}/api/issues-report/getContractList`, {}, { withCredentials: true }),
	        api.post(`${API_BASE_URL}/api/issues-report/status-list`, {}, { withCredentials: true }),
	        api.post(`${API_BASE_URL}/api/issues-report/location-list`, {}, { withCredentials: true }),
	        api.post(`${API_BASE_URL}/api/issues-report/category-list`, {}, { withCredentials: true }),
	        api.post(`${API_BASE_URL}/api/issues-report/title-list`, {}, { withCredentials: true }),
	      ]);

	      setHodOptions([
	        { value: "", label: "Select HOD" },
	        ...(hodRes.data || []).map(h => ({
	          value: h.hod_fk ?? h.user_id,
	          label: h.hod_name ?? h.user_name
	        }))
	      ]);

	      setContractOptions([
	        { value: "", label: "Select Contract" },
	        ...(contractRes.data || []).map(c => ({
	          value: c.contract_id ?? c.contract_fk,
	          label: c.contract_name ?? c.contract_fk
	        }))
	      ]);

	      setStatusOptions([
	        { value: "", label: "Select Status" },
	        ...(statusRes.data || []).map(item => ({
	          value: item.status_fk,
	          label: item.status_fk
	        }))
	      ]);

	      setLocationOptions([
	        { value: "", label: "Select Location" },
	        ...(locationRes.data || []).map(item => ({
	          value: item.location,
	          label: item.location
	        }))
	      ]);

		  setCategoryOptions([
		    { value: "", label: "Select Category" },
		    ...(categoryRes.data || []).map(item => ({
		      value: item.category_fk,
		      label: item.category_fk
		    }))
		  ]);

		  setLocationAndDescriptionOptions([
		    { value: "", label: "Select" },
		    ...(titleRes.data || []).map(item => ({
		      value: item.issue_id,
		      label: `${item.issue_id} - ${item.location ?? ""} - ${item.title ?? ""}`,
		      fullData: item   // 🔥 keep full object (important)
		    }))
		  ]);

	    } catch (err) {
	      console.error("Error loading filters", err);
	    }
	  };
	  
	  const handleGenerateReport = async () => {
	    try {

	      const payload = {
	        hod_user_id_fk: filters.hod,
	        contract_id_fk: filters.contract,
	        status_fk: filters.status,
	        location: filters.location,
	        category_fk: filters.category,
	        issue_id: filters.locationAndDescription
	      };

	      const response = await api.post(
	        `${API_BASE_URL}/api/issues-details-report/generate`,
	        payload,
	        {
	          responseType: "blob",
	          withCredentials: true
	        }
	      );

	      const blob = new Blob([response.data], {
	        type: "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
	      });

	      const url = window.URL.createObjectURL(blob);
	      const link = document.createElement("a");
	      link.href = url;
	      link.download = "Issues_Details_Report.docx";
	      document.body.appendChild(link);
	      link.click();
	      link.remove();

	    } catch (error) {
	      console.error("Error downloading report:", error);
	    }
	  };

	  return (
	    <div className={styles.container}>
		{/* Top Bar */}
	          <div className="pageHeading">
	            <h2>Issues Details Report</h2>
	              <div  className="rightBtns">
	                &nbsp;
	              </div>
	          </div>

			  <div className="innerPage">

			    {/* ✅ ROW 1 */}
			    <div className={styles.filterRow}>
			      <div className={styles.inputField}>
			        <label className={styles.label}>HOD</label>
			        <Select
			          options={hodOptions}
			          value={hodOptions.find((o) => o.value === filters.hod)}
			          onChange={(opt) =>
			            setFilters({ ...filters, hod: opt.value })
			          }
			          placeholder="Select HOD"
			          className={styles.filterOptions}
			        />
			      </div>

			      <div className={styles.inputField}>
			        <label className={styles.label}>Contract</label>
			        <Select
			          options={contractOptions}
			          value={contractOptions.find((o) => o.value === filters.contract)}
			          onChange={(opt) =>
			            setFilters({ ...filters, contract: opt.value })
			          }
			          placeholder="Select Contract"
			          className={styles.filterOptions}
			        />
			      </div>

			      <div className={styles.inputField}>
			        <label className={styles.label}>Status</label>
			        <Select
			          options={statusOptions}
			          value={statusOptions.find((o) => o.value === filters.status)}
			          onChange={(opt) =>
			            setFilters({ ...filters, status: opt.value })
			          }
			          placeholder="Select Status"
			          className={styles.filterOptions}
			        />
			      </div>

			      <div className={styles.inputField}>
			        <label className={styles.label}>Location</label>
			        <Select
			          options={locationOptions}
			          value={locationOptions.find((o) => o.value === filters.location)}
			          onChange={(opt) =>
			            setFilters({ ...filters, location: opt.value })
			          }
			          placeholder="Select Location"
			          className={styles.filterOptions}
			        />
			      </div>
			    </div>

			    {/* ✅ ROW 2 */}
			    <div className={styles.filterRow}>
			      <div className={styles.inputField}>
			        <label className={styles.label}>Category</label>
			        <Select
			          options={categoryOptions}
			          value={categoryOptions.find((o) => o.value === filters.category)}
			          onChange={(opt) =>
			            setFilters({ ...filters, category: opt.value })
			          }
			          placeholder="Select Category"
			          className={styles.filterOptions}
			        />
			      </div>

			      <div className={`${styles.inputField} ${styles.wideField}`}>
			        <label className={styles.label}>Location & Description</label>
					<Select
					  options={locationAndDescriptionOptions}
					  value={locationAndDescriptionOptions.find(
					    (o) => o.value === filters.locationAndDescription
					  )}
					  onChange={(opt) => {
					    setFilters({
					      ...filters,
					      locationAndDescription: opt.value,
					      contract: opt.fullData?.contract_id_fk || "",
					      hod: opt.fullData?.hod_user_id_fk || "",
					      status: opt.fullData?.status_fk || ""
					    });
					  }}
					  placeholder="Select Location & Description"
					  className={styles.filterOptions}
					/>
			      </div>
			    </div>

			    {/* ✅ ROW 3 */}
			    <div className={styles.buttonRow}>
			      <button
			        className="btn btn-2 btn-primary"
			        onClick={handleClearFilters}
			      >
			        Clear Filter
			      </button>

			      <button
			        className="btn btn-2 btn-primary"
			        onClick={handleGenerateReport}
			      >
			        Generate Report
			      </button>
			    </div>

			  </div>
		  <Outlet />
		</div>
	  );
	}