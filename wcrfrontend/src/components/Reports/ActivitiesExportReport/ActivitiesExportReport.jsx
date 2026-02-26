import React, { useContext, useEffect, useState } from "react";
import { RefreshContext } from "../../../context/RefreshContext";
import Select from "react-select";
import styles from './ActivitiesExportReport.module.css';
import api from "../../../api/axiosInstance";
import { Outlet, useLocation } from "react-router-dom";
import { API_BASE_URL } from "../../../config";

export default function ActivitiesExportReport() {
  const { refresh } = useContext(RefreshContext);
  const location = useLocation();

  const [search, setSearch] = useState("");
  const [page, setPage] = useState(1);

  const [projectOptions, setProjectOptions] = useState([]);
  const [contractOptions, setContractOptions] = useState([]);

  const [filters, setFilters] = useState({
    project: "",
    contract: "",
  });

   //  CLEAR FILTERS
   
  const handleClearFilters = () => {
    setFilters({
      project: "",
      contract: "",
    });
    setSearch("");
    setPage(1);
  };


   /* LOAD DROPDOWN DATA */

  useEffect(() => {
    loadFilters();
  }, []);

  const loadFilters = async () => {
    try {
      const projectRes = await api.get(
        `${API_BASE_URL}/api/activities-export/projects`,
        { withCredentials: true }
      );

      const contractRes = await api.get(
        `${API_BASE_URL}/api/activities-export/contracts`,
        { withCredentials: true }
      );

      // ✅ MAP PROJECT OPTIONS
      setProjectOptions(
        (projectRes.data || []).map((p) => ({
          value: p.project_id,
          label: `${p.project_id} - ${p.project_name}`
        }))
      );

      // ✅ MAP CONTRACT OPTIONS
      setContractOptions(
        (contractRes.data || []).map((c) => ({
          value: c.contract_id_fk,
          label: `${c.contract_id_fk} - ${c.contract_short_name || ""}`
        }))
      );

    } catch (err) {
      console.error("Error loading filters", err);
    }
  };


     /* GENERATE REPORT */

  const handleGenerateReport = async () => {
    try {
      const response = await api.post(
        "/api/activities-export/generate",
        {
          contract_id_fk: filters.contract,
          project_id: filters.project   // include if required by backend
        },
        {
          responseType: "blob",
          withCredentials: true
        }
      );

      // ✅ Create Blob with correct Excel MIME type
      const blob = new Blob([response.data], {
        type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
      });

      const url = window.URL.createObjectURL(blob);

      const link = document.createElement("a");
      link.href = url;

      // ✅ Proper dynamic file name
     const timestamp = new Date().toISOString().replace(/[:.]/g, "-");
      link.setAttribute(
        "download",
        `Activities_Export_Report_${timestamp}.xlsx`
      );

      document.body.appendChild(link);
      link.click();
      link.remove();

      window.URL.revokeObjectURL(url);

    } catch (error) {
      console.error("Report generation failed", error);
    }
  };
  
  return (
    <div className={styles.container}>
      {/* Top Bar */}
      <div className="pageHeading">
        <h2>Activities Export Report</h2>
        <div className="rightBtns">&nbsp;</div>
      </div>

      <div className="innerPage">
        <div className="form-row">
          {/* HOD */}
		  <div className="form-field">
		        <label className={styles.label}>Project</label>
		        <Select
		          options={projectOptions}
		          value={projectOptions.find((o) => o.value === filters.hod)}
		          onChange={(opt) =>
		            setFilters({ ...filters, project: opt.value })
		          }
		          placeholder="Select Project"
		          className={styles.filterOptions}
		        />
		      </div>

          {/* Contract */}
		  <div className="form-field">
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
        </div>

        {/* BUTTON ROW */}
        <div className="form-action-btns">
          <button
            className="btn btn-2 btn-primary"
            onClick={handleClearFilters}
          >
            Clear Filters
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