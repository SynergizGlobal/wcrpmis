import React, { useState, useRef } from 'react';

export default function UploadModal({ type, name, onClose, onConfirm }) {
  const [selectedFile, setSelectedFile] = useState(null);
  const inputRef = useRef(null);

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file) setSelectedFile(file);
  };

  const handleRemove = () => {
    setSelectedFile(null);
    if (inputRef.current) inputRef.current.value = '';
  };

  const formatSize = (bytes) => {
    if (bytes < 1024) return `${bytes} B`;
    if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)} KB`;
    return `${(bytes / (1024 * 1024)).toFixed(1)} MB`;
  };

  return (
    <div style={s.overlay}>
      <div style={s.modal}>

        {/* Header */}
        <div style={s.header}>
          <h3 style={s.title}>Upload File</h3>
          <button style={s.closeBtn} onClick={onClose}>×</button>
        </div>

        <div style={s.divider} />

        {/* Body */}
        <div style={s.body}>

          {/* Context row */}
          <div style={s.infoRow}>
            <span style={s.label}>{type === 'department' ? 'Department' : 'Status'}:</span>
            <span style={s.value}>{name}</span>
          </div>

          {/* File input */}
          <div style={s.fieldGroup}>
            <label style={s.fieldLabel} htmlFor="upload-file">
              Select File <span style={s.required}>*</span>
            </label>
            <input
              ref={inputRef}
              id="upload-file"
              type="file"
              style={s.fileInput}
              onChange={handleFileChange}
            />
          </div>

          {/* Selected file preview */}
          {selectedFile && (
            <div style={s.filePreview}>
              <div style={s.fileDetails}>
                <span style={s.fileIcon}>📎</span>
                <div>
                  <p style={s.fileName}>{selectedFile.name}</p>
                  <p style={s.fileSize}>{formatSize(selectedFile.size)}</p>
                </div>
              </div>
              <button style={s.removeBtn} onClick={handleRemove}>Remove</button>
            </div>
          )}

        </div>

        <div style={s.divider} />

        {/* Footer */}
        <div style={s.footer}>
          <button style={s.cancelBtn} onClick={onClose}>Cancel</button>
          <button
            style={{ ...s.uploadBtn, ...(selectedFile ? {} : s.uploadBtnDisabled) }}
            onClick={() => selectedFile && onConfirm(selectedFile)}
            disabled={!selectedFile}
          >
            Upload
          </button>
        </div>

      </div>
    </div>
  );
}

const s = {
  overlay: {
    position: 'fixed',
    inset: 0,
    background: 'rgba(0, 0, 0, 0.4)',
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    zIndex: 1000,
  },
  modal: {
    background: '#ffffff',
    border: '1px solid #b0b0b0',
    borderRadius: '4px',
    width: '440px',
    maxWidth: '95vw',
    boxShadow: '0 4px 16px rgba(0,0,0,0.15)',
    fontFamily: 'Arial, sans-serif',
  },
  header: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    padding: '14px 20px',
    background: '#1a3a5c',
  },
  title: {
    margin: 0,
    fontSize: '15px',
    fontWeight: '700',
    color: '#ffffff',
    letterSpacing: '0.3px',
  },
  closeBtn: {
    background: 'none',
    border: 'none',
    cursor: 'pointer',
    fontSize: '22px',
    color: '#ffffff',
    lineHeight: 1,
    padding: '0 4px',
    opacity: 0.85,
  },
  divider: {
    height: '1px',
    background: '#d0d0d0',
  },
  body: {
    padding: '20px 24px',
    display: 'flex',
    flexDirection: 'column',
    gap: '16px',
  },
  infoRow: {
    display: 'flex',
    alignItems: 'center',
    gap: '8px',
    padding: '10px 12px',
    background: '#f4f6f9',
    border: '1px solid #dce1e9',
    borderRadius: '3px',
  },
  label: {
    fontSize: '13px',
    color: '#555',
    fontWeight: '600',
  },
  value: {
    fontSize: '13px',
    color: '#1a1a1a',
    fontWeight: '700',
  },
  fieldGroup: {
    display: 'flex',
    flexDirection: 'column',
    gap: '6px',
  },
  fieldLabel: {
    fontSize: '13px',
    fontWeight: '600',
    color: '#333',
  },
  required: {
    color: '#c0392b',
    marginLeft: '2px',
  },
  fileInput: {
    fontSize: '13px',
    color: '#333',
    border: '1px solid #b0b0b0',
    borderRadius: '3px',
    padding: '6px 8px',
    background: '#fff',
    width: '100%',
    boxSizing: 'border-box',
    cursor: 'pointer',
  },
  filePreview: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'space-between',
    padding: '10px 12px',
    background: '#eef3ea',
    border: '1px solid #b8d4a8',
    borderRadius: '3px',
  },
  fileDetails: {
    display: 'flex',
    alignItems: 'center',
    gap: '10px',
  },
  fileIcon: {
    fontSize: '20px',
  },
  fileName: {
    margin: 0,
    fontSize: '13px',
    fontWeight: '600',
    color: '#1a1a1a',
    maxWidth: '260px',
    overflow: 'hidden',
    textOverflow: 'ellipsis',
    whiteSpace: 'nowrap',
  },
  fileSize: {
    margin: '2px 0 0',
    fontSize: '12px',
    color: '#555',
  },
  removeBtn: {
    background: 'none',
    border: '1px solid #999',
    borderRadius: '3px',
    padding: '4px 10px',
    fontSize: '12px',
    color: '#555',
    cursor: 'pointer',
    flexShrink: 0,
  },
  footer: {
    display: 'flex',
    justifyContent: 'flex-end',
    alignItems: 'center',
    gap: '10px',
    padding: '12px 20px',
    background: '#f4f6f9',
  },
  cancelBtn: {
    padding: '7px 20px',
    borderRadius: '3px',
    border: '1px solid #999',
    background: '#fff',
    fontSize: '13px',
    fontWeight: '500',
    color: '#333',
    cursor: 'pointer',
  },
  uploadBtn: {
    padding: '7px 22px',
    borderRadius: '3px',
    border: 'none',
    background: '#1a3a5c',
    fontSize: '13px',
    fontWeight: '600',
    color: '#fff',
    cursor: 'pointer',
  },
  uploadBtnDisabled: {
    background: '#8a9db5',
    cursor: 'not-allowed',
  },
};