import React from 'react';
import { Outlet } from 'react-router-dom';
import styles from './PendingIssuesReport.module.css';

export default function PendingIssuesReport() {
  return (
    <div className={styles.container}>
      <h3>PendingIssuesReport Component</h3>
      <Outlet />
    </div>
  );
}