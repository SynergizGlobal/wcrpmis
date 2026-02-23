import React, { useState, useEffect } from 'react';
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
  const [loadingField, setLoadingField] = useState(null);
  const [errors, setErrors] = useState({});
  const [reportTitle, setReportTitle] = useState('');
  const [filtersMap, setFiltersMap] = useState({});

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

  // UI visibility conditions
  const showDateDiv    = reportNo === '8';
  const showContractDiv = reportNo === '2';
  const showHodDiv     = ![8].includes(parseInt(reportNo));
  const showCSdiv      = [1, 7].includes(parseInt(reportNo));

  useEffect(() => {
    setReportTitle(reportTitles[reportNo] || "Contract Reports");
    initializeReport();
  }, [reportNo]);

  // ─── Init ────────────────────────────────────────────────────────────────────

  const initializeReport = async () => {
    setLoading(true);
    const currentReportTitle = reportTitles[reportNo] || "Contract Reports";

    await loadProjects();

    const savedFilters = localStorage.getItem(`contarctReportFilters${reportNo}`);
    if (savedFilters) {
      try {
        const filters = {};
        const temp = savedFilters.split('^');
        for (let i = 0; i < temp.length; i++) {
          if (temp[i].trim() !== '') {
            const temp2 = temp[i].split('=');
            const key   = temp2[0];
            const value = temp2[1];
            if (key === 'hod_designation')    filters.hod_designations  = value.split(',');
            else if (key === 'contractor_id_fk') filters.contractor_id_fk = value;
            else if (key === 'status')           filters.status           = value;
            else if (key === 'contract_status_fk') filters.contract_status_fk = value;
            else if (key === 'contract_id')      filters.contract_id      = value;
          }
        }
        setFormData(prev => ({ ...prev, ...filters }));
        setFiltersMap(filters);
      } catch (e) {
        console.error('Error parsing saved filters:', e);
      }
    }

    await loadAllDropdowns(currentReportTitle);
    setLoading(false);
  };

  // ─── Data loaders ────────────────────────────────────────────────────────────

  const loadProjects = async () => {
    try {
      const response = await api.get('/contract-report/api/getProjectList');
      setDropdowns(prev => ({
        ...prev,
        projects: [
          { value: '', label: 'Select Project' },
          ...response.data.map(proj => ({ value: proj.project_id, label: proj.project_name }))
        ]
      }));
    } catch (error) {
      console.error('Error loading projects:', error);
    }
  };

  const loadAllDropdowns = async (currentReportTitle) => {
    const requestBody = prepareRequestBody();
    await Promise.all([
      loadContractorsWithBody(requestBody),
      loadHODsWithBody(requestBody),
      loadContractsWithBody(requestBody),
      loadStatusOptionsWithBody(requestBody)
    ]);
    await loadContractStatusOptionsWithBody(requestBody, currentReportTitle);
  };

  // ─── prepareRequestBody ──────────────────────────────────────────────────────

  const prepareRequestBody = (customData = null) => {
    const d = customData || formData;
    const body = {};

    if (d.project_id_fk)      body.project_id_fk      = d.project_id_fk;
    if (d.contractor_id_fk)   body.contractor_id_fk   = d.contractor_id_fk;
    if (d.contract_status_fk) body.contract_status_fk = d.contract_status_fk;
    if (d.contract_id)        body.contract_id        = d.contract_id;
    if (d.status)             body.status             = d.status;

    // Backend SQL: u.designation IN (?,?) — needs designation strings
    if (Array.isArray(d.hod_designations) && d.hod_designations.length > 0) {
      body.hod_designations = d.hod_designations;
    }

    return body;
  };

  // ─── Individual loaders ──────────────────────────────────────────────────────

  const loadHODsWithBody = async (requestBody) => {
    try {
      const response = await api.post('/contract-report/ajax/getHODListInContractReport', requestBody);
      let options = [];
      if (response.data && response.data.length > 0) {
        // Deduplicate by designation — backend filters on designation string
        const seen = new Set();
        response.data.forEach(hod => {
          if (!seen.has(hod.designation)) {
            seen.add(hod.designation);
            options.push({ value: hod.designation, label: `${hod.designation} - ${hod.user_name || ''}` });
          }
        });
      }
      setDropdowns(prev => ({ ...prev, hodOptions: options }));
    } catch (error) {
      console.error('Error loading HODs:', error);
      setDropdowns(prev => ({ ...prev, hodOptions: [] }));
    }
  };

  const loadContractorsWithBody = async (requestBody) => {
    try {
      const response = await api.post('/contract-report/ajax/getContractorsListInContractReport', requestBody);
      let options = [];
      if (response.data && response.data.length > 0) {
        options = response.data.map(c => ({
          value: c.contractor_id_fk,
          label: c.contractor_name ? `${c.contractor_id_fk} - ${c.contractor_name}` : c.contractor_id_fk
        }));
      }
      setDropdowns(prev => ({ ...prev, contractorOptions: options }));
    } catch (error) {
      console.error('Error loading contractors:', error);
      setDropdowns(prev => ({ ...prev, contractorOptions: [] }));
    }
  };

  const loadStatusOptionsWithBody = async (requestBody) => {
    try {
      const response = await api.post('/contract-report/ajax/getStatsuListInContractReport', requestBody);
      let options = [];
      if (response.data && response.data.length > 0) {
        options = response.data.map(s => ({ value: s.status, label: s.status }));
      }
      setDropdowns(prev => ({ ...prev, statusOptions: options }));
    } catch (error) {
      console.error('Error loading status options:', error);
      setDropdowns(prev => ({ ...prev, statusOptions: [] }));
    }
  };

  const loadContractStatusOptionsWithBody = async (requestBody, currentReportTitle) => {
    try {
      let options = [];
      if (requestBody.status) {
        const response = await api.post('/contract-report/ajax/getStatusofWorkItems', { status: requestBody.status });
        if (response.data && response.data.length > 0) {
          options = response.data.map(item => ({ value: item.contract_status_fk, label: item.contract_status_fk }));
        }
      } else {
        const response = await api.post('/contract-report/ajax/getContractStatusListInContractReport', requestBody);
        if (response.data && response.data.length > 0) {
          let dataOptions = response.data.map(item => ({ value: item.contract_status_fk, label: item.contract_status_fk }));
          const name = currentReportTitle || reportTitle;
          if (['DOC Report','BG Report','Insurance Report','DOC, BG & Insurance Report'].includes(name)) {
            dataOptions = dataOptions.filter(opt => opt.value !== 'Closed');
          }
          options = dataOptions;
        }
      }
      setDropdowns(prev => ({ ...prev, contractStatusOptions: options }));
    } catch (error) {
      console.error('Error loading contract status:', error);
      setDropdowns(prev => ({ ...prev, contractStatusOptions: [] }));
    }
  };

  const loadContractsWithBody = async (requestBody) => {
    try {
      const response = await api.post('/contract-report/ajax/getContractListInContractReport', requestBody);
      let options = [{ value: '', label: 'Select Contract' }];
      if (response.data && response.data.length > 0) {
        options = [...options, ...response.data.map(c => ({
          value: c.contract_id,
          label: c.contract_short_name ? `${c.contract_id} - ${c.contract_short_name}` : c.contract_id
        }))];
      }
      setDropdowns(prev => ({ ...prev, contractOptions: options }));
    } catch (error) {
      console.error('Error loading contracts:', error);
      setDropdowns(prev => ({ ...prev, contractOptions: [{ value: '', label: 'Select Contract' }] }));
    }
  };

  // ─── Filters map / localStorage ──────────────────────────────────────────────

  const addToFiltersMap = (name, value) => {
    setFiltersMap(prev => {
      const newMap = { ...prev };
      Object.keys(newMap).forEach(key => {
        if (key.includes(name) || (name === 'hod_designations' && key === 'hod_designation')) {
          delete newMap[key];
        }
      });
      if (name === 'hod_designations' && value && value.length > 0) {
        newMap['hod_designation'] = value.join(',');
      } else if (name === 'contractor_id_fk'   && value) { newMap['contractor_id_fk']   = value; }
        else if (name === 'status'               && value) { newMap['status']               = value; }
        else if (name === 'contract_status_fk'   && value) { newMap['contract_status_fk']   = value; }
        else if (name === 'contract_id'          && value) { newMap['contract_id']          = value; }
        else if (name === 'date'                 && value) { newMap['date']                 = value; }
        else if (name === 'todate'               && value) { newMap['todate']               = value; }

      let filters = '';
      Object.keys(newMap).forEach(key => { filters += `${key}=${newMap[key]}^`; });
      localStorage.setItem(`contarctReportFilters${reportNo}`, filters);
      return newMap;
    });
  };

  // ─── Change handlers ─────────────────────────────────────────────────────────

  const handleInputChange = async (name, value) => {
    const updatedFormData = { ...formData, [name]: value };
    setFormData(updatedFormData);
    setErrors(prev => ({ ...prev, [name]: '' }));
    addToFiltersMap(name, value);

    setLoading(true);
    setLoadingField(name);

    try {
      const requestBody = prepareRequestBody(updatedFormData);
      console.log('Request Body after', name, ':', requestBody);

      // Mirror JSP getResetFiltersList() — each dropdown only reloads if its own value is empty
      const promises = [];

      if (!updatedFormData.hod_designations || updatedFormData.hod_designations.length === 0) {
        promises.push(loadHODsWithBody(requestBody));
      }
      if (!updatedFormData.contractor_id_fk) {
        promises.push(loadContractorsWithBody(requestBody));
      }
      if (!updatedFormData.contract_id) {
        promises.push(loadContractsWithBody(requestBody));
      }
      if (!updatedFormData.status) {
        promises.push(loadStatusOptionsWithBody(requestBody));
      }

      await Promise.all(promises);

      if (!updatedFormData.contract_status_fk) {
        await loadContractStatusOptionsWithBody(requestBody, reportTitle);
      }
    } catch (error) {
      console.error('Error reloading dropdowns:', error);
    } finally {
      setLoading(false);
      setLoadingField(null);
    }
  };

  const handleMultiSelectChange = (name, selectedOptions) => {
    const values = selectedOptions ? selectedOptions.map(opt => opt.value) : [];
    handleInputChange(name, values);
  };

  const handleProjectChange = async (selected) => {
    await handleInputChange('project_id_fk', selected?.value || '');
  };

  const clearFilters = () => {
    const clearedData = {
      project_id_fk: '', hod_designations: [], contractor_id_fk: '',
      status: '', contract_status_fk: '', contract_id: '', date: null, todate: null
    };
    setFormData(clearedData);
    setFiltersMap({});
    localStorage.removeItem(`contarctReportFilters${reportNo}`);
    setErrors({});
    setLoading(true);
    const requestBody = prepareRequestBody(clearedData);
    Promise.all([
      loadContractorsWithBody(requestBody),
      loadHODsWithBody(requestBody),
      loadStatusOptionsWithBody(requestBody),
      loadContractsWithBody(requestBody),
      loadContractStatusOptionsWithBody(requestBody, reportTitle)
    ]).finally(() => setLoading(false));
  };

  const formatDate = (date) => {
    if (!date) return '';
    const d = new Date(date);
    return `${String(d.getDate()).padStart(2,'0')}-${String(d.getMonth()+1).padStart(2,'0')}-${d.getFullYear()}`;
  };

  // ─── Generate report ─────────────────────────────────────────────────────────

  const generateReport = async () => {
    if (reportNo === '2' && !formData.contract_id) {
      setErrors({ contract_id: 'Please select contract' });
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
      const formDataToSend = new FormData();
      formDataToSend.append('project_id_fk', formData.project_id_fk || '');
      formDataToSend.append('hod_designations',
        Array.isArray(formData.hod_designations) && formData.hod_designations.length > 0
          ? formData.hod_designations.join(',') : '');
      formDataToSend.append('contractor_id_fk', formData.contractor_id_fk || '');
      formDataToSend.append('status', formData.status || '');
      formDataToSend.append('contract_status_fk', formData.contract_status_fk || '');
      formDataToSend.append('contract_id', formData.contract_id || '');
      formDataToSend.append('date', formData.date ? formatDate(formData.date) : '');
      formDataToSend.append('todate', formData.todate ? formatDate(formData.todate) : '');
      formDataToSend.append('report_no', reportNo);

      const response = await api.post(endpoints[reportNo], formDataToSend, {
        headers: { Accept: 'application/msword' },
        responseType: 'blob'
      });

      const url  = window.URL.createObjectURL(new Blob([response.data]));
      const link = document.createElement('a');
      link.href  = url;
      const contentDisposition = response.headers['content-disposition'];
      let filename = `${reportTitle.replace(/\s+/g,'_')}_${Date.now()}.doc`;
      if (contentDisposition) {
        const m = contentDisposition.match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/);
        if (m && m[1]) filename = m[1].replace(/['"]/g, '');
      }
      link.setAttribute('download', filename);
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (error) {
      console.error('Error generating report:', error);
      if (error.response) {
        if (error.response.data instanceof Blob) {
          const reader = new FileReader();
          reader.onload = () => {
            try { alert(JSON.parse(reader.result).message || 'Error generating report'); }
            catch { alert('Error generating report. Please try again.'); }
          };
          reader.readAsText(error.response.data);
        } else if (error.response.status === 404) { alert('Requested page not found. [404]'); }
          else if (error.response.status === 500) { alert('Internal Server Error [500].'); }
          else { alert('Error generating report. Please try again.'); }
      } else { alert('Not connect.\n Verify Network.'); }
    } finally {
      setLoading(false);
    }
  };

  // ─── Helpers ─────────────────────────────────────────────────────────────────

  const getSelectedValue = (options, value) => {
    if (!value) return null;
    return options.find(opt => opt.value === value) || null;
  };

  const getSelectedValues = (options, values) => {
    if (!values || values.length === 0) return [];
    return options.filter(opt => values.includes(opt.value));
  };

  // ─── Render ──────────────────────────────────────────────────────────────────

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

            {/* ── Row 1: Project | Contract* | Contractor | Contract Status | Status of Work ── */}
            <div className={styles.formGrid}>

              {/* 1. Project */}
              <div className={styles.formGroup}>
                <label className={styles.searchableLabel}>Project</label>
                <Select
                  options={dropdowns.projects}
                  value={getSelectedValue(dropdowns.projects, formData.project_id_fk)}
                  onChange={handleProjectChange}
                  isClearable
                  placeholder="Select Project"
                  className={styles.select}
                  classNamePrefix="react-select"
                  isDisabled={loading}
                />
              </div>

              {/* 2. Contract — report 2 only (moved to row 1) */}
              {showContractDiv && (
                <div className={styles.formGroup} id="contractDiv">
                  <label className={styles.searchableLabel}>
                    Contract <span className={styles.required}>*</span>
                  </label>
                  <Select
                    options={dropdowns.contractOptions}
                    value={getSelectedValue(dropdowns.contractOptions, formData.contract_id)}
                    onChange={(selected) => {
                      handleInputChange('contract_id', selected?.value || '');
                      if (selected?.value) setErrors(prev => ({ ...prev, contract_id: '' }));
                    }}
                    isClearable
                    placeholder="Select Contract"
                    className={styles.select}
                    classNamePrefix="react-select"
                    isDisabled={loading}
                  />
                  {errors.contract_id && (
                    <span className={styles.errorMsg}>{errors.contract_id}</span>
                  )}
                </div>
              )}

              {/* 3. Contractor */}
              <div className={styles.formGroup}>
                <label className={styles.searchableLabel}>Contractor</label>
                <Select
                  options={dropdowns.contractorOptions}
                  value={getSelectedValue(dropdowns.contractorOptions, formData.contractor_id_fk)}
                  onChange={(selected) => handleInputChange('contractor_id_fk', selected?.value || '')}
                  isClearable
                  placeholder="Select Contractor"
                  className={styles.select}
                  classNamePrefix="react-select"
                  isDisabled={loading}
                />
                {errors.contractor_id_fk && (
                  <span className={styles.errorMsg}>{errors.contractor_id_fk}</span>
                )}
              </div>

              {/* 4. Contract Status — reports 1 & 7 only */}
              {showCSdiv && (
                <div className={styles.formGroup} id="CSdiv">
                  <label className={styles.searchableLabel}>Contract Status</label>
                  <Select
                    options={dropdowns.statusOptions}
                    value={getSelectedValue(dropdowns.statusOptions, formData.status)}
                    onChange={(selected) => handleInputChange('status', selected?.value || '')}
                    isClearable
                    placeholder="Select Contract Status"
                    className={styles.select}
                    classNamePrefix="react-select"
                    isDisabled={loading}
                  />
                  {errors.status && (
                    <span className={styles.errorMsg}>{errors.status}</span>
                  )}
                </div>
              )}

              {/* 5. Status of Work */}
              <div className={styles.formGroup}>
                <label className={styles.searchableLabel}>Status of Work</label>
                <Select
                  options={dropdowns.contractStatusOptions}
                  value={getSelectedValue(dropdowns.contractStatusOptions, formData.contract_status_fk)}
                  onChange={(selected) => handleInputChange('contract_status_fk', selected?.value || '')}
                  isClearable
                  placeholder="Select Status of Work"
                  className={styles.select}
                  classNamePrefix="react-select"
                  isDisabled={loading}
                />
                {errors.contract_status_fk && (
                  <span className={styles.errorMsg}>{errors.contract_status_fk}</span>
                )}
              </div>

            </div>
            {/* ── End Row 1 ── */}

            {/* ── Row 2: HOD | Date fields (conditional) ── */}
            <div className={styles.formGrid}>

              {/* 6. HOD — all reports except report 8 */}
              {showHodDiv && (
                <div className={styles.formGroup} id="hodDiv">
                  <label className={styles.searchableLabel}>HOD</label>
                  <Select
                    options={dropdowns.hodOptions}
                    value={getSelectedValues(dropdowns.hodOptions, formData.hod_designations)}
                    onChange={(selected) => handleMultiSelectChange('hod_designations', selected)}
                    isMulti
                    isClearable
                    placeholder="Select HOD(s)"
                    className={styles.select}
                    classNamePrefix="react-select"
                    isDisabled={loading && loadingField !== 'hod_designations'}
                  />
                  {errors.hod_designations && (
                    <span className={styles.errorMsg}>{errors.hod_designations}</span>
                  )}
                </div>
              )}

              {/* 7. Date fields — report 8 only */}
              {showDateDiv && (
                <>
                  <div className={styles.formGroup} id="dateDiv">
                    <label className={styles.searchableLabel}>Validity Expiry From Date</label>
                    <DatePicker
                      selected={formData.date}
                      onChange={(date) => {
                        setFormData(prev => ({ ...prev, date }));
                        addToFiltersMap('date', date ? formatDate(date) : '');
                      }}
                      dateFormat="dd-MM-yyyy"
                      className={styles.dateInput}
                      placeholderText="Select From Date"
                      disabled={loading}
                      isClearable
                    />
                    {errors.date && <span className={styles.errorMsg}>{errors.date}</span>}
                  </div>

                  <div className={styles.formGroup}>
                    <label className={styles.searchableLabel}>Validity Expiry To Date</label>
                    <DatePicker
                      selected={formData.todate}
                      onChange={(date) => {
                        setFormData(prev => ({ ...prev, todate: date }));
                        addToFiltersMap('todate', date ? formatDate(date) : '');
                      }}
                      dateFormat="dd-MM-yyyy"
                      className={styles.dateInput}
                      placeholderText="Select To Date"
                      disabled={loading}
                      isClearable
                    />
                    {errors.todate && <span className={styles.errorMsg}>{errors.todate}</span>}
                  </div>
                </>
              )}

            </div>
            {/* ── End Row 2 ── */}

            {/* ── Action Buttons ── */}
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