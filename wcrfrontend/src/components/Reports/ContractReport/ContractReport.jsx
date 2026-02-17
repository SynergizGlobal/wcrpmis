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
  const [filtersMap, setFiltersMap] = useState({});
  
  // Report titles mapping
  const reportTitles = {
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
  
  // UI visibility conditions based on report type
  const showNextRow = [2, 3, 4, 5, 6, 8, 9].includes(parseInt(reportNo));
  const showDateDiv = reportNo === '8';
  const showContractDiv = reportNo === '2';
  const showHodDiv = ![8].includes(parseInt(reportNo));
  const showCSdiv = [1, 7].includes(parseInt(reportNo));
  
  useEffect(() => {
    setReportTitle(reportTitles[reportNo] || "Contract Reports");
    initializeReport();
  }, [reportNo]);
  
  const initializeReport = async () => {
    setLoading(true);
    
    // Load saved filters from localStorage
    const savedFilters = localStorage.getItem(`contarctReportFilters${reportNo}`);
    if (savedFilters) {
      try {
        // Parse JSP-style filters (key=value^key=value^)
        const filters = {};
        const temp = savedFilters.split('^');
        for(let i = 0; i < temp.length; i++) {
          if(temp[i].trim() !== '') {
            const temp2 = temp[i].split('=');
            const key = temp2[0];
            const value = temp2[1];
            
            if(key === 'hod_designation') {
              filters.hod_designations = value.split(',');
            } else if(key === 'contractor_id_fk') {
              filters.contractor_id_fk = value;
            } else if(key === 'status') {
              filters.status = value;
            } else if(key === 'contract_status_fk') {
              filters.contract_status_fk = value;
            } else if(key === 'contract_id') {
              filters.contract_id = value;
            }
          }
        }
        
        setFormData(prev => ({ ...prev, ...filters }));
        setFiltersMap(filters);
      } catch (e) {
        console.error('Error parsing saved filters:', e);
      }
    }
    
    // Load projects first
    await loadProjects();
    
    // Load other dropdowns based on saved filters or initial state
    await loadAllDropdowns();
    
    setLoading(false);
  };
  
  const loadProjects = async () => {
    try {
      const response = await api.get('/contract-report/api/getProjectList');
      setDropdowns(prev => ({
        ...prev,
        projects: [
          { value: '', label: 'Select' },
          ...response.data.map(proj => ({
            value: proj.project_id,
            label: proj.project_name
          }))
        ]
      }));
    } catch (error) {
      console.error('Error loading projects:', error);
    }
  };
  
  const loadAllDropdowns = async () => {
    await Promise.all([
      loadHODs(),
      loadContractors(),
      loadStatusOptions(),
      loadContractStatusOptions(),
      loadContracts()
    ]);
  };
  
  const prepareRequestBody = () => {
    // CRITICAL FIX: Always send array for hod_designations, never send empty string
    const requestBody = {
      hod_designations: Array.isArray(formData.hod_designations) && formData.hod_designations.length > 0 
        ? formData.hod_designations  // Send as array when values exist
        : null,  // Send null instead of empty string or empty array
      contractor_id_fk: formData.contractor_id_fk || null,
      contract_status_fk: formData.contract_status_fk || null,
      contract_id: formData.contract_id || null,
      status: formData.status || null,
      project_id_fk: formData.project_id_fk || null
    };
    
    // Remove null values to clean up request
    Object.keys(requestBody).forEach(key => {
      if (requestBody[key] === null) {
        delete requestBody[key];
      }
    });
    
    return requestBody;
  };
  
  const loadHODs = async () => {
    try {
      const requestBody = prepareRequestBody();
      
      const response = await api.post('/contract-report/ajax/getHODListInContractReport', requestBody);
      
      if (response.data && response.data.length > 0) {
        const options = response.data.map(hod => ({
          value: hod.designation,
          label: `${hod.designation} - ${hod.user_name || ''}`
        }));
        setDropdowns(prev => ({ ...prev, hodOptions: options }));
      } else {
        setDropdowns(prev => ({ ...prev, hodOptions: [] }));
      }
    } catch (error) {
      console.error('Error loading HODs:', error);
      setDropdowns(prev => ({ ...prev, hodOptions: [] }));
    }
  };
  
  const loadContractors = async () => {
    try {
      const requestBody = prepareRequestBody();
      
      const response = await api.post('/contract-report/ajax/getContractorsListInContractReport', requestBody);
      
      // Always include "All" option at the top
      let options = [{ value: '', label: 'All' }];
      
      if (response.data && response.data.length > 0) {
        const dataOptions = response.data.map(contractor => {
          const contractorName = contractor.contractor_name ? ` - ${contractor.contractor_name}` : '';
          return {
            value: contractor.contractor_id_fk,
            label: `${contractor.contractor_id_fk}${contractorName}`
          };
        });
        options = [...options, ...dataOptions];
      }
      
      setDropdowns(prev => ({ ...prev, contractorOptions: options }));
    } catch (error) {
      console.error('Error loading contractors:', error);
      setDropdowns(prev => ({ ...prev, contractorOptions: [{ value: '', label: 'All' }] }));
    }
  };
  
  const loadStatusOptions = async () => {
    try {
      const requestBody = prepareRequestBody();
      
      const response = await api.post('/contract-report/ajax/getStatsuListInContractReport', requestBody);
      
      // Always include "All" option at the top
      let options = [{ value: '', label: 'All' }];
      
      if (response.data && response.data.length > 0) {
        const dataOptions = response.data.map(status => ({
          value: status.status,
          label: status.status
        }));
        options = [...options, ...dataOptions];
      }
      
      setDropdowns(prev => ({ ...prev, statusOptions: options }));
    } catch (error) {
      console.error('Error loading status options:', error);
      setDropdowns(prev => ({ ...prev, statusOptions: [{ value: '', label: 'All' }] }));
    }
  };
  
  const loadContractStatusOptions = async () => {
    try {
      let response;
      let options = [{ value: '', label: 'All' }];
      
      if (formData.status) {
        // For getStatusofWorkItems, send simple object with status
        const requestBody = { status: formData.status };
        response = await api.post('/contract-report/ajax/getStatusofWorkItems', requestBody);
        
        if (response.data && response.data.length > 0) {
          const dataOptions = response.data.map(item => ({
            value: item.contract_status_fk,
            label: item.contract_status_fk
          }));
          options = [...options, ...dataOptions];
        }
      } else {
        const requestBody = prepareRequestBody();
        response = await api.post('/contract-report/ajax/getContractStatusListInContractReport', requestBody);
        
        if (response.data && response.data.length > 0) {
          let dataOptions = response.data.map(item => ({
            value: item.contract_status_fk,
            label: item.contract_status_fk
          }));
          
          // Remove 'Closed' option for specific reports
          const name = reportTitle;
          if(name === 'DOC Report' || name === 'BG Report' || name === 'Insurance Report' || name === 'DOC, BG & Insurance Report') {
            dataOptions = dataOptions.filter(opt => opt.value !== 'Closed');
          }
          
          options = [...options, ...dataOptions];
        }
      }
      
      setDropdowns(prev => ({ ...prev, contractStatusOptions: options }));
    } catch (error) {
      console.error('Error loading contract status:', error);
      setDropdowns(prev => ({ ...prev, contractStatusOptions: [{ value: '', label: 'All' }] }));
    }
  };
  
  const loadContracts = async () => {
    try {
      const requestBody = prepareRequestBody();
      
      const response = await api.post('/contract-report/ajax/getContractListInContractReport', requestBody);
      
      // Always include "Select" option at the top
      let options = [{ value: '', label: 'Select' }];
      
      if (response.data && response.data.length > 0) {
        const dataOptions = response.data.map(contract => {
          const contractName = contract.contract_short_name ? ` - ${contract.contract_short_name}` : '';
          return {
            value: contract.contract_id,
            label: `${contract.contract_id}${contractName}`
          };
        });
        options = [...options, ...dataOptions];
      }
      
      setDropdowns(prev => ({ ...prev, contractOptions: options }));
    } catch (error) {
      console.error('Error loading contracts:', error);
      setDropdowns(prev => ({ ...prev, contractOptions: [{ value: '', label: 'Select' }] }));
    }
  };
  
  const handleInputChange = useCallback(async (name, value) => {
    setFormData(prev => {
      let processedValue = value;
      
      // Handle hod_designations specially - ensure it's always an array
      if (name === 'hod_designations') {
        processedValue = Array.isArray(value) ? value : [];
      }
      
      const updated = { ...prev, [name]: processedValue };
      
      // Update filtersMap
      const newFiltersMap = { ...filtersMap };
      
      // Remove existing key first
      Object.keys(newFiltersMap).forEach(key => {
        if(key.includes(name) || (name === 'hod_designations' && key === 'hod_designation')) {
          delete newFiltersMap[key];
        }
      });
      
      // Only add to filtersMap if NOT empty
      if (name === 'hod_designations') {
        if (processedValue && processedValue.length > 0) {
          newFiltersMap['hod_designation'] = processedValue.join(',');
        }
      } else if (name === 'contractor_id_fk' && processedValue) {
        newFiltersMap['contractor_id_fk'] = processedValue;
      } else if (name === 'status' && processedValue) {
        newFiltersMap['status'] = processedValue;
      } else if (name === 'contract_status_fk' && processedValue) {
        newFiltersMap['contract_status_fk'] = processedValue;
      } else if (name === 'contract_id' && processedValue) {
        newFiltersMap['contract_id'] = processedValue;
      }
      
      setFiltersMap(newFiltersMap);
      
      // Save to localStorage in JSP format
      let filters = '';
      Object.keys(newFiltersMap).forEach(key => {
        filters = filters + key + "=" + newFiltersMap[key] + "^";
      });
      localStorage.setItem(`contarctReportFilters${reportNo}`, filters);
      
      return updated;
    });
    
    // Clear error for this field
    setErrors(prev => ({ ...prev, [name]: '' }));
    
    // Reload dependent dropdowns
    setLoading(true);
    await loadAllDropdowns();
    setLoading(false);
  }, [reportNo, filtersMap]);
  
  const handleMultiSelectChange = (name, selectedOptions) => {
    const values = selectedOptions ? selectedOptions.map(opt => opt.value) : [];
    handleInputChange(name, values);
  };
  
  const handleProjectChange = async (selected) => {
    const value = selected?.value || '';
    setFormData(prev => {
      const updated = { ...prev, project_id_fk: value };
      
      // Save to localStorage
      let filters = '';
      Object.keys(filtersMap).forEach(key => {
        filters = filters + key + "=" + filtersMap[key] + "^";
      });
      localStorage.setItem(`contarctReportFilters${reportNo}`, filters);
      
      return updated;
    });
    
    setLoading(true);
    await loadAllDropdowns();
    setLoading(false);
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
    
    setFiltersMap({});
    localStorage.removeItem(`contarctReportFilters${reportNo}`);
    setErrors({});
    
    // Reload dropdowns
    setLoading(true);
    setDropdowns(prev => ({
      ...prev,
      hodOptions: [],
      contractorOptions: [{ value: '', label: 'All' }],
      statusOptions: [{ value: '', label: 'All' }],
      contractStatusOptions: [{ value: '', label: 'All' }],
      contractOptions: [{ value: '', label: 'Select' }]
    }));
    
    loadAllDropdowns().finally(() => setLoading(false));
  };
  
  const generateReport = async () => {
    // Validate required fields
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
        1: '/contract-report/generate-contract-report',
        2: '/contract-report/generate-contract-detail-report',
        3: '/contract-report/generate-contract-doc-report',
        4: '/contract-report/generate-contract-bg-report',
        5: '/contract-report/generate-contract-insurance-report',
        6: '/contract-report/generate-contract-doc-bg-insurance-report',
        7: '/contract-report/generate-list-of-contracts-report',
        8: '/contract-report/generate-bg-insurance-report',
        9: '/contract-report/generate-contract-completion-report'
      };
      
      const endpoint = endpoints[reportNo];
      
      // CRITICAL FIX: For report generation, convert hod_designations array to comma-separated string
      // This matches what the JSP does in form submission
      const submitData = {
        project_id_fk: formData.project_id_fk || '',
        hod_designations: Array.isArray(formData.hod_designations) && formData.hod_designations.length > 0 
          ? formData.hod_designations.join(',') 
          : '',
        contractor_id_fk: formData.contractor_id_fk || '',
        status: formData.status || '',
        contract_status_fk: formData.contract_status_fk || '',
        contract_id: formData.contract_id || '',
        date: formData.date ? formatDate(formData.date) : '',
        todate: formData.todate ? formatDate(formData.todate) : '',
        report_no: reportNo
      };
      
      const response = await api.post(endpoint, submitData, {
        responseType: 'blob'
      });
      
      // Create download link
      const url = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href = url;
      
      // Get filename from Content-Disposition header or generate one
      const contentDisposition = response.headers['content-disposition'];
      let filename = `${reportTitle.replace(/\s+/g, '_')}_${Date.now()}.pdf`;
      
      if (contentDisposition) {
        const filenameMatch = contentDisposition.match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/);
        if (filenameMatch && filenameMatch[1]) {
          filename = filenameMatch[1].replace(/['"]/g, '');
        }
      }
      
      link.setAttribute('download', filename);
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
      
    } catch (error) {
      console.error('Error generating report:', error);
      
      // Error handling
      if (error.response) {
        if (error.response.status === 404) {
          alert('Requested page not found. [404]');
        } else if (error.response.status === 500) {
          alert('Internal Server Error [500].');
        } else {
          alert('Error generating report. Please try again.');
        }
      } else {
        alert('Not connect.\n Verify Network.');
      }
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
  
  return (
    <div className={styles.pageWrapper}>
      <div className={styles.reportCard}>
        <div className={styles.reportHeader}>
          <h6 id="rptName">{reportTitle}</h6>
        </div>
        
        <div className={styles.reportBody}>
          {loading && (
            <div className={styles.pageLoader}>
              <div className={styles.preloaderWrapper}>
                <div className={styles.spinner}></div>
              </div>
            </div>
          )}
          
          <form id="contractReportForm" onSubmit={(e) => e.preventDefault()}>
            <div className={styles.formGrid}>
              {/* Project Selection */}
              <div className={styles.formGroup}>
                <label className={styles.searchableLabel}>Project</label>
                <Select
                  options={dropdowns.projects}
                  value={dropdowns.projects.find(opt => opt.value === formData.project_id_fk) || dropdowns.projects[0]}
                  onChange={handleProjectChange}
                  isClearable={false}
                  placeholder="Select"
                  className={styles.select}
                  classNamePrefix="react-select"
                  isDisabled={loading}
                />
              </div>
              
              {/* HOD Selection - Conditionally shown */}
              {showHodDiv && (
                <div className={styles.formGroup} id="hodDiv">
                  <label className={styles.searchableLabel}>HOD</label>
                  <Select
                    options={dropdowns.hodOptions}
                    value={dropdowns.hodOptions.filter(opt => 
                      formData.hod_designations && formData.hod_designations.includes(opt.value)
                    )}
                    onChange={(selected) => handleMultiSelectChange('hod_designations', selected)}
                    isMulti
                    isClearable
                    placeholder="Select"
                    className={styles.select}
                    classNamePrefix="react-select"
                    isDisabled={loading}
                  />
                  {errors.hod_designations && (
                    <span className={styles.errorMsg}>{errors.hod_designations}</span>
                  )}
                </div>
              )}
              
              {/* Contractor Selection */}
              <div className={styles.formGroup}>
                <label className={styles.searchableLabel}>Contractor</label>
                <Select
                  options={dropdowns.contractorOptions}
                  value={dropdowns.contractorOptions.find(opt => opt.value === formData.contractor_id_fk) || dropdowns.contractorOptions[0]}
                  onChange={(selected) => handleInputChange('contractor_id_fk', selected?.value || '')}
                  isClearable={false}
                  placeholder="All"
                  className={styles.select}
                  classNamePrefix="react-select"
                  isDisabled={loading}
                />
                {errors.contractor_id_fk && (
                  <span className={styles.errorMsg}>{errors.contractor_id_fk}</span>
                )}
              </div>
              
              {/* Contract Status - Conditionally shown */}
              {showCSdiv && (
                <div className={styles.formGroup} id="CSdiv">
                  <label className={styles.searchableLabel}>Contract Status</label>
                  <Select
                    options={dropdowns.statusOptions}
                    value={dropdowns.statusOptions.find(opt => opt.value === formData.status) || dropdowns.statusOptions[0]}
                    onChange={(selected) => {
                      handleInputChange('status', selected?.value || '');
                      // Trigger getStatusofWorkItems when status changes
                      if (selected?.value) {
                        setTimeout(() => loadContractStatusOptions(), 100);
                      }
                    }}
                    isClearable={false}
                    placeholder="All"
                    className={styles.select}
                    classNamePrefix="react-select"
                    isDisabled={loading}
                  />
                  {errors.status && (
                    <span className={styles.errorMsg}>{errors.status}</span>
                  )}
                </div>
              )}
              
              {/* Status of Work */}
              <div className={styles.formGroup}>
                <label className={styles.searchableLabel}>Status of Work</label>
                <Select
                  options={dropdowns.contractStatusOptions}
                  value={dropdowns.contractStatusOptions.find(opt => opt.value === formData.contract_status_fk) || dropdowns.contractStatusOptions[0]}
                  onChange={(selected) => handleInputChange('contract_status_fk', selected?.value || '')}
                  isClearable={false}
                  placeholder="All"
                  className={styles.select}
                  classNamePrefix="react-select"
                  isDisabled={loading}
                />
                {errors.contract_status_fk && (
                  <span className={styles.errorMsg}>{errors.contract_status_fk}</span>
                )}
              </div>
            </div>
            
            {/* Additional Fields - Conditionally shown */}
            {showNextRow && (
              <div className={styles.formGrid}>
                {/* Date Range - Only for report 8 */}
                {showDateDiv && (
                  <>
                    <div className={styles.formGroup} id="dateDiv">
                      <label className={styles.searchableLabel}>Validity Expiry From Date</label>
                      <DatePicker
                        selected={formData.date}
                        onChange={(date) => handleInputChange('date', date)}
                        dateFormat="dd-MM-yyyy"
                        className={styles.dateInput}
                        placeholderText="Select"
                        disabled={loading}
                      />
                      {errors.date && (
                        <span className={styles.errorMsg}>{errors.date}</span>
                      )}
                    </div>
                    
                    <div className={styles.formGroup}>
                      <label className={styles.searchableLabel}>Validity Expiry To Date</label>
                      <DatePicker
                        selected={formData.todate}
                        onChange={(date) => handleInputChange('todate', date)}
                        dateFormat="dd-MM-yyyy"
                        className={styles.dateInput}
                        placeholderText="Select"
                        disabled={loading}
                      />
                      {errors.todate && (
                        <span className={styles.errorMsg}>{errors.todate}</span>
                      )}
                    </div>
                  </>
                )}
                
                {/* Contract Selection - Only for report 2 */}
                {showContractDiv && (
                  <div className={styles.formGroup} id="contractDiv">
                    <label className={styles.searchableLabel}>
                      Contract <span className={styles.required}>*</span>
                    </label>
                    <Select
                      options={dropdowns.contractOptions}
                      value={dropdowns.contractOptions.find(opt => opt.value === formData.contract_id) || dropdowns.contractOptions[0]}
                      onChange={(selected) => handleInputChange('contract_id', selected?.value || '')}
                      isClearable={false}
                      placeholder="Select"
                      className={styles.select}
                      classNamePrefix="react-select"
                      isDisabled={loading}
                    />
                    {errors.contract_id && (
                      <span className={styles.errorMsg}>{errors.contract_id}</span>
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
                Reset
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default ContractReport;