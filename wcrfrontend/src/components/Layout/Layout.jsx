import React, { useState, useEffect, useRef } from "react";
import { Outlet, useLocation } from "react-router-dom";
import Header from "../Header/Header";
import Sidebar from "../Sidebar/Sidebar";
import Footer from "../Footer/Footer";
import styles from "./layout.module.css";
import { usePageTitle } from "../../context/PageTitleContext";
import useAutoLogout from "../../hooks/useAutoLogout";

export default function Layout() {
  const location = useLocation();
  const { setPageTitle } = usePageTitle();
  useAutoLogout(25); // ⏰ Auto logout after inactivity (in minutes)

  const isIframe = window.self !== window.top;

  const [sidebarOpen, setSidebarOpen] = useState(false);
  const sidebarRef = useRef(null);

  // 🧠 Automatically set the correct header title even for nested routes
  useEffect(() => {
    const routeTitles = {
      "/home": "Western Central Railways",
      "/dashboard": "Dashboard",
      "/updateforms": "Update Forms",
      "/reports": "Reports",
      "/modules": "Modules",
      "/works": "Works",
      "/documents": "Documents",
      "/quicklinks": "Quick Links",
      "/admin": "Admin Panel",
    };

    const currentPath = location.pathname.toLowerCase();

    // ✅ Match the nearest route prefix
    const matchedKey =
      Object.keys(routeTitles).find((key) =>
        currentPath.startsWith(key)
      ) || "/home";

    setPageTitle(routeTitles[matchedKey]);
  }, [location.pathname, setPageTitle]);

  // 🔄 Sidebar toggle & responsiveness logic
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
      {!isIframe && <Header toggleSidebar={toggleSidebar} />}
      {sidebarOpen && window.innerWidth <= 767 && (
        <div
          className={`${styles.overlay} ${sidebarOpen ? styles.visible : ""}`}
          onClick={() => setSidebarOpen(false)}
        ></div>
      )}
      <div className={`ifNoOverflowHidden ${styles.main}`}>
        {!isIframe &&  <Sidebar
          sidebarOpen={sidebarOpen}
          setSidebarOpen={setSidebarOpen}
          sidebarRef={sidebarRef}
        /> }
        <div className={`ifNoOverflow ${styles.content}`}>
          <Outlet />
        </div>
      </div>
      {!isIframe &&  <Footer />}
    </div>
  );
}
