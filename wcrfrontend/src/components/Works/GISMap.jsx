import React, { useState, useEffect, useRef } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { ChevronDown } from "react-feather";
import styles from "./WorkOverviewDashboard.module.css";
import { usePageTitle } from "../../context/PageTitleContext";
import { API_BASE_URL } from "../../config";

export default function GISMap() {
  const location = useLocation();
  const navigate = useNavigate();
  const menuRef = useRef(null);
  const [iframeSrc, setIframeSrc] = useState("");

  const searchParams = new URLSearchParams(location.search);
  const projectId = searchParams.get("project_id");
  const contractId = searchParams.get("contract_id");
  const structureType = searchParams.get("structure_type");
  const from = searchParams.get("from");
  const to = searchParams.get("to");
  
  

  const { setPageTitle } = usePageTitle();

  const [openMenu, setOpenMenu] = useState(null);
  const [activeItem, setActiveItem] = useState("Contract Overview");

  const [projectName, setProjectName] = useState("");
  const [contractName, setContractName] = useState("");

  // -------- FETCH PROJECT + CONTRACT NAMES ----------
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

  // -------- MENU CONFIG (REACT ROUTES) ----------
  const menuItems = [
    {
      label: "Execution Overview",
      path: "execution-overview-dashboard",
      urllink:
        "/views/Civilworks3/Civilworks?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link&:toolbar=no&:tabs=no",
    },
    {
      label: "Contract Overview",
      path: "contract-overview-dashboard",
      urllink:
        "/views/Contractoverview_17665486646740/ContractOverview?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link&:toolbar=no&:tabs=no",
    },
    {
      label: "Progress Table",
	  path: "progress-table-dashboard",
      urllink:
        "/views/ProgressTable_17667286424510/Structure?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link&:toolbar=no&:tabs=no",
    },
    {
      label: "Timeline Schedule",
      path: "timeline-schedule-dashboard",
      urllink:
        "/views/Timelineshedule-WORKS_17667291338900/Contractleveldash?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link&:toolbar=no&:tabs=no",
    },
    {
      label: "Issues",
      path: "issues-overview-dashboard",
      urllink:
        "/views/issue_overview_17665490518840/Issuesoverview?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link&:toolbar=no&:tabs=no",
    },
    {
      label: "Monthwise Progress",
      path: "monthwise-progress-dashboard",
      urllink:
        "/views/MonthwiseProgress_17665495233480/Dashboard1?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link&:toolbar=no&:tabs=no",
    },
    {
      label: "Monthwise Plan",
      path: "monthwise-plan-dashboard",
      urllink:
        "/views/ProjectWeightage/Dashboard1?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link&:toolbar=no&:tabs=no",
    },
    {
      label: "Daily Progress",
      path: "daily-progress-dashboard",
      urllink:
        "/views/DailyProgress_17665488107090/DailyProgressoverview?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link&:toolbar=no&:tabs=no",
    },
    {
      label: "GIS Map",
      path: "gis-map-dashboard",
      urllink:
        "/views/Execution_Stripchart_17665488941740/EngineeringStripChart-wcr?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link&:toolbar=no&:tabs=no",
    },
    {
      label: "Site Photos",
      path: "site-photos-dashboard",
      urllink: "", // no Tableau link provided
    },
  ];



  // -------- NAVIGATION HANDLER ----------
  const navigateToPage = async (path, label,urllink) => {
    setActiveItem(label);

    navigate(
      `../${path}?project_id=${projectId}&contract_id=${contractId}` +
        (structureType ? `&structure_type=${structureType}` : "") +
        (from ? `&from=${from}` : "") +
        (to ? `&to=${to}` : "")
    );
	const url = await fetchTrustedTicketUrl(path,urllink);
	setIframeSrc(url);	
  };
      const fetchTrustedTicketUrl = async (path,urllink) => {
    try {
      const res = await fetch(`${API_BASE_URL}/tableau/ticket`, {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        body: new URLSearchParams({
          username: "SynTrack",
          client_ip: "10.48.192.7",
        }),
      });

      const ticket = await res.text();

      const params = [];
      if (projectId) params.push(`projectId=${projectId}`);
      if (contractId) params.push(`contract_id=${contractId}`);
      if (structureType) params.push(`structure_type=${structureType}`);
      if (from) params.push(`from=${from}`);
      if (to) params.push(`to=${to}`);

      const joiner = urllink.includes("?") ? "&" : "?";
      const queryParams = params.length ? joiner + params.join("&") : "";

      return `http://115.124.125.227:8000/trusted/${ticket}/${urllink}${queryParams}`;
    } catch (err) {
      console.error("Error fetching trusted ticket:", err);
      return "";
    }
  };
  // -------- LOAD DEFAULT PAGE ----------
  useEffect(() => {
    navigateToPage(menuItems[8].path, menuItems[8].label,menuItems[8].urllink);
    // eslint-disable-next-line
  }, []);

  return (
    <>
      {/* ------------ MENU -------------- */}
      <nav className={styles.dashboardMenu} ref={menuRef}>
        <ul className={styles.menuList}>
          {menuItems.map((item) => (
            <li
              key={item.label}
              className={`${styles.menuItem} ${
                activeItem === item.label ? styles.active : ""
              }`}
              onClick={() => navigateToPage(item.path, item.label,item.urllink)}
            >
              <div className={styles.menuLabel}>
                <span>{item.label}</span>
                <ChevronDown size={14} style={{ opacity: 0 }} />
              </div>
            </li>
          ))}
        </ul>
      </nav>

      {/* ------------ CONTENT -------------- */}
      <div className={styles.container}>
        <div style={{ marginTop: "20px", height: "80vh", position: "relative" }}>
          
          {/* ------------ BACK BUTTON -------------- */}
          <button
            onClick={() => navigate(`/Works?project_id=${projectId}`)}
            style={{
              position: "absolute",
              top: "0",
              right: "10px",
              zIndex: 9999,
              padding: "6px 12px",
              borderRadius: "4px",
              border: "none",
              background: "#007bff",
              color: "#fff",
              cursor: "pointer",
            }}
          >
            Back
          </button>

		  {/* IFRAME */}
		  {iframeSrc && (
		    <iframe
		      title="GIS Map"
		      src={`${iframeSrc}`}
		      style={{
		        marginTop: "25px",
		        width: "100%",
		        height: "100%",
		        borderRadius: "10px",
		      }}
		      frameBorder="0"
		      allowFullScreen
		    />
		  )}
        </div>
      </div>
    </>
  );
}
