import React from 'react';
import { Outlet } from 'react-router-dom';
import styles from './Work.module.css';

export default function Work() {
  return (
    <div className={styles.container}>
      <h3>Work Component</h3>
      <Outlet />
    </div>
  );
}