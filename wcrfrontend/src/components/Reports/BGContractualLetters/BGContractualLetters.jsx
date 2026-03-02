import React, { useState, useMemo } from "react";
import { useForm } from "react-hook-form";
import styles from "./BGContractualLetters.module.css";
import { FaTimes } from "react-icons/fa";

/* DateInput Component */
const DateInput = ({ register, name, placeholder, disabled }) => {
  return (
    <div className={styles.dateField}>
      <input
        {...register(name)}
        type="date"
        placeholder={placeholder}
        disabled={disabled}
        className={styles.dateInput}
      />
    </div>
  );
};

const BGContractualLetters = () => {
  const {
    register,
    formState: { isSubmitting },
  } = useForm();

  const [data, setData] = useState([]); // Use API to populate later

  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const [entriesPerPage, setEntriesPerPage] = useState(10);

  /* Search Filter */
  const filteredData = useMemo(() => {
    return data.filter((item) =>
      Object.values(item)
        .join(" ")
        .toLowerCase()
        .includes(searchTerm.toLowerCase())
    );
  }, [data, searchTerm]);

  const totalEntries = filteredData.length;

  const totalPages = Math.max(
    1,
    Math.ceil(totalEntries / entriesPerPage)
  );

  const startIndex =
    totalEntries === 0
      ? 0
      : (currentPage - 1) * entriesPerPage + 1;

  const endIndex = Math.min(
    currentPage * entriesPerPage,
    totalEntries
  );

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
        <span
          className={`${styles.pageBtn} ${
            currentPage === 1 ? styles.disabledBtn : ""
          }`}
          onClick={() =>
            currentPage > 1 && setCurrentPage(currentPage - 1)
          }
        >
          Previous
        </span>

        {pages.map((item, idx) =>
          item === "..." ? (
            <span key={idx} className={styles.ellipsis}>
              ...
            </span>
          ) : (
            <span
              key={idx}
              onClick={() => setCurrentPage(item)}
              className={`${styles.pageNumber} ${
                item === currentPage ? styles.activePage : ""
              }`}
            >
              {item}
            </span>
          )
        )}

        <span
          className={`${styles.pageBtn} ${
            currentPage === totalPages ? styles.disabledBtn : ""
          }`}
          onClick={() =>
            currentPage < totalPages &&
            setCurrentPage(currentPage + 1)
          }
        >
          Next
        </span>
      </>
    );
  };

  return (
    <div className={styles.pageContainer}>
      <div className={styles.header}>
        List of Contracts Closer to BG Expiry Date
      </div>

      <div className={styles.filterSection}>
        <div className={styles.dateRow}>
          <div className={styles.dateWrapper}>
            <label>Start Date</label>
            <DateInput
              register={register}
              name="start_date"
              placeholder="Enter Value"
              disabled={isSubmitting}
            />
          </div>

          <div className={styles.dateWrapper}>
            <label>End Date</label>
            <DateInput
              register={register}
              name="end_date"
              placeholder="Enter Value"
              disabled={isSubmitting}
            />
          </div>
        </div>

        <div className={styles.controlsRow}>
          <div className={styles.entriesControl}>
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

          <div className={styles.searchControl}>
            <input
              type="text"
              placeholder="Search"
              value={searchTerm}
              onChange={handleSearchChange}
            />
            <span className={styles.searchIcon}>🔍</span>
            {searchTerm && (
              <FaTimes
                className={styles.clearIcon}
                onClick={clearSearch}
              />
            )}
          </div>
        </div>
      </div>

      <div className={styles.tableWrapper}>
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
              <tr>
                <td colSpan="7" className={styles.noData}>
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
                  <td>{item.doc}</td>
                  <td>{item.download}</td>
                  <td>{item.status}</td>
                </tr>
              ))
            )}
          </tbody>
        </table>

        <div className={styles.tableFooter}>
          <div className={styles.tableInfo}>
            Showing {startIndex} to {endIndex} of {totalEntries} entries
          </div>

          <div className={styles.pagination}>
            {renderPageButtons()}
          </div>
        </div>
      </div>
    </div>
  );
};

export default BGContractualLetters;