import React, { useState } from 'react';
import styles from './UtilityShiftingReport.module.css';

const UtilityShiftingReport = () => {
  const [formData, setFormData] = useState({
    project: '',
    executionAgency: '',
    impactedContract: '',
    hod: ''
  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    alert('Report Generated');
  };

  const handleReset = () => {
    setFormData({
      project: '',
      executionAgency: '',
      impactedContract: '',
      hod: ''
    });
  };

  return (
    <div className={styles.pageWrapper}>
      <div className={styles.reportCard}>
        <div className={styles.reportHeader}>
          Utility Shifting Report
        </div>

        <div className={styles.reportBody}>
          <form onSubmit={handleSubmit}>
            <div className={styles.formGrid}>
              <div className={styles.formGroup}>
                <label>Project</label>
                <input
                  type="text"
                  name="project"
                  value={formData.project}
                  onChange={handleChange}
                  placeholder="Select"
                />
              </div>

              <div className={styles.formGroup}>
                <label>Execution Agency</label>
                <input
                  type="text"
                  name="executionAgency"
                  value={formData.executionAgency}
                  onChange={handleChange}
                  placeholder="Select"
                />
              </div>

              <div className={styles.formGroup}>
                <label>Impacted Contract</label>
                <input
                  type="text"
                  name="impactedContract"
                  value={formData.impactedContract}
                  onChange={handleChange}
                  placeholder="Select"
                />
              </div>

              <div className={styles.formGroup}>
                <label>HOD</label>
                <input
                  type="text"
                  name="hod"
                  value={formData.hod}
                  onChange={handleChange}
                  placeholder="Select"
                />
              </div>
            </div>

            <div className={styles.actionButtons}>
              <button type="submit" className={styles.generateBtn}>
                Generate Report
              </button>
              <button
                type="button"
                className={styles.resetBtn}
                onClick={handleReset}
              >
                Reset
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default UtilityShiftingReport;
