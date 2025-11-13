import React from 'react';
import { Outlet } from 'react-router-dom';
import styles from './UpdateStructureForm.module.css';

export default function UpdateStructureForm() {
  return (
    <div className={styles.container}>
      <h3>UpdateStructureForm Component</h3>
      <Outlet />
    </div>
  );
}