import React, { useState, useEffect, useRef } from "react"; 
import styles from "./Header.module.css";
import { usePageTitle } from "../../context/PageTitleContext";
import logo from "../../assets/images/wcr-logo.png";

export default function Header({ toggleSidebar }) {
  const [dropdownOpen, setDropdownOpen] = useState(false);
  const { pageTitle } = usePageTitle();
  const [userName, setUserName] = useState("");

   const dropdownRef = useRef();

  //  const pageTitles = {
  //   "/dashboard": "Dashboard",
  //   "/updateforms": "Update Forms",
  //   "/reports": "Reports",
  //   "/modules": (
  //                 <>
  //                 <span className="fw-400">Project Name:</span>{" "} NS-1: DOUBLING
  //                 </>
  //               ),
  //   "/works": "Works",
  //   "/documents": "Documents",
  //   "/quicklinks": "Quick Links",
  //   "/admin": "Admin Panel",
  // };

  // const pageTitle = pageTitles[location.pathname] || "Western Central Railways";

  

  const toggleDropdown = () => {
    setDropdownOpen(!dropdownOpen);
  };

    const handleLogout = () => {
      localStorage.removeItem("token");
      window.location.href = "/wcrpmis/";
    };

    useEffect(() => {
    const storedUser = localStorage.getItem("user");
    if (storedUser) {
      try {
        const parsedUser = JSON.parse(storedUser);
        setUserName(parsedUser.userName || parsedUser.userId || "User"); 
      } catch {
        setUserName("User");
      }
    }
  }, []);

    useEffect(() => {
  const handleClickOutside = (event) => {
    if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
      setDropdownOpen(false);
    }
  };
  document.addEventListener("mousedown", handleClickOutside);
  return () => document.removeEventListener("mousedown", handleClickOutside);
}, []);

   return (
    <header className={styles.header}>
      <div className={styles.left}>
        <button className={styles.hamburger} onClick={toggleSidebar}>
          &#9776;
        </button>
        <h1 className={styles.pageName}>{pageTitle || "Western Central Railways"}</h1>
      </div>

      <div className={styles.right}>
        <div className={styles.profile} onClick={toggleDropdown}>
          <img src={logo} alt="Logo" className={styles.logo} />
          <span className={styles.username}>
            {userName} ▼
          </span>
        </div>
      <div ref={dropdownRef}>
        {dropdownOpen && (
          <div className={styles.dropdownMenu}>
            <ul>
              {/* <li>My Profile</li>
              <li>Settings</li> */}
              <li onClick={handleLogout}>Logout</li>
            </ul>
          </div>
        )}
      </div>
      </div>
    </header>
  );
}
