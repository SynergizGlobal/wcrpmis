import React from "react";
import styles from "./InsuranceContractualLetters.module.css";

const InsuranceContractualLetters = () => {
  return (
    <div className={styles.pageWrapper}>
      <div className={styles.tableCard}>

        {/* ================= TITLE ================= */}
        <div className={styles.titleBar}>
          List of Contracts Closer to Insurance Expiry Date
        </div>

        {/* ================= TOP CONTROLS ================= */}
        <div className={styles.topControls}>

          {/* Show Entries */}
          <div className={styles.showEntries}>
            <span>Show</span>
            <select>
              <option>5</option>
              <option>10</option>
              <option>25</option>
              <option>50</option>
              <option>100</option>
            </select>
            <span>entries</span>
          </div>

          {/* Search */}
          <div className={styles.searchBox}>
            <input
              type="text"
              placeholder="Search"
              className={styles.searchInput}
            />
            <span className={styles.searchIcon}>🔍</span>
          </div>

        </div>

        {/* ================= TABLE ================= */}
        <div className={styles.tableWrapper}>
          <table className={styles.table}>
            <thead>
              <tr>
                <th>S. No.</th>
                <th>Contract ID</th>
                <th>Contract Short Name</th>
                <th>Contractor Name</th>
                <th>Insurance Type</th>
                <th>Insurance No.</th>
                <th>No. of Insurances</th>
                <th>Expiry Month</th>
                <th>Download Letter</th>
                <th>Status</th>
              </tr>
            </thead>

            <tbody>
              <tr>
                <td colSpan="10" className={styles.noData}>
                  No data available in table
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        {/* ================= FOOTER ================= */}
        <div className={styles.footer}>
          <span>Showing 0 to 0 of 0 entries</span>

          <div className={styles.pagination}>
            <span className={styles.pageBtn}>PREVIOUS</span>
            <span className={styles.pageBtn}>NEXT</span>
          </div>
        </div>

      </div>
    </div>
  );
};

export default InsuranceContractualLetters;
