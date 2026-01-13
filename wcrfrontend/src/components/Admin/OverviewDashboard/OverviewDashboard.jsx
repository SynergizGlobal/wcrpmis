import React, { useEffect, useMemo, useState } from "react";
import { useForm, Controller, useFieldArray } from "react-hook-form";
import { useNavigate, useLocation } from "react-router-dom";
import api from "../../../api/axiosInstance";
import { API_BASE_URL } from "../../../config";
import Select from "react-select";
import styles from './OverviewDashboard.module.css';

import { MdOutlineDeleteSweep } from 'react-icons/md';
import { BiListPlus } from 'react-icons/bi';
	import { CirclePlus } from "lucide-react";
	import { MdEditNote, MdDelete  } from 'react-icons/md';

const DUMMY_DATA = [
  { id: 1, name: "Risk", parent: 0, order: 1, status: "Active" },
  { id: 2, name: "Safety", parent: 0, order: 2, status: "Active" },
  { id: 3, name: "Finance", parent: 0, order: 3, status: "Inactive" },
  { id: 4, name: "R&R Overview", parent: 0, order: 4, status: "Inactive" },
  { id: 5, name: "Land Acquisition", parent: 0, order: 5, status: "Inactive" },
  { id: 6, name: "Utility Shifting", parent: 0, order: 6, status: "Inactive" },
  { id: 7, name: "Execution", parent: 0, order: 7, status: "Inactive" },
  { id: 8, name: "Engineering", parent: 54, order: 1, status: "Inactive" },
  { id: 9, name: "Electrical", parent: 54, order: 2, status: "Active" },
  { id: 10, name: "S&T", parent: 54, order: 3, status: "Inactive" },
];

