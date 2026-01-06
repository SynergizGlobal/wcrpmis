import React, { useEffect, useState } from "react";
import { API_BASE_URL } from "../../../../../config.js";
import { FaEdit, FaTrash } from "react-icons/fa";
import styles from "./IssueCategory.module.css";
import Swal from "sweetalert2";

export default function IssueCategory() {
  const [rows, setRows] = useState([]);

  const [showModal, setShowModal] = useState(false);
  const [mode, setMode] = useState("add");
  const [search, setSearch] = useState("");

  const [issueCategory, setIssueCategory] = useState("");
  const [issueCategoryOld, setIssueCategoryOld] = useState("");

  /* ================= FETCH ================= */
  const fetchData = async () => {
    const res = await fetch(`${API_BASE_URL}/issue-category`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({})
    });

    const json = await res.json();
    if (json.status !== "success") return;

    const categoryList = json.issueCategoryList || [];
    const countList = json.issueCategoryDetails?.countList || [];


    // Build countMap → category|table
    const countMap = {};
    countList.forEach(c => {
      countMap[`${c.category}|${c.tName}`] = Number(c.count || 0);
    });

    // Merge category + counts
    const merged = categoryList.map((c, idx) => ({
      id: idx + 1,
      category: c.category,
      issueCount: countMap[`${c.category}|issue`] || 0,
      issueCategoryTitleCount:
        countMap[`${c.category}|issue_category_title`] || 0,
      issueContractCategoryCount:
        countMap[`${c.category}|issue_contarct_category`] || 0
    }));

    setRows(merged);
  };

  useEffect(() => {
    fetchData();
  }, []);

  /* ================= SEARCH ================= */
  const filteredRows = rows.filter(r =>
    r.category.toLowerCase().includes(search.toLowerCase())
  );

  /* ================= ADD ================= */
  const handleAdd = () => {
    setMode("add");
    setIssueCategory("");
    setShowModal(true);
  };

  /* ================= EDIT ================= */
  const handleEdit = row => {
    setMode("edit");
    setIssueCategory(row.category);
    setIssueCategoryOld(row.category);
    setShowModal(true);
  };

  /* ================= SAVE ================= */
  const handleSave = async () => {
    if (!issueCategory.trim()) {
      window.alert("Category is required");
      return;
    }

    const formData = new FormData();
    let message = "";

    try {
      if (mode === "add") {
        formData.append("category", issueCategory);

        await fetch(`${API_BASE_URL}/add-issue-category`, {
          method: "POST",
          body: formData
        });

        message = "Issue Category added successfully!";
      } else {
        formData.append("value_old", issueCategoryOld);
        formData.append("value_new", issueCategory);

        await fetch(`${API_BASE_URL}/update-issue-category`, {
          method: "POST",
          body: formData
        });

        message = "Issue Category updated successfully!";
      }

      window.alert(message);
      setShowModal(false);
      fetchData();
    } catch (error) {
      window.alert("Something went wrong. Please try again.");
    }
  };


  /* ================= DELETE ================= */
  const handleDelete = row => {
    Swal.fire({
      title: "Are you sure?",
      text: "You will be deleting this Issue Category!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Yes, delete it!",
      cancelButtonText: "Cancel"
    }).then(async result => {
      if (result.isConfirmed) {
        const formData = new FormData();
        formData.append("category", row.category);

        await fetch(`${API_BASE_URL}/delete-issue-category`, {
          method: "POST",
          body: formData
        });

        Swal.fire("Deleted!", "Issue Category deleted.", "success");
        fetchData();
      }
    });
  };

  return (
    <div className={styles.container}>
      <div className="card">
        <div className="formHeading">
          <h2 className="center-align">Issue Category</h2>
        </div>

        <div className="innerPage">
          <div className={styles.topActions}>
            <button className="btn btn-primary" onClick={handleAdd}>
              + Add Category
            </button>
          </div>

          <div className={styles.searchBox}>
            <input
              type="text"
              placeholder="Search"
              value={search}
              onChange={e => setSearch(e.target.value)}
            />
            {search && (
              <span className={styles.clear} onClick={() => setSearch("")}>
                ✕
              </span>
            )}
          </div>

          <div className={`dataTable ${styles.tableWrapper}`}>
            <table className={styles.table}>
              <thead>
                <tr>
                  <th>Category</th>
                  <th>Issue</th>
                  <th>Category Title</th>
                  <th>Contract Category</th>
                  <th className={styles.actionCol}>Action</th>
                </tr>
              </thead>

			  <tbody>
			    {filteredRows.map(r => {
			      const canDelete =
			        !r.issueCount &&
			        !r.issueCategoryTitleCount &&
			        !r.issueContractCategoryCount;

			      return (
			        <tr key={r.id}>
			          <td>{r.category}</td>
			          <td>{r.issueCount ? `(${r.issueCount})` : ""}</td>
			          <td>
			            {r.issueCategoryTitleCount
			              ? `(${r.issueCategoryTitleCount})`
			              : ""}
			          </td>
			          <td>
			            {r.issueContractCategoryCount
			              ? `(${r.issueContractCategoryCount})`
			              : ""}
			          </td>

			          <td className={styles.actionCol}>
			            {/* EDIT → always visible */}
			            <button
			              className={styles.editBtn}
			              onClick={() => handleEdit(r)}
			              title="Edit"
			            >
			              <FaEdit />
			            </button>

			            {/* DELETE → only if no FK references */}
			            {canDelete && (
			              <button
			                className={styles.deleteBtn}
			                onClick={() => handleDelete(r)}
			                title="Delete"
			              >
			                <FaTrash />
			              </button>
			            )}
			          </td>
			        </tr>
			      );
			    })}

			    {filteredRows.length === 0 && (
			      <tr>
			        <td colSpan={5} className="center-align">
			          No records found
			        </td>
			      </tr>
			    )}
			  </tbody>

            </table>
          </div>

          {/* FOOTER */}
          <div className={styles.footerText}>
            Showing {filteredRows.length} of {filteredRows.length} entries
          </div>
        </div>
      </div>

      {/* ================= MODAL ================= */}
      {showModal && (
        <div className={styles.modalOverlay}>
          <div className={styles.modal}>
            <div className={styles.modalHeader}>
              <span>{mode === "add" ? "Add" : "Update"} Category</span>
              <span
                className={styles.close}
                onClick={() => setShowModal(false)}
              >
                ✕
              </span>
            </div>

            <div className={styles.modalBody}>
              <input
                type="text"
                className={styles.input}
                value={issueCategory}
                onChange={e => setIssueCategory(e.target.value)}
                placeholder="Enter Category"
              />

              <div className={styles.modalActions}>
                <button className="btn btn-primary" onClick={handleSave}>
                  {mode === "add" ? "ADD" : "UPDATE"}
                </button>
                <button
                  className="btn btn-white"
                  onClick={() => setShowModal(false)}
                >
                  CANCEL
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
