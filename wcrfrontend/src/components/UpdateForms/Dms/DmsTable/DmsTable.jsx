import React, { useMemo, useState } from "react";
import { useTable, usePagination, useGlobalFilter } from "react-table";
import styles from './DmsTable.module.css';

export default function DmsTable({ columns, mockData }) {
  const data = useMemo(() => mockData, [mockData]);
  const cols = useMemo(
    () => columns.map(c => ({ Header: c, accessor: c })),
    [columns]
  );

  const {
    getTableProps,
    getTableBodyProps,
    headerGroups,
    page,
    prepareRow,
    state: { pageIndex, pageSize, globalFilter },
    setGlobalFilter,
    setPageSize,
    pageOptions,
    pageCount,
    gotoPage,
    nextPage,
    previousPage,
    canNextPage,
    canPreviousPage
  } = useTable(
    { columns: cols, data, initialState: { pageSize: 10 } },
    useGlobalFilter,
    usePagination
  );

  const startEntry = data.length === 0 ? 0 : pageIndex * pageSize + 1;
  const endEntry = Math.min((pageIndex + 1) * pageSize, data.length);

  const renderPageNumbers = () => {
    const pages = [];
    const maxVisible = 5;

    let start = Math.max(0, pageIndex - Math.floor(maxVisible / 2));
    let end = Math.min(pageCount - 1, start + maxVisible - 1);

    if (start > 0) {
      pages.push(
        <button key="start" onClick={() => gotoPage(0)}>1</button>
      );
      if (start > 1) pages.push(<span key="dots1">…</span>);
    }

    for (let i = start; i <= end; i++) {
      pages.push(
        <button
          key={i}
          onClick={() => gotoPage(i)}
          className={i === pageIndex ? styles.activePage : ""}
        >
          {i + 1}
        </button>
      );
    }

    if (end < pageCount - 1) {
      if (end < pageCount - 2) pages.push(<span key="dots2">…</span>);
      pages.push(
        <button key="end" onClick={() => gotoPage(pageCount - 1)}>
          {pageCount}
        </button>
      );
    }

    return pages;
  };


  return (
    <>
      <div className={styles.controls}>
        <select value={pageSize} onChange={e => setPageSize(+e.target.value)}>
          {[10,25,50].map(n => <option key={n}>{n}</option>)}
        </select>

        <input
          placeholder="Search..."
          value={globalFilter || ""}
          onChange={e => setGlobalFilter(e.target.value)}
        />
      </div>

      <table {...getTableProps()} className={styles.table}>
        <thead>
          {headerGroups.map(hg => (
            <tr {...hg.getHeaderGroupProps()}>
              {hg.headers.map(col => (
                <th {...col.getHeaderProps()}>{col.render("Header")}</th>
              ))}
            </tr>
          ))}
        </thead>

        <tbody {...getTableBodyProps()}>
          {page.map(row => {
            prepareRow(row);
            return (
              <tr {...row.getRowProps()}>
                {row.cells.map(cell => (
                  <td {...cell.getCellProps()}>{cell.render("Cell")}</td>
                ))}
              </tr>
            );
          })}
        </tbody>
      </table>

      <div className={styles.footer}>
      {/* LEFT */}
      <span>
        Showing {startEntry} to {endEntry} of {data.length} entries
      </span>

      {/* RIGHT */}
      <div className={styles.pagination}>
        <button onClick={previousPage} disabled={!canPreviousPage}>
          Prev
        </button>

        {renderPageNumbers()}

        <button onClick={nextPage} disabled={!canNextPage}>
          Next
        </button>
      </div>
    </div>
    </>
  );
}