export default function OverviewDashboard() {

      const navigate = useNavigate();
      const { state } = useLocation(); 

  const [rows, setRows] = useState(DUMMY_DATA);

  const [search, setSearch] = useState("");
  const [statusFilter, setStatusFilter] = useState("");
  const [parentFilter, setParentFilter] = useState("");

  const [page, setPage] = useState(1);
  const [pageSize, setPageSize] = useState(10);

  const [showModal, setShowModal] = useState(false);
  const [editRow, setEditRow] = useState(null);

  const [form, setForm] = useState({
    name: "",
    parent: 0,
    order: "",
    status: "Active",
    link: "",
    workFilterName: ""
  });

  const [errors, setErrors] = useState({});

  // ===== FILTER LOGIC =====
  const filtered = useMemo(() => {
    return rows.filter(r => {
      if (search && !r.name.toLowerCase().includes(search.toLowerCase())) return false;
      if (statusFilter && r.status !== statusFilter) return false;
      if (parentFilter && String(r.parent) !== parentFilter) return false;
      return true;
    });
  }, [rows, search, statusFilter, parentFilter]);

  // ===== PAGINATION =====
  const totalPages = Math.ceil(filtered.length / pageSize);
  const pagedData = filtered.slice((page - 1) * pageSize, page * pageSize);

  // ===== OPEN ADD =====
  const openAdd = () => {
    setEditRow(null);
    setForm({
      name: "",
      parent: 0,
      order: "",
      status: "Active",
      link: "",
      workFilterName: ""
    });
    setShowModal(true);
  };

  // ===== OPEN EDIT =====
  const openEdit = (row) => {
    setEditRow(row);
    setForm({ ...row });
    setShowModal(true);
  };

  // ===== SAVE =====
  const onSave = () => {
    if (!form.name) {
      alert("Name is required");
      return;
    }

    if (editRow) {
      setRows(prev =>
        prev.map(r => (r.id === editRow.id ? { ...form, id: editRow.id } : r))
      );
    } else {
      setRows(prev => [
        ...prev,
        { ...form, id: Date.now() }
      ]);
    }

    setShowModal(false);
  };

  // ===== DELETE =====
  const onDelete = (id) => {
    if (window.confirm("Delete this record?")) {
      setRows(prev => prev.filter(r => r.id !== id));
    }
  };


  return (
    <div className={styles.container}>

        {/* ===== HEADER ===== */}
        <div className="pageHeading">
	          <h2>Overview Dashboard</h2>
	          <div className="rightBtns">
	            <button className="btn btn-primary" onClick={openAdd}><CirclePlus size={16} /> Add Dashboard</button>
	          </div>
	        </div>



        {/* ===== FILTERS ===== */}
      <div className="innerPage">
        <div className={styles.filterRow}>
          <div className={`filterOptions ${styles.statusFilter}`}>
            <label>Parent</label>
            <select classNamePrefix="react-select" value={parentFilter} onChange={e => setParentFilter(e.target.value)}>
              <option value="">Select</option>
              <option value="0">0</option>
              <option value="54">54</option>
            </select>
          </div>

          <div className={`filterOptions ${styles.statusFilter}`}>
            <label>Status</label>
            <select value={statusFilter} onChange={e => setStatusFilter(e.target.value)}>
              <option value="">Select</option>
              <option value="Active">Active</option>
              <option value="Inactive">Inactive</option>
            </select>
          </div>

          <button
           style={{ minWidth: 100 }}
            className="btn btn-2 btn-primary"
						type="button"
            onClick={() => {
            setSearch(""); setStatusFilter(""); setParentFilter("");
          }}>Clear Filters</button>

          
        </div>
        <br />

        {/* ===== TABLE CONTROLS ===== */}
        <div className={styles.tableTopRow}>
          <div className="showEntriesCount">
            Show
            <select value={pageSize} onChange={e => { setPageSize(+e.target.value); setPage(1); }}>
              <option value={10}>10</option>
              <option value={25}>25</option>
              <option value={50}>50</option>
            </select>
            entries
          </div>
          <div className={styles.searchWrapper}>
            <input className={styles.searchInput} placeholder="Search" value={search} onChange={e => setSearch(e.target.value)} />
            <span className={styles.searchIcon}>🔍</span>
            {/* {search && <span onClick={() => setSearch("")}>✕</span>} */}
          </div>
        </div>

        {/* ===== TABLE ===== */}
        <div className={`dataTable ${styles.tableWrapper}`}>
          <table className={styles.projectTable}>
            <thead>
              <tr>
                <th>Name</th>
                <th>Parent</th>
                <th>Order</th>
                <th>Status</th>
                <th>Action</th>
              </tr>
            </thead>

            <tbody>
              {pagedData.map(row => (
                <tr key={row.id}>
                  <td>{row.name}</td>
                  <td>{row.parent}</td>
                  <td>{row.order}</td>
                  <td>{row.status}</td>
                  <td>
                    <div className="d-flex align-items-center gap-10">
                      <button className="btn btn-2 btn-outline-primary" onClick={() => openEdit(row)}><MdEditNote size={22} /></button>
                      <button className="btn btn-2 btn-outline-danger" onClick={() => onDelete(row.id)}><MdDelete size={20} /></button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {/* ===== FOOTER ===== */}
        <div className={styles.paginationBar}>
          <div>
            Showing {(page - 1) * pageSize + 1} to {Math.min(page * pageSize, filtered.length)} of {filtered.length} entries
          </div>

          <div className={styles.paginationBtns}>
            <button disabled={page === 1} onClick={() => setPage(p => p - 1)}>{"<"}</button>
            {Array.from({ length: totalPages }).map((_, i) => (
              <button
                key={i}
                className={page === i + 1 ? styles.active : ""}
                onClick={() => setPage(i + 1)}
              >
                {i + 1}
              </button>
            ))}
            <button disabled={page === totalPages} onClick={() => setPage(p => p + 1)}>{">"}</button>
          </div>
        </div>

        {/* ===== MODAL ===== */}
        {showModal && (
          <div className={styles.modalOverlay}>
            <div className={styles.modal}>

              <div className={styles.modalHeader}>
                <span>{editRow ? "Edit Module" : "Add Module"}</span>
                <span onClick={() => setShowModal(false)}>✕</span>
              </div>

              <div className={styles.modalBody}>
                <div className="form-row">
                  <div className="form-field">
                    <label>Name <span className="red">*</span></label>
                    <input placeholder="Name" value={form.name} onChange={e => setForm({ ...form, name: e.target.value })} />
                    {errors.name && <span className="text-danger">Required</span>}
                  </div>
                  <div className="form-field">
                    <label>Parent</label>
                    <select value={form.parent} onChange={e => setForm({ ...form, parent: e.target.value })}>
                      <option value="">select</option>
                    </select>
                  </div>
                  <div className="form-field">
                    <label>Link <span className="red">*</span></label>
                    <input placeholder="Link" value={form.link || ""} onChange={e => setForm({ ...form, link: e.target.value })} />
                    {errors.link && <span className="text-danger">Required</span>}
                  </div>
                  <div className="form-field">
                    <label>Order</label>
                    <input placeholder="Order" value={form.order} onChange={e => setForm({ ...form, order: e.target.value })} />
                  </div>
                  <div className="form-field">
                    <label>Status</label>
                    <select value={form.status} onChange={e => setForm({ ...form, status: e.target.value })}>
                      <option value="Active">Active</option>
                      <option value="Inactive">Inactive</option>
                    </select>
                  </div>

                  <div className="form-field">
                    <label>Work Filter Name</label>
                    <input placeholder="Work Filter Name" value={form.workFilterName || ""} onChange={e => setForm({ ...form, workFilterName: e.target.value })} />
                  </div>
                </div>

                <div className="form-post-buttons">
                  <button className="btn btn-primary" onClick={onSave}>ADD</button>
                  <button className="btn btn-white" onClick={() => setShowModal(false)}>CANCEL</button>
                </div>
              </div>

            </div>
          </div>
        )}



      </div>
    </div>
  );
}