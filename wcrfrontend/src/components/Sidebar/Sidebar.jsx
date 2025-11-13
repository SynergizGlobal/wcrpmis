import React, { useState } from "react";
import { NavLink } from "react-router-dom";
import styles from "./Sidebar.module.css";

import homeIcon from "../../assets/images/icons/home.svg";
import modulesIcon from "../../assets/images/icons/modules.svg";
import worksIcon from "../../assets/images/icons/works.svg";
import updateFormsIcon from "../../assets/images/icons/updateForms.svg";
import reportsIcon from "../../assets/images/icons/reports.svg";
import documentsIcon from "../../assets/images/icons/documents.svg";
import quickLinksIcon from "../../assets/images/icons/quickLinks.svg";
import adminIcon from "../../assets/images/icons/admin.svg";

export default function Sidebar({ sidebarOpen, setSidebarOpen, sidebarRef }) {

  const [modulesOpen, setModulesOpen] = useState(false);
  const [worksOpen, setWorksOpen] = useState(false); 
  const [newBGOpen, setNewBGOpen] = useState(false);


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

        {/* Modules Dropdown */}
        <li>
          <div 
            className={`dropdownHeader ${styles.dropdownHeader}`}
            onClick={() => setModulesOpen(!modulesOpen)}
          >
            <img src={modulesIcon} width="25" height="25" />
            <div className={styles.navName}>Modules</div>
            <span className={styles.arrow}>{modulesOpen ? "▲" : "▼"}</span>
            </div>

          {modulesOpen && (
            <ul className={`submenu ${styles.submenu}`}>
              <li><NavLink to="/modules/fortnight-meeting">Fortnight Meeting</NavLink></li>
              <li><NavLink to="/modules/drawing-status">Drawing Status</NavLink></li>
              <li><NavLink to="/modules/project-performance">Project Performance Appraisal</NavLink></li>
              <li><NavLink to="/modules/timeline-schedule">Timeline Schedule Module</NavLink></li>
              <li><NavLink to="/modules/bg-insurance-dashboard">BG & Insurance Dashboard</NavLink></li>
              <li><NavLink to="/modules/usage-analysis-dashboard">Usage Analysis Dashboard</NavLink></li>
              <li><NavLink to="/modules/contractwise-physical-progress">Contractwise Physical Progress</NavLink></li>
              <li><NavLink to="/modules/componentwise-progress">Component Wise Progress</NavLink></li>
              <li><NavLink to="/modules/progress-table">Progress Table</NavLink></li>
              <li><NavLink to="/modules/contract-status">Contract Status</NavLink></li>
              <li><NavLink to="/modules/issues">Issues</NavLink></li>
            </ul>
          )}
        </li>

		{/* Works Dropdown */}
        <li>
		  <div 
		    className={styles.dropdownHeader}
		    onClick={() => setWorksOpen(!worksOpen)}
          >
            <img src={worksIcon} width="25" height="25" />
		    <div className={styles.navName}>Works</div>
		    <span className={styles.arrow}>{worksOpen ? "▲" : "▼"}</span>
		  </div>

		  {worksOpen && (
		    <ul className={styles.submenu}>
		      {/* First-level submenu */}
		      <li>
		        <div 
		          className={styles.dropdownHeader}
		          onClick={(e) => {
		            e.stopPropagation(); // prevent parent toggle
		            setNewBGOpen(!newBGOpen);
		          }}
		        >
		          New BG Line
		          <span className={styles.arrow}>{newBGOpen ? "▲" : "▼"}</span>
            </div>
            
		        {newBGOpen && (
		          <ul className={styles.submenu}>
		            <li>
		              <NavLink to="Works?project_id=P01">
		                Ramganjmandi – Bhopal New BG Rail Line Project
		              </NavLink>
		            </li>
		            <li>
		              <NavLink to="Works?project_id=P02">
		                Lalitpur-Singrauli New BG Rail Line Project
          </NavLink>
        </li>
		          </ul>
		        )}
		      </li>
		    </ul>
		  )}
		</li>


        {/* Update Forms */}
        <li>
          <NavLink 
            to="/updateforms"
            className={({ isActive }) => isActive ? `${styles.activeLink}` : undefined}
          >
            <img src={updateFormsIcon} width="25" height="25" />
            <div className={styles.navName}>Update Forms</div>
          </NavLink>
        </li>

        {/* Reports */}
        <li>
          <NavLink 
            to="/reports"
            className={({ isActive }) => isActive ? `${styles.activeLink}` : undefined}
          >
            <img src={reportsIcon} width="25" height="25" />
            <div className={styles.navName}>
              Reports
            </div>
          </NavLink>
        </li>

        {/* Documents */}
        <li>
          <NavLink 
            to="/documents"
            className={({ isActive }) => isActive ? `${styles.activeLink}` : undefined}
          >
            <img src={documentsIcon} width="25" height="25" />
            <div className={styles.navName}>Documents</div>
          </NavLink>
        </li>

        {/* Quick Links */}
        <li>
          <NavLink 
            to="/quicklinks"
            className={({ isActive }) => isActive ? `${styles.activeLink}` : undefined}
          >
            <img src={quickLinksIcon} width="25" height="25" />
            <div className={styles.navName}>Quick Links</div>
          </NavLink>
        </li>

        {/* Admin */}
        <li>
          <NavLink 
            to="/admin"
            className={({ isActive }) => isActive ? `${styles.activeLink}` : undefined}
          >
            <img src={adminIcon} width="25" height="25" />
            <div className={styles.navName}>Admin</div>
          </NavLink>
        </li>

      </ul>
    </aside>
  );
}
