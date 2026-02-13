import React from "react";
import styles from "./QuickLinks.module.css";

const QuickLinks = () => {
  return (
    <div className={styles.menuPage}>
      <div className={styles.menuContainer}>

        <div className={styles.card}>
          <a
            href="https://www.shramikkalyan.indianrailways.gov.in/"
            target="_blank"
            rel="noopener noreferrer"
            className={styles.cardButton}
          >
            Shramik Kalyan Portal
          </a>
        </div>

        <div className={styles.card}>
          <a
            href="https://shramsuvidha.gov.in/"
            target="_blank"
            rel="noopener noreferrer"
            className={styles.cardButton}
          >
            Shram Suvidha
          </a>
        </div>

      </div>
    </div>
  );
};

export default QuickLinks;
