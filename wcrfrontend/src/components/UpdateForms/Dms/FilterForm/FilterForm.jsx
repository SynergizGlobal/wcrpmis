import React, { useState } from 'react';
import styles from './FilterForm.module.css';
import UploadModal from './UploadModal';
import DeleteModal from './DeleteModal';
import AddDepartmentModal from './AddDepartmentModal';
import AddStatusModal from './addStatusmodal';
import AddFolderModal from './addFoldermodal';

export default function FilterForm() {
  const [searchValues, setSearchValues] = useState({
    department: '',
    status: '',
    folder: ''
  });

  const [departments, setDepartments] = useState([
  ]);

  const [statuses, setStatuses] = useState([
  ]);

  const [folders, setFolders] = useState([
  ]);

  const [selectedDepartment, setSelectedDepartment] = useState(null);
  const [selectedStatus, setSelectedStatus] = useState(null);
  const [selectedFolder, setSelectedFolder] = useState(null);
  
 
  const [showUploadModal, setShowUploadModal] = useState(false);
  const [showDeleteModal, setShowDeleteModal] = useState(false);
  const [showAddDepartmentModal, setShowAddDepartmentModal] = useState(false);
  const [showAddStatusModal, setShowAddStatusModal] = useState(false);
  const [showAddFolderModal, setShowAddFolderModal] = useState(false);
  
  const [modalType, setModalType] = useState(''); // 'department' or 'status'

  const handleSearchChange = (field, value) => {
    setSearchValues(prev => ({
      ...prev,
      [field]: value
    }));
  };
  const handleAddDepartment = (departmentName) => {
    const newDepartment = {
      id: departments.length + 1,
      name: departmentName
    };
    setDepartments([...departments, newDepartment]);
    setShowAddDepartmentModal(false);
  };
  const handleAddStatus = (statusName) => {
    const newStatus = {
      id: statuses.length + 1,
      name: statusName
    };
    setStatuses([...statuses, newStatus]);
    setShowAddStatusModal(false);
  };

  // Handle Add Folder
  const handleAddFolder = (folderName, parentFolderId) => {
    const newFolder = {
      id: folders.length + 1,
      name: folderName,
      level: parentFolderId ? 1 : 0 // Simplified level logic
    };
    setFolders([...folders, newFolder]);
    setShowAddFolderModal(false);
  };
  const handleUploadClick = (type, id) => {
    setModalType(type);
    if (type === 'department') {
      setSelectedDepartment(id);
    } else {
      setSelectedStatus(id);
    }
    setShowUploadModal(true);
  };
  const handleDeleteClick = (type, id) => {
    setModalType(type);
    if (type === 'department') {
      setSelectedDepartment(id);
    } else {
      setSelectedStatus(id);
    }
    setShowDeleteModal(true);
  };

  const handleUploadConfirm = () => {
    console.log(`Upload ${modalType}:`, modalType === 'department' ? selectedDepartment : selectedStatus);
    setShowUploadModal(false);
  };

  const handleDeleteConfirm = () => {
    if (modalType === 'department') {
      setDepartments(departments.filter(dept => dept.id !== selectedDepartment));
    } else if (modalType === 'status') {
      setStatuses(statuses.filter(status => status.id !== selectedStatus));
    }
    setShowDeleteModal(false);
  };
  const filteredDepartments = departments.filter(dept => 
    dept.name.toLowerCase().includes(searchValues.department.toLowerCase())
  );
  
  const filteredStatuses = statuses.filter(status => 
    status.name.toLowerCase().includes(searchValues.status.toLowerCase())
  );
  
  const filteredFolders = folders.filter(folder => 
    folder.name.toLowerCase().includes(searchValues.folder.toLowerCase())
  );

  return (
    <div className={styles.container}>
      <div className={styles.mainLayout}>
        {/* Left Column */}
        <div className={styles.leftColumn}>
          {/* Department Container */}
          <div className={styles.sectionContainer}>
            <h2 className={styles.sectionTitle}>Department</h2>
            
            <div className={styles.addSection}>
              <button 
                className={styles.addButton}
                onClick={() => setShowAddDepartmentModal(true)}
              >
                Add Department
              </button>
              <div className={styles.searchBox}>
                <input
                  type="text"
                  placeholder="Search"
                  value={searchValues.department}
                  onChange={(e) => handleSearchChange('department', e.target.value)}
                  className={styles.searchInput}
                />
              </div>
            </div>

            <div className={styles.tableSection}>
              <table className={styles.dataTable}>
                <thead>
                  <tr>
                    <th className={styles.tableHeader}>Department</th>
                    <th className={styles.tableHeader}>Action</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredDepartments.map(dept => (
                    <tr key={dept.id} className={styles.tableRow}>
                      <td className={styles.tableCell}>
                        • {dept.name}
                      </td>
                      <td className={styles.actionCell}>
                        <div className={styles.actionButtons}>
                          <button 
                            className={styles.actionButton}
                            onClick={() => handleUploadClick('department', dept.id)}
                          >
                            Upload
                          </button>
                          <button 
                            className={`${styles.actionButton} ${styles.deleteButton}`}
                            onClick={() => handleDeleteClick('department', dept.id)}
                          >
                            Delete
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
              <div className={styles.tablePaginationInfo}>
                Showing  {filteredDepartments.length} of {filteredDepartments.length} entries
              </div>
            </div>
          </div>
          <div className={styles.sectionContainer}>
            <h2 className={styles.sectionTitle}>Status</h2>
            
            <div className={styles.addSection}>
              <button 
                className={styles.addButton}
                onClick={() => setShowAddStatusModal(true)}
              >
                Add Status
              </button>
              <div className={styles.searchBox}>
                <input
                  type="text"
                  placeholder="Search"
                  value={searchValues.status}
                  onChange={(e) => handleSearchChange('status', e.target.value)}
                  className={styles.searchInput}
                />
              </div>
            </div>

            <div className={styles.tableSection}>
              <table className={styles.dataTable}>
                <thead>
                  <tr>
                    <th className={styles.tableHeader}>Status</th>
                    <th className={styles.tableHeader}>Action</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredStatuses.map(status => (
                    <tr key={status.id} className={styles.tableRow}>
                      <td className={styles.tableCell}>
                        • {status.name}
                      </td>
                      <td className={styles.actionCell}>
                        <div className={styles.actionButtons}>
                          <button 
                            className={styles.actionButton}
                            onClick={() => handleUploadClick('status', status.id)}
                          >
                            Upload
                          </button>
                          <button 
                            className={`${styles.actionButton} ${styles.deleteButton}`}
                            onClick={() => handleDeleteClick('status', status.id)}
                          >
                            Delete
                          </button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
              <div className={styles.tablePaginationInfo}>
                Showing  {filteredStatuses.length} of {filteredStatuses.length} entries
              </div>
            </div>
          </div>
        </div>
        <div className={styles.rightColumn}>
          <div className={styles.sectionContainer}>
            <h2 className={styles.sectionTitle}>Folders</h2>
            
            <div className={styles.addSection}>
              <button 
                className={styles.addButton}
                onClick={() => setShowAddFolderModal(true)}
              >
                Add Folder
              </button>
              <div className={styles.searchBox}>
                <input
                  type="text"
                  placeholder="Search"
                  value={searchValues.folder}
                  onChange={(e) => handleSearchChange('folder', e.target.value)}
                  className={styles.searchInput}
                />
              </div>
            </div>

            <div className={styles.dataSection}>
              <h3 className={styles.dataTitle}>Folder</h3>
              <ul className={styles.dataList}>
                {filteredFolders.map(folder => (
                  <li 
                    key={folder.id} 
                    className={`${styles.dataItem} ${styles[folder.level === 0 ? 'folderLevel0' : folder.level === 1 ? 'folderLevel1' : 'folderLevel2']}`}
                  >
                    • {folder.name}
                  </li>
                ))}
              </ul>
              <div className={styles.paginationInfo}>
                Showing  {filteredFolders.length} of {filteredFolders.length} entries
              </div>
            </div>
          </div>
        </div>
      </div>
      {showUploadModal && (
        <UploadModal
          type={modalType}
          name={modalType === 'department' 
            ? departments.find(d => d.id === selectedDepartment)?.name 
            : statuses.find(s => s.id === selectedStatus)?.name}
          onClose={() => setShowUploadModal(false)}
          onConfirm={handleUploadConfirm}
        />
      )}
      {showDeleteModal && (
        <DeleteModal
          type={modalType}
          name={modalType === 'department' 
            ? departments.find(d => d.id === selectedDepartment)?.name 
            : statuses.find(s => s.id === selectedStatus)?.name}
          onClose={() => setShowDeleteModal(false)}
          onConfirm={handleDeleteConfirm}
        />
      )}

      {/* Add Department Modal */}
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
    </div>
  );
}