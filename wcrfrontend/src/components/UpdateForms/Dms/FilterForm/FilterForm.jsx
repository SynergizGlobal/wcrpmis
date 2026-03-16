import React, { useState, useEffect } from 'react';
import styles from './FilterForm.module.css';
import UploadModal from './UploadModal';
import DeleteModal from './DeleteModal';
import AddDepartmentModal from './AddDepartmentModal';
import AddStatusModal from './addStatusmodal';
import axiosInstance from '../../../../api/axiosInstance';

import { FaUpload, FaTrash } from "react-icons/fa";

// =========================
// FOLDER TREE NODE
// =========================
function FolderTreeNode({ folder, path, depth, expandedFolders, toggleFolder, selectedPath, setSelectedPath, renamingId, setRenamingId, renameValue, setRenameValue, handleRename, onDelete }) {
  const currentPath = [...path, folder];
  const isExpanded = expandedFolders.has(folder.id);
  const isSelected = selectedPath.at(-1)?.id === folder.id;
  const hasChildren = folder.children?.length > 0;
  const [hovered, setHovered] = React.useState(false);

  return (
    <div style={{ marginLeft: depth > 0 ? '20px' : '0' }}>
      <div
        style={{
          display: 'flex',
          alignItems: 'center',
          gap: '6px',
          padding: '7px 10px',
          borderRadius: '7px',
          cursor: 'pointer',
          background: isSelected
            ? 'linear-gradient(90deg, #eef1fd 0%, #f3f5ff 100%)'
            : hovered ? '#f7f8fe' : 'transparent',
          border: isSelected ? '1px solid #c7cff5' : '1px solid transparent',
          transition: 'all 0.15s ease',
          marginBottom: '2px',
        }}
        onMouseEnter={() => setHovered(true)}
        onMouseLeave={() => setHovered(false)}
        onClick={() => setSelectedPath(currentPath)}
        onDoubleClick={() => { setRenamingId(folder.id); setRenameValue(folder.name); }}
      >
        {/* Expand Arrow */}
        <span
          style={{
            width: '18px',
            height: '18px',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: hasChildren ? '#6b7280' : 'transparent',
            fontSize: '9px',
            flexShrink: 0,
            transition: 'transform 0.2s ease',
            transform: isExpanded ? 'rotate(90deg)' : 'rotate(0deg)',
          }}
          onClick={e => { e.stopPropagation(); if (hasChildren) toggleFolder(folder.id); }}
        >
          {hasChildren ? '▶' : ''}
        </span>

        {/* Folder Icon */}
        <span style={{ fontSize: '15px', flexShrink: 0, lineHeight: 1 }}>
          {isExpanded ? '📂' : '📁'}
        </span>

        {/* Name or Rename Input */}
        {renamingId === folder.id ? (
          <input
            value={renameValue}
            onChange={e => setRenameValue(e.target.value)}
            onBlur={() => { handleRename(folder.id, renameValue); setRenamingId(null); }}
            onKeyDown={e => { if (e.key === 'Enter') { handleRename(folder.id, renameValue); setRenamingId(null); } }}
            autoFocus
            style={{
              flex: 1, border: '1px solid #6366f1', borderRadius: '4px',
              padding: '2px 6px', fontSize: '13px', outline: 'none',
              boxShadow: '0 0 0 3px rgba(99,102,241,0.15)',
            }}
            onClick={e => e.stopPropagation()}
          />
        ) : (
          <span style={{
            flex: 1, fontSize: '13.5px',
            fontWeight: isSelected ? 600 : 400,
            color: isSelected ? '#3730a3' : '#374151',
            whiteSpace: 'nowrap', overflow: 'hidden', textOverflow: 'ellipsis',
            letterSpacing: '0.01em',
          }}>
            {folder.name}
          </span>
        )}

        {/* Child count badge */}
        {hasChildren && (
          <span style={{
            fontSize: '10px', fontWeight: 600, color: '#6366f1',
            background: '#eef0fd', borderRadius: '10px', padding: '1px 7px', flexShrink: 0,
          }}>
            {folder.children.length}
          </span>
        )}

        {/* Delete button — visible on hover */}
        {hovered && (
          <button
            onClick={e => { e.stopPropagation(); onDelete(folder.id, folder.name); }}
            title="Delete folder"
            style={{
              flexShrink: 0,
              background: 'none',
              border: '1px solid #fca5a5',
              borderRadius: '5px',
              color: '#ef4444',
              cursor: 'pointer',
              padding: '3px 6px',
              fontSize: '11px',
              display: 'flex',
              alignItems: 'center',
              gap: '3px',
              transition: 'all 0.15s ease',
              lineHeight: 1,
            }}
            onMouseEnter={e => { e.currentTarget.style.background = '#fef2f2'; e.currentTarget.style.borderColor = '#ef4444'; }}
            onMouseLeave={e => { e.currentTarget.style.background = 'none'; e.currentTarget.style.borderColor = '#fca5a5'; }}
          >
            🗑 Delete
          </button>
        )}
      </div>

      {/* Children */}
      {isExpanded && hasChildren && (
        <div style={{ borderLeft: '2px solid #e5e7f0', marginLeft: '22px', paddingLeft: '4px' }}>
          {folder.children.map(child => (
            <FolderTreeNode
              key={child.id}
              folder={child}
              path={currentPath}
              depth={depth + 1}
              expandedFolders={expandedFolders}
              toggleFolder={toggleFolder}
              selectedPath={selectedPath}
              setSelectedPath={setSelectedPath}
              renamingId={renamingId}
              setRenamingId={setRenamingId}
              renameValue={renameValue}
              setRenameValue={setRenameValue}
              handleRename={handleRename}
              onDelete={onDelete}
            />
          ))}
        </div>
      )}
    </div>
  );
}

