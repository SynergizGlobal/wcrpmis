import React, { useState } from 'react';
import styles from './FilterForm.module.css';

export default function AddStatusModal({ onClose, onConfirm }) {
  const [statusName, setStatusName] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    if (statusName.trim()) {
      onConfirm(statusName.trim());
    }
  };

  return (
    <div className={styles.modalOverlay}>
      <div className={styles.modalContent}>
        <div className={styles.modalHeader}>
          <h3>Add New Status</h3>
          <button className={styles.modalClose} onClick={onClose}>×</button>
        </div>
        <form onSubmit={handleSubmit}>
          <div className={styles.modalBody}>
            <div className={styles.formGroup}>
              <label htmlFor="statusName">Status Name:</label>
              <input
                type="text"
                id="statusName"
                value={statusName}
                onChange={(e) => setStatusName(e.target.value)}
                className={styles.textInput}
                placeholder="Enter status name"
                autoFocus
              />
            </div>
          </div>
          <div className={styles.modalFooter}>
            <button type="button" className={styles.modalButtonSecondary} onClick={onClose}>
              Cancel
            </button>
            <button type="submit" className={styles.modalButtonPrimary}>
              Add Status
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}