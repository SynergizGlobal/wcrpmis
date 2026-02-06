import React from 'react';
import { Outlet } from 'react-router-dom';
import styles from './FilterForm.module.css';

export default function FilterForm() {
  return (
    <div className={styles.container}>
      <h3>FilterForm Component</h3>
      <Outlet />
    </div>
  );
}