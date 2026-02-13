import React, { useRef, useState } from "react";
import styles from "./FAQ.module.css";

const FAQ = () => {
  const [showModal, setShowModal] = useState(false);

  const [title, setTitle] = useState("");
  const [category, setCategory] = useState("");
  const [selectedFile, setSelectedFile] = useState(null);

  const [errors, setErrors] = useState({});

  const fileInputRef = useRef(null);

  const handleFileClick = () => {
    fileInputRef.current.click();
  };

  const handleFileChange = (e) => {
    if (e.target.files.length > 0) {
      setSelectedFile(e.target.files[0]);
    }
  };

  const handleSubmit = () => {
    const newErrors = {};

    if (!title.trim()) newErrors.title = true;
    if (!category.trim()) newErrors.category = true;
    if (!selectedFile) newErrors.file = true;

    setErrors(newErrors);

    if (Object.keys(newErrors).length === 0) {
      alert("Form is valid. Ready to upload.");
      setShowModal(false);
      setTitle("");
      setCategory("");
      setSelectedFile(null);
      setErrors({});
    }
  };

  return (
    <div className={styles.pageWrapper}>
      <div className={styles.card}>
        <div className={styles.header}>
          User Manual
        </div>

        <div className={styles.topBar}>
          <div className={styles.searchWrapper}>
            <input
              type="text"
              placeholder="Search ..."
              className={styles.searchInput}
            />
            <span className={styles.searchIcon}>🔍</span>
          </div>

          <button
            className={styles.uploadBtn}
            onClick={() => setShowModal(true)}
          >
            Upload
          </button>
        </div>

        <div className={styles.emptyState}>
          No folders available
        </div>
      </div>

      {showModal && (
        <div className={styles.modalOverlay}>
          <div className={styles.modal}>
            <div className={styles.modalHeader}>
              <span>Faq File Upload</span>
              <span
                className={styles.closeBtn}
                onClick={() => {
                  setShowModal(false);
                  setErrors({});
                }}
              >
                Close
              </span>
            </div>

            <div className={styles.modalBody}>
              <div className={styles.formRow}>
                <div className={styles.formGroup}>
                  <label>Title</label>
                  <input
                    type="text"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                  />
                  {errors.title && (
                    <div className={styles.requiredText}>Required</div>
                  )}
                </div>

                <div className={styles.formGroup}>
                  <label>Category</label>
                  <input
                    type="text"
                    value={category}
                    onChange={(e) => setCategory(e.target.value)}
                  />
                  {errors.category && (
                    <div className={styles.requiredText}>Required</div>
                  )}
                </div>
              </div>

              <div className={styles.fileRow}>
                <button
                  type="button"
                  className={styles.uploadFileBtn}
                  onClick={handleFileClick}
                >
                  Upload File
                </button>

                <span className={styles.fileLine}></span>
              </div>

              {selectedFile && (
                <div className={styles.fileName}>
                  Selected file: <strong>{selectedFile.name}</strong>
                </div>
              )}

              {errors.file && (
                <div className={styles.requiredText}>Required</div>
              )}

              <input
                type="file"
                ref={fileInputRef}
                className={styles.hiddenFileInput}
                onChange={handleFileChange}
              />

              <div className={styles.modalActions}>
                <button
                  type="button"
                  className={styles.submitBtn}
                  onClick={handleSubmit}
                >
                  UPLOAD
                </button>
                <button
                  type="button"
                  className={styles.cancelBtn}
                  onClick={() => {
                    setShowModal(false);
                    setErrors({});
                  }}
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
};

export default FAQ;
