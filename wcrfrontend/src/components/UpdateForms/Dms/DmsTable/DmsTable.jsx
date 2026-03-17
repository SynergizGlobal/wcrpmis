import React, { useMemo, useState } from "react";
import { useTable, usePagination, useGlobalFilter, useSortBy } from "react-table";
import styles from './DmsTable.module.css';

export default function DmsTable({ columns = [], mockData = [], loading, onRowClick, renderActions }) {

  const data = useMemo(() => Array.isArray(mockData) ? mockData : [], [mockData]);
  const cols = useMemo(() => {
  return columns.map((col) => {
    if (typeof col === "string") {
      return {
        Header: col,
        accessor: col,
        id: col
      };
    }
    return {
      ...col,
      id: col.id || col.accessor
    };
  });
}, [columns]);


  const {
    getTableProps,
    getTableBodyProps,
    props,
    headerGroups,
    page,
    prepareRow,
    state: { pageIndex, pageSize, globalFilter },
    setGlobalFilter,
    setPageSize,
    pageCount,
    gotoPage,
    nextPage,
    previousPage,
    canNextPage,
    canPreviousPage
  } = useTable(
    { columns: cols, data, initialState: { pageSize: 10 } },
    useGlobalFilter,
    useSortBy,
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
          {headerGroups.map((hg) => {
            const { key, ...rest } = hg.getHeaderGroupProps();
            return (
              // <tr key={key} {...rest}>
              <tr {...hg.getHeaderGroupProps()}>
                {hg.headers.map((col) => {
                  const { key, ...rest } = col.getHeaderProps();
                  return (
                    <th
                      key={key}
                      {...col.getHeaderProps(col.getSortByToggleProps())}
                      style={{ cursor: "pointer" }}
                    >
                      <div className="d-flex align-center justify-content-center">
                        {col.render("Header")}
                        <span style={{ marginLeft: 6, display: "inline-flex", flexDirection: "column", lineHeight: "8px", fontSize: "10px" }}>
                          <span style={{ color: col.isSorted && !col.isSortedDesc ? "#000" : "#ccc" }}>
                            ▲
                          </span>
                          <span style={{ color: col.isSorted && col.isSortedDesc ? "#000" : "#ccc" }}>
                            ▼
                          </span>
                        </span>
                      </div>
                    </th>
                  );
                })}
              </tr>
            );
          })}
        </thead>

        <tbody {...getTableBodyProps()}>
          {loading ? (
            <tr>
              <td colSpan={columns.length}>Loading...</td>
            </tr>
          ) : page.length === 0 ? (
            <tr>
              <td colSpan={columns.length}>No data available</td>
            </tr>
          ) : (
            page.map((row) => {
              prepareRow(row);
              const { key, ...restRow } = row.getRowProps();

              return (
                <tr
                  key={key}
                  {...restRow}
                  onClick={() => onRowClick && onRowClick(row.original)}
                  style={{ cursor: onRowClick ? "pointer" : "default" }}
                >
                  {row.cells.map((cell) => {
                    const { key, ...restCell } = cell.getCellProps();
                    if (cell.column.id === "Actions" && renderActions) {
                      return (
                        <td key={key} {...restCell}>
                          {renderActions(row.original)}
                        </td>
                      );
                    }
                    return (
                      <td key={key} {...restCell}>
                        {cell.render("Cell")}
                      </td>
                    );
                  })}
                </tr>
              );
            })
          )}
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