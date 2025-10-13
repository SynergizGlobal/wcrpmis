import React, { useEffect, useState } from "react";
import axios from "axios";
import styles from "./UpdateForms.module.css";
import { API_BASE_URL } from "../../config";
import { ChevronRight } from "lucide-react";

export default function UpdateForms() {
  const [forms, setForms] = useState([]);
  const [openMenu, setOpenMenu] = useState(null);

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

  const renderSubMenu = (menu) => (
    <div className={styles.subDropdown}>
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
              href={`${API_BASE_URL}/${sub.webFormUrl}`}
              className={styles.link}
            >
              {sub.formName}
            </a>
          )}
        </div>
      ))}
    </div>
  );

  return (
    <div className={styles.updateFormsContainer}>
      <h2 className={styles.pageTitle}>UPDATE FORMS</h2>

      <div className={styles.formsGrid}>
        {forms.map((form) => (
          <div key={form.formId} className={styles.card}>
            <div
              className={styles.cardHeader}
              onClick={() => toggleMenu(form.formId)}
            >
              <div className={styles.iconPlaceholder}>
                {/* Optional: load module icons dynamically */}
              </div>
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
