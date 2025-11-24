import React, { useEffect, useState, useRef } from "react";
import { Link } from "react-router-dom";
import styles from './Admin.module.css';
import { Outlet, useNavigate, useLocation } from "react-router-dom"; 

export default function Admin() {

    const menuRef = useRef(null);
    const navigate = useNavigate();
    const location = useLocation();

   const isSubRoute =
  !location.pathname.endsWith("/admin") &&
  !location.pathname.endsWith("/admin/");


  return (
        <div
          className={`${styles.adminsContainer} ${
            isSubRoute ? styles.noStyle : ""
          }`}
          ref={menuRef}
        >
    {!isSubRoute && (
      <div className={styles.adminMenuListInner}>
        <div className={styles.adminMenuList}>
          <div className={styles.menuItem}><Link to="left-menu">Overview Dashboards</Link></div>
          <div className={styles.menuItem}><Link to="users">Users</Link></div>
          <div className={styles.menuItem}><Link to="access-dashboards">Dashboards</Link></div>
          <div className={styles.menuItem}><Link to="access-forms">Forms</Link></div>
          <div className={styles.menuItem}><Link to="access-reports">Reports</Link></div>
          <div className={styles.menuItem}><Link to="web-links">Web Links</Link></div>
          <div className={styles.menuItem}><Link to="template-upload">Template Upload</Link></div>
          <div className={styles.menuItem}><Link to="user-manuals">WR PMIS Manuals</Link></div>
          <div className={styles.menuItem}><Link to="reference-forms">Reference Forms</Link></div>
          <div className={styles.menuItem}><Link to="delete-activities">Delete Activities</Link></div>
        </div>
      </div>
    )}
      <Outlet />
    </div>
  );
}