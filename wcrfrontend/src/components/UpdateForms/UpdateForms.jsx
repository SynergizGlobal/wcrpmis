import React, { useEffect, useState, useRef } from "react";
import api from "../../api/axiosInstance";
import styles from "./UpdateForms.module.css";
import { API_BASE_URL } from "../../config";
import { ChevronRight } from "lucide-react";
import { Outlet, useNavigate, useLocation } from "react-router-dom"; 

// Import icons
import projectIcon from "../../assets/images/icons/uf-projects.svg";
import worksIcon from "../../assets/images/icons/uf-works.svg";
import contractsIcon from "../../assets/images/icons/uf-contratc_tenders.svg";
import monitoringIcon from "../../assets/images/icons/uf-execution_monitoring.svg";
import finaceIcon from "../../assets/images/icons/uf-finance.svg";
import designIcon from "../../assets/images/icons/uf-design_drawings.svg";
import issuesIcon from "../../assets/images/icons/uf-issues.svg";
import laIcon from "../../assets/images/icons/uf-la.svg";
import safetyIcon from "../../assets/images/icons/uf-safety.svg";
import trainingIcon from "../../assets/images/icons/uf-training.svg";
import riskIcon from "../../assets/images/icons/uf-risk.svg";
import validateDataIcon from "../../assets/images/icons/uf-validate_data.svg";
import utilityShiftingIcon from "../../assets/images/icons/uf-utility_shifting.svg";
import fortnightPlanIcon from "../../assets/images/icons/uf-fortnight_plan.svg";
import alertsIcon from "../../assets/images/icons/uf-alerts.svg";
import gemPaymentStatusIcon from "../../assets/images/icons/uf-gem_payment_status.svg";
import dmsIcon from "../../assets/images/icons/dms.svg";

export default function UpdateForms() {
  const [forms, setForms] = useState([]);
  const [openMenu, setOpenMenu] = useState(null);
  const menuRef = useRef(null);
  const navigate = useNavigate();
  const location = useLocation();

  // Fetch forms
  useEffect(() => {
    fetchForms();
  }, []);

  const fetchForms = async () => {
    try {
      const res = await api.get(`${API_BASE_URL}/forms/api/getUpdateForms`, {
        withCredentials: true,
      });
      setForms(res.data);
    } catch (err) {
      console.error("Error fetching forms:", err);
    }
  };

  const toggleMenu = (id) => {
    setOpenMenu(openMenu === id ? null : id);
  };

  //Close submenu when clicked outside
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (menuRef.current && !menuRef.current.contains(event.target)) {
        setOpenMenu(null);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  // Map module names to icons
  const getIconForModule = (name) => {
    const lower = name.toLowerCase();
    if (lower.includes("projects")) return projectIcon;
    if (lower.includes("work")) return worksIcon;
    if (lower.includes("contract")) return contractsIcon;
    if (lower.includes("execution") || lower.includes("monitoring"))
      return monitoringIcon;
    if (lower.includes("finance")) return finaceIcon;
    if (lower.includes("design")) return designIcon;
    if (lower.includes("issues")) return issuesIcon;
    if (lower.includes("dms")) return dmsIcon;
    if (lower.includes("land acquisition")) return laIcon;
    if (lower.includes("safety")) return safetyIcon;
    if (lower.includes("training")) return trainingIcon;
    if (lower.includes("risk")) return riskIcon;
    if (lower.includes("validate data")) return validateDataIcon;
    if (lower.includes("utility shifting")) return utilityShiftingIcon;
    if (lower.includes("fortnight plan")) return fortnightPlanIcon;
    if (lower.includes("alert")) return alertsIcon;
    if (lower.includes("gem payment")) return gemPaymentStatusIcon;
    return null;
  };

  //Handle route navigation without reload
const handleNavigation = async(formName, webFormUrl, hasSubMenu = false) => {
    const lower = formName.toLowerCase();

  // ✅ Special handling for DMS
  if (lower.includes("dms")) {
    try {
      // Call backend DMS API
      const res = await api.get(`${API_BASE_URL}/dms/dms`, {
        withCredentials: true,
      });

      // Expect backend to return the redirect link as plain text or JSON
      const redirectUrl =
        typeof res.data === "string"
          ? res.data
          : res.data?.redirectUrl || res.data?.url;

      if (redirectUrl) {
        // Open in new tab
        window.open(redirectUrl, "_blank", "noopener,noreferrer");
      } else {
        alert("⚠️ DMS link not found in response");
      }
    } catch (err) {
      console.error("❌ Error opening DMS:", err);
      alert("Failed to open DMS. Please try again.");
    }
    return;
  }

  if (webFormUrl && webFormUrl.trim() !== "") {
    navigate(`/updateforms/${webFormUrl}`);
  } else {
    const path = formName.toLowerCase().replace(/\s+/g, "-");
    navigate(`/updateforms/${path}`);
  }
};
  //Render submenus
  const renderSubMenu = (menu) => (
    <div className={styles.subDropdown} ref={menuRef}>
      {menu.map((sub) => (
        <div key={sub.formId} className={styles.menuItem}>
          {sub.formsSubMenuLevel2?.length > 0 ? (
            <>
              <div
                className={styles.menuHeader}
                onClick={() => toggleMenu(sub.formId)}
              >
                <span>{sub.formName}</span>
                <ChevronRight
                  size={16}
                  className={`${styles.arrow} ${
                    openMenu === sub.formId ? styles.arrowOpen : ""
                  }`}
                />
              </div>
              {openMenu === sub.formId && renderSubMenu(sub.formsSubMenuLevel2)}
            </>
          ) : (
            <a
              onClick={() => handleNavigation(sub.formName, sub.webFormUrl)}
              className={styles.link}
            >
              {sub.formName}
            </a>
          )}
        </div>
      ))}
    </div>
  );

  // Hide main menu when on subpage 
  const isSubRoute =
  !location.pathname.endsWith("/updateforms") &&
  !location.pathname.endsWith("/updateforms/");

  return (
    <div
      className={`${styles.updateFormsContainer} ${
        isSubRoute ? styles.noStyle : ""
      }`}
      ref={menuRef}
    >
      {!isSubRoute && (
        <div className={styles.formsGrid}>
          {forms.map((form) => (
            <div
                key={form.formId}
                className={styles.card}
                onClick={() =>
                  form.formsSubMenu?.length
                    ? toggleMenu(form.formId)
                    : handleNavigation(form.formName, form.webFormUrl)
                }
              >
              <img
                src={getIconForModule(form.formName)}
                alt="icon"
                className={styles.icon}
              />
              <div className={styles.cardHeader}>
                <span>{form.formName}</span>
                {form.formsSubMenu?.length > 0 && (
                  <ChevronRight
                    size={16}
                    className={`${styles.arrow} ${
                      openMenu === form.formId ? styles.arrowOpen : ""
                    }`}
                  />
                )}
              </div>
              {openMenu === form.formId &&
                renderSubMenu(form.formsSubMenu || [])}
            </div>
          ))}
        </div>
      )}

      {/* Loads child component */}
      <Outlet />
    </div>
  );
}