// =========================
// MAIN COMPONENT
// =========================

export default function FilterForm() {

  const [searchValues, setSearchValues] = useState({ department: '', status: '' });
  const [departments, setDepartments] = useState([]);
  const [statuses, setStatuses] = useState([]);
  const [selectedDepartment, setSelectedDepartment] = useState(null);
  const [selectedStatus, setSelectedStatus] = useState(null);

  const [showUploadModal, setShowUploadModal] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [showAddDepartmentModal, setShowAddDepartmentModal] = useState(false);
  const [showAddStatusModal, setShowAddStatusModal] = useState(false);

  const [modalType, setModalType] = useState('');
  const [selectedItemId, setSelectedItemId] = useState(null);
  const [selectedItemName, setSelectedItemName] = useState('');

  // Folder tree state
  const [folders, setFolders] = useState([]);
  const [expandedFolders, setExpandedFolders] = useState(new Set());
  const [folderSearch, setFolderSearch] = useState('');
  const [selectedPath, setSelectedPath] = useState([]);
  const [newFolderName, setNewFolderName] = useState('');
  const [renamingId, setRenamingId] = useState(null);
  const [renameValue, setRenameValue] = useState('');
  const [addingFolder, setAddingFolder] = useState(false);

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
    try { const res = await axiosInstance.get('/api/departments/get'); setDepartments(extractData(res)); }
    catch (err) { console.error(err); }
  };

  const fetchStatuses = async () => {
    try { const res = await axiosInstance.get('/api/statuses/get'); setStatuses(extractData(res)); }
    catch (err) { console.error(err); }
  };

  const convertTree = (list) => {
    const map = {};
    const roots = [];
    list.forEach(f => { map[f.id] = { ...f, children: [] }; });
    list.forEach(f => {
      if (f.parentId) map[f.parentId]?.children.push(map[f.id]);
      else roots.push(map[f.id]);
    });
    return roots;
  };

  const fetchFolders = async () => {
    try {
      const res = await axiosInstance.get('/api/folders/tree');
      setFolders(convertTree(res.data || []));
    } catch (err) { console.error('Folders fetch error', err); }
  };

  const toggleFolder = (id) => {
    setExpandedFolders(prev => {
      const next = new Set(prev);
      next.has(id) ? next.delete(id) : next.add(id);
      return next;
    });
  };

  const filterFolders = (nodes, query) => {
    if (!query) return nodes;
    const q = query.toLowerCase();
    return nodes.map(node => {
      const children = filterFolders(node.children || [], query);
      const match = node.name.toLowerCase().includes(q);
      if (match || children.length) {
        expandedFolders.add(node.id);
        return { ...node, children };
      }
      return null;
    }).filter(Boolean);
  };

  const handleRename = async (id, name) => {
    try {
      await axiosInstance.put(`/api/folders/rename/${id}`, null, { params: { name } });
      fetchFolders();
    } catch (err) { console.error(err); }
  };

  const handleAddFolder = async () => {
    if (!newFolderName.trim()) return;
    setAddingFolder(true);
    try {
      const parent = selectedPath[selectedPath.length - 1];
      await axiosInstance.post('/api/folders/create', {
        name: newFolderName,
        parentId: parent ? parent.id : null
      });
      setNewFolderName('');
      await fetchFolders();
    } catch (err) {
      console.error('Folder creation failed', err);
      alert('Failed to create folder');
    } finally {
      setAddingFolder(false);
    }
  };

  const handleSearchChange = (field, value) => setSearchValues(prev => ({ ...prev, [field]: value }));

  const handleAddDepartment = async (name) => {
    try { await axiosInstance.post('/api/departments', { name }); fetchDepartments(); setShowAddDepartmentModal(false); }
    catch (err) { alert(err.response?.data || 'Failed to add department'); }
  };

  const handleAddStatus = async (name) => {
    try { await axiosInstance.post('/api/statuses/create', { name }); fetchStatuses(); setShowAddStatusModal(false); }
    catch (err) { alert(err.response?.data || 'Failed to add status'); }
  };

  const handleDeleteConfirm = async () => {
    try {
      if (modalType === 'department') { await axiosInstance.delete(`/api/departments/${selectedItemId}`); fetchDepartments(); }
      if (modalType === 'status') { await axiosInstance.delete(`/api/statuses/${selectedItemId}`); fetchStatuses(); }
      if (modalType === 'folder') { await axiosInstance.delete(`/api/folders/delete-folder/${selectedItemId}`); fetchFolders(); }
      setShowDeleteModal(false);
    } catch (err) { alert(err.response?.data || 'Delete failed'); }
  };

  const handleUploadConfirm = async (file) => {
    const formData = new FormData();
    formData.append('file', file);
    try {
      await axiosInstance.post('/api/upload', formData, { headers: { 'Content-Type': 'multipart/form-data' } });
      setShowUploadModal(false);
    } catch (err) { alert(err.response?.data || 'Upload failed'); }
  };

  const handleUploadClick = (type, id) => {
    setModalType(type);
    if (type === 'department') setSelectedDepartment(id);
    if (type === 'status') setSelectedStatus(id);
    setShowUploadModal(true);
  };

  const handleDeleteClick = (type, id, name) => {
    setModalType(type); setSelectedItemId(id); setSelectedItemName(name); setShowDeleteModal(true);
  };

  const filteredDepartments = departments.filter(d =>
    d.name?.toLowerCase().includes(searchValues.department.toLowerCase())
  );
  const filteredStatuses = statuses.filter(s =>
    s.name?.toLowerCase().includes(searchValues.status.toLowerCase())
  );

  const uploadModalName = modalType === 'department'
    ? departments.find(d => (d.id || d._id) === selectedDepartment)?.name
    : statuses.find(s => (s.id || s._id) === selectedStatus)?.name;

  const visibleFolders = filterFolders(folders, folderSearch);

  return (
    <div className={styles.container}>
      <div className={styles.mainLayout}>

        {/* DEPARTMENT */}
        <div className={styles.card}>
          <div className={styles.sectionContainer}>
            <h2 className={styles.sectionTitle}>Department</h2>
            <div className={styles.addSection}>
              <button className={styles.addButton} onClick={() => setShowAddDepartmentModal(true)}>Add Department</button>
              <div className={styles.searchBox}>
                <input className={styles.searchInput} placeholder="Search" value={searchValues.department}
                  onChange={e => handleSearchChange('department', e.target.value)} />
              </div>
            </div>
            <div className="dataTable">
              <table className={styles.tableWrapper}>
                <thead><tr><th>Name</th><th>Action</th></tr></thead>
                <tbody>
                  {filteredDepartments.map(d => {
                    const id = d.id || d._id;
                    return (
                      <tr key={id}>
                        <td>{d.name}</td>
                        <td>
                          <div className={styles.actionBtns}>
                            <button className="btn btn-2 btn-primary" onClick={() => handleUploadClick('department', id)}><FaUpload size="16" /></button>
                            <button className="btn btn-2 btn-red" onClick={() => handleDeleteClick('department', id, d.name)}><FaTrash size="16" /></button>
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
              <button className={styles.addButton} onClick={() => setShowAddStatusModal(true)}>Add Status</button>
              <div className={styles.searchBox}>
                <input className={styles.searchInput} placeholder="Search" value={searchValues.status}
                  onChange={e => handleSearchChange('status', e.target.value)} />
              </div>
            </div>
            <div className="dataTable">
              <table className={styles.tableWrapper}>
                <thead><tr><th>Name</th><th>Action</th></tr></thead>
                <tbody>
                  {filteredStatuses.map(s => {
                    const id = s.id || s._id;
                    return (
                      <tr key={id}>
                        <td>{s.name}</td>
                        <td>
                          <div className={styles.actionBtns}>
                            <button className="btn btn-2 btn-primary" onClick={() => handleUploadClick('status', id)}><FaUpload size="16" /></button>
                            <button className="btn btn-2 btn-red" onClick={() => handleDeleteClick('status', id, s.name)}><FaTrash size="16" /></button>
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

        {/* =====================
            FOLDERS — Professional Tree UI
        ===================== */}
        <div className={styles.card}>
          <div className={styles.sectionContainer}>
            <h2 className={styles.sectionTitle}>Folders</h2>

            {/* Search Bar */}
            <div style={{ position: 'relative', marginBottom: '12px' }}>
              <span style={{
                position: 'absolute', left: '12px', top: '50%', transform: 'translateY(-50%)',
                color: '#9ca3af', fontSize: '13px', pointerEvents: 'none',
              }}>🔍</span>
              <input
                placeholder="Search folders..."
                value={folderSearch}
                onChange={e => setFolderSearch(e.target.value)}
                style={{
                  width: '100%', padding: '9px 12px 9px 34px', borderRadius: '8px',
                  border: '1.5px solid #e5e7eb', fontSize: '13.5px', color: '#374151',
                  background: '#fafafa', outline: 'none', boxSizing: 'border-box', transition: 'border-color 0.2s',
                }}
                onFocus={e => e.target.style.borderColor = '#6366f1'}
                onBlur={e => e.target.style.borderColor = '#e5e7eb'}
              />
            </div>

            {/* Breadcrumb */}
            {selectedPath.length > 0 && (
              <div style={{
                display: 'flex', flexWrap: 'wrap', alignItems: 'center', gap: '4px',
                padding: '6px 10px', background: '#f0f1fd', borderRadius: '7px',
                marginBottom: '10px', border: '1px solid #dde1f9',
              }}>
                <span style={{ fontSize: '11px', color: '#6366f1', fontWeight: 700, marginRight: '2px', letterSpacing: '0.05em', textTransform: 'uppercase' }}>Path:</span>
                {selectedPath.map((p, i) => (
                  <React.Fragment key={p.id}>
                    <span
                      style={{
                        fontSize: '12px', color: '#4338ca', cursor: 'pointer', fontWeight: 500,
                        padding: '2px 6px', borderRadius: '4px', background: 'white', border: '1px solid #c7d2fe',
                      }}
                      onClick={() => setSelectedPath(selectedPath.slice(0, i + 1))}
                    >{p.name}</span>
                    {i < selectedPath.length - 1 && <span style={{ color: '#9ca3af', fontSize: '11px' }}>›</span>}
                  </React.Fragment>
                ))}
                <button onClick={() => setSelectedPath([])} style={{
                  marginLeft: 'auto', background: 'none', border: 'none',
                  color: '#9ca3af', fontSize: '16px', cursor: 'pointer', lineHeight: 1, padding: '0 2px',
                }} title="Clear selection">×</button>
              </div>
            )}

            {/* Folder Tree */}
            <div style={{
              background: '#ffffff', border: '1.5px solid #e8eaf0', borderRadius: '10px',
              padding: '8px', maxHeight: '300px', overflowY: 'auto', marginBottom: '12px',
              boxShadow: 'inset 0 1px 3px rgba(0,0,0,0.04)',
            }}>
              {visibleFolders.length === 0 ? (
                <div style={{ textAlign: 'center', padding: '32px 16px', color: '#9ca3af', fontSize: '13px' }}>
                  <div style={{ fontSize: '28px', marginBottom: '8px' }}>📂</div>
                  {folderSearch ? 'No folders match your search' : 'No folders yet. Add one below.'}
                </div>
              ) : (
                visibleFolders.map(folder => (
                  <FolderTreeNode
                    key={folder.id}
                    folder={folder}
                    path={[]}
                    depth={0}
                    expandedFolders={expandedFolders}
                    toggleFolder={toggleFolder}
                    selectedPath={selectedPath}
                    setSelectedPath={setSelectedPath}
                    renamingId={renamingId}
                    setRenamingId={setRenamingId}
                    renameValue={renameValue}
                    setRenameValue={setRenameValue}
                    handleRename={handleRename}
                    onDelete={(id, name) => handleDeleteClick('folder', id, name)}
                  />
                ))
              )}
            </div>

            {/* Add Folder Row */}
            <div style={{
              display: 'flex', gap: '8px', alignItems: 'center',
              padding: '10px 12px',
              background: 'linear-gradient(135deg, #f8f9ff 0%, #f3f4fd 100%)',
              borderRadius: '9px', border: '1.5px dashed #c7cff5',
            }}>
              <span style={{ fontSize: '15px', flexShrink: 0 }}>📁</span>
              <input
                placeholder={selectedPath.length
                  ? `Sub-folder inside "${selectedPath.at(-1).name}"...`
                  : 'New root folder name...'}
                value={newFolderName}
                onChange={e => setNewFolderName(e.target.value)}
                onKeyDown={e => { if (e.key === 'Enter') handleAddFolder(); }}
                style={{
                  flex: 1, border: '1.5px solid #e0e4f4', borderRadius: '7px',
                  padding: '8px 12px', fontSize: '13px', background: 'white', color: '#374151',
                  outline: 'none', transition: 'border-color 0.2s',
                }}
                onFocus={e => e.target.style.borderColor = '#6366f1'}
                onBlur={e => e.target.style.borderColor = '#e0e4f4'}
              />
              <button
                onClick={handleAddFolder}
                disabled={addingFolder || !newFolderName.trim()}
                style={{
                  padding: '8px 16px',
                  background: newFolderName.trim()
                    ? 'linear-gradient(135deg, #6366f1 0%, #4f46e5 100%)'
                    : '#e5e7eb',
                  color: newFolderName.trim() ? 'white' : '#9ca3af',
                  border: 'none', borderRadius: '7px', fontSize: '13px', fontWeight: 600,
                  cursor: newFolderName.trim() ? 'pointer' : 'not-allowed',
                  whiteSpace: 'nowrap', transition: 'all 0.2s',
                  boxShadow: newFolderName.trim() ? '0 2px 8px rgba(99,102,241,0.3)' : 'none',
                }}
              >
                {addingFolder ? 'Adding...' : '+ Add Folder'}
              </button>
            </div>

            {/* Hint */}
            <p style={{ fontSize: '11.5px', color: '#9ca3af', marginTop: '7px', marginBottom: 0 }}>
               Select a folder first to add a sub-folder inside it. Double-click any folder to rename.
            </p>

          </div>
        </div>

      </div>

      {/* MODALS */}
      {showUploadModal && (
        <UploadModal type={modalType} name={uploadModalName}
          onClose={() => setShowUploadModal(false)} onConfirm={handleUploadConfirm} />
      )}
      {showDeleteModal && (
        <DeleteModal type={modalType} name={selectedItemName}
          onClose={() => setShowDeleteModal(false)} onConfirm={handleDeleteConfirm} />
      )}
      {showAddDepartmentModal && (
        <AddDepartmentModal onClose={() => setShowAddDepartmentModal(false)} onConfirm={handleAddDepartment} />
      )}
      {showAddStatusModal && (
        <AddStatusModal onClose={() => setShowAddStatusModal(false)} onConfirm={handleAddStatus} />
      )}
    </div>
  );
}