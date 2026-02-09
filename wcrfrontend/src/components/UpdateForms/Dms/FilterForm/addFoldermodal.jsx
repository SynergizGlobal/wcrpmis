import React, { useState } from 'react';
import styles from './FilterForm.module.css';

export default function AddFolderModal({ folders, onClose, onConfirm }) {
  const [folderName, setFolderName] = useState('');
  const [parentFolderId, setParentFolderId] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    if (folderName.trim()) {
      onConfirm(folderName.trim(), parentFolderId || null);
    }
  };

  const topLevelFolders = folders.filter(folder => folder.level === 0);

  return (
    <div className={styles.modalOverlay}>
      <div className={styles.modalContent}>
        <div className={styles.modalHeader}>
          <h3>Add New Folder</h3>
          <button className={styles.modalClose} onClick={onClose}>×</button>
        </div>
        <form onSubmit={handleSubmit}>
          <div className={styles.modalBody}>
            <div className={styles.formGroup}>
              <label htmlFor="folderName">Folder Name:</label>
              <input
                type="text"
                id="folderName"
                value={folderName}
                onChange={(e) => setFolderName(e.target.value)}
                className={styles.textInput}
                placeholder="Enter folder name"
                autoFocus
              />
            </div>
            <div className={styles.formGroup}>
              <label htmlFor="parentFolder">Parent Folder (Optional):</label>
              <select
                id="parentFolder"
                value={parentFolderId}
                onChange={(e) => setParentFolderId(e.target.value)}
                className={styles.selectInput}
              >
                <option value="">None (Top Level)</option>
                {topLevelFolders.map(folder => (
                  <option key={folder.id} value={folder.id}>
                    {folder.name}
                  </option>
                ))}
              </select>
            </div>
          </div>
          <div className={styles.modalFooter}>
            <button type="button" className={styles.modalButtonSecondary} onClick={onClose}>
              Cancel
            </button>
            <button type="submit" className={styles.modalButtonPrimary}>
              Add Folder
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}