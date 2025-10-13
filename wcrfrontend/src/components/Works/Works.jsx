import React, { useEffect } from "react"; 
import { useLocation } from "react-router-dom";
import styles from './Works.module.css';
import Dashboard from "../Dashboard/Dashboard";
import { usePageTitle } from "../../context/PageTitleContext";

export default function Works() {

     const { saveRouteTitle } = usePageTitle();
    const location = useLocation();

  
       useEffect(() => {
          saveRouteTitle(
            location.pathname,
            <>
            <span className="fw-400">Work Name:</span>{" "} NS-1: DOUBLING
            </>
            );
        }, [location.pathname, saveRouteTitle]);

  return (
    <div className={styles.container}>
      
      <Dashboard/>
    </div>
  );
}