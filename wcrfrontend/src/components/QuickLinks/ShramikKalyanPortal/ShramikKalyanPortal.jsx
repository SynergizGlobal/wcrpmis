import React from "react";
import styles from "./ShramikKalyanPortal.module.css";

const ShramikKalyanPortal = () => {
  return (
    <div className={styles.pageWrapper}>
      <div className={styles.card}>
        <a
          href="https://www.shramikkalyan.indianrailways.gov.in/"
          target="_blank"
          rel="noopener noreferrer"
          className={styles.button}
        >
          Shramik Kalyan Portal
        </a>
      </div>
    </div>
  );
};

export default ShramikKalyanPortal;