import React, { useState, useEffect, useRef } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { ChevronDown } from "react-feather";
import { API_BASE_URL } from "../../config";
import styles from "./ProjectOverviewDashboard.module.css";
import { usePageTitle } from "../../context/PageTitleContext";

export default function ProjectOverviewDashboard({ contracts = [] }) {
  const location = useLocation();
  const navigate = useNavigate();
  const menuRef = useRef(null);

  const searchParams = new URLSearchParams(location.search);
  const projectId = searchParams.get("project_id");
  const [selectedContract, setSelectedContract] = useState("All Contracts");

  const { setPageTitle } = usePageTitle();
  const [iframeSrc, setIframeSrc] = useState("");
  const defaultMenu =
    new URLSearchParams(location.search).get("menu") ||
    "Execution Overview";
  const [activeItem, setActiveItem] = useState(defaultMenu);
  const [openMenu, setOpenMenu] = useState(null);
  const [projectName, setProjectName] = useState("");

  // ---------------- FETCH PROJECT NAME ----------------
  useEffect(() => {
    if (!projectId) return;

    const fetchProjectName = async () => {
      try {
        const res = await fetch(
          `${API_BASE_URL}/execution/project-info?projectId=${projectId}`
        );
        const data = await res.json();
        setProjectName(data.projectName || "");
      } catch (err) {
        console.error("PROJECT NAME ERROR", err);
      }
    };

    fetchProjectName();
  }, [projectId]);

  // ----------- SET PAGE TITLE ----------- 
  useEffect(() => {
    if (projectName) setPageTitle(projectName);
  }, [projectName]);

  const menuItems = [ { label: "Execution Overview", path: "/views/Execution_Stripchart7/EngineeringStripChart-wcr?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link" }, { label: "Timeline Schedule", path: "/views/Timelineshedule-WORKS/Contractleveldash?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link" }, { label: "Progress Chart", path: "/views/ProgressChart/StructureLevel?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link" }, { label: "Progress Table", path: "/views/ProgressTable/Structure?:showAppBanner=false&:origin=viz_share_link&:display_count=n&:showVizHome=n" }, { label: "Project Weightages", path: "/views/ProjectWeightage/Dashboard1?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link" }, { label: "Contracts Overview", path: "/views/Contractoverview/ContractOverview?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link", subItems: [ { label: "In-Progress", path: "/views/Contracts/in-progress" }, { label: "Completed", path: "/views/Contracts/completed" } ] }, { label: "Land Acquisition", path: "views/LandAcquisitionOverview/LAOverview?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link", subItems: [ { label: "Land Strip Chart", path: "/views/LandAcquisitionStripchart/LAStripchart?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link" }, { label: "Land Details", path: "/views/LADetailsDashboard/LA-Details?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link" } ] }, { label: "Issues", path: "/views/issue_overview/Issuesoverview?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link", subItems: [{ label: "Issue Pending", path: "/views/Issues/pending" }] }, { label: "Utility Shifting", path: "/views/UtilityShiftingoverview/UtilityShiftingOverviewDashboard?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link", subItems: [ { label: "Utility Strip Chart", path: "/views/UtilityShiftingoverview/UtilityShiftingOverviewDashboard?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link" }, { label: "Utility Shifting Details", path: "/views/UtilityShiftingDetails/Utilityshiftingdetails?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link" }, { label: "Utility Shifting Map", path: "/views/UtilityShifting/map" } ] } ];

  // ---------------- BUILD TRUSTED TABLEAU URL ----------------
  const buildTrustedUrl = async (tableauPath) => {
    try {
      const res = await fetch(`${API_BASE_URL}/tableau/ticket`, {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: new URLSearchParams({ username: "tableau", client_ip: "10.48.192.7" }),
      });

      const ticket = await res.text();
      let params = "&:showAppBanner=false&:toolbar=no&:tabs=no&:showVizHome=n&:display_count=n";
      if (projectId) params += `&projectId=${projectId}`;
      if (activeItem === "Progress Table" && selectedContract !== "All Contracts") {
        params += `&contract=${encodeURIComponent(selectedContract)}`;
      }

      return `http://115.124.125.227:8000/trusted/${ticket}${tableauPath}${params}`;
    } catch (err) {
      console.error("TICKET URL ERROR", err);
      return "";
    }
  };

  // ---------------- HANDLE MENU CLICK ----------------
  const handleItemClick = async (path, label) => {
    setActiveItem(label);

    const url = await buildTrustedUrl(path);
    setIframeSrc(url);

    setOpenMenu(null);
  };

  // -------- LOAD FIRST MENU BY DEFAULT --------
  useEffect(() => {
    const menuFromUrl = new URLSearchParams(location.search).get("menu");

    if (menuFromUrl) {
      const matched = menuItems.find((m) => m.label === menuFromUrl);
      if (matched) {
        handleItemClick(matched.path, matched.label);
        return;
      }
    }

    handleItemClick(menuItems[0].path, menuItems[0].label);
  }, [selectedContract]);

  // ---------------- UI ----------------
  return (
    <>
      <nav className={styles.dashboardMenu} ref={menuRef}>
        <ul className={styles.menuList}>
          {menuItems.map((item) => (
            <li
              key={item.label}
              className={`${styles.menuItem} ${activeItem === item.label ? styles.active : ""}`}
              onClick={() => {
                if (item.subItems) {
                  setOpenMenu(openMenu === item.label ? null : item.label);
                } else {
                  handleItemClick(item.path, item.label);
                }
              }}
            >
              <div className={styles.menuLabel}>
                {item.label}
                {item.subItems && (
                  <ChevronDown
                    className={`${styles.arrowIcon} ${openMenu === item.label ? styles.arrowOpen : ""}`}
                    size={14}
                  />
                )}
              </div>

              {item.subItems && openMenu === item.label && (
                <ul className={styles.subMenuList}>
                  {item.subItems.map((sub) => (
                    <li
                      key={sub.label}
                      className={styles.subMenuItem}
                      onClick={() => handleItemClick(sub.path, sub.label)}
                    >
                      {sub.label}
                    </li>
                  ))}
                </ul>
              )}
            </li>
          ))}
        </ul>
      </nav>

      {/* Contract Filter (only for Progress Table) */}
      {activeItem === "Progress Table" && contracts.length > 0 && (
        <div style={{ margin: "10px 0" }}>
          <label htmlFor="contractFilter">Select Contract: </label>
          <select
            id="contractFilter"
            value={selectedContract}
            onChange={(e) => setSelectedContract(e.target.value)}
            style={{ padding: "5px", borderRadius: "4px" }}
          >
            {contracts.map((c) => (
              <option key={c} value={c}>
                {c}
              </option>
            ))}
          </select>
        </div>
      )}

      <div className={styles.container}>
        <div style={{ height: "82vh", position: "relative" }}>
          <button
            onMouseDown={(e) => {
              e.preventDefault();
              navigate(`/works?project_id=${projectId}`);
            }}
            style={{
              position: "absolute",
              top: "0px",
              right: "15px",
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

          <iframe
            title="Project Overview"
            src={iframeSrc}
            style={{ marginTop: "25px", width: "100%", height: "100%", borderRadius: "10px" }}
            frameBorder="0"
            allowFullScreen
          />
        </div>
      </div>
    </>
  );
}
