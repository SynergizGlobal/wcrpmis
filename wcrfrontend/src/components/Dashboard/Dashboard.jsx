import React, { useEffect, useState, useRef } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import styles from "./Dashboard.module.css";
import { ChevronDown } from "lucide-react";
import { usePageTitle } from "../../context/PageTitleContext";

export default function Dashboard({ title, data }) {
  const { setPageTitle } = usePageTitle();
  const [openMenu, setOpenMenu] = useState(null);
  const menuRef = useRef(null);
  const location = useLocation();
  const navigate = useNavigate();

  const menuItems = [
    { label: "PROJECT OVERVIEW", path: "/project-overview" },
    {
      label: "EXECUTION",
      path: "/execution",
      subItems: [
        { label: "Timeline Schedule", path: "/execution/timeline" },
        { label: "Progress Table", path: "/execution/table" },
        { label: "Horizontal Progress", path: "/execution/horizontal" },
        { label: "Progress Chart", path: "/execution/chart" },
        { label: "Engineering", path: "/execution/engineering" },
        { label: "Execution Subprojects", path: "/execution/subprojects" },
        { label: "Station Subchart", path: "/execution/stationsubchart" },
        { label: "Daily Progress", path: "/execution/daily" },
        { label: "Activities Workspace", path: "/execution/activities" },
      ],
    },
    { label: "CONTRACTS", path: "/contracts" },
    { label: "LAND ACQUISITION", path: "/land-acquisition" },
    { label: "ISSUES", path: "/issues" },
    { label: "UTILITY SHIFTING", path: "/utility-shifting" },
  ];

  const toggleSubMenu = (label) => {
    setOpenMenu(openMenu === label ? null : label);
  };

  // ✅ Close on outside click
  useEffect(() => {
    const handleClickOutside = (e) => {
      if (menuRef.current && !menuRef.current.contains(e.target)) {
        setOpenMenu(null);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  // ✅ Update header title
  useEffect(() => {
    if (title) {
      setPageTitle(title);
    }
  }, [title, setPageTitle]);

  // ✅ Close submenus on route change
  useEffect(() => {
    setOpenMenu(null);
  }, [location.pathname]);

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
                className={`${styles.menuItem} ${isOpen ? styles.active : ""}`}
                onClick={() => {
                  if (hasSubmenu) toggleSubMenu(item.label);
                  else navigate(item.path);
                }}
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
                          navigate(sub.path);
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
        <h2>{title}</h2>
        {data && <p>Total Records: {data.length}</p>}
      </div>
    </>
  );
}
