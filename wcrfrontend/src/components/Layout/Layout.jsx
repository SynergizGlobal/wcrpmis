import React, { useState, useEffect, useRef } from "react";
import { Outlet } from "react-router-dom";   // ✅ Import Outlet
import Header from "../Header/Header";
import Sidebar from "../Sidebar/Sidebar";
import Footer from "../Footer/Footer";
import styles from "./layout.module.css";

export default function Layout() {
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const sidebarRef = useRef(null);

  const toggleSidebar = () => setSidebarOpen((prev) => !prev);

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (
        sidebarOpen &&
        sidebarRef.current &&
        !sidebarRef.current.contains(event.target) &&
        window.innerWidth <= 767 
      ) {
        setSidebarOpen(false);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, [sidebarOpen]);

  useEffect(() => {
    const handleResize = () => {
      if (window.innerWidth > 767 && sidebarOpen) {
        setSidebarOpen(false);
      }
    };
    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, [sidebarOpen]);

  return (
    <div className={styles.layout}>
      <Header toggleSidebar={toggleSidebar} />
      {sidebarOpen && window.innerWidth <= 767 && (
        <div
          className={`${styles.overlay} ${sidebarOpen ? styles.visible : ""}`}
          onClick={() => setSidebarOpen(false)}
        ></div>
      )}
      <div className={styles.main}>
        <Sidebar 
          sidebarOpen={sidebarOpen}
          setSidebarOpen={setSidebarOpen}
          sidebarRef={sidebarRef}
        />
        <div className={styles.content}>
          <Outlet /> 
        </div>
      </div>
      <Footer />
    </div>
  );
}
