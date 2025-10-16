import React from 'react';
import { Outlet } from 'react-router-dom';
import styles from './Works.module.css';

export default function Works() {
  return (
    <div className={styles.container}>
      <h3>Works Component</h3>
      {/* Nested child routes will render here if any */}
      <Outlet />
    </div>
  );
}