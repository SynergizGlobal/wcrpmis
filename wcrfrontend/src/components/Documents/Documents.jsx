import React from "react";
import { Link, Outlet, useMatch } from "react-router-dom";
import styles from "./Documents.module.css";

const Documents = () => {
  const match = useMatch("/documents");

  // If it is the main /documents page → show menu layout
  if (match) {
    return (
      <div className={styles.menuPage}>
        <div className={styles.menuWrapper}>
          <div className={styles.menuList}>

            <div className={styles.card}>
              <Link to="codes-and-manuals" className={styles.cardButton}>
                Codes & Manuals
              </Link>
            </div>

            <div className={styles.card}>
              <Link to="faq" className={styles.cardButton}>
                FAQ
              </Link>
            </div>

            <div className={styles.card}>
              <Link to="policiesandcirculars" className={styles.cardButton}>
                Policies & Circulars
              </Link>
            </div>

          </div>
        </div>
      </div>
    );
  }

  // If it's a child route → render child page ONLY
  return <Outlet />;
};

export default Documents;
