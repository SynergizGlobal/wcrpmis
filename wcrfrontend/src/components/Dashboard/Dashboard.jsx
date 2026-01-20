import React, { useEffect, useState, useRef } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import styles from "./Dashboard.module.css";
import { ChevronDown } from "lucide-react";
import { usePageTitle } from "../../context/PageTitleContext";
import axios from "axios";
import { API_BASE_URL } from "../../config";

export default function Dashboard({ title, data, children }) {
  const { setPageTitle } = usePageTitle();
  const [openMenu, setOpenMenu] = useState(null);
  const [menuItems, setMenuItems] = useState([]);
  const menuRef = useRef(null);
  const location = useLocation();
  const navigate = useNavigate();

  // Get projectId from URL query param
  // 1️⃣ Try query param first
  const searchParams = new URLSearchParams(location.search);
  let projectId = searchParams.get("project_id");

  // 2️⃣ Fallback to path param if query param is missing
  if (!projectId) {
    const segments = location.pathname.split("/").filter(Boolean);
    const lastSegment = segments[segments.length - 1];

    // prevent route names being treated as ID
    if (lastSegment && !lastSegment.includes("-")) {
      projectId = lastSegment;
    }
  }


  // ------------------- LOAD MENUS FROM API -------------------
  useEffect(() => {
    const fetchMenus = async () => {
      try {
        const { data } = await axios.get(`${API_BASE_URL}/execution/menu`, {
          withCredentials: true,
        });

        const formatted = data.map((m) => ({
          label: m.menuName,
          url: m.menuName.toLowerCase().replace(/\s+/g, "-"),
          subItems:
            m.subMenus?.map((s) => ({
              label: s.subMenuName,
              url: s.subMenuName.toLowerCase().replace(/\s+/g, "-"),
            })) || [],
        }));

        setMenuItems(formatted);
      } catch (err) {
        console.error("Failed to load menus:", err);
      }
    };

    fetchMenus();
  }, []);


  // ------------------- ACTIVE MENU HIGHLIGHT -------------------
  const getActiveClass = (item) => {
    const pathParts = location.pathname.split("/");
    return pathParts.includes(item.url) ? styles.active : "";
  };

  const toggleSubMenu = (label) => {
    setOpenMenu(openMenu === label ? null : label);
  };

  const navigateToMenu = (url) => {
    navigate(`/${url}/${projectId}`);
    setOpenMenu(null);
  };


  // ------------------- CLOSE MENU ON OUTSIDE CLICK -------------------
  useEffect(() => {
    const handleClickOutside = (e) => {
      if (menuRef.current && !menuRef.current.contains(e.target)) {
        setOpenMenu(null);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, []);

  useEffect(() => {
    if (title) setPageTitle(title);
  }, [title, setPageTitle]);

  useEffect(() => {
    setOpenMenu(null);
  }, [location.pathname]);

  const handleMainMenuClick = (item, index) => {
    if (!projectId) return;

    // first menu special case
    if (index === 0) {
      navigate(`/Works?project_id=${projectId}`);
    } else {
      navigate(`/${item.url}/${projectId}`);
    }

    setOpenMenu(null);
  };


  return (
    <>
      <div className={styles.topBar}>
        <nav className={styles.dashboardMenu} ref={menuRef}>
          <ul className={styles.menuList}>
            {menuItems.map((item, index) => {
              const hasSubmenu = item.subItems?.length > 0;
              const isOpen = openMenu === item.label;

              return (
                <li key={item.label} className={`${styles.menuItem} ${getActiveClass(item)}`}>
				<div
				  className={styles.menuLabel}
				  onClick={(e) => {
				    e.stopPropagation();
				    handleMainMenuClick(item, index); // 🔥 ALWAYS navigate
				  }}
				>

                    <span>{item.label}</span>
					{hasSubmenu && (
					  <ChevronDown
					    size={16}
					    className={`${styles.arrowIcon} ${isOpen ? styles.arrowOpen : ""}`}
					    onClick={(e) => {
					      e.stopPropagation();
					      toggleSubMenu(item.label); // 👈 ONLY toggle
					    }}
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
					      navigateToMenu(sub.url);
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
      </div>

      <div className={styles.container}>
        <h2>{title}</h2>
        {data && <p>Total Records: {data.length}</p>}
        {children}
      </div>
    </>
  );
}
