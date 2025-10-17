import React from 'react';
import { Outlet } from 'react-router-dom';
import styles from './LandAcquisitionForm.module.css';

export default function LandAcquisitionForm() {
  return (
    <div className={styles.container}>
      <h3>LandAcquisitionForm Component</h3>
      <Outlet />
    </div>
  );
}