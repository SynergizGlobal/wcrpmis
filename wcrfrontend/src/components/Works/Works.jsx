import React, { useEffect, useState } from "react";
import api from "../../api/axiosInstance";
import GanttBarChart from "../../components/Charts/GanttBarChart/GanttBarChart";
import { useLocation, useNavigate } from "react-router-dom";
import styles from './Works.module.css';
import Dashboard from "../Dashboard/Dashboard";
import { usePageTitle } from "../../context/PageTitleContext";

import { API_BASE_URL } from "../../config";
import { EXCEL_SAMPLE } from "../../data/excelSample";

export default function Works() {
  const { saveRouteTitle } = usePageTitle();
  const location = useLocation();
  const navigate = useNavigate();

  const [dbData, setDbData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [contracts, setContracts] = useState(["All Contracts"]);
  const [projectName, setProjectName] = useState("Unknown Project");

  // ------------------- Get project_id from URL -------------------
  const searchParams = new URLSearchParams(location.search);
  const projectId = searchParams.get("project_id"); 
  console.log("Project ID from URL:", projectId);
  // ----------------------------------------------------------------

  // ------------------- Handle segment click -------------------
  const handleSegmentClick = ({ contract_id, fromKm, toKm }) => {
    const from = Number(fromKm) * 1000;
    const to = Number(toKm) * 1000;

    navigate(
      `/work-overview-dashboard?project_id=${projectId}&contract_id=${contract_id}&from=${from}&to=${to}`
    );
  };
  // -------------------------------------------------------------

  // ------------------- Update route title dynamically -------------------
  useEffect(() => {
    saveRouteTitle(
      location.pathname,
      <>
        {projectName}
      </>
    );
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [location.pathname, projectName]);
  // ----------------------------------------------------------------------

  // ------------------- Fetch execution progress -------------------
  useEffect(() => {
    const load = async () => {
      setLoading(true);
      try {
        const { data } = await api.get(`${API_BASE_URL}/execution/progress`, {
          params: { project_id: projectId }, 
          withCredentials: true,
        });

        console.log("Fetched execution progress data:", data);

        if (data.length > 0) {
          // Set project name dynamically from API response (assumes first project in array)
          setProjectName(data[0].project || "Unknown Project");
        }

        setDbData(data);

        // Extract unique contract IDs
        const allContractIds = [
          "All Contracts",
          ...Array.from(new Set(data.map(item => item.contract_id)))
        ];
        setContracts(allContractIds);

      } catch (e) {
        console.error("DB fetch failed, using excel sample", e);
        setDbData(EXCEL_SAMPLE); // fallback
        setContracts(["All Contracts"]);
        setProjectName("Sample Project");
      } finally {
        setLoading(false);
      }
    };
    load();
  }, [projectId]);

  return (
    <div className={styles.container}>
      <Dashboard contracts={contracts}>
        {loading ? (
          <p style={{ textAlign: "center" }}>Loading chart…</p>
        ) : (
          <GanttBarChart
            excelData={dbData}   
            onSegmentClick={handleSegmentClick}
          />
        )}
      </Dashboard>
    </div>
  );
}
