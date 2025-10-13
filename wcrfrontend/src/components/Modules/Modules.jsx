import React, { useEffect } from "react"; 
import { useLocation } from "react-router-dom";
import styles from './Modules.module.css';
import Dashboard from "../Dashboard/Dashboard";
import { usePageTitle } from "../../context/PageTitleContext";

export default function Modules() {

   const { saveRouteTitle } = usePageTitle();
  const location = useLocation();

   useEffect(() => {
    saveRouteTitle(
      location.pathname,
      <>
      <span className="fw-400">Project Name:</span>{" "} NS-1: DOUBLING
      </>
      );
  }, [location.pathname, saveRouteTitle]);

  const reportData = [
    { id: 1, name: "Inspection A" },
    { id: 2, name: "Inspection B" },
  ];

  return (
    <div className={styles.container}>
      <Dashboard data={reportData} />
    </div>
  );
}