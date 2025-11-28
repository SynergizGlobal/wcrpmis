import React, { useState, useRef, useEffect } from "react";
import { useLocation } from "react-router-dom";
import { ChevronDown } from "react-feather";
import { API_BASE_URL } from "../../config";
import styles from "./WorkOverviewDashboard.module.css";
import { usePageTitle } from "../../context/PageTitleContext";
import { useNavigate } from "react-router-dom";

export default function WorkOverviewDashboard() {
  const location = useLocation();
  const menuRef = useRef(null);

  const searchParams = new URLSearchParams(location.search);
  const projectId = searchParams.get("project_id");
  const contractId = searchParams.get("contract_id");
  const structureType = searchParams.get("structure_type");
  const fromKm = searchParams.get("from");
  const toKm = searchParams.get("to");

  const { setPageTitle } = usePageTitle();
  const navigate = useNavigate();

  const [iframeSrc, setIframeSrc] = useState("");
  const [openMenu, setOpenMenu] = useState(null);
  const [activeItem, setActiveItem] = useState("Execution Strip Chart");

  // ---------------- NEW STATES FOR TITLE ----------------
  const [projectName, setProjectName] = useState("");
  const [contractName, setContractName] = useState("");

  // ---------------- FETCH PROJECT + CONTRACT NAMES ----------------
  useEffect(() => {
    if (!projectId || !contractId) return;

    const fetchNames = async () => {
      try {
        const res = await fetch(
          `${API_BASE_URL}/execution/project-contract-info?projectId=${projectId}&contractId=${contractId}`
        );
        const data = await res.json();

        setProjectName(data.projectName || "");
        setContractName(data.contractName || "");
      } catch (err) {
        console.error("Error fetching names:", err);
      }
    };

    fetchNames();
  }, [projectId, contractId]);

  // ---------------- COMBINE TITLE ----------------
  useEffect(() => {
    if (projectName && contractName) {
      setPageTitle(`${projectName} - ${contractName}`);
    }
  }, [projectName, contractName, setPageTitle]);

  const menuItems = [
    {
      label: "Execution Strip Chart",
      path: "/views/Execution_Stripchart7/EngineeringStripChart-wcr?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link&:toolbar=no&:tabs=no",
    },
    {
      label: "Contract Overview",
      path: "/views/Contractoverview/ContractOverview?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link&:toolbar=no&:tabs=no",
    },
    {
      label: "Progress Table",
      path: "/views/ProgressTable/Structure?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link&:toolbar=no&:tabs=no",
    },
    {
      label: "Timeline Schedule",
      path: "/views/Timelineshedule-WORKS/Contractleveldash?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link&:toolbar=no&:tabs=no",
    },
    {
      label: "Issues",
      path: "/views/issue_overview/Issuesoverview?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link&:toolbar=no&:tabs=no",
    },
    {
      label: "Monthwise Progress",
      path: "/views/MonthwiseProgress/Dashboard1?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link&:toolbar=no&:tabs=no",
    },
    {
      label: "Monthwise Plan",
      path: "/views/MonthwisePlanned/monthwiseplanned?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link&:toolbar=no&:tabs=no",
    },
    {
      label: "Daily Progress",
      path: "/views/DailyProgress_17636259286610/DailyProgressoverview?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link&:toolbar=no&:tabs=no",
    },
    {
      label: "GIS Map",
      path: "/views/GISMap/ContractGISMap?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link&:toolbar=no&:tabs=no",
    },
    {
      label: "Site Photos",
      path: "/views/SitePhotos/ContractSitePhotos?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link&:toolbar=no&:tabs=no",
    },
  ];

  const toggleSubMenu = (label) => {
    setOpenMenu(openMenu === label ? null : label);
  };

  const fetchTrustedTicketUrl = async (path) => {
    try {
      const res = await fetch(`${API_BASE_URL}/tableau/ticket`, {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        body: new URLSearchParams({
          username: "tableau",
          client_ip: "10.48.192.7",
        }),
      });

      const ticket = await res.text();

      const params = [];
      if (projectId) params.push(`projectId=${projectId}`);
      if (contractId) params.push(`contract_id=${contractId}`);
      if (structureType) params.push(`structure_type=${structureType}`);
      if (fromKm) params.push(`from=${fromKm}`);
      if (toKm) params.push(`to=${toKm}`);

      const joiner = path.includes("?") ? "&" : "?";
      const queryParams = params.length ? joiner + params.join("&") : "";

      return `http://115.124.125.227:8000/trusted/${ticket}/${path}${queryParams}`;
    } catch (err) {
      console.error("Error fetching trusted ticket:", err);
      return "";
    }
  };

  const handleItemClick = async (path, label) => {
    setActiveItem(label);
    const url = await fetchTrustedTicketUrl(path);
    setIframeSrc(url);
  };

  // ----------- LOAD FIRST MENU BY DEFAULT -----------
  useEffect(() => {
    handleItemClick(
      menuItems[0].path,
      menuItems[0].label
    );
  }, []);

  return (
    <>
      <nav className={styles.dashboardMenu} ref={menuRef}>
        <ul className={styles.menuList}>
          {menuItems.map((item) => {
            const hasSubmenu = !!item.subItems;
            const isOpen = openMenu === item.label;

            return (
              <li
                key={item.label}
                className={`${styles.menuItem} ${
                  activeItem === item.label ? styles.active : ""
                }`}
                onClick={() =>
                  hasSubmenu
                    ? toggleSubMenu(item.label)
                    : handleItemClick(item.path, item.label)
                }
              >
                <div className={styles.menuLabel}>
                  <span>{item.label}</span>
                  {hasSubmenu && (
                    <ChevronDown
                      className={`${styles.arrowIcon} ${
                        isOpen ? styles.arrowOpen : ""
                      }`}
                      size={16}
                    />
                  )}
                </div>

                {hasSubmenu && isOpen && (
                  <ul className={styles.subMenuList}>
                    {item.subItems.map((sub) => (
                      <li
                        key={sub.label}
                        className={styles.subMenuItem}
                        onClick={(e) => {
                          e.stopPropagation();
                          handleItemClick(sub.path, sub.label);
                          setOpenMenu(null);
                        }}
                      >
                        {sub.label}
                      </li>
                    ))}
                  </ul>
                )}
              </li>
            );
          })}
        </ul>
      </nav>

      <div className={styles.container}>
        <div style={{ marginTop: "20px", height: "80vh", position: "relative" }}>
          
          {/* ------------ BACK BUTTON FIXED -------------- */}
          <button
            onMouseDown={(e) => {
              e.preventDefault();
              navigate(`/works?project_id=${projectId}`);
            }}
            style={{
              position: "absolute",
              top: "0px",
              right: "10px",
              zIndex: 9999,
              padding: "6px 12px",
              borderRadius: "4px",
              border: "none",
              background: "#007bff",
              color: "#fff",
              cursor: "pointer"
            }}
          >
            Back
          </button>

          <iframe
            title="Work Overview Dashboard"
			src={`${iframeSrc}&:showAppBanner=false&:showShareOptions=false&:display_count=no&:showVizHome=false&:toolbar=no&:tabs=no`}
			style={{
			  marginTop: "25px",
			  width: "100%",
			  height: "100%",
			  borderRadius: "10px"
			}}
            frameBorder="0"
            allowFullScreen
          />
        </div>
      </div>
    </>
  );
}
