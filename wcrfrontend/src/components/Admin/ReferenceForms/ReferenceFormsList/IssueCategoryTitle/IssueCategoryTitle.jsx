import React, { useEffect, useState } from "react";
import { API_BASE_URL } from "../../../../../config.js";
import { FaEdit, FaTrash, FaPlus, FaTimes } from "react-icons/fa";
import styles from "./IssueCategoryTitle.module.css";
import Swal from "sweetalert2";

export default function IssueCategoryTitle() {
  const [rows, setRows] = useState([]);
  const [issueCategories, setIssueCategories] = useState([]);
  const [categoryTitles, setCategoryTitles] = useState([""]);

  const [showModal, setShowModal] = useState(false);
  const [mode, setMode] = useState("add");
  const [search, setSearch] = useState("");

  const [issueCategory, setIssueCategory] = useState("");
  const [issueCategoryOld, setIssueCategoryOld] = useState("");
  const [oldTitles, setOldTitles] = useState([]);

  /* ================= FETCH ================= */
  const fetchData = async () => {
    const res = await fetch(`${API_BASE_URL}/issue-category-title`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({})
    });

    const json = await res.json();
    if (json.status !== "success") return;

    // 🔹 Category dropdown
    setIssueCategories(
      (json.issueCategoryDetails || []).map(c => ({
        value: c.issue_category_fk,
        label: c.issue_category_fk
      }))
    );

    // 🔹 Table rows (aggregated)
    setRows(
      (json.issueCategoryTitleDetails || []).map((r, idx) => ({
        id: idx + 1,
        issueCategory: r.issue_category_fk,
        titles: r.short_description
          ? r.short_description.split(",")
          : []
      }))
    );
  };

  useEffect(() => {
    fetchData();
  }, []);

  /* ================= SEARCH ================= */
  const filteredRows = rows.filter(r => {
    const category = (r.issueCategory || "").toLowerCase();
    const titles = (r.titles || []).join(", ").toLowerCase();
    const term = search.toLowerCase();

    return category.includes(term) || titles.includes(term);
  });


  /* ================= ADD ================= */
  const handleAdd = () => {
    setMode("add");
    setIssueCategory("");
    setIssueCategoryOld("");
    setCategoryTitles([""]);
    setShowModal(true);
  };

  /* ================= EDIT ================= */
  const handleEdit = row => {
    setMode("edit");

    setIssueCategory(row.issueCategory);
    setIssueCategoryOld(row.issueCategory);

    const titles =
      Array.isArray(row.titles) && row.titles.length > 0
        ? row.titles
        : [""];

    // 🔥 STORE OLD TITLES SEPARATELY
    setOldTitles(titles);

    // Editable titles
    setCategoryTitles([...titles]);

    setShowModal(true);
  };


  /* ================= SAVE ================= */
  const handleSave = async () => {
    if (!issueCategory) {
      window.alert("Category is required");
      return;
    }

    const validTitles = categoryTitles
      .map(t => t.trim())
      .filter(Boolean);

    if (validTitles.length === 0) {
      window.alert("At least one Category Title is required");
      return;
    }

    const formData = new FormData();

    if (mode === "add") {
      // ADD expects these
      formData.append("issue_category_fk", issueCategory);
      formData.append("short_description", validTitles.join(","));
    } else {
      // 🔥 UPDATE expects *_new and value_old
      formData.append("issue_category_fk_old", issueCategoryOld);
      formData.append("issue_category_fk_new", issueCategory);

      formData.append(
        "short_description_new",
        validTitles.join(",")
      );

      // 🔥 VERY IMPORTANT (used in update loop)
      formData.append(
        "value_old",
        validTitles.join(",")
      );
    }

    const url =
      mode === "add"
        ? "add-issue-category-title"
        : "update-issue-category-title";

    await fetch(`${API_BASE_URL}/${url}`, {
      method: "POST",
      body: formData
    });

    window.alert(
      mode === "add"
        ? "Issue Category Title added successfully"
        : "Issue Category Title updated successfully"
    );

    setShowModal(false);
    fetchData();
  };


  /* ================= DELETE ================= */
  const handleDelete = row => {
    Swal.fire({
      title: "Are you sure?",
      text: "You will be deleting all titles under this category!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonText: "Yes, delete it!"
    }).then(async result => {
      if (result.isConfirmed) {
        const formData = new FormData();
        formData.append("issue_category_fk", row.issueCategory);

        await fetch(`${API_BASE_URL}/delete-issue-category-title`, {
          method: "POST",
          body: formData
        });

        Swal.fire("Deleted!", "Category Titles deleted.", "success");
        fetchData();
      }
    });
  };

  /* ================= TITLE ROWS ================= */
  const addTitleRow = () => setCategoryTitles([...categoryTitles, ""]);
  const removeTitleRow = i =>
    setCategoryTitles(categoryTitles.filter((_, idx) => idx !== i));
  const updateTitleRow = (i, val) => {
    const updated = [...categoryTitles];
    updated[i] = val;
    setCategoryTitles(updated);
  };

  return (
    <div className={styles.container}>
      <div className="card">
        <div className="formHeading">
          <h2 className="center-align">Issue Category Title</h2>
        </div>

        <div className="innerPage">
          <div className={styles.topActions}>
            <button className="btn btn-primary" onClick={handleAdd}>
              + Add Category Title
            </button>
          </div>

          <div className={styles.searchBox}>
            <input
              type="text"
              placeholder="Search Category"
              value={search}
              onChange={e => setSearch(e.target.value)}
            />
          </div>

          <div className={`dataTable ${styles.tableWrapper}`}>
            <table className={styles.table}>
              <thead>
                <tr>
                  <th>Category</th>
                  <th>Category Titles</th>
                  <th className={styles.actionCol}>Action</th>
                </tr>
              </thead>
              <tbody>
                {filteredRows.map(r => (
                  <tr key={r.id}>
                    <td>{r.issueCategory}</td>
                    <td>{r.titles.join(", ")}</td>
                    <td className={styles.actionCol}>
                      <button
                        className={styles.editBtn}
                        onClick={() => handleEdit(r)}
                      >
                        <FaEdit />
                      </button>
                      <button
                        className={styles.deleteBtn}
                        onClick={() => handleDelete(r)}
                      >
                        <FaTrash />
                      </button>
                    </td>
                  </tr>
                ))}

                {filteredRows.length === 0 && (
                  <tr>
                    <td colSpan={3} className="center-align">
                      No records found
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
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
              <h4>{mode === "add" ? "Add" : "Update"} Category Title</h4>
              <span
                className={styles.close}
                onClick={() => setShowModal(false)}
              >
                ✕
              </span>
            </div>

            <div className={styles.modalBody}>
              <p className={styles.searchableLabel}>Category</p>
              <select
                className={styles.select}
                value={issueCategory}
                onChange={e => setIssueCategory(e.target.value)}
              >
                <option value="">Select</option>
                {issueCategories.map(i => (
                  <option key={i.value} value={i.value}>
                    {i.label}
                  </option>
                ))}
              </select>

              <p className={styles.searchableLabel}>Category Title(s)</p>

              {categoryTitles.map((t, i) => (
                <div key={i} className={styles.contractRow}>
                  <input
                    className={styles.input}
                    value={t}
                    placeholder="Enter Category Title"
                    onChange={e => updateTitleRow(i, e.target.value)}
                  />
                  {categoryTitles.length > 1 && (
                    <button
                      className={styles.removeBtn}
                      onClick={() => removeTitleRow(i)}
                    >
                      <FaTimes />
                    </button>
                  )}
                </div>
              ))}

              <button className="btn btn-primary" onClick={addTitleRow}>
                <FaPlus /> Add More
              </button>

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
