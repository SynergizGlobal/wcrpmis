import React, { useState } from 'react';
import styles from './FilterForm.module.css';

export default function AddFolderModal({ folders, onClose, onConfirm }) {
  const [folderName, setFolderName] = useState('');
  const [subFolderInput, setSubFolderInput] = useState('');
  const [subFolders, setSubFolders] = useState([]);

  const handleAddSubFolder = () => {
    const trimmed = subFolderInput.trim();
    if (!trimmed) return;
    if (subFolders.includes(trimmed)) {
      alert('Sub-folder already added');
      return;
    }
    setSubFolders(prev => [...prev, trimmed]);
    setSubFolderInput('');
  };

  const handleRemoveSubFolder = (name) => {
    setSubFolders(prev => prev.filter(s => s !== name));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!folderName.trim()) return;
    onConfirm(folderName.trim(), null, subFolders);
  };

  return (
    <div className={styles.modalOverlay}>
      <div className={styles.modalContent}>
        <div className={styles.modalHeader}>
          <h3>Add Folder</h3>
          <button className={styles.modalClose} onClick={onClose}>×</button>
        </div>

        <form onSubmit={handleSubmit}>
          <div className={styles.modalBody}>

            {/* Folder Name */}
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

            {/* Sub-folders */}
            <div className={styles.formGroup}>
              <label>Sub-folders:</label>
              <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
                <input
                  type="text"
                  value={subFolderInput}
                  onChange={(e) => setSubFolderInput(e.target.value)}
                  className={styles.textInput}
                  placeholder="Enter sub-folder name"
                  onKeyDown={(e) => {
                    if (e.key === 'Enter') {
                      e.preventDefault();
                      handleAddSubFolder();
                    }
                  }}
                  style={{ flex: 1, margin: 0 }}
                />
                <button
                  type="button"
                  onClick={handleAddSubFolder}
                  className={styles.modalButtonPrimary}
                  style={{ whiteSpace: 'nowrap', padding: '8px 18px' }}
                >
                  Add
                </button>
              </div>

              {/* Sub-folder tags list */}
              {subFolders.length > 0 && (
                <ul style={{
                  marginTop: '10px',
                  padding: 0,
                  listStyle: 'none',
                  display: 'flex',
                  flexWrap: 'wrap',
                  gap: '6px'
                }}>
                  {subFolders.map(sf => (
                    <li key={sf} style={{
                      display: 'flex',
                      alignItems: 'center',
                      gap: '6px',
                      backgroundColor: '#e9ecef',
                      borderRadius: '4px',
                      padding: '4px 10px',
                      fontSize: '13px',
                      color: '#333'
                    }}>
                      {sf}
                      <button
                        type="button"
                        onClick={() => handleRemoveSubFolder(sf)}
                        style={{
                          background: 'none',
                          border: 'none',
                          cursor: 'pointer',
                          color: '#dc2626',
                          fontWeight: 'bold',
                          fontSize: '14px',
                          lineHeight: 1,
                          padding: 0,
                        }}
                      >
                        ×
                      </button>
                    </li>
                  ))}
                </ul>
              )}
            </div>

          </div>

          <div className={styles.modalFooter}>
            <button
              type="button"
              className={styles.modalButtonSecondary}
              onClick={onClose}
            >
              Cancel
            </button>
            <button
              type="submit"
              className={styles.modalButtonPrimary}
            >
              Save
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}