import React from "react";
import styles from "./Footer.module.css";

import logo from "../../assets/images/wcr-logo.png";
import sygLogo from "../../assets/images/syg-logo.png";

export default function Footer() {
  return (
    <footer className={styles.footer}>
      <div className={styles.innerFooter}>
        <p>
          © 2025 @ WCR <img src={logo} width="20" height="20" /> | Designed & Developed by <img src={sygLogo} width="110" height="26" />
        </p>
        <select className={styles.dropdown}>
          <option>Production</option>
          <option>Testing</option>
          <option>Development</option>
        </select>
      </div>
    </footer>
  );
}
