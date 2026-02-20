import React from 'react';
import styles from './FilterForm.module.css';

export default function DeleteModal({ type, name, onClose, onConfirm }) {
  return (
    <div className={styles.modalOverlay}>
      <div className={styles.modalContent}>
        <div className={styles.modalHeader}>
          <h3>
  Delete {type.charAt(0).toUpperCase() + type.slice(1)}
</h3>

          <button
            className={styles.modalClose}
            onClick={onClose}
          >
            ×
          </button>
        </div>

        <div className={styles.modalBody}>
          <p>
            Are you sure you want to delete{' '}
            <strong>{name || 'this item'}</strong>?
          </p>
          <p className={styles.warningText}>
            This action cannot be undone.
          </p>
        </div>

        <div className={styles.modalFooter}>
          <button
            className={styles.modalButtonSecondary}
            onClick={onClose}
          >
            Cancel
          </button>
          <button
            className={styles.modalButtonDanger}
            onClick={onConfirm}
          >
            Delete
          </button>
        </div>
      </div>
    </div>
  );
}
