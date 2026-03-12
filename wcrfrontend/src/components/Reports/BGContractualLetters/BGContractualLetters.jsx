import React, { useState, useMemo, useCallback } from "react";
import { useForm } from "react-hook-form";
import styles from "./BGContractualLetters.module.css";
import { FaTimes } from "react-icons/fa";

/* ─────────────────────────────────────────────
   Helpers (ported from JSP JS)
───────────────────────────────────────────── */

/** Returns current Indian financial year string, e.g. "2025-26" */
function getCurrentFinancialYear() {
  const today = new Date();
  const month = today.getMonth() + 1;
  if (month <= 3) {
    return `${today.getFullYear() - 1}-${today.getFullYear()}`;
  }
  return `${today.getFullYear()}-${String(today.getFullYear() + 1).slice(-2)}`;
}

/** Add days / weeks / months / years to a Date and return new Date */
function addDate(dt, amount, dateType) {
  const d = new Date(dt);
  switch (dateType) {
    case "days":   d.setDate(d.getDate() + amount); break;
    case "weeks":  d.setDate(d.getDate() + 7 * amount); break;
    case "months": d.setMonth(d.getMonth() + amount); break;
    case "years":  d.setFullYear(d.getFullYear() + amount); break;
    default: break;
  }
  return d;
}

/** Format a Date as "YYYY-MM-DD" */
function toISODate(dt) {
  const d = new Date(dt);
  const mm = String(d.getMonth() + 1).padStart(2, "0");
  const dd = String(d.getDate()).padStart(2, "0");
  return `${d.getFullYear()}-${mm}-${dd}`;
}

/** Format a Date as "DD-MM-YYYY" for display */
function toDMY(dateStr) {
  if (!dateStr) return "";
  const d = new Date(dateStr);
  if (isNaN(d)) return dateStr;
  return `${String(d.getDate()).padStart(2, "0")}-${String(d.getMonth() + 1).padStart(2, "0")}-${d.getFullYear()}`;
}

/**
 * Build and trigger download of a Word (.doc) letter.
 * Mirrors the export2Word() function and the hidden #docx div in the JSP.
 */
