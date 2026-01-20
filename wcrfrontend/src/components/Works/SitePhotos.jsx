import React, { useState, useEffect, useRef } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { ChevronDown } from "react-feather";
import styles from "./WorkOverviewDashboard.module.css";
import { usePageTitle } from "../../context/PageTitleContext";
import { API_BASE_URL } from "../../config";

export default function SitePhotos() {
	const location = useLocation();
	const navigate = useNavigate();
	const menuRef = useRef(null);

	const searchParams = new URLSearchParams(location.search);
	const projectId = searchParams.get("project_id");
	const contractId = searchParams.get("contract_id");
	const structureType = searchParams.get("structure_type");
	const from = searchParams.get("from");
	const to = searchParams.get("to");

	const { setPageTitle } = usePageTitle();

	const [openMenu, setOpenMenu] = useState(null);
	const [activeItem, setActiveItem] = useState("Execution Overview");

	const [projectName, setProjectName] = useState("");
	const [contractName, setContractName] = useState("");
	
	useEffect(() => {
	  if (!projectId || !contractId) return;

	  fetch(
	    `${API_BASE_URL}/execution/project-contract-info?projectId=${projectId}&contractId=${contractId}`
	  )
	    .then((res) => res.json())
	    .then((data) => {
	      setProjectName(data.projectName || "");
	      setContractName(data.contractName || "");
	    })
	    .catch(console.error);
	}, [projectId, contractId]);

	useEffect(() => {
	  if (projectName && contractName) {
	    setPageTitle(`${projectName} - ${contractName}`);
	  }
	}, [projectName, contractName, setPageTitle]);	
	
	const menuItems = [
	  { label: "Execution Overview", path: "execution-overview-dashboard" },
	  { label: "Contract Overview", path: "contract-overview-dashboard" },
	  { label: "Progress Table", path: "progress-tatble" },
	  { label: "Timeline Schedule", path: "timeline-schedule-dashboard" },
	  { label: "Issues", path: "issues-overview-dashboard" },
	  { label: "Monthwise Progress", path: "monthwise-progress-dashboard" },
	  { label: "Monthwise Plan", path: "monthwise-plan-dashboard" },
	  { label: "Daily Progress", path: "daily-progress-dashboard" },
	  { label: "GIS Map", path: "gis-map-dashboard" },
	  { label: "Site Photos", path: "site-photos-dashboard" },
	];
	
	const navigateToPage = (path, label) => {
	  setActiveItem(label);

	  navigate(
	    `../${path}?project_id=${projectId}&contract_id=${contractId}` +
	      (structureType ? `&structure_type=${structureType}` : "") +
	      (from ? `&from=${from}` : "") +
	      (to ? `&to=${to}` : "")
	  );
	};
	

  return (
	<nav className={styles.dashboardMenu} ref={menuRef}>
	  <ul className={styles.menuList}>
	    {menuItems.map((item) => (
	      <li
	        key={item.label}
	        className={`${styles.menuItem} ${
	          activeItem === item.label ? styles.active : ""
	        }`}
	        onClick={() => navigateToPage(item.path, item.label)}
	      >
	        <div className={styles.menuLabel}>
	          <span>{item.label}</span>
	          <ChevronDown size={14} style={{ opacity: 0 }} />
	        </div>
	      </li>
	    ))}
	  </ul>
	</nav>
  );
}
