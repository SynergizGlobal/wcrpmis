import React, { useState, useMemo } from "react";
import styles from "./DateofCompletionLetters.module.css";
import { Search, X } from "lucide-react";

const DateofCompletionLetters = () => {

  const [data, setData] = useState([]); 

  const [searchTerm, setSearchTerm] = useState("");
  const [entriesPerPage, setEntriesPerPage] = useState(10);
  const [currentPage, setCurrentPage] = useState(1);

  /* 🔍 Search Filtering */
  const filteredData = useMemo(() => {
    return data.filter((item) =>
      Object.values(item)
        .join(" ")
        .toLowerCase()
        .includes(searchTerm.toLowerCase())
    );
  }, [data, searchTerm]);

  const totalEntries = filteredData.length;

  /* 📄 Pagination Calculations */
  const totalPages = Math.max(
    1,
    Math.ceil(totalEntries / entriesPerPage)
  );

  const currentData = filteredData.slice(
    (currentPage - 1) * entriesPerPage,
    currentPage * entriesPerPage
  );

  const startIndex =
    totalEntries === 0
      ? 0
      : (currentPage - 1) * entriesPerPage + 1;

  const endIndex = Math.min(
    currentPage * entriesPerPage,
    totalEntries
  );

  const handleEntriesChange = (e) => {
    setEntriesPerPage(Number(e.target.value));
    setCurrentPage(1);
  };

  const clearSearch = () => {
    setSearchTerm("");
    setCurrentPage(1);
  };

  const handleSearchChange = (e) => {
    setSearchTerm(e.target.value);
    setCurrentPage(1);
  };

  /* 🔢 Pagination Button Renderer */
  const renderPageButtons = () => {
    if (totalPages <= 1) return null;

    const pages = Array.from({ length: totalPages }, (_, i) => i + 1)
      .filter((p) => {
        if (p <= 2 || p > totalPages - 2) return true;
        if (p >= currentPage - 1 && p <= currentPage + 1)
          return true;
        return false;
      })
      .reduce((acc, p, idx, arr) => {
        if (idx > 0 && p - arr[idx - 1] > 1)
          acc.push("...");
        acc.push(p);
        return acc;
      }, []);

    return (
      <>
        {/* Previous */}
        <button
          disabled={currentPage === 1}
          onClick={() => setCurrentPage(currentPage - 1)}
          className={styles.pageBtn}
        >
          Previous
        </button>

        {/* Page Numbers */}
        {pages.map((item, idx) =>
          item === "..." ? (
            <span key={idx} className={styles.ellipsis}>
              ...
            </span>
          ) : (
            <button
              key={idx}
              onClick={() => setCurrentPage(item)}
              className={`${styles.pageBtn} ${
                item === currentPage ? styles.activePage : ""
              }`}
            >
              {item}
            </button>
          )
        )}

        {/* Next */}
        <button
          disabled={currentPage === totalPages}
          onClick={() => setCurrentPage(currentPage + 1)}
          className={styles.pageBtn}
        >
          Next
        </button>
      </>
    );
  };

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
          <select
            value={entriesPerPage}
            onChange={handleEntriesChange}
          >
            <option value={5}>5</option>
            <option value={10}>10</option>
            <option value={25}>25</option>
            <option value={50}>50</option>
            <option value={100}>100</option>
          </select>
          <span>entries</span>
        </div>

        {/* Search */}
        <div className={styles.searchBox}>
          <input
            type="text"
            placeholder="Search"
            value={searchTerm}
            onChange={handleSearchChange}
          />
          <Search size={14} className={styles.searchIcon} />
          {searchTerm && (
            <X
              size={14}
              className={styles.clearIcon}
              onClick={clearSearch}
            />
          )}
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
            {currentData.length === 0 ? (
              <tr className={styles.emptyRow}>
                <td colSpan="7">
                  {searchTerm
                    ? "No matching records found"
                    : "No data available in table"}
                </td>
              </tr>
            ) : (
              currentData.map((item, index) => (
                <tr key={item.id}>
                  <td>{startIndex + index}</td>
                  <td>{item.contractId}</td>
                  <td>{item.contractName}</td>
                  <td>{item.contractor}</td>
                  <td>{item.latestDoc}</td>
                  <td>{item.download}</td>
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
          Showing {startIndex} to {endIndex} of {totalEntries} entries
        </span>

        <div className={styles.pagination}>
          {renderPageButtons()}
        </div>
      </div>
    </div>
  );
};

export default DateofCompletionLetters;