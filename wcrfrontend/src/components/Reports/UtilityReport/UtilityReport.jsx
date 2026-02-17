import React, { useState, useEffect } from 'react';
import styles from './UtilityShiftingReport.module.css';
import axios from 'axios';
import { API_BASE_URL } from "../../../config";


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



  const handleReset = () => {
    setFormData({
      project: '',
      executionAgency: '',
      impactedContract: '',
      hod: ''
    });
  };
  
  
  const [dropdowns, setDropdowns] = useState({
    projectsList: [],
    executionAgency: [],
    impactedContractsList: [],
    utilityHODList: []
  });
  
  
  const [generating, setGenerating] = useState(false); 

  
  useEffect(() => {
    axios.get(`${API_BASE_URL}/utility-report`, { withCredentials: true })
      .then(res => {
        const data = res.data;

        setDropdowns({
          projectsList: data.projectsList || [],
          executionAgency: data.executionAgency || [],
          impactedContractsList: data.impactedContractsList || [],
          utilityHODList: data.utilityHODList || []
        });
      })
      .catch(err => console.error(err));
  }, []);
  
  
  
  const handleSubmit = () => {

      if (generating) return;          

      setGenerating(true);             

      const form = document.createElement("form");
      form.method = "POST";
      form.action = `${API_BASE_URL}/generate-utility-report`;

      const appendField = (name, value) => {
          const input = document.createElement("input");
          input.type = "hidden";
          input.name = name;
          input.value = value || "";
          form.appendChild(input);
      };

      appendField("project_id_fk", formData.project);
      appendField("execution_agency_fk", formData.executionAgency);
      appendField("impacted_contract_id_fk", formData.impactedContract);
      appendField("hod_user_id_fk", formData.hod);

      document.body.appendChild(form);
      form.submit();
      document.body.removeChild(form);
	  
      setTimeout(() => setGenerating(false), 2000);
  };




  return (
    <div className={styles.pageWrapper}>
      <div className={styles.reportCard}>
        <div className={styles.reportHeader}>
          Utility Shifting Report
        </div>

        <div className={styles.reportBody}>
          <form >
            <div className={styles.formGrid}>
              <div className={styles.formGroup}>
                <label>Project</label>
				<select
				  name="project"
				  value={formData.project}
				  onChange={handleChange}
				>
				  <option value="">Select</option>
				  {dropdowns.projectsList.map(p => (
				    <option key={p.project_id_fk} value={p.project_id_fk}>
				      {p.project_id_fk} - {p.project_name}
				    </option>
				  ))}
				</select>

              </div>

              <div className={styles.formGroup}>
                <label>Execution Agency</label>
				<select
				  name="executionAgency"
				  value={formData.executionAgency}
				  onChange={handleChange}
				>
				  <option value="">Select</option>
				  {dropdowns.executionAgency.map(a => (
				    <option key={a.id} value={a.execution_agency_fk}>
				      {a.execution_agency_fk}
				    </option>
				  ))}
				</select>

              </div>

              <div className={styles.formGroup}>
                <label>Impacted Contract</label>
				<select
				  name="impactedContract"
				  value={formData.impactedContract}
				  onChange={handleChange}
				>
				  <option value="">Select</option>
				  {dropdowns.impactedContractsList.map(c => (
				    <option key={c.id} value={c.contract_id_fk}>
				      {c.contract_id_fk} - {c.contract_short_name}
				    </option>
				  ))}
				</select>

              </div>

              <div className={styles.formGroup}>
                <label>HOD</label>
				<select
				  name="hod"
				  value={formData.hod}
				  onChange={handleChange}
				>
				  <option value="">Select</option>
				  {dropdowns.utilityHODList.map(h => (
				    <option key={h.id} value={h.hod_user_id_fk}>
				      {h.user_name} - {h.designation}
				    </option>
				  ))}
				</select>

              </div>
            </div>
			
            <div className={styles.actionButtons}>
			<button
			    type="button"
			    className={styles.generateBtn}
			    onClick={handleSubmit}
				disabled = {generating}
			>
			    {generating ? 'generating..' : 'Generate Report'}
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
