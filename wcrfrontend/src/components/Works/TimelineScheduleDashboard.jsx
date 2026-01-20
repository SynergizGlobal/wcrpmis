import React, { useState, useEffect, useRef } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { ChevronDown } from "react-feather";
import styles from "./WorkOverviewDashboard.module.css";
import { usePageTitle } from "../../context/PageTitleContext";
import { API_BASE_URL } from "../../config";

export default function TimelineSchedule() {
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
  
  const [contracts, setContracts] = useState([]);
  const [selectedContract, setSelectedContract] = useState(contractId || "");  
  
  useEffect(() => {
    if (!projectId) return;

    const fetchContracts = async () => {
      try {
        const res = await fetch(
          `${API_BASE_URL}/execution/contracts?projectId=${projectId}`
        );
        const data = await res.json();

        console.log("Contracts API response:", data);

        // ✅ Always normalize to array
        let contractsArray = [];

        if (Array.isArray(data)) {
          contractsArray = data;
        } else if (Array.isArray(data?.data)) {
          contractsArray = data.data;
        } else if (Array.isArray(data?.contracts)) {
          contractsArray = data.contracts;
        }

        setContracts(contractsArray);
      } catch (error) {
        console.error("Error fetching contracts:", error);
        setContracts([]); // fallback safety
      }
    };

    fetchContracts();
  }, [projectId]);



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
  
  
  const onContractChange = async (e) => {
    const newContractId = e.target.value;
    setSelectedContract(newContractId);

    navigate(
      `?project_id=${projectId}&contract_id=${newContractId}` +
        (structureType ? `&structure_type=${structureType}` : "") +
        (from ? `&from=${from}` : "") +
        (to ? `&to=${to}` : "")
    );

    const activeMenu = menuItems.find((m) => m.label === activeItem);
    if (activeMenu) {
      const url = await fetchTrustedTicketUrl(
        activeMenu.path,
        activeMenu.urllink,
        newContractId
      );
      setIframeSrc(url);
    }
  };




  // -------- NAVIGATION HANDLER ----------
  const navigateToPage = async (path, label,urllink) => {
    setActiveItem(label);

    navigate(
      `../${path}?project_id=${projectId}&contract_id=${contractId}` +
        (structureType ? `&structure_type=${structureType}` : "") +
        (from ? `&from=${from}` : "") +
        (to ? `&to=${to}` : "")
    );
	const url = await fetchTrustedTicketUrl(path, urllink, selectedContract);

	setIframeSrc(url);	
  };
      const fetchTrustedTicketUrl = async (path, urllink, contractOverride) => {
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
	  if (contractOverride || selectedContract)
	    params.push(`contract_id=${contractOverride || selectedContract}`);
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
    navigateToPage(menuItems[3].path, menuItems[3].label,menuItems[3].urllink);
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
		  
		  {/* ------------ CONTRACT FILTER -------------- */}
		  <div
		    style={{
		      position: "absolute",
		      top: "45px",
		      right: "10px",
		      zIndex: 9999,
		      background: "#fff",
		      padding: "6px",
		      borderRadius: "6px",
		      boxShadow: "0 2px 6px rgba(0,0,0,0.2)",
		    }}
		  >
		    <select
		      value={selectedContract}
		      onChange={onContractChange}
		      style={{
		        padding: "6px",
		        minWidth: "180px",
		        borderRadius: "4px",
		      }}
		    >
		      <option value="">Select Contract</option>
		      {contracts.map((c) => (
		        <option key={c.id} value={c.id}>
		          {c.name}
		        </option>
		      ))}
		    </select>
		  </div>
		  
		  


		  {/* IFRAME */}
		  {iframeSrc && (
		    <iframe
		      title="Timeline Schedule"
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
