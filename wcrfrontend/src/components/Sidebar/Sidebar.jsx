import React from "react";
import { NavLink } from "react-router-dom";
import styles from "./Sidebar.module.css";

import homeIcon from "../../assets/images/icons/home.svg"
import modulesIcon from "../../assets/images/icons/modules.svg"
import worksIcon from "../../assets/images/icons/works.svg"
import updateFormsIcon from "../../assets/images/icons/updateForms.svg"
import reportsIcon from "../../assets/images/icons/reports.svg"
import documentsIcon from "../../assets/images/icons/documents.svg"
import quickLinksIcon from "../../assets/images/icons/quickLinks.svg"
import adminIcon from "../../assets/images/icons/admin.svg"

export default function Sidebar({ sidebarOpen, setSidebarOpen, sidebarRef }) {

  const handleLinkClick = () => {
    if (window.innerWidth <= 767) {
      setSidebarOpen(false);
    }
  };

  return (
    <aside ref={sidebarRef} className={`sidebar ${styles.sidebar} ${sidebarOpen ? styles.open : ""}`}>
      <ul onClick={handleLinkClick}>
        <li>
          <NavLink 
            to="/home"
            className={({ isActive }) => 
              isActive ? `${styles.activeLink}` : undefined
            }
          >
            <img src={homeIcon} width="25" height="25" />
            <div className={styles.navName}>
              Home
            </div>
            
          </NavLink>
        </li>
        <li>
          <NavLink 
            to="/modules"
            className={({ isActive }) => 
              isActive ? `${styles.activeLink}` : undefined
            }
          >
            <img src={modulesIcon} width="25" height="25" />
            <div className={styles.navName}>
              Modules
            </div>
          </NavLink>
        </li>
        <li>
          <NavLink 
            to="/works"
            className={({ isActive }) => 
              isActive ? `${styles.activeLink}` : undefined
            }
          >
            <img src={worksIcon} width="25" height="25" />
            <div className={styles.navName}>
              Works
            </div>
            
          </NavLink>
        </li>
        <li>
          <NavLink 
            to="/updateforms"
            className={({ isActive }) => 
              isActive ? `${styles.activeLink}` : undefined
            }
          >
            <img src={updateFormsIcon} width="25" height="25" />
            <div className={styles.navName}>
              Update Forms
            </div>
            
          </NavLink>
        </li>
        <li>
          <NavLink 
            to="/reports"
            className={({ isActive }) => 
              isActive ? `${styles.activeLink}` : undefined
            }
          >
            <img src={reportsIcon} width="25" height="25" />
            <div className={styles.navName}>
              Reports
            </div>
          </NavLink>
        </li>
        <li>
          <NavLink 
            to="/documents"
            className={({ isActive }) => 
              isActive ? `${styles.activeLink}` : undefined
            }
          >
            <img src={documentsIcon} width="25" height="25" />
            <div className={styles.navName}>
              Documents
            </div>
            
          </NavLink>
        </li>
        <li>
          <NavLink 
            to="/quicklinks"
            className={({ isActive }) => 
              isActive ? `${styles.activeLink}` : undefined
            }
          >
            <img src={quickLinksIcon} width="25" height="25" />
            <div className={styles.navName}>
              Quick Links
            </div>
          </NavLink>
        </li>
        <li>
          <NavLink 
            to="/admin"
            className={({ isActive }) => 
              isActive ? `${styles.activeLink}` : undefined
            }
          >
            <img src={adminIcon} width="25" height="25" />
            <div className={styles.navName}>
              Admin
            </div>
          </NavLink>
        </li>
      </ul>
    </aside>
  );
}
