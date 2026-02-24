/*import React from 'react';
import { Outlet } from 'react-router-dom';
import styles from './LandAcquisitionReport.module.css';

export default function LandAcquisitionReport() {
  return (
    <div className={styles.container}>
      <h3>LandAcquisitionReport Component</h3>
      <Outlet />
    </div>
  );
}*/

import React, { useEffect, useState, useContext } from "react";
import { RefreshContext } from "../../../context/RefreshContext";
import Select from "react-select";
import { Outlet, useLocation } from 'react-router-dom';
import styles from './LandAcquisitionReport.module.css'
import api from "../../../api/axiosInstance";
import { API_BASE_URL } from "../../../config";

export default function LandAcquisitionReport() {
	 const { refresh } = useContext(RefreshContext);
	  const location = useLocation();
	  
	  const [search, setSearch] = useState("");
	  const [page, setPage] = useState(1);
	  
	  const [projects, setProjects] = useState([]);
	  const [categories, setCategories] = useState([]);
	  const [subCategories, setSubCategories] = useState([]);

	  const [projectOptions, setProjectOptions] = useState([]);
	  const [typeOfLandOptions, setTypeOfLandOptions] = useState([]);
	  const [subCategoryOptions, setSubCategoryOptions] = useState([]);

	  const [filters, setFilters] = useState({
	    project: "",
	    typeOfLand: "",
	    subCategory: "",
	  });
	  
	  const handleClearFilters = () => {
	    setFilters({
			project: "",
		    typeOfLand: "",
		    subCategory: "",
	    });
	    setSearch("");
	    setPage(1);
	  };

	  /* -------------------- DROPDOWN OPTIONS -------------------- */
	  useEffect(() => {
	    loadDropdowns();
	  }, []);

	  const loadDropdowns = async () => {
	    try {

	      const projectRes = await api.get(
	        `${API_BASE_URL}/api/land-report/project-list`,
	        { withCredentials: true }
	      );

	      const typeRes = await api.get(
	        `${API_BASE_URL}/api/land-report/type-list`,
	        { withCredentials: true }
	      );

	      const subCatRes = await api.get(
	        `${API_BASE_URL}/api/land-report/sub-category-list`,
	        { withCredentials: true }
	      );

	      // 🔥 Convert API data → react-select format

	      const projectOpts = projectRes.data.map(item => ({
	        value: item.project_id_fk,
	        label: item.project_name
	      }));

	      const typeOpts = typeRes.data.map(item => ({
	        value: item.category_fk,
	        label: item.category_fk
	      }));

	      const subCatOpts = subCatRes.data.map(item => ({
	        value: item.la_sub_category,
	        label: item.la_sub_category
	      }));

	      setProjectOptions(projectOpts);
	      setTypeOfLandOptions(typeOpts);
	      setSubCategoryOptions(subCatOpts);

	    } catch (err) {
	      console.error("Error loading LA dropdowns", err);
	    }
	  };

	  
	  const handleGenerateReport = async () => {
	    const response = await api.post(
	      `${API_BASE_URL}/api/land-report/generate`,
	      filters,
	     { responseType: "blob", withCredentials: true }
	    );

	    const url = window.URL.createObjectURL(
	      new Blob([response.data])
	    );

	    const link = document.createElement("a");
	    link.href = url;
	    link.setAttribute("download", "Land_Acquisition_Report.xlsx");
	    document.body.appendChild(link);
	    link.click();
	  };


	  return (
	    <div className={styles.container}>
		{/* Top Bar */}
	          <div className="pageHeading">
	            <h2>Land Acquisition Report</h2>
	              <div  className="rightBtns">
	                &nbsp;
	              </div>
	          </div>

			  <div className="innerPage">

			    {/* ✅ ROW 1 */}
			    <div className={styles.filterRow}>
			      <div className={styles.inputField}>
			        <label className={styles.label}>Project</label>
			        <Select
			          options={projectOptions}
			          value={projectOptions.find((o) => o.value === filters.project)}
			          onChange={(opt) =>
			            setFilters({ ...filters, project: opt.value })
			          }
			          placeholder="Select Project"
			          className={styles.filterOptions}
			        />
			      </div>

			      <div className={styles.inputField}>
			        <label className={styles.label}>Type Of Land</label>
			        <Select
			          options={typeOfLandOptions}
			          value={typeOfLandOptions.find((o) => o.value === filters.typeOfLand)}
			          onChange={(opt) =>
			            setFilters({ ...filters, typeOfLand: opt.value })
			          }
			          placeholder="Select Type Of Land"
			          className={styles.filterOptions}
			        />
			      </div>

			      <div className={styles.inputField}>
			        <label className={styles.label}>Sub Category</label>
			        <Select
			          options={subCategoryOptions}
			          value={subCategoryOptions.find((o) => o.value === filters.subCategory)}
			          onChange={(opt) =>
			            setFilters({ ...filters, subCategory: opt.value })
			          }
			          placeholder="Select Sub Category"
			          className={styles.filterOptions}
			        />
			      </div>
                 </div>	     

			    {/* ✅ ROW 2 */}
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