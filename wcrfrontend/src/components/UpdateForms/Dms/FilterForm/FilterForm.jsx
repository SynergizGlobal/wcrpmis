import React, { useState, useEffect } from 'react';
import styles from './FilterForm.module.css';
import UploadModal from './UploadModal';
import DeleteModal from './DeleteModal';
import AddDepartmentModal from './AddDepartmentModal';
import AddStatusModal from './addStatusmodal';
import AddFolderModal from './addFoldermodal';
import axiosInstance from '../../../../api/axiosInstance';

import { FaUpload, FaTrash } from "react-icons/fa";
import { MdEditNote } from "react-icons/md";

// =========================
// EDIT FOLDER MODAL
// Fields are driven by the FOLDER_EDIT_FIELDS array (same pattern as
// LandAcquisitionProcessForm's `columns` prop — your teammate's "! command").
// To add/remove/reorder fields, just edit FOLDER_EDIT_FIELDS below.
// =========================

function EditFolderModal({ folder, onClose, onConfirm }) {
  const [folderName, setFolderName] = useState(folder?.name ?? '');
  const [subFolderInput, setSubFolderInput] = useState('');

  // Pre-populate existing sub-folders from API data
  const existingSubFolders = folder?.subFolders || folder?.subfolders || folder?.children || [];
  const [subFolders, setSubFolders] = useState(
    existingSubFolders.map(sf => sf.name || sf)
  );

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
    if (!folderName.trim()) {
      alert('Folder name is required');
      return;
    }
    onConfirm(folder.id || folder._id, { name: folderName, subFolders });
  };

  return (
    <div className={styles.modalOverlay}>
      <div className={styles.modalContent}>

        <div className={styles.modalHeader}>
          <h3>Edit Folder</h3>
          <button className={styles.modalClose} onClick={onClose}>×</button>
        </div>

        <div className={styles.modalBody}>
          <form onSubmit={handleSubmit}>

            {/* Folder Name */}
            <div className={styles.formGroup}>
              <label>Folder Name:</label>
              <input
                type="text"
                className={styles.textInput}
                value={folderName}
                onChange={e => setFolderName(e.target.value)}
                placeholder="Enter folder name"
              />
            </div>

            {/* Sub-folders */}
            <div className={styles.formGroup}>
              <label>Sub-folders:</label>
              <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
                <input
                  type="text"
                  className={styles.textInput}
                  value={subFolderInput}
                  onChange={e => setSubFolderInput(e.target.value)}
                  placeholder="Enter sub-folder name"
                  onKeyDown={e => { if (e.key === 'Enter') { e.preventDefault(); handleAddSubFolder(); } }}
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

              {/* Sub-folder chips */}
              {subFolders.length > 0 && (
                <div style={{ display: 'flex', flexWrap: 'wrap', gap: '6px', marginTop: '10px' }}>
                  {subFolders.map(sf => (
                    <span key={sf} style={{
                      display: 'inline-flex',
                      alignItems: 'center',
                      gap: '6px',
                      backgroundColor: '#e8edf7',
                      color: '#3a4a6b',
                      borderRadius: '12px',
                      padding: '3px 10px',
                      fontSize: '12px',
                      fontWeight: 500,
                      border: '1px solid #c5d0e8',
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
                    </span>
                  ))}
                </div>
              )}
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
                Save Changes
              </button>
            </div>
          </form>
        </div>

      </div>
    </div>
  );
}


// =========================
// FOLDER ROW — clean chip display with expand/collapse
// =========================
const PREVIEW_COUNT = 3;

function FolderRow({ folder, onEdit, onDelete, styles }) {
  const [expanded, setExpanded] = React.useState(false);
  const subFolderList = folder.subFolders || folder.subfolders || folder.children || [];
  const visible = expanded ? subFolderList : subFolderList.slice(0, PREVIEW_COUNT);
  const hasMore = subFolderList.length > PREVIEW_COUNT;

  return (
    <tr>
      <td style={{ fontWeight: 500, verticalAlign: 'top', paddingTop: '12px', whiteSpace: 'nowrap' }}>
        {folder.name}
      </td>
      <td style={{ verticalAlign: 'top', padding: '8px 10px' }}>
        {subFolderList.length === 0 ? (
          <span style={{ color: '#aaa', fontStyle: 'italic', fontSize: '12px' }}>No sub-folders</span>
        ) : (
          <div style={{ display: 'flex', flexWrap: 'wrap', gap: '6px', alignItems: 'center' }}>
            {visible.map((sf, i) => (
              <span key={i} style={{
                display: 'inline-block',
                backgroundColor: '#e8edf7',
                color: '#3a4a6b',
                borderRadius: '12px',
                padding: '3px 10px',
                fontSize: '12px',
                fontWeight: 500,
                border: '1px solid #c5d0e8',
                whiteSpace: 'nowrap',
              }}>
                {sf.name || sf}
              </span>
            ))}
            {hasMore && (
              <button
                type="button"
                onClick={() => setExpanded(prev => !prev)}
                style={{
                  background: 'none',
                  border: 'none',
                  color: '#4f6ef7',
                  fontSize: '12px',
                  cursor: 'pointer',
                  fontWeight: 600,
                  padding: '3px 6px',
                  whiteSpace: 'nowrap',
                }}
              >
                {expanded
                  ? '▲ Show less'
                  : `+${subFolderList.length - PREVIEW_COUNT} more`}
              </button>
            )}
          </div>
        )}
      </td>
      <td style={{ verticalAlign: 'top', paddingTop: '8px' }}>
        <div className={styles.actionBtns}>
          <button className="btn btn-2 btn-primary" onClick={onEdit}>
            <MdEditNote size="16" />
          </button>
          <button className="btn btn-2 btn-red" onClick={onDelete}>
            <FaTrash size="16" />
          </button>
        </div>
      </td>
    </tr>
  );
}

// =========================
// MAIN COMPONENT
// =========================

export default function FilterForm() {

  const [searchValues, setSearchValues] = useState({
    department: '',
    status: '',
    folder: ''
  });

  const [departments, setDepartments] = useState([]);
  const [statuses, setStatuses] = useState([]);
  const [folders, setFolders] = useState([]);

  const [selectedDepartment, setSelectedDepartment] = useState(null);
  const [selectedStatus, setSelectedStatus] = useState(null);

  const [showUploadModal, setShowUploadModal] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [showAddDepartmentModal, setShowAddDepartmentModal] = useState(false);
  const [showAddStatusModal, setShowAddStatusModal] = useState(false);
  const [showAddFolderModal, setShowAddFolderModal] = useState(false);

  // NEW: edit folder modal state
  const [showEditFolderModal, setShowEditFolderModal] = useState(false);
  const [folderToEdit, setFolderToEdit] = useState(null);

  const [modalType, setModalType] = useState('');
  const [selectedItemId, setSelectedItemId] = useState(null);
  const [selectedItemName, setSelectedItemName] = useState('');


  useEffect(() => {
    fetchDepartments();
    fetchStatuses();
    fetchFolders();
  }, []);

  const extractData = (res) => {
    if (Array.isArray(res.data)) return res.data;
    if (Array.isArray(res.data?.data)) return res.data.data;
    return [];
  };

  const fetchDepartments = async () => {
    try {
      const res = await axiosInstance.get('/api/departments/get');
      setDepartments(extractData(res));
    } catch (err) {
      console.error(err);
    }
  };

  const fetchStatuses = async () => {
    try {
      const res = await axiosInstance.get('/api/statuses/get');
      setStatuses(extractData(res));
    } catch (err) {
      console.error(err);
    }
  };

  const fetchFolders = async () => {
    try {
      const res = await axiosInstance.get('/api/folders/get');
      const folderList = extractData(res);

      // Fetch subfolders for each folder from /api/subfolders/{folderId}
      const foldersWithSubs = await Promise.all(
        folderList.map(async (folder) => {
          const folderId = folder.id || folder._id;
          try {
            const subRes = await axiosInstance.get(`/api/subfolders/${folderId}`);
            const subs = Array.isArray(subRes.data) ? subRes.data
                       : Array.isArray(subRes.data?.data) ? subRes.data.data
                       : [];
            return { ...folder, subFolders: subs };
          } catch {
            return { ...folder, subFolders: [] };
          }
        })
      );

      setFolders(foldersWithSubs);
    } catch (err) {
      console.error(err);
    }
  };

  const handleSearchChange = (field, value) => {
    setSearchValues(prev => ({
      ...prev,
      [field]: value
    }));
  };

  // =========================
  // ADD DEPARTMENT
  // =========================
  const handleAddDepartment = async (name) => {
    try {
      await axiosInstance.post('/api/departments', { name });
      fetchDepartments();
      setShowAddDepartmentModal(false);
    } catch (err) {
      alert(err.response?.data || 'Failed to add department');
    }
  };

  // =========================
  // ADD STATUS
  // =========================
  const handleAddStatus = async (name) => {
    try {
      await axiosInstance.post('/api/statuses/create', { name });
      fetchStatuses();
      setShowAddStatusModal(false);
    } catch (err) {
      alert(err.response?.data || 'Failed to add status');
    }
  };

  // =========================
  // ADD FOLDER
  // =========================
  const handleAddFolder = async (folderName, parentFolderId, subFolders = []) => {
    const exists = folders.some(
      f => f.name?.toLowerCase() === folderName.toLowerCase()
    );

    if (exists) {
      alert('Folder with this name already exists');
      return;
    }

    try {
      const res = await axiosInstance.post('/api/folders/create', {
        name: folderName,
        parentFolderId: parentFolderId || null
      });

      const newFolderId =
        res.data?.id || res.data?._id ||
        res.data?.data?.id || res.data?.data?._id;

      if (subFolders.length > 0 && newFolderId) {
        await Promise.all(
          subFolders.map(sfName =>
            axiosInstance.post('/api/folders/create', {
              name: sfName,
              parentFolderId: newFolderId
            })
          )
        );
      }

      fetchFolders();
      setShowAddFolderModal(false);
    } catch (err) {
      const msg =
        err.response?.data?.message ||
        err.response?.data ||
        'Folder creation failed';
      alert(msg);
    }
  };

  // =========================
  // EDIT FOLDER
  // =========================
  const handleEditFolderClick = (folder) => {
    setFolderToEdit(folder);
    setShowEditFolderModal(true);
  };

  const handleEditFolderConfirm = async (id, updatedData) => {
    try {
      // 1. Update the folder name via existing folder update endpoint
      await axiosInstance.put(`/api/folders/update-folder/${id}`, { name: updatedData.name });

      // 2. Find truly new sub-folders (not already in DB) and POST each to /api/subfolders/create/{folderId}
      const existingSubFolders = folderToEdit?.subFolders || [];
      const existingNames = existingSubFolders.map(sf => (sf.name || sf).toLowerCase());
      const newSubFolders = (updatedData.subFolders || []).filter(
        sfName => !existingNames.includes(sfName.toLowerCase())
      );

      if (newSubFolders.length > 0) {
        await Promise.all(
          newSubFolders.map(sfName =>
            axiosInstance.post(`/api/subfolders/create/${id}`, { name: sfName })
          )
        );
      }

      fetchFolders();
      setShowEditFolderModal(false);
      setFolderToEdit(null);
    } catch (err) {
      const msg =
        err.response?.data?.message ||
        err.response?.data ||
        'Folder update failed';
      alert(msg);
    }
  };

  // =========================
  // DELETE
  // =========================
  const handleDeleteConfirm = async () => {
    try {
      if (modalType === 'department') {
        await axiosInstance.delete(`/api/departments/${selectedItemId}`);
        fetchDepartments();
      }
      if (modalType === 'status') {
        await axiosInstance.delete(`/api/statuses/${selectedItemId}`);
        fetchStatuses();
      }
      if (modalType === 'folder') {
        await axiosInstance.delete(`/api/folders/delete-folder/${selectedItemId}`);
        fetchFolders();
      }
      setShowDeleteModal(false);
    } catch (err) {
      alert(err.response?.data || 'Delete failed');
    }
  };

  // =========================
  // UPLOAD
  // =========================
  const handleUploadConfirm = async (file) => {
    const formData = new FormData();
    formData.append('file', file);
    try {
      await axiosInstance.post('/api/upload', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
      setShowUploadModal(false);
    } catch (err) {
      alert(err.response?.data || 'Upload failed');
    }
  };

  const handleUploadClick = (type, id) => {
    setModalType(type);
    if (type === 'department') setSelectedDepartment(id);
    if (type === 'status') setSelectedStatus(id);
    setShowUploadModal(true);
  };

  const handleDeleteClick = (type, id, name) => {
    setModalType(type);
    setSelectedItemId(id);
    setSelectedItemName(name);
    setShowDeleteModal(true);
  };

  // =========================
  // FILTERS
  // =========================
  const filteredDepartments = departments.filter(d =>
    d.name?.toLowerCase().includes(searchValues.department.toLowerCase())
  );

  const filteredStatuses = statuses.filter(s =>
    s.name?.toLowerCase().includes(searchValues.status.toLowerCase())
  );

  const filteredFolders = folders.filter(f =>
    f.name?.toLowerCase().includes(searchValues.folder.toLowerCase())
  );

  // =========================
  // UPLOAD MODAL NAME LOOKUP
  // =========================
  const uploadModalName = modalType === 'department'
    ? departments.find(d => (d.id || d._id) === selectedDepartment)?.name
    : statuses.find(s => (s.id || s._id) === selectedStatus)?.name;

  return (
    <div className={styles.container}>
      <div className={styles.mainLayout}>

        {/* DEPARTMENT */}
        <div className={styles.card}>
          <div className={styles.sectionContainer}>
            <h2 className={styles.sectionTitle}>Department</h2>

            <div className={styles.addSection}>
              <button className={styles.addButton}
                onClick={() => setShowAddDepartmentModal(true)}>
                Add Department
              </button>

              <div className={styles.searchBox}>
                <input
                  className={styles.searchInput}
                  placeholder="Search"
                  value={searchValues.department}
                  onChange={e => handleSearchChange('department', e.target.value)}
                />
              </div>
            </div>

            <div className="dataTable">
              <table className={styles.tableWrapper}>
                <thead>
                  <tr>
                    <th>Name</th>
                    <th>Action</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredDepartments.map(d => {
                    const id = d.id || d._id;
                    return (
                      <tr key={id}>
                        <td>{d.name}</td>
                        <td>
                          <div className={styles.actionBtns}>
                            <button className="btn btn-2 btn-primary" onClick={() => handleUploadClick('department', id)}>
                              <FaUpload size="16" />
                            </button>
                            <button
                              className="btn btn-2 btn-red"
                              onClick={() => handleDeleteClick('department', id, d.name)}
                            >
                              <FaTrash size="16" />
                            </button>
                          </div>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </div>
        </div>

        {/* STATUS */}
        <div className={styles.card}>
          <div className={styles.sectionContainer}>
            <h2 className={styles.sectionTitle}>Status</h2>

            <div className={styles.addSection}>
              <button className={styles.addButton}
                onClick={() => setShowAddStatusModal(true)}>
                Add Status
              </button>

              <div className={styles.searchBox}>
                <input
                  className={styles.searchInput}
                  placeholder="Search"
                  value={searchValues.status}
                  onChange={e => handleSearchChange('status', e.target.value)}
                />
              </div>
            </div>

            <div className="dataTable">
              <table className={styles.tableWrapper}>
                <thead>
                  <tr>
                    <th>Name</th>
                    <th>Action</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredStatuses.map(s => {
                    const id = s.id || s._id;
                    return (
                      <tr key={id}>
                        <td>{s.name}</td>
                        <td>
                          <div className={styles.actionBtns}>
                            <button className="btn btn-2 btn-primary" onClick={() => handleUploadClick('status', id)}>
                              <FaUpload size="16" />
                            </button>
                            <button
                              className="btn btn-2 btn-red"
                              onClick={() => handleDeleteClick('status', id, s.name)}
                            >
                              <FaTrash size="16" />
                            </button>
                          </div>
                        </td>
                      </tr>
                    );
                  })}
                </tbody>
              </table>
            </div>
          </div>
        </div>

        {/* FOLDERS */}
        <div className={styles.card}>
          <div className={styles.sectionContainer}>
            <h2 className={styles.sectionTitle}>Folders</h2>

            <button className={styles.addButton}
              onClick={() => setShowAddFolderModal(true)}>
              Add Folder
            </button>
            <br />

            <div className="dataTable">
              <table className={styles.tableWrapper}>
                <thead>
                  <tr>
                    <th>Name</th>
                    <th>Sub-folders</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredFolders.map(f => {
                    const id = f.id || f._id;
                    return (
                      <FolderRow
                        key={id}
                        folder={f}
                        onEdit={() => handleEditFolderClick(f)}
                        onDelete={() => handleDeleteClick('folder', id, f.name)}
                        styles={styles}
                      />
                    );
                  })}
                </tbody>
              </table>
            </div>
          </div>
        </div>

      </div>

      {/* =========================
          MODALS
      ========================= */}

      {showUploadModal && (
        <UploadModal
          type={modalType}
          name={uploadModalName}
          onClose={() => setShowUploadModal(false)}
          onConfirm={handleUploadConfirm}
        />
      )}

      {showDeleteModal && (
        <DeleteModal
          type={modalType}
          name={selectedItemName}
          onClose={() => setShowDeleteModal(false)}
          onConfirm={handleDeleteConfirm}
        />
      )}

      {showAddDepartmentModal && (
        <AddDepartmentModal
          onClose={() => setShowAddDepartmentModal(false)}
          onConfirm={handleAddDepartment}
        />
      )}

      {showAddStatusModal && (
        <AddStatusModal
          onClose={() => setShowAddStatusModal(false)}
          onConfirm={handleAddStatus}
        />
      )}

      {showAddFolderModal && (
        <AddFolderModal
          folders={folders}
          onClose={() => setShowAddFolderModal(false)}
          onConfirm={handleAddFolder}
        />
      )}

      {/* NEW: Edit Folder Modal */}
      {showEditFolderModal && folderToEdit && (
        <EditFolderModal
          folder={folderToEdit}
          onClose={() => {
            setShowEditFolderModal(false);
            setFolderToEdit(null);
          }}
          onConfirm={handleEditFolderConfirm}
        />
      )}
    </div>
  );
}