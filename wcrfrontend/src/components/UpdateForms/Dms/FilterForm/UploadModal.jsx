import React from 'react';
import styles from './FilterForm.module.css';

export default function UploadModal({ type, name, onClose, onConfirm }) {
  return (
    <div className={styles.modalOverlay}>
      <div className={styles.modalContent}>
        <div className={styles.modalHeader}>
          <h3>Upload {type === 'department' ? 'Department' : 'Status'}</h3>
          <button className={styles.modalClose} onClick={onClose}>×</button>
        </div>
        <div className={styles.modalBody}>
          <p>Are you sure you want to upload files for <strong>{name}</strong>?</p>
          <div className={styles.modalForm}>
            <div className={styles.formGroup}>
              <label htmlFor="file">Select File:</label>
              <input type="file" id="file" className={styles.fileInput} />
            </div>
          </div>
        </div>
        <div className={styles.modalFooter}>
          <button className={styles.modalButtonSecondary} onClick={onClose}>
            Cancel
          </button>
          <button className={styles.modalButtonPrimary} onClick={onConfirm}>
            Upload
          </button>
        </div>
      </div>
    </div>
  );
}