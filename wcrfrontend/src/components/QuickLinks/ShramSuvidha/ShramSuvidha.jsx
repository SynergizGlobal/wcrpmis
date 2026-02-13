import React from "react";
import styles from "./ShramSuvidha.module.css";

const ShramSuvidha = () => {
  return (
    <div className={styles.pageWrapper}>
      <div className={styles.card}>
        <a
          href="https://return.shramsuvidha.gov.in/user/login"
          target="_blank"
          rel="noopener noreferrer"
          className={styles.button}
        >
          Shram Suvidha
        </a>
      </div>
    </div>
  );
};

export default ShramSuvidha;
