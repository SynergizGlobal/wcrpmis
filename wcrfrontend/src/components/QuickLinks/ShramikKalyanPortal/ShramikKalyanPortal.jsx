import React from "react";
import styles from "./ShramikKalyanPortal.module.css";

const ShramikKalyanPortal = () => {

  const redirectToPortal = () => {
    window.open(
      "https://www.shramikkalyan.indianrailways.gov.in/",
      "_blank"
    );
  };

  const redirectToShramSuvidha = () => {
    window.open(
      "https://shramsuvidha.gov.in/",
      "_blank"
    );
  };

  return (
    <div className={styles.menuPage}>
      <div className={styles.menuContainer}>

        <div className={styles.card}>
          <button
            type="button"
            onClick={redirectToPortal}
            className={styles.cardButton}
          >
            Shramik Kalyan Portal
          </button>
        </div>

        <div className={styles.card}>
          <button
            type="button"
            onClick={redirectToShramSuvidha}
            className={styles.cardButton}
          >
            Shram Suvidha
          </button>
        </div>

      </div>
    </div>
  );
};

export default ShramikKalyanPortal;