function exportToWord(val) {
  const financialYear   = getCurrentFinancialYear();
  const expiryDate      = val.bg_valid_upto;
  const twoMonthsBefore = toISODate(addDate(new Date(expiryDate), -2, "months"));

  const contractorAddr = `${(val.contractor_name || "").toUpperCase()}, ${(val.address || "").toUpperCase()}`;
  const bankAddr       = (val.issuing_bank || "").toUpperCase();

  const css = `
    <style>
      @page Section1 {
        margin: 1.97in 0.79in 1.0in 1.97in;
        size: 595.28pt 841.89pt;
        mso-page-orientation: Portrait;
        mso-header-margin: 0.49in;
        mso-footer-margin: 0.49in;
      }
      div.Section1 { page: Section1; }
      p.headerFooter { margin: 0in; text-align: center; }
    </style>`;

  const body = `
  <div class="Section1" style="width:793.7px;margin:2in 1.25in 1.25in 2in;font-family:Arial,sans-serif;font-size:10.5pt;color:black;">

    <div style="width:100%;margin-bottom:16px;">
      <table style="width:100%;border-collapse:collapse;">
        <tr>
          <td style="text-align:left;white-space:nowrap;font-family:Arial,sans-serif;font-size:10.5pt;">
            <strong>Letter No. MRVC/ACCTS/EXP/BG/<u>${financialYear}</u></strong>
          </td>
          <td style="text-align:right;white-space:nowrap;font-family:Arial,sans-serif;font-size:10.5pt;">
            <strong>Date: ${twoMonthsBefore}</strong>
          </td>
        </tr>
      </table>
    </div>

    <p style="margin:0 0 12px 0;font-family:Arial,sans-serif;font-size:10.5pt;">
      <strong>${val.hod_user_id_fk} MRVC</strong>
    </p>

    <p style="margin:0 0 20px 0;font-family:Arial,sans-serif;font-size:10.5pt;">
      <strong>
        SUB: Extension of BG No. <u>${val.bg_number}</u> Dt: <u>${val.bg_date || ""}</u>
        for Rs <u>${val.bg_value}</u> against CA/ PO. No.
        <u>${val.loa_letter_number}</u> of <u>${val.contractor_name}</u>
      </strong>
    </p>

    <p style="text-align:justify;line-height:1.5;font-family:Arial,sans-serif;font-size:10.5pt;">
      The validity of the subject Bank Guarantee received from
      <strong><u>${val.contractor_name}</u></strong> expires on
      <strong><u>${expiryDate}</u></strong>.
      You are requested to arrange for extension of BG before the expiry date in case the contract
      is not completed in all respects. In case the contract has been completed satisfactorily in
      all respects, advice to this effect must be sent to this office before
      <strong><u>${expiryDate}</u></strong>.
    </p>

    <p style="text-align:justify;line-height:1.5;font-family:Arial,sans-serif;font-size:10.5pt;">
      If this office does not receive the extension/reply within the stipulated date before the
      expiry date of BG, the issuing bank should be advised by this office to deposit the amount
      due to MRVC under guarantee. A copy of this letter is being endorsed to the firm's bank for
      taking necessary action.
    </p>

    <p style="text-align:right;margin:0 0 30px 0;font-family:Arial,sans-serif;font-size:10.5pt;">
      <strong>/FACAO(MRVC)</strong>
    </p>

    <p style="margin:0 0 12px 0;font-family:Arial,sans-serif;font-size:10.5pt;"><strong>Copy to:</strong></p>

    <table style="width:100%;border-collapse:collapse;font-size:10.5pt;color:black;">
      <tr>
        <td style="width:10%;vertical-align:top;padding-right:10px;font-family:Arial,sans-serif;font-size:10.5pt;"><strong>1</strong></td>
        <td style="width:40%;vertical-align:top;padding-right:20px;font-family:Arial,sans-serif;font-size:10.5pt;">
          <u>${contractorAddr}</u>
        </td>
        <td style="width:50%;vertical-align:top;font-family:Arial,sans-serif;font-size:10.5pt;">
          Contractor is advised to take speedy steps for extension of the above BG
          [or submit the completion report to enable this office to release the said BG].
        </td>
      </tr>
      <tr style="height:5px;"></tr>
      <tr>
        <td style="width:10%;vertical-align:top;padding-right:10px;font-family:Arial,sans-serif;font-size:10.5pt;"><strong>2</strong></td>
        <td style="width:40%;vertical-align:top;padding-right:20px;font-family:Arial,sans-serif;font-size:10.5pt;">
          <u>${bankAddr}</u>
        </td>
        <td style="width:50%;vertical-align:top;font-family:Arial,sans-serif;font-size:10.5pt;">
          The above BG confirmed vide your L.N as issued by you, expires on
          <u>${toDMY(expiryDate)}</u>.
          If the firm fails to extend the same unconditionally before the expiry date,
          these offices claim for Rs. ${val.bg_value} is deemed to be lodged on your bank
          which may please be noted.
        </td>
      </tr>
    </table>

  </div>`;

  const fullHtml = `<html><head>${css}</head><body>${body}</body></html>`;

  const blob = new Blob(["\ufeff", fullHtml], { type: "application/msword" });
  const url  = URL.createObjectURL(blob);
  const link = document.createElement("a");
  link.href     = url;
  link.download = `BG Expiry Letter_${val.bg_number}`;
  document.body.appendChild(link);

  if (navigator.msSaveOrOpenBlob) {
    navigator.msSaveOrOpenBlob(blob, "BG_Expiry_Letter.doc");
  } else {
    link.click();
  }

  document.body.removeChild(link);
  URL.revokeObjectURL(url);
}

