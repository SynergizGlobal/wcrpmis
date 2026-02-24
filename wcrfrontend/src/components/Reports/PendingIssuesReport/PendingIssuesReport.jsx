import React, { useContext, useEffect, useState } from "react";
import { RefreshContext } from "../../../context/RefreshContext";
import Select from "react-select";
import styles from "./PendingIssuesReport.module.css";
import api from "../../../api/axiosInstance";
import { Outlet, useLocation } from "react-router-dom";
import { API_BASE_URL } from "../../../config";

export default function PendingIssuesReport() {
  const { refresh } = useContext(RefreshContext);
  const location = useLocation();

  const [search, setSearch] = useState("");
  const [page, setPage] = useState(1);

  const [hodOptions, setHodOptions] = useState([]);
  const [contractOptions, setContractOptions] = useState([]);

  const [filters, setFilters] = useState({
    hod: "",
    contract: "",
  });

  /* =========================
     CLEAR FILTERS
  ========================== */
  const handleClearFilters = () => {
    setFilters({
      hod: "",
      contract: "",
    });
    setSearch("");
    setPage(1);
  };

  /* =========================
     LOAD DROPDOWN DATA
  ========================== */
  useEffect(() => {
    loadFilters();
  }, []);

  const loadFilters = async () => {
    try {
      const hodRes = await api.post(
        `${API_BASE_URL}/api/issues-report/getHodList`,
        {},
        { withCredentials: true }
      );

      const contractRes = await api.post(
        `${API_BASE_URL}/api/issues-report/getContractList`,
        {},
        { withCredentials: true }
      );

      // 🔹 MAP HOD OPTIONS
	  setHodOptions(
	    (hodRes.data || []).map((h) => ({
	      value: h.hod_fk ?? h.hod_name ?? h.user_id,
	      label: h.hod_name ?? h.user_name ?? h.hod_fk
	    }))
	  );

      // 🔹 MAP CONTRACT OPTIONS
	  setContractOptions(
	    (contractRes.data || []).map((c) => ({
	      value: c.contract_id ?? c.contract_fk,
	      label: c.contract_name ?? c.contract_fk
	    }))
	  );

    } catch (err) {
      console.error("Error loading filters", err);
    }
  };

  /* =========================
     GENERATE REPORT
  ========================== */
  const handleGenerateReport = async () => {
    try {
      const response = await api.post(
        "/api/issues-report/generate-pending",
        {
          hod_user_id_fk: filters.hod,
          contract_id_fk: filters.contract
        },
        {
          responseType: "blob"
        }
      );

      const url = window.URL.createObjectURL(
        new Blob([response.data])
      );

      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", "Pending_Issues_Report.docx");
      document.body.appendChild(link);
      link.click();
      link.remove();

    } catch (error) {
      console.error("Report generation failed", error);
    }
  };
  
 /* const handleGeneratePdf = async () => {
    try {
      const response = await api.post(
        "/api/issues-report/generate-pending-pdf",
        {
          hod_user_id_fk: filters.hod,
          contract_id_fk: filters.contract
        },
        {
          responseType: "blob"
        }
      );

      const url = window.URL.createObjectURL(
        new Blob([response.data])
      );

      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", "Pending_Issues_Report.pdf");
      document.body.appendChild(link);
      link.click();
      link.remove();

    } catch (error) {
      console.error("PDF generation failed", error);
    }
  };*/
  /* =========================
     RENDER
  ========================== */
  return (
    <div className={styles.container}>
      {/* Top Bar */}
      <div className="pageHeading">
        <h2>Pending Issues Report</h2>
        <div className="rightBtns">&nbsp;</div>
      </div>

      <div className="innerPage">
        <div className="form-row">
          {/* HOD */}
		  <div className="form-field">
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
	      {/* <button
		    className="btn btn-secondary"
		    onClick={handleGeneratePdf}
		  >
		    Generate PDF
		  </button>*/}
        </div>
      </div>

      <Outlet />
    </div>
  );
}