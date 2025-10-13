import React, { useEffect, useState, useRef } from "react";
import axios from "axios";
import styles from "./UpdateForms.module.css";
import { API_BASE_URL } from "../../config";
import { ChevronRight } from "lucide-react";

import projectIcon from "../../assets/images/icons/uf-projects.svg";
import worksIcon from "../../assets/images/icons/uf-works.svg";
import contractsIcon from "../../assets/images/icons/uf-contratc_tenders.svg";
import monitoringIcon from "../../assets/images/icons/uf-execution_monitoring.svg";
import finaceIcon from "../../assets/images/icons/uf-finance.svg";
import designIcon from "../../assets/images/icons/uf-design_drawings.svg"
import issuesIcon from "../../assets/images/icons/uf-issues.svg"
import laIcon from "../../assets/images/icons/uf-la.svg"
import safetyIcon from "../../assets/images/icons/uf-safety.svg"
import trainingIcon from "../../assets/images/icons/uf-training.svg"
import riskIcon from "../../assets/images/icons/uf-risk.svg"
import validateDataIcon from "../../assets/images/icons/uf-validate_data.svg"
import utilityShiftingIcon from "../../assets/images/icons/uf-utility_shifting.svg"
import fortnightPlanIcon from "../../assets/images/icons/uf-fortnight_plan.svg"
import alertsIcon from "../../assets/images/icons/uf-alerts.svg"
import gemPaymentStatusIcon from "../../assets/images/icons/uf-gem_payment_status.svg"

export default function UpdateForms() {
  const [forms, setForms] = useState([]);
  const [openMenu, setOpenMenu] = useState(null);
  const menuRef = useRef(null);

  useEffect(() => {
    fetchForms();
  }, []);

  const fetchForms = async () => {
    try {
      const res = await axios.get(`${API_BASE_URL}/forms/api/getUpdateForms`,{
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

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (menuRef.current && !menuRef.current.contains(event.target)) {
        setOpenMenu(null);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

   const getIconForModule = (name) => {
    const lower = name.toLowerCase();
    if (lower.includes("projects")) return projectIcon;
    if (lower.includes("work")) return worksIcon;
    if (lower.includes("contract")) return contractsIcon;
    if (lower.includes("execution") || lower.includes("monitoring"))
      return monitoringIcon;
    // if (lower.includes("")) return finaceIcon;
    if (lower.includes("design & drawing")) return designIcon;
    if (lower.includes("issues")) return issuesIcon;
    if (lower.includes("land acquisition")) return laIcon;
    if (lower.includes("utility shifting")) return utilityShiftingIcon;
    if (lower.includes("fortnight plan")) return fortnightPlanIcon;
    if (lower.includes("validate data")) return validateDataIcon;
    if (lower.includes("gem payment status")) return gemPaymentStatusIcon;
    // if (lower.includes("")) return trainingIcon;
    // if (lower.includes("")) return riskIcon;
    // if (lower.includes("")) return alertsIcon;

  };




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
              {openMenu === sub.formId &&
                renderSubMenu(sub.formsSubMenuLevel2)}
            </>
          ) : (
            <a
              href={`/wcrpmis/${sub.webFormUrl}`}
              className={styles.link}
              target="_blank"
              rel="noopener noreferrer"
            >
              {sub.formName}
            </a>
          )}
        </div>
      ))}
    </div>
  );

  return (
    <div className={styles.updateFormsContainer} ref={menuRef}>

      <div className={styles.formsGrid}>
        {forms.map((form) => (
          <div key={form.formId} className={styles.card}>
            <img
                src={getIconForModule(form.formName)}
                alt="icon"
                className={styles.icon}
              />
            <div
              className={styles.cardHeader}
              onClick={() => toggleMenu(form.formId)}
            >
              {/* <div className={styles.iconPlaceholder}> */}
                {/* Optional: load module icons dynamically */}
              {/* </div> */}
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
    </div>
  );
}