/* ─────────────────────────────────────────────
   DateInput sub-component
───────────────────────────────────────────── */
const DateInput = ({ register, name, label, disabled }) => (
  <div className={styles.dateWrapper}>
    <label>{label}</label>
    <div className={styles.dateField}>
      <input
        {...register(name)}
        type="date"
        disabled={disabled}
        className={styles.dateInput}
      />
    </div>
  </div>
);

/* ─────────────────────────────────────────────
   Main Component
───────────────────────────────────────────── */
const BGContractualLetters = () => {
  const { register, handleSubmit } = useForm();

  const [data,           setData]           = useState([]);
  const [loading,        setLoading]        = useState(false);
  const [error,          setError]          = useState(null);
  const [searchTerm,     setSearchTerm]     = useState("");
  const [currentPage,    setCurrentPage]    = useState(1);
  const [entriesPerPage, setEntriesPerPage] = useState(10);

  /* ── Fetch table data from backend ── */
  const fetchData = useCallback(async (formValues) => {
    setLoading(true);
    setError(null);
    setData([]);
    setCurrentPage(1);

    try {
      const params = new URLSearchParams();

      // date inputs are type="date" → "YYYY-MM-DD"; API expects "YYYY-MM-DD" as well
      if (formValues.start_date) params.append("date_of_start", formValues.start_date);
      if (formValues.end_date)   params.append("bg_date",       formValues.end_date);

      const response = await fetch("/ajax/generate-bg-contractual-letters", {
        method:      "POST",
        headers:     { "Content-Type": "application/x-www-form-urlencoded" },
        credentials: "include",           // sends session cookie for User lookup
        body:        params.toString(),
      });

      if (!response.ok) {
        throw new Error("Server error: " + response.status + " " + response.statusText);
      }

      // Guard: server may return an HTML login-redirect instead of JSON.
      // Catch it early to give a clear message instead of a cryptic SyntaxError.
      const contentType = response.headers.get("content-type") || "";
      const responseText = await response.text();

      if (!contentType.includes("application/json") || responseText.trimStart().startsWith("<")) {
        throw new Error(
          "Session may have expired or you are not authorised. Please refresh the page and log in again."
        );
      }

      let result;
      try {
        result = JSON.parse(responseText);
      } catch (_e) {
        throw new Error("Server returned an unexpected response. Please try again.");
      }

      setData(Array.isArray(result) ? result : []);
    } catch (err) {
      console.error("fetchData:", err);
      setError(err.message || "Failed to fetch data. Please try again.");
    } finally {
      setLoading(false);
    }
  }, []);

  /* ── Fetch BG details then trigger Word download ── */
  const handleDownload = useCallback(async (contractId, bgNumber) => {
    try {
      const cleanBg = (bgNumber || "").replace(/['"]/g, "").trim();
      const params  = new URLSearchParams({ contract_id: contractId, bg_number: cleanBg });

      const response = await fetch("/ajax/get-contract-bg-details", {
        method:      "POST",
        headers:     { "Content-Type": "application/x-www-form-urlencoded" },
        credentials: "include",
        body:        params.toString(),
      });

      if (!response.ok) throw new Error(`Server error: ${response.status}`);

      const details = await response.json();
      if (Array.isArray(details) && details.length > 0) {
        exportToWord(details[0]);
      } else {
        alert("No BG details found for this record.");
      }
    } catch (err) {
      console.error("handleDownload:", err);
      alert("Failed to fetch BG details. " + err.message);
    }
  }, []);

  /* ── Update letter status (mirrors updateLetterStatus in JSP) ── */
  const handleStatusChange = useCallback(async (contractId, newStatus, bgNumber) => {
    // Optimistic update
    setData((prev) =>
      prev.map((row) =>
        row.contract_id === contractId && row.bg_number === bgNumber
          ? { ...row, bg_letter_status: newStatus }
          : row
      )
    );

    try {
      const params = new URLSearchParams({
        contract_id:   contractId,
        letter_status: newStatus,
        bg_number:     bgNumber,
      });
      await fetch("/ajax/UpdateLetterStatus", {
        method:      "POST",
        headers:     { "Content-Type": "application/x-www-form-urlencoded" },
        credentials: "include",
        body:        params.toString(),
      });
    } catch (err) {
      console.error("handleStatusChange:", err);
    }
  }, []);

  /* ── NOTE: No auto-fetch on mount.
       The JSP fired getContractList() because the session was already established
       server-side. In React (SPA) the session cookie may not be present at mount
       time, causing the server to return an HTML login-redirect instead of JSON.
       Users must click Search (optionally with date filters) to load data. ── */

  /* ── Client-side search filter ── */
  const filteredData = useMemo(() => {
    if (!searchTerm) return data;
    const q = searchTerm.toLowerCase();
    return data.filter((row) =>
      [
        row.contract_id,
        row.contract_short_name,
        row.contractor_name,
        row.bg_type_fk,
        row.issuing_bank,
        row.bg_number,
        row.bg_value,
        row.bg_valid_upto,
        row.bg_letter_status,
      ]
        .join(" ")
        .toLowerCase()
        .includes(q)
    );
  }, [data, searchTerm]);

  /* ── Pagination ── */
  const totalEntries = filteredData.length;
  const totalPages   = Math.max(1, Math.ceil(totalEntries / entriesPerPage));
  const startIndex   = totalEntries === 0 ? 0 : (currentPage - 1) * entriesPerPage + 1;
  const endIndex     = Math.min(currentPage * entriesPerPage, totalEntries);
  const currentData  = filteredData.slice((currentPage - 1) * entriesPerPage, currentPage * entriesPerPage);

  const handleEntriesChange = (e) => { setEntriesPerPage(Number(e.target.value)); setCurrentPage(1); };
  const handleSearchChange  = (e) => { setSearchTerm(e.target.value); setCurrentPage(1); };
  const clearSearch         = ()  => { setSearchTerm(""); setCurrentPage(1); };

  const renderPageButtons = () => {
    if (totalPages <= 1) return null;

    const pages = Array.from({ length: totalPages }, (_, i) => i + 1)
      .filter((p) =>
        p <= 2 || p > totalPages - 2 || (p >= currentPage - 1 && p <= currentPage + 1)
      )
      .reduce((acc, p, idx, arr) => {
        if (idx > 0 && p - arr[idx - 1] > 1) acc.push("...");
        acc.push(p);
        return acc;
      }, []);

    return (
      <>
        <span
          className={`${styles.pageBtn} ${currentPage === 1 ? styles.disabledBtn : ""}`}
          onClick={() => currentPage > 1 && setCurrentPage(currentPage - 1)}
        >
          Previous
        </span>

        {pages.map((item, idx) =>
          item === "..." ? (
            <span key={idx} className={styles.ellipsis}>...</span>
          ) : (
            <span
              key={idx}
              onClick={() => setCurrentPage(item)}
              className={`${styles.pageNumber} ${item === currentPage ? styles.activePage : ""}`}
            >
              {item}
            </span>
          )
        )}

        <span
          className={`${styles.pageBtn} ${currentPage === totalPages ? styles.disabledBtn : ""}`}
          onClick={() => currentPage < totalPages && setCurrentPage(currentPage + 1)}
        >
          Next
        </span>
      </>
    );
  };

  /* ── Render ── */
  return (
    <div className={styles.pageContainer}>

      {/* ── Page Header ── */}
      <div className={styles.header}>
        List of Contracts Closer to BG Expiry Date
      </div>

      {/* ── Filter Section ── */}
      <div className={styles.filterSection}>
        <div className={styles.dateRow}>
          <DateInput register={register} name="start_date" label="Start Date" disabled={loading} />
          <DateInput register={register} name="end_date"   label="End Date"   disabled={loading} />

          <div className={styles.dateWrapper} style={{ alignSelf: "flex-end" }}>
            <button
              type="button"
              className={styles.searchBtn}
              disabled={loading}
              onClick={handleSubmit(fetchData)}
            >
              {loading ? "Loading…" : "Search"}
            </button>
          </div>
        </div>

        {error && <div className={styles.errorBanner}>{error}</div>}

        <div className={styles.controlsRow}>
          <div className={styles.entriesControl}>
            <span>Show</span>
            <select value={entriesPerPage} onChange={handleEntriesChange}>
              {[5, 10, 25, 50, 100].map((n) => (
                <option key={n} value={n}>{n}</option>
              ))}
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
              <FaTimes className={styles.clearIcon} onClick={clearSearch} />
            )}
          </div>
        </div>
      </div>

      {/* ── Data Table ── */}
      <div className={styles.tableWrapper}>
        <table className={styles.table}>
          <thead>
            <tr>
              <th>S. No.</th>
              <th>Contract ID</th>
              <th>Contract Short Name</th>
              <th>Contractor Name</th>
              <th>BG Type</th>
              <th>Issuing Bank</th>
              <th>BG Number</th>
              <th>Amount (INR)</th>
              <th>Expiry Date</th>
              <th>Download Letter</th>
              <th>Status</th>
            </tr>
          </thead>

          <tbody>
            {loading ? (
              <tr>
                <td colSpan="11" className={styles.noData}>Loading data…</td>
              </tr>
            ) : currentData.length === 0 ? (
              <tr>
                <td colSpan="11" className={styles.noData}>
                  {searchTerm ? "No matching records found" : "No data available in table"}
                </td>
              </tr>
            ) : (
              currentData.map((item, index) => {
                const isExpired   = new Date(item.bg_valid_upto) < new Date();
                const cleanBg     = (item.bg_number || "").replace(/['"]/g, "").trim();
                const isSubmitted = item.bg_letter_status === "Submitted";

                return (
                  <tr key={`${item.contract_id}-${cleanBg}-${index}`}>
                    <td>{startIndex + index}</td>

                    {/* Contract ID → contract detail page */}
                    <td>
                      <a href={`/wrpmis/get-contract/${item.contract_id}`}>
                        {item.contract_id}
                      </a>
                    </td>

                    <td>{item.contract_short_name}</td>
                    <td>{item.contractor_name}</td>
                    <td>{item.bg_type_fk}</td>
                    <td>{item.issuing_bank}</td>

                    {/* BG Number → same contract detail page */}
                    <td>
                      <a href={`/wrpmis/get-contract/${item.contract_id}`}>
                        {cleanBg}
                      </a>
                    </td>

                    <td>{item.bg_value}</td>

                    {/* Expiry date — red when already expired */}
                    <td style={{ color: isExpired ? "red" : "inherit", whiteSpace: "nowrap" }}>
                      {item.bg_valid_upto}
                    </td>

                    {/* Download button — disabled when BG is already expired */}
                    <td>
                      <button
                        type="button"
                        className={`${styles.downloadBtn}${isExpired ? ` ${styles.downloadBtnDisabled}` : ""}`}
                        disabled={isExpired}
                        title="Download BG Expiry Letter"
                        onClick={() => handleDownload(item.contract_id, cleanBg)}
                      >
                        ⬇ Download
                      </button>
                    </td>

                    {/* Status dropdown — locked once Submitted */}
                    <td>
                      <select
                        className={styles.statusSelect}
                        disabled={isSubmitted}
                        value={item.bg_letter_status || "Not Submitted"}
                        onChange={(e) =>
                          handleStatusChange(item.contract_id, e.target.value, cleanBg)
                        }
                      >
                        <option value="Not Submitted">Not Submitted</option>
                        <option value="Submitted">Submitted</option>
                      </select>
                    </td>
                  </tr>
                );
              })
            )}
          </tbody>
        </table>

        {/* Pagination footer */}
        <div className={styles.tableFooter}>
          <div className={styles.tableInfo}>
            Showing {startIndex} to {endIndex} of {totalEntries} entries
          </div>
          <div className={styles.pagination}>{renderPageButtons()}</div>
        </div>
      </div>
    </div>
  );
};

export default BGContractualLetters;