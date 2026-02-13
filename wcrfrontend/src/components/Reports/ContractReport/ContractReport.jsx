import React, { useState, useEffect, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import styles from './ContractReport.module.css';
import api from "../../../api/axiosInstance";
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import Select from 'react-select';

const ContractReport = () => {
  const { reportNo } = useParams();
  const navigate = useNavigate();
  
  const [formData, setFormData] = useState({
    project_id_fk: '',
    hod_designations: [],
    contractor_id_fk: '',
    status: '',
    contract_status_fk: '',
    contract_id: '',
    date: null,
    todate: null
  });
  
  const [dropdowns, setDropdowns] = useState({
    projects: [],
    hodOptions: [],
    contractorOptions: [],
    statusOptions: [],
    contractStatusOptions: [],
    contractOptions: []
  });
  
  const [loading, setLoading] = useState(false);
  const [errors, setErrors] = useState({});
  const [reportTitle, setReportTitle] = useState('');
  
  useEffect(() => {
    const initializeReport = async () => {
      setLoading(true);
      
      const titles = {
        1: "List of Contracts",
        2: "Contract Detail Report",
        3: "DOC Report",
        4: "BG Report",
        5: "Insurance Report",
        6: "DOC, BG & Insurance Report",
        7: "List of Contracts Report",
        8: "BG Insurance Report",
        9: "Contract Completion Report"
      };
      setReportTitle(titles[reportNo] || "Contract Reports");
      
      const savedFilters = localStorage.getItem(`contractReportFilters${reportNo}`);
      if (savedFilters) {
        const filters = JSON.parse(savedFilters);
        setFormData(prev => ({ ...prev, ...filters }));
      }
      
      await loadProjects();
      
      if (formData.project_id_fk) {
        await loadDependentDropdowns();
      }
      
      setLoading(false);
    };
    
    initializeReport();
  }, [reportNo]);
  
  const loadProjects = async () => {
    try {
      const response = await api.get('/projects');
      setDropdowns(prev => ({
        ...prev,
        projects: response.data.map(proj => ({
          value: proj.project_id,
          label: proj.project_name
        }))
      }));
    } catch (error) {
      console.error('Error loading projects:', error);
    }
  };
  
  const loadDependentDropdowns = async () => {
    await Promise.all([
      loadHODs(),
      loadContractors(),
      loadStatusOptions(),
      loadContractStatusOptions(),
      loadContracts()
    ]);
  };
  
  const loadHODs = async () => {
    try {
      const response = await api.post('/ajax/getHODListInContractReport', {
        hod_designations: formData.hod_designations,
        contractor_id_fk: formData.contractor_id_fk,
        contract_status_fk: formData.contract_status_fk,
        contract_id: formData.contract_id
      });
      
      const options = response.data.map(hod => ({
        value: hod.designation,
        label: `${hod.designation} - ${hod.user_name}`
      }));
      
      setDropdowns(prev => ({ ...prev, hodOptions: options }));
    } catch (error) {
      console.error('Error loading HODs:', error);
    }
  };
  
  const loadContractors = async () => {
    try {
      const response = await api.post('/ajax/getContractorsListInContractReport', {
        hod_designations: formData.hod_designations,
        contractor_id_fk: formData.contractor_id_fk,
        contract_status_fk: formData.contract_status_fk,
        contract_id: formData.contract_id
      });
      
      const options = response.data.map(contractor => ({
        value: contractor.contractor_id_fk,
        label: `${contractor.contractor_id_fk} - ${contractor.contractor_name || ''}`
      }));
      
      setDropdowns(prev => ({ ...prev, contractorOptions: options }));
    } catch (error) {
      console.error('Error loading contractors:', error);
    }
  };
  
  const loadStatusOptions = async () => {
    try {
      const response = await api.post('/ajax/getStatsuListInContractReport', {
        hod_designations: formData.hod_designations,
        contractor_id_fk: formData.contractor_id_fk,
        contract_status_fk: formData.contract_status_fk,
        contract_id: formData.contract_id
      });
      
      const options = response.data.map(status => ({
        value: status.status,
        label: status.status
      }));
      
      setDropdowns(prev => ({ ...prev, statusOptions: options }));
    } catch (error) {
      console.error('Error loading status options:', error);
    }
  };
  
  const loadContractStatusOptions = async () => {
    try {
      if (formData.status) {
        const response = await api.post('/ajax/getStatusofWorkItems', {
          status: formData.status
        });
        
        const options = response.data.map(status => ({
          value: status.contract_status_fk,
          label: status.contract_status_fk
        }));
        
        const reportsToHideClosed = [3, 4, 5, 6];
        let filteredOptions = options;
        if (reportsToHideClosed.includes(parseInt(reportNo))) {
          filteredOptions = options.filter(opt => opt.value !== 'Closed');
        }
        
        setDropdowns(prev => ({ ...prev, contractStatusOptions: filteredOptions }));
      } else {
        const response = await api.post('/ajax/getContractStatusListInContractReport', {
          hod_designations: formData.hod_designations,
          contractor_id_fk: formData.contractor_id_fk,
          contract_status_fk: formData.contract_status_fk,
          contract_id: formData.contract_id
        });
        
        const options = response.data.map(status => ({
          value: status.contract_status_fk,
          label: status.contract_status_fk
        }));
        
        setDropdowns(prev => ({ ...prev, contractStatusOptions: options }));
      }
    } catch (error) {
      console.error('Error loading contract status:', error);
    }
  };
  
  const loadContracts = async () => {
    try {
      const response = await api.post('/ajax/getContractListInContractReport', {
        hod_designations: formData.hod_designations,
        contractor_id_fk: formData.contractor_id_fk,
        contract_status_fk: formData.contract_status_fk,
        contract_id: formData.contract_id
      });
      
      const options = response.data.map(contract => ({
        value: contract.contract_id,
        label: `${contract.contract_id} - ${contract.contract_short_name || ''}`
      }));
      
      setDropdowns(prev => ({ ...prev, contractOptions: options }));
    } catch (error) {
      console.error('Error loading contracts:', error);
    }
  };
  
  const handleInputChange = useCallback(async (name, value) => {
    setFormData(prev => {
      const updated = { ...prev, [name]: value };
      localStorage.setItem(`contractReportFilters${reportNo}`, JSON.stringify(updated));
      return updated;
    });
    
    setErrors(prev => ({ ...prev, [name]: '' }));
    
    setLoading(true);
    await loadDependentDropdowns();
    setLoading(false);
  }, [reportNo]);
  
  const handleMultiSelectChange = (name, selectedOptions) => {
    const values = selectedOptions ? selectedOptions.map(opt => opt.value) : [];
    handleInputChange(name, values);
  };
  
  const clearFilters = () => {
    setFormData({
      project_id_fk: '',
      hod_designations: [],
      contractor_id_fk: '',
      status: '',
      contract_status_fk: '',
      contract_id: '',
      date: null,
      todate: null
    });
    
    localStorage.removeItem(`contractReportFilters${reportNo}`);
    setErrors({});
    
    setDropdowns({
      projects: dropdowns.projects,
      hodOptions: [],
      contractorOptions: [],
      statusOptions: [],
      contractStatusOptions: [],
      contractOptions: []
    });
  };
  
  const generateReport = async () => {
    const newErrors = {};
    
    if (reportNo === '2' && !formData.contract_id) {
      newErrors.contract_id = 'Please select contract';
    }
    
    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }
    
    setLoading(true);
    
    try {
      const endpoints = {
        1: '/generate-contract-report',
        2: '/generate-contract-detail-report',
        3: '/generate-contract-doc-report',
        4: '/generate-contract-bg-report',
        5: '/generate-contract-insurance-report',
        6: '/generate-contract-doc-bg-insurance-report',
        7: '/generate-list-of-contracts-report',
        8: '/generate-bg-insurance-report',
        9: '/generate-contract-completion-report'
      };
      
      const endpoint = endpoints[reportNo];
      
      const submitData = {
        ...formData,
        date: formData.date ? formatDate(formData.date) : '',
        todate: formData.todate ? formatDate(formData.todate) : '',
        report_no: reportNo
      };
      
      const response = await api.post(endpoint, submitData, {
        responseType: 'blob'
      });
      
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      const filename = `${reportTitle.replace(/\s+/g, '_')}_${Date.now()}.pdf`;
      link.setAttribute('download', filename);
      document.body.appendChild(link);
      link.click();
      link.remove();
      
    } catch (error) {
      console.error('Error generating report:', error);
      alert('Error generating report. Please try again.');
    } finally {
      setLoading(false);
    }
  };
  
  const formatDate = (date) => {
    if (!date) return '';
    const d = new Date(date);
    const day = String(d.getDate()).padStart(2, '0');
    const month = String(d.getMonth() + 1).padStart(2, '0');
    const year = d.getFullYear();
    return `${day}-${month}-${year}`;
  };
  
  const showNextRow = [2, 3, 4, 5, 6, 8, 9].includes(parseInt(reportNo));
  const showDateDiv = reportNo === '8';
  const showContractDiv = reportNo === '2';
  const showHodDiv = ![8].includes(parseInt(reportNo));
  const showCSdiv = [1, 7].includes(parseInt(reportNo));
  
  return (
    <div className={styles.pageWrapper}>
      <div className={styles.reportCard}>
        <div className={styles.reportHeader}>
          {reportTitle}
        </div>
        
        <div className={styles.reportBody}>
          {loading && (
            <div className={styles.loader}>
              <div className={styles.spinner}></div>
              <p>Loading...</p>
            </div>
          )}
          
          <form id="contractReportForm" onSubmit={(e) => e.preventDefault()}>
            <div className={styles.formGrid}>
              {/* Project Selection */}
              <div className={styles.formGroup}>
                <label>Project</label>
                <Select
                  options={dropdowns.projects}
                  value={dropdowns.projects.find(opt => opt.value === formData.project_id_fk)}
                  onChange={(selected) => handleInputChange('project_id_fk', selected?.value || '')}
                  isClearable
                  placeholder="Select"
                  className={styles.select}
                  classNamePrefix="react-select"
                />
              </div>
              
              {/* HOD Selection - Conditionally shown */}
              {showHodDiv && (
                <div className={styles.formGroup}>
                  <label>HOD</label>
                  <Select
                    options={dropdowns.hodOptions}
                    value={dropdowns.hodOptions.filter(opt => 
                      formData.hod_designations.includes(opt.value)
                    )}
                    onChange={(selected) => handleMultiSelectChange('hod_designations', selected)}
                    isMulti
                    isClearable
                    placeholder="Select"
                    className={styles.select}
                    classNamePrefix="react-select"
                  />
                  {errors.hod_designations && (
                    <span className={styles.error}>{errors.hod_designations}</span>
                  )}
                </div>
              )}
              
              {/* Contractor Selection */}
              <div className={styles.formGroup}>
                <label>Contractor</label>
                <Select
                  options={dropdowns.contractorOptions}
                  value={dropdowns.contractorOptions.find(opt => opt.value === formData.contractor_id_fk)}
                  onChange={(selected) => handleInputChange('contractor_id_fk', selected?.value || '')}
                  isClearable
                  placeholder="Select"
                  className={styles.select}
                  classNamePrefix="react-select"
                />
                {errors.contractor_id_fk && (
                  <span className={styles.error}>{errors.contractor_id_fk}</span>
                )}
              </div>
              
              {/* Contract Status - Conditionally shown */}
              {showCSdiv && (
                <div className={styles.formGroup}>
                  <label>Contract Status</label>
                  <Select
                    options={dropdowns.statusOptions}
                    value={dropdowns.statusOptions.find(opt => opt.value === formData.status)}
                    onChange={(selected) => handleInputChange('status', selected?.value || '')}
                    isClearable
                    placeholder="Select"
                    className={styles.select}
                    classNamePrefix="react-select"
                  />
                  {errors.status && (
                    <span className={styles.error}>{errors.status}</span>
                  )}
                </div>
              )}
              
              {/* Status of Work */}
              <div className={styles.formGroup}>
                <label>Status of Work</label>
                <Select
                  options={dropdowns.contractStatusOptions}
                  value={dropdowns.contractStatusOptions.find(opt => opt.value === formData.contract_status_fk)}
                  onChange={(selected) => handleInputChange('contract_status_fk', selected?.value || '')}
                  isClearable
                  placeholder="Select"
                  className={styles.select}
                  classNamePrefix="react-select"
                />
                {errors.contract_status_fk && (
                  <span className={styles.error}>{errors.contract_status_fk}</span>
                )}
              </div>
            </div>
            
            {/* Additional Fields - Conditionally shown */}
            {showNextRow && (
              <div className={styles.formGrid}>
                {/* Date Range - Only for report 8 */}
                {showDateDiv && (
                  <>
                    <div className={styles.formGroup}>
                      <label>Validity Expiry From Date</label>
                      <DatePicker
                        selected={formData.date}
                        onChange={(date) => handleInputChange('date', date)}
                        dateFormat="dd-MM-yyyy"
                        className={styles.dateInput}
                        placeholderText="Select"
                      />
                      {errors.date && (
                        <span className={styles.error}>{errors.date}</span>
                      )}
                    </div>
                    
                    <div className={styles.formGroup}>
                      <label>Validity Expiry To Date</label>
                      <DatePicker
                        selected={formData.todate}
                        onChange={(date) => handleInputChange('todate', date)}
                        dateFormat="dd-MM-yyyy"
                        className={styles.dateInput}
                        placeholderText="Select"
                      />
                      {errors.todate && (
                        <span className={styles.error}>{errors.todate}</span>
                      )}
                    </div>
                  </>
                )}
                
                {/* Contract Selection - Only for report 2 */}
                {showContractDiv && (
                  <div className={styles.formGroup}>
                    <label>
                      Contract <span className={styles.required}>*</span>
                    </label>
                    <Select
                      options={dropdowns.contractOptions}
                      value={dropdowns.contractOptions.find(opt => opt.value === formData.contract_id)}
                      onChange={(selected) => handleInputChange('contract_id', selected?.value || '')}
                      isClearable
                      placeholder="Select"
                      className={styles.select}
                      classNamePrefix="react-select"
                    />
                    {errors.contract_id && (
                      <span className={styles.error}>{errors.contract_id}</span>
                    )}
                  </div>
                )}
              </div>
            )}
            
            {/* Action Buttons */}
            <div className={styles.actionButtons}>
              <button
                type="button"
                onClick={generateReport}
                className={styles.generateBtn}
                disabled={loading}
              >
                {loading ? 'Generating...' : 'Generate Report'}
              </button>
              
              <button
                type="button"
                onClick={clearFilters}
                className={styles.resetBtn}
                disabled={loading}
              >
                Reset Filters
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default ContractReport;