import React, { useState } from 'react';
import styles from './FilterForm.module.css';

export default function AddDepartmentModal({ onClose, onConfirm }) {
  const [departmentName, setDepartmentName] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    if (departmentName.trim()) {
      onConfirm(departmentName.trim());
    }
  };

  return (
    <div className={styles.modalOverlay}>
      <div className={styles.modalContent}>
        <div className={styles.modalHeader}>
          <h3>Add New Department</h3>
          <button className={styles.modalClose} onClick={onClose}>×</button>
        </div>
        <form onSubmit={handleSubmit}>
          <div className={styles.modalBody}>
            <div className={styles.formGroup}>
              <label htmlFor="departmentName">Department Name:</label>
              <input
                type="text"
                id="departmentName"
                value={departmentName}
                onChange={(e) => setDepartmentName(e.target.value)}
                className={styles.textInput}
                placeholder="Enter department name"
                autoFocus
              />
            </div>
          </div>
          <div className={styles.modalFooter}>
            <button type="button" className={styles.modalButtonSecondary} onClick={onClose}>
              Cancel
            </button>
            <button type="submit" className={styles.modalButtonPrimary}>
              Add Department
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}