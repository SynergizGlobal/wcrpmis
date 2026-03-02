import React, { useState, useMemo } from "react";
import styles from "./InsuranceContractualLetters.module.css";

const InsuranceContractualLetters = () => {
  const [data, setData] = useState([]);

  const [searchTerm, setSearchTerm] = useState("");
  const [entriesPerPage, setEntriesPerPage] = useState(10);
  const [currentPage, setCurrentPage] = useState(1);

  /* 🔍 Search Filter */
  const filteredData = useMemo(() => {
    return data.filter((item) =>
      Object.values(item)
        .join(" ")
        .toLowerCase()
        .includes(searchTerm.toLowerCase())
    );
  }, [data, searchTerm]);

  const totalEntries = filteredData.length;
  const totalPages = Math.max(1, Math.ceil(totalEntries / entriesPerPage));

  const startIndex =
    totalEntries === 0 ? 0 : (currentPage - 1) * entriesPerPage + 1;

  const endIndex = Math.min(currentPage * entriesPerPage, totalEntries);

  const currentData = filteredData.slice(
    (currentPage - 1) * entriesPerPage,
    currentPage * entriesPerPage
  );

  const handleEntriesChange = (e) => {
    setEntriesPerPage(Number(e.target.value));
    setCurrentPage(1);
  };

  const handleSearchChange = (e) => {
    setSearchTerm(e.target.value);
    setCurrentPage(1);
  };

  const clearSearch = () => {
    setSearchTerm("");
    setCurrentPage(1);
  };

  const renderPageButtons = () => {
    if (totalPages <= 1) return null;

    const pages = Array.from({ length: totalPages }, (_, i) => i + 1)
      .filter((p) => {
        if (p <= 2 || p > totalPages - 2) return true;
        if (p >= currentPage - 1 && p <= currentPage + 1) return true;
        return false;
      })
      .reduce((acc, p, idx, arr) => {
        if (idx > 0 && p - arr[idx - 1] > 1) acc.push("...");
        acc.push(p);
        return acc;
      }, []);

    return (
      <>
        <button
          className={`${styles.pageBtn} ${
            currentPage === 1 ? styles.disabledBtn : ""
          }`}
          disabled={currentPage === 1}
          onClick={() => setCurrentPage((prev) => prev - 1)}
        >
          Previous
        </button>

        {pages.map((item, idx) =>
          item === "..." ? (
            <span key={idx} className={styles.ellipsis}>
              ...
            </span>
          ) : (
            <button
              key={idx}
              onClick={() => setCurrentPage(item)}
              className={`${styles.pageNumber} ${
                item === currentPage ? styles.activePage : ""
              }`}
            >
              {item}
            </button>
          )
        )}

        <button
          className={`${styles.pageBtn} ${
            currentPage === totalPages ? styles.disabledBtn : ""
          }`}
          disabled={currentPage === totalPages}
          onClick={() => setCurrentPage((prev) => prev + 1)}
        >
          Next
        </button>
      </>
    );
  };

  return (
    <div className={styles.pageWrapper}>
      <div className={styles.tableCard}>
        <div className={styles.titleBar}>
          List of Contracts Closer to Insurance Expiry Date
        </div>

        {/* Controls */}
        <div className={styles.topControls}>
          <div className={styles.showEntries}>
            <span>Show</span>
            <select value={entriesPerPage} onChange={handleEntriesChange}>
              <option value={5}>5</option>
              <option value={10}>10</option>
              <option value={25}>25</option>
              <option value={50}>50</option>
              <option value={100}>100</option>
            </select>
            <span>entries</span>
          </div>

          {/* SEARCH BAR */}
          <div className={styles.searchBox}>
            <input
              type="text"
              placeholder="Search"
              value={searchTerm}
              onChange={handleSearchChange}
              className={styles.searchInput}
            />

            {searchTerm && (
              <span className={styles.clearBtn} onClick={clearSearch}>
                ×
              </span>
            )}

            <span className={styles.searchIcon}>🔍</span>
          </div>
        </div>

        {/* Table */}
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
                <th>Status</th>
              </tr>
            </thead>

            <tbody>
              {currentData.length === 0 ? (
                <tr>
                  <td colSpan="9" style={{ textAlign: "center", padding: "15px" }}>
                    No data available in table
                  </td>
                </tr>
              ) : (
                currentData.map((item, index) => (
                  <tr key={item.id}>
                    <td>{startIndex + index}</td>
                    <td>{item.contractId}</td>
                    <td>{item.contractName}</td>
                    <td>{item.contractor}</td>
                    <td>{item.insuranceType}</td>
                    <td>{item.insuranceNo}</td>
                    <td>{item.noOfInsurances}</td>
                    <td>{item.expiryMonth}</td>
                    <td>{item.status}</td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>

        {/* Footer */}
        <div className={styles.footer}>
          <span>
            Showing {totalEntries === 0 ? 0 : startIndex} to {endIndex} of{" "}
            {totalEntries} entries
          </span>

          <div className={styles.pagination}>{renderPageButtons()}</div>
        </div>
      </div>
    </div>
  );
};

export default InsuranceContractualLetters;