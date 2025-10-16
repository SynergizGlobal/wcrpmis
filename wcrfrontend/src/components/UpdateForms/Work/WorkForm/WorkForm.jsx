import React from 'react';
import { Outlet } from 'react-router-dom';
import styles from './WorkForm.module.css';

export default function WorkForm() {
  return (
    <div className={styles.container}>
      <h3>WorkForm Component</h3>
      <Outlet />
    </div>
  );
}