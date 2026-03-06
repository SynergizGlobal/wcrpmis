import React, { useEffect, useRef, useState } from "react";
import { Link, Outlet, useNavigate, useLocation } from "react-router-dom";
import styles from "./Reports.module.css";
import { 
  ChevronRight, 
  FileText, 
  BarChart2, 
  TrendingUp, 
  AlertCircle, 
  Map, 
  Wrench 
} from "lucide-react";
import api from "../../api/axiosInstance";
import { API_BASE_URL } from "../../config";
import axios from "axios";

export default function Reports() {
  const menuRef = useRef(null);
  const navigate = useNavigate();
  const location = useLocation();
  const issuesRef = useRef(null);
  const contractsRef = useRef(null);
  const progressReportRef = useRef(null);
  const [openMenu, setOpenMenu] = useState(null);

  const [openIssues, setOpenIssues] = useState(false);
  const [openContracts, setOpenContracts] = useState(false);
  const [openProgressReport, setOpenProgressReport] = useState(false);
  const [openLandAcquisition, setOpenLandAcquisition] = useState(false);

  const isSubRoute =
    !location.pathname.endsWith("/reports") &&
    !location.pathname.endsWith("/reports/");

  const handleIssuesClick = () => {
    setOpenIssues((prev) => !prev);
  };

  const handleContractsClick = () => {
    setOpenContracts((prev) => !prev);
  };

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (issuesRef.current && !issuesRef.current.contains(event.target)) {
        setOpenIssues(false);
      }
      if (contractsRef.current && !contractsRef.current.contains(event.target)) {
        setOpenContracts(false);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  const goTo = (path) => {
    setOpenIssues(false);
    setOpenContracts(false);
    setOpenLandAcquisition(false);
    navigate(path);
  };

  const handleTPCClick = async () => {
    setOpenProgressReport(false);
    try {
      const res = await axios.post(
        `${API_BASE_URL}/tpc-status-report`,
        {},
        {
          responseType: "blob",
          withCredentials: true,
          headers: { "Content-Type": "application/json" },
        }
      );
      const disposition = res.headers["content-disposition"];
      let fileName = "TPC-Report.docx";
      if (disposition) {
        const match = disposition.match(/filename="(.+)"/);
        if (match) fileName = match[1];
      }
      const blob = new Blob([res.data]);
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement("a");
      a.href = url;
      a.download = fileName;
      a.click();
      window.URL.revokeObjectURL(url);
      setOpenProgressReport(false);
    } catch (err) {
      console.error("TPC report download failed", err);
    }
  };

  const handleSIPROnClick = async () => {
    setOpenProgressReport(false);
    try {
      const res = await axios.post(
        `${API_BASE_URL}/station-improvements-report`,
        {},
        { responseType: "blob", withCredentials: true }
      );
      const disposition = res.headers["content-disposition"];
      let fileName = "Station-Improvements.docx";
      if (disposition) {
        const match = disposition.match(/filename="(.+)"/);
        if (match && match[1]) fileName = match[1];
      }
      const blob = new Blob([res.data]);
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement("a");
      a.href = url;
      a.download = fileName;
      document.body.appendChild(a);
      a.click();
      a.remove();
      window.URL.revokeObjectURL(url);
      setOpenProgressReport(false);
    } catch (err) {
      console.error("Report download failed", err);
    }
  };

  const handleIssuesSummaryDownload = async () => {
    try {
      const response = await api.post(
        `${API_BASE_URL}/api/issues-report/generate-issues-summary-report`,
        {},
        { responseType: "blob", withCredentials: true }
      );
      const blob = new Blob([response.data], {
        type: "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
      });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = url;
      link.download = "Issues_Summary_Report.docx";
      document.body.appendChild(link);
      link.click();
      link.remove();
    } catch (error) {
      console.error("Error downloading summary report", error);
    }
  };

  const handleContractorsDownload = async () => {
    try {
      const res = await api.post(
        "/contract-report/contractorslist",
        {},
        { responseType: "blob" }
      );
      const url = window.URL.createObjectURL(new Blob([res.data]));
      const a = document.createElement("a");
      a.href = url;
      a.download = "List_of_Contractors_Report.docx";
      document.body.appendChild(a);
      a.click();
      a.remove();
      window.URL.revokeObjectURL(url);
    } catch (err) {
      console.error("Contractors report download failed", err);
    }
  };

  return (
    <div
      className={`${styles.adminsContainer} ${isSubRoute ? styles.noStyle : ""}`}
      ref={menuRef}
    >
      {!isSubRoute && (
        <div className={styles.adminMenuListInner}>
          <div className={styles.adminMenuList}>

            {/* ── CONTRACTS ── */}
            <div className={styles.card} ref={contractsRef}>
              <div
                className={styles.cardHeader}
                onClick={() => setOpenContracts((prev) => !prev)}
              >
                <FileText size={18} className={styles.cardIcon} />
                <span>Contracts</span>
                <ChevronRight
                  size={16}
                  className={`${styles.arrow} ${openContracts ? styles.arrowOpen : ""}`}
                />
              </div>
              {openContracts && (
                <div className={styles.subDropdown}>
                  <div className={styles.menuItem}>
                    <Link to="/reports/contracts/2" onClick={() => setOpenContracts(false)}>
                      Contract Detail
                    </Link>
                  </div>
                  <div className={styles.menuItem}>
                    <Link
                      to="#"
                      onClick={(e) => {
                        e.preventDefault();
                        setOpenContracts(false);
                        handleContractorsDownload();
                      }}
                    >
                      List of Contractors
                    </Link>
                  </div>
                  <div className={styles.menuItem}>
                    <Link to="/reports/contracts/7" onClick={() => setOpenContracts(false)}>
                      List of Contracts
                    </Link>
                  </div>
                  <div className={styles.menuItem}>
                    <Link to="/reports/contracts/8" onClick={() => setOpenContracts(false)}>
                      BG/Insurance Report
                    </Link>
                  </div>
                  <div className={styles.menuItem}>
                    <Link to="/reports/contracts/9" onClick={() => setOpenContracts(false)}>
                      Date of Completion Report
                    </Link>
                  </div>
                  <div className={styles.menuItem}>
                    <Link to="/bg-contractual-letters" onClick={() => setOpenContracts(false)}>
                      BG Contractual Letters
                    </Link>
                  </div>
                  <div className={styles.menuItem}>
                    <Link to="/insurance-contractual-letters" onClick={() => setOpenContracts(false)}>
                      Insurance Contractual Letters
                    </Link>
                  </div>
                  <div className={styles.menuItem}>
                    <Link to="/doc-contractual-letters" onClick={() => setOpenContracts(false)}>
                      Date of Completion Letters
                    </Link>
                  </div>
                </div>
              )}
            </div>

            {/* ── CONTRACT-WISE ACTIVITIES ── */}
            <div className={styles.card}>
              <div
                className={styles.cardHeader}
                onClick={() => goTo("/activitiesexportreport")}
              >
                <BarChart2 size={18} className={styles.cardIcon} />
                <span>Contract-wise Activities</span>
              </div>
            </div>

            {/* ── PROGRESS REPORT ── */}
            <div className={styles.card} ref={progressReportRef}>
              <div
                className={styles.cardHeader}
                onClick={() => setOpenProgressReport((prev) => !prev)}
              >
                <TrendingUp size={18} className={styles.cardIcon} />
                <span>Progress Report</span>
                <ChevronRight
                  size={16}
                  className={`${styles.arrow} ${openProgressReport ? styles.arrowOpen : ""}`}
                />
              </div>
              {openProgressReport && (
                <div className={styles.progSubDropdown}>
                  <span className={styles.progMenuLink} onClick={handleTPCClick}>
                    TCP
                  </span>
                  <Link
                    to="/reports/network-expansion-works"
                    className={styles.progMenuLink}
                    onClick={() => setOpenProgressReport(false)}
                  >
                    Network Expansion Works
                  </Link>
                  <span className={styles.progMenuLink} onClick={handleSIPROnClick}>
                    Station Improvement Progress Report
                  </span>
                </div>
              )}
            </div>

            {/* ── ISSUES ── */}
            <div className={styles.card} ref={issuesRef}>
              <div
                className={styles.cardHeader}
                onClick={() => setOpenIssues((prev) => !prev)}
              >
                <AlertCircle size={18} className={styles.cardIcon} />
                <span>Issues</span>
                <ChevronRight
                  size={16}
                  className={`${styles.arrow} ${openIssues ? styles.arrowOpen : ""}`}
                />
              </div>
              {openIssues && (
                <div className={styles.subDropdown}>
                  <div className={styles.menuItem}>
                    <Link to="/pendingissuesreport" onClick={() => setOpenIssues(false)}>
                      Pending Issues Report
                    </Link>
                  </div>
                  <div className={styles.menuItem}>
                    <Link
                      to="#"
                      onClick={(e) => {
                        e.preventDefault();
                        handleIssuesSummaryDownload();
                        setOpenIssues(false);
                      }}
                    >
                      Issues Summary Report
                    </Link>
                  </div>
                  <div className={styles.menuItem}>
                    <Link to="/issuesdetailsreport" onClick={() => setOpenIssues(false)}>
                      Issues Details Report
                    </Link>
                  </div>
                </div>
              )}
            </div>

            {/* ── LAND ACQUISITION ── */}
            <div className={styles.card}>
              <div
                className={styles.cardHeader}
                onClick={() => goTo("/landacquisitionreport")}
              >
                <Map size={18} className={styles.cardIcon} />
                <span>Land Acquisition</span>
              </div>
            </div>

            {/* ── UTILITY SHIFTING ── */}
            <div className={styles.card}>
              <div className={styles.cardHeader}>
                <Wrench size={18} className={styles.cardIcon} />
                <Link to="/utility-report">Utility Shifting</Link>
              </div>
            </div>

          </div>
        </div>
      )}
      <Outlet />
    </div>
  );
}