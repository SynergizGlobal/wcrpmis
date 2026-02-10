import React, { useEffect, useRef, useState } from "react";
import { Link, Outlet, useNavigate, useLocation } from "react-router-dom";
import styles from "./Reports.module.css";
import { ChevronRight } from "lucide-react";

export default function Reports() {
  const menuRef = useRef(null);
  const navigate = useNavigate();
  const location = useLocation();
  const issuesRef = useRef(null);
  const contractsRef = useRef(null);
  const [openMenu, setOpenMenu] = useState(null);
  
  const [openIssues, setOpenIssues] = useState(false);
  const [openContracts, setOpenContracts] = useState(false);

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
      if (
        issuesRef.current &&
        !issuesRef.current.contains(event.target)
      ) {
        setOpenIssues(false);
      }
      
      if (
        contractsRef.current &&
        !contractsRef.current.contains(event.target)
      ) {
        setOpenContracts(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () =>
      document.removeEventListener("mousedown", handleClickOutside);
  }, []);  

  const goTo = (path) => {
    setOpenIssues(false);
    setOpenContracts(false);
    navigate(path);
  };

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
            {/* ✅ CONTRACTS — Dropdown card */}
            <div className={styles.card} ref={contractsRef}>
              <div className={styles.cardHeader} onClick={() => setOpenContracts((prev) => !prev)}>
                <span>Contracts</span>
                <ChevronRight
                  size={16}
                  className={`${styles.arrow} ${
                    openContracts ? styles.arrowOpen : ""
                  }`}
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
                    <Link to="/contractorslist" onClick={() => setOpenContracts(false)}>
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
                    <Link to="/reports/contracts/9"  onClick={() => setOpenContracts(false)}>
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
            
            <div className={styles.card}>  
              <div className={styles.cardHeader}>
                <Link to="contract-wise-activities">Contract-wise Activities</Link>
              </div>
            </div>
            
            <div className={styles.card}>
              <div className={styles.cardHeader}>
                <Link to="Progress-report">Progress Report</Link>
              </div>
            </div>

            {/* ✅ ISSUES — UpdateForms-style card */}
            <div className={styles.card} ref={issuesRef}>
              <div className={styles.cardHeader} onClick={() => setOpenIssues((prev) => !prev)}>
                <span>Issues</span>
                <ChevronRight
                  size={16}
                  className={`${styles.arrow} ${
                    openIssues ? styles.arrowOpen : ""
                  }`}
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
                    <Link to="/issuessummaryreport" onClick={() => setOpenIssues(false)}>
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

            <div className={styles.card}>
              <div className={styles.cardHeader}>
                <Link to="land-acquisition">Land Acquisition</Link>
              </div>
            </div>
            
            <div className={styles.card}>
              <div className={styles.cardHeader}>
                <Link to="utility-shifting">Utility Shifting</Link>
              </div>
            </div>
          </div>
        </div>
      )}

      <Outlet />
    </div>
  );
}