import { Outlet, NavLink } from "react-router-dom";
import styles from "./Dms.module.css";

export default function DMS() {
  return (
    <div className={styles.container}>
      <div className="pageHeading">
        <h2>Document Management System</h2>
        <div className="rightBtns">
          &nbsp;
        </div>
      </div>

      <div className="innerPage">
      {/* Tabs */}
      <div className={styles.topNav}>
        <NavLink to="correspondence" className={({ isActive }) => isActive ? styles.active : ""}>
          Correspondence
        </NavLink>
        <NavLink to="documents" className={({ isActive }) => isActive ? styles.active : ""}>
          Documents
        </NavLink>
        <NavLink to="folders" className={({ isActive }) => isActive ? styles.active : ""}>
          Folders
        </NavLink>
        <NavLink to="filterform" className={({ isActive }) => isActive ? styles.active : ""}>
          Filter Form
        </NavLink>
      </div>

      {/* Child pages load here */}
      <Outlet />
      </div>
    </div>
  );
}
