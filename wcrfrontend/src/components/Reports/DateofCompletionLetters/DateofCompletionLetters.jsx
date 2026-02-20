import React, { useState } from "react";
import styles from "./DateofCompletionLetters.module.css";
import { Search } from "lucide-react";

const DateofCompletionLetters = () => {
  const [searchTerm, setSearchTerm] = useState("");

  return (
    <div className={styles.wrapper}>
      {/* Header */}
      <div className={styles.header}>
        <h2>List of Contracts Closer to DOC Expiry Date</h2>
      </div>

      {/* Controls */}
      <div className={styles.controls}>
        <div className={styles.entriesControl}>
          <label>Show</label>
          <select>
		  <option>5</option>
	     <option>10</option>
         <option>25</option>
         <option>50</option>
		 <option>100</option>
          </select>
          <span>entries</span>
        </div>

        {/* Updated Search Box (UI unchanged except style) */}
        <div className={styles.searchBox}>
          <input
            type="text"
            placeholder="Search"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
          <Search size={14} className={styles.searchIcon} />
        </div>
      </div>

      {/* Table */}
      <div className={styles.tableContainer}>
        <table className={styles.table}>
          <thead>
            <tr>
              <th>S. No.</th>
              <th>Contract ID</th>
              <th>Contract Short Name</th>
              <th>Contractor Name</th>
              <th>Latest DOC</th>
              <th>Download Letter</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            <tr className={styles.emptyRow}>
              <td colSpan="7">No data available in table</td>
            </tr>
          </tbody>
        </table>
      </div>
     
      {/* Footer */}
      <div className={styles.footer}>
        <span>Showing 0 to 0 of 0 entries</span>
        <div className={styles.pagination}>
          <button>PREVIOUS</button>
          <button>NEXT</button>
        </div>
      </div>
    </div>
  );
};

export default DateofCompletionLetters;
