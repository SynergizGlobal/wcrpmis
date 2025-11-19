import React, { useEffect, useState } from "react";
import api from "../../api/axiosInstance";
import GanttBarChart from "../../components/Charts/GanttBarChart/GanttBarChart";
import { useLocation } from "react-router-dom";
import styles from './Works.module.css';
import Dashboard from "../Dashboard/Dashboard";
import { usePageTitle } from "../../context/PageTitleContext";

import { API_BASE_URL } from "../../config";
import { EXCEL_SAMPLE } from "../../data/excelSample";

export default function Works() {

     const { saveRouteTitle } = usePageTitle();
     const location = useLocation();
     const [dbData, setDbData] = useState(null);
     const [loading, setLoading] = useState(true);

  
       useEffect(() => {
          saveRouteTitle(
            location.pathname,
            <>
            <span className="fw-400">Work Name:</span>{" "} NS-1: DOUBLING
            </>
            );
        }, [location.pathname, saveRouteTitle]);

        useEffect(() => {
        const load = async () => {
          try {
            // Example: adjust endpoint to your backend
            const { data } = await api.get(`${API_BASE_URL}/execution/progress`, {
              withCredentials: true,
            });
            // data is expected like:
            // [{ contract, contractor, structure, subStructure, fromKm, toKm, status }, ...]
            setDbData(data);
          } catch (e) {
            console.error("DB fetch failed, using excel sample", e);
            setDbData(EXCEL_SAMPLE); // fallback to sample
          } finally {
            setLoading(false);
          }
        };
        load();
      }, []);

  return (
    <div className={styles.container}>
      
      <Dashboard title="Execution - Progress Chart">
        {loading ? (
          <p style={{ textAlign: "center" }}>Loading chart…</p>
        ) : (
          <GanttBarChart
            title="AGENCY POSITION IN GHATOLI - BIAORA NEW BG RAIL LINE PROJECT"
            subtitle="GHATOLI - BIAORA SECTION (76.226 Kms.)"
            excelData={dbData}   // <-- works with DB JSON OR Excel-style 2D array
          />
        )}
      </Dashboard>
    </div>
  );
}