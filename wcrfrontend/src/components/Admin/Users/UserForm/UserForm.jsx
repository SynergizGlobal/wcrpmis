import React, { useState, useEffect, useCallback, useRef, useMemo } from "react";
import { useForm, Controller, useFieldArray } from "react-hook-form";
import Select from "react-select";
import { useNavigate, useParams, useLocation } from "react-router-dom";
import api from "../../../../api/axiosInstance";
import styles from "./UserForm.module.css";
import { API_BASE_URL } from "../../../../config";

import { RiAttachment2 } from 'react-icons/ri';
import { MdOutlineDeleteSweep } from 'react-icons/md';
import { BiListPlus } from 'react-icons/bi';
import { FiEye, FiTrash2 } from 'react-icons/fi';

export default function UserForm() {
    
    const navigate = useNavigate();
    const { id: userId } = useParams();
    const { state } = useLocation();
    const isEdit = Boolean(state?.user) || Boolean(userId);

    // State for dropdown options
    const [dropdownOptions, setDropdownOptions] = useState({
        roles: [],
        types: [],
        departments: [],
        reportingToList: [],
        pmisKeys: [],
        moduleList: [],
        contractsList: [],
        landList: [],
        utilityList: [],
        rrList: [],
        structuresList: [],
    });

    const [loading, setLoading] = useState(false);
    const [dropdownLoading, setDropdownLoading] = useState(false);
    const [userDataLoaded, setUserDataLoaded] = useState(false);
    const [userData, setUserData] = useState(null);
    const [dynamicReportingTo, setDynamicReportingTo] = useState([]);
    
    // PMIS Key states
    const [pmisKeyFlag, setPmisKeyFlag] = useState(isEdit);
    const [pmisKeyError, setPmisKeyError] = useState("");
    const [isCheckingPmisKey, setIsCheckingPmisKey] = useState(false);

    // Image states
    const [imagePreview, setImagePreview] = useState(null);
    const [imageError, setImageError] = useState("");
    const fileInputRef = useRef(null);
    const [existingImageUrl, setExistingImageUrl] = useState("");

    const {
        register,
        control,
        handleSubmit,
        watch,
        setValue,
        getValues,
        trigger,
        formState: { errors, isValid },
      } = useForm({
        defaultValues: {
          user_name: "",
          designation: "",
          department_fk: "",
          user_type_fk: "",
          reporting_to_id_srfk: "",
          user_role_name_fk: "",
          email_id: "",
          mobile_number: "",
          personal_contact_number: "",
          landline: "",
          extension: "",
          pmis_key_fk: "",
          user_id: "",
          fileName: "",
          // Module permissions
          permissions_check: [],
          // Dynamic permissions
          contract_id: [], // for Contracts module
          land_work: [], // for Land Acquisition module
          us_work: [], // for Utility Shifting module
          rr_work: [], // for R&R module
      
          // Structure permissions (Execution & Monitoring) - ALWAYS start with ONE empty row
          executionMonitoringRows: [{
            contract_id: null,
            structures: [],
            availableStructures: []
          }]
        },
        mode: "onChange"
      });

  // Watch department and user type for dynamic reporting to
  const watchDepartment = watch("department_fk");
  const watchUserType = watch("user_type_fk");
  const watchPmisKey = watch("pmis_key_fk");
  const watchFileName = watch("fileName");

  // UseFieldArray for dynamic rows
  const { fields: structureRows, append: appendStructureRow, remove: removeStructureRow } = useFieldArray({
    control,
    name: "executionMonitoringRows",
  });

  // Module states
  const [enabledModules, setEnabledModules] = useState([]);
  const [moduleAccess, setModuleAccess] = useState({});
  const [contractOptions, setContractOptions] = useState([]);
  const [landOptions, setLandOptions] = useState([]);
  const [utilityOptions, setUtilityOptions] = useState([]);
  const [rrOptions, setRrOptions] = useState([]);

  const [selectAllContracts, setSelectAllContracts] = useState(false);

  // ===================== MEMOIZED SELECT OPTIONS =====================
  const departmentOptions = useMemo(() => {
    return (dropdownOptions.departments || []).map(dept => ({
      value: dept.department,
      label: dept.department_name
    }));
  }, [dropdownOptions.departments]);

  const userTypeOptions = useMemo(() => {
    return (dropdownOptions.types || []).map(type => ({
      value: type.user_type_fk,
      label: type.user_type_fk
    }));
  }, [dropdownOptions.types]);

  const reportingToOptions = useMemo(() => {
    return (dropdownOptions.reportingToList || []).map(rep => ({
      label: `${rep.designation} - ${rep.user_name}`,
      value: rep.user_id
    }));
  }, [dropdownOptions.reportingToList]);

  const userRoleOptions = useMemo(() => {
    return (dropdownOptions.roles || []).map(role => ({
      value: role.user_role_name,
      label: role.user_role_name,
      name: role.user_role_code
    }));
  }, [dropdownOptions.roles]);

  const moduleOptions = useMemo(() => {
    return dropdownOptions.moduleList || [];
  }, [dropdownOptions.moduleList]);

  // ===================== API FUNCTIONS - USER DATA FETCHING =====================

  // Fetch user data by ID
  const fetchUserData = async (userId) => {
    try {
      setLoading(true);
     
      const requestBody = {
        user_id: userId,
      };

      const response = await api.post(
        `${API_BASE_URL}/users/ajax/form/get-user/getUser`,
        requestBody
      );

      if (response.data?.usrObj) {
        const userData = response.data.usrObj;
        console.log("Fetched user data:", userData);
        setUserData(userData);
        return userData;
      } else {
        console.error("No user data found in response");
        alert("User data not found");
        navigate("/admin/users");
      }
    } catch (err) {
      console.error("❌ Error fetching user data:", err);
      
      if (err.response) {
        console.error("Response error:", err.response.data);
        
        if (err.response.status === 404) {
          alert("User not found");
        } else if (err.response.status === 500) {
          alert("Server error while fetching user data");
        }
      } else {
        alert("Network error while fetching user data");
      }
      
      navigate("/admin/users");
    } finally {
      setLoading(false);
    }
  };

  // ===================== IMAGE HANDLING FUNCTIONS =====================
  
  // Validate image file
  const validateImage = (file) => {
    if (!file) {
      return "User image is required";
    }

    // Check file type
    const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/gif', 'image/webp'];
    if (!allowedTypes.includes(file.type)) {
      return "Please upload a valid image file (JPEG, PNG, GIF, WebP)";
    }

    // Check file size (max 5MB)
    const maxSize = 5 * 1024 * 1024; // 5MB
    if (file.size > maxSize) {
      return "Image size should be less than 5MB";
    }

    return "";
  };

  // Handle image selection
  const handleImageChange = (event) => {
    const file = event.target.files[0];
    setImageError("");

    if (file) {
      const validationError = validateImage(file);
      if (validationError) {
        setImageError(validationError);
        setImagePreview(null);
        setValue("fileName", null, { shouldValidate: true });
        return;
      }

      // Create preview URL
      const previewUrl = URL.createObjectURL(file);
      setImagePreview(previewUrl);
      setImageError("");
      
      // Clear existing image URL when new image is selected
      setExistingImageUrl("");
    } else {
      setImagePreview(null);
    }
  };

  // Handle image view
  const handleViewImage = () => {
    if (imagePreview) {
      window.open(imagePreview, '_blank');
    } else if (existingImageUrl) {
      window.open(existingImageUrl, '_blank');
    }
  };

  // Handle image removal
  const handleRemoveImage = () => {
    setImagePreview(null);
    setExistingImageUrl("");
    setImageError("");
    
    if (fileInputRef.current) {
      fileInputRef.current.value = "";
    }
    
    setValue("fileName", null, { shouldValidate: true });
  };

  // Clean up object URLs on unmount
  useEffect(() => {
    return () => {
      if (imagePreview) {
        URL.revokeObjectURL(imagePreview);
      }
    };
  }, [imagePreview]);

  // Watch for file changes to update preview
  useEffect(() => {
    if (watchFileName && watchFileName[0]) {
      const file = watchFileName[0];
      const validationError = validateImage(file);
      
      if (!validationError) {
        const previewUrl = URL.createObjectURL(file);
        setImagePreview(previewUrl);
      }
    }
  }, [watchFileName]);

  // ===================== END IMAGE FUNCTIONS =====================

  // ===================== PMIS KEY FUNCTIONS =====================
  
  // Generate random PMIS key
  const generatePMISKey = useCallback(() => {
    const randomLetters = (length) => {
      const letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
      let result = "";
      for (let i = 0; i < length; i++) {
        result += letters.charAt(Math.floor(Math.random() * letters.length));
      }
      return result;
    };

    const randomDigits = (length) => {
      const digits = "0123456789";
      let result = "";
      for (let i = 0; i < length; i++) {
        result += digits.charAt(Math.floor(Math.random() * digits.length));
      }
      return result;
    };

    return (
      randomLetters(2) + 
      randomDigits(2) + 
      randomLetters(2) + 
      randomDigits(2) + 
      randomLetters(2) + 
      randomDigits(2)
    );
  }, []);

  const checkPMISKeyAvailability = async (pmisKey) => {
    try {
      setIsCheckingPmisKey(true);
      setPmisKeyError("");
      
      const response = await api.post(`${API_BASE_URL}/users/ajax/checkPMISKeyAvailability`, {
        pmis_key_fk: pmisKey
      });

      if (response.data?.keyAvailability === 'Taken') {
        setPmisKeyError("Sorry... Already taken");
        setPmisKeyFlag(false);
        return false;
      } else {
        setPmisKeyError("Key Available");
        setPmisKeyFlag(true);
        return true;
      }
      
    } catch (err) {
      console.error("Error checking PMIS key:", err);
      setPmisKeyError("Available (check failed)");
      setPmisKeyFlag(true);
      return true;
      
    } finally {
      setIsCheckingPmisKey(false);
    }
  };

  // Handle PMIS key blur
  const handlePmisKeyBlur = async () => {
    const pmisKey = getValues("pmis_key_fk");
    const existingUserKey = isEdit ? getValues("pmis_key_fk") : "";
    
    if (pmisKey && pmisKey.trim() !== "" && pmisKey !== existingUserKey) {
      await checkPMISKeyAvailability(pmisKey);
    } else if (pmisKey === existingUserKey) {
      setPmisKeyFlag(true);
      setPmisKeyError("");
    } else if (!pmisKey || pmisKey.trim() === "") {
      setPmisKeyError("PMIS key is required");
      setPmisKeyFlag(false);
    }
  };

  // ===================== END PMIS KEY FUNCTIONS =====================

  // ===================== OTHER API FUNCTIONS =====================

  // Fetch contracts list
  const fetchContractsList = async () => {
    try {
      const requestBody = {
        user_id: getValues("user_id") || "",
        department_fk: getValues("department_fk")?.value || getValues("department_fk") || ""
      };
      
      const response = await api.post(
        `${API_BASE_URL}/users/ajax/form/add-user-form/getContractsList`,
        requestBody
      );

      if (response.data?.contractsList) {
        setDropdownOptions(prev => ({
          ...prev,
          contractsList: response.data.contractsList
        }));

        const options = response.data.contractsList.map(contract => ({
          value: contract.contract_id,
          label: contract.contract_short_name
        }));
        
        setContractOptions(options);
        return options;
      }
      return [];
    } catch (err) {
      console.error("❌ Error fetching contracts list:", err);
      setContractOptions([]);
      return [];
    }
  };

  // Fetch Land Acquisition list
  const fetchLandList = async () => {
    try {
      const requestBody = {
        user_id: getValues("user_id") || "",
        department_fk: getValues("department_fk")?.value || getValues("department_fk") || ""
      };
      
      const response = await api.post(
        `${API_BASE_URL}/users/ajax/form/add-user-form/getLandList`,
        requestBody
      );

      if (response.data?.landList) {
        setDropdownOptions(prev => ({
          ...prev,
          landList: response.data.landList
        }));

        const options = response.data.landList.map(land => ({
          value: land.project_id_fk,
          label: land.project_name
        }));
        
        setLandOptions(options);
        return options;
      }
      return [];
    } catch (err) {
      console.error("❌ Error fetching land list:", err);
      setLandOptions([]);
      return [];
    }
  };

  // Fetch Utility Shifting list
  const fetchUtilityList = async () => {
    try {
      const requestBody = {
        user_id: getValues("user_id") || "",
        department_fk: getValues("department_fk")?.value || getValues("department_fk") || ""
      };
      
      const response = await api.post(
        `${API_BASE_URL}/users/ajax/form/add-user-form/getUtilityList`,
        requestBody
      );

      if (response.data?.utilityList) {
        setDropdownOptions(prev => ({
          ...prev,
          utilityList: response.data.utilityList
        }));

        const options = response.data.utilityList.map(utility => ({
          value: utility.project_id_fk,
          label: utility.project_name
        }));
        
        setUtilityOptions(options);
        return options;
      }
      return [];
    } catch (err) {
      console.error("❌ Error fetching utility list:", err);
      setUtilityOptions([]);
      return [];
    }
  };

  // Fetch R&R list
  const fetchRRList = async () => {
    try {
      const requestBody = {
        user_id: getValues("user_id") || "",
        department_fk: getValues("department_fk")?.value || getValues("department_fk") || ""
      };
      
      const response = await api.post(
        `${API_BASE_URL}/users/ajax/form/add-user-form/getRRList`,
        requestBody
      );

      if (response.data?.rrList) {
        setDropdownOptions(prev => ({
          ...prev,
          rrList: response.data.rrList
        }));

        const options = response.data.rrList.map(rr => ({
          value: rr.project_id_fk,
          label: rr.project_name
        }));
        
        setRrOptions(options);
        return options;
      }
      return [];
    } catch (err) {
      console.error("❌ Error fetching R&R list:", err);
      setRrOptions([]);
      return [];
    }
  };

  // Fetch all dropdown data
  const fetchDropdownData = async () => {
    try {
      setDropdownLoading(true);
      
      const response = await api.post(`${API_BASE_URL}/users/ajax/form/add-user-form`, {});
      
      if (response.data) {
        const updatedOptions = {
          roles: response.data.roles || [],
          types: response.data.types || [],
          departments: response.data.departments || [],
          reportingToList: response.data.reportingToList || [],
          pmisKeys: response.data.pmisKeys || [],
          moduleList: response.data.moduleList || [],
          contractsList: [],
          landList: [],
          utilityList: [],
          rrList: [],
          structuresList: response.data.structuresList || [],
        };
        
        setDropdownOptions(updatedOptions);

        // Fetch all module lists in parallel
        await Promise.all([
          fetchContractsList(),
          fetchLandList(),
          fetchUtilityList(),
          fetchRRList(),
        ]);

        // For add mode, enable all modules by default
        if (!isEdit && response.data.moduleList) {
          const allModules = response.data.moduleList.map(module => module.module_name);
          setEnabledModules(allModules);
          
          const permissionsCheck = allModules.map(module => ({
            value: `${module}_Active`,
            moduleName: module
          }));
          setValue("permissions_check", permissionsCheck);
        }

        // Mark dropdowns as loaded
        return true;
      }
      return false;
    } catch (err) {
      console.error("❌ Error fetching dropdown data:", err);
      alert("Error loading form data");
      return false;
    } finally {
      setDropdownLoading(false);
    }
  };

  const fetchReportingToPersonsList = async () => {
    try {
      const department_fk = watchDepartment?.value || watchDepartment;
      const user_type_fk = watchUserType?.value || watchUserType;
      
      if (!department_fk || !user_type_fk) return;

      const response = await api.post(`${API_BASE_URL}/users/ajax/getUserReportingToList`, {
        department_fk,
        user_type_fk
      });

      if (response.data) {
        setDynamicReportingTo(
          response.data.map(user => ({
            value: user.user_id,
            label: user.designation ? `${user.designation} - ${user.user_name}` : user.user_name
          }))
        );
      }
    } catch (err) {
      console.error("Error fetching reporting to list:", err);
    }
  };

  // Get structures by contract ID for a specific row
  const getStructuresByContractId = async (contractId, rowIndex) => {
    try {
      if (!contractId) {
        // Clear structures for this row
        const currentRows = [...getValues("executionMonitoringRows")];
        currentRows[rowIndex].availableStructures = [];
        setValue("executionMonitoringRows", currentRows);
        return;
      }

      const response = await api.post(`${API_BASE_URL}/users/ajax/getStructuresByContractId`, {
        contract_id_fk: contractId
      });

      if (response.data) {
        const structureOptions = response.data.map(structure => ({
          value: structure.structure_id_fk,
          label: structure.structure
        }));

        const currentRows = [...getValues("executionMonitoringRows")];
        currentRows[rowIndex].availableStructures = structureOptions;
        setValue("executionMonitoringRows", currentRows);
      } else {
        const currentRows = [...getValues("executionMonitoringRows")];
        currentRows[rowIndex].availableStructures = [];
        setValue("executionMonitoringRows", currentRows);
      }
    } catch (err) {
      console.error("Error fetching structures:", err);
      const currentRows = [...getValues("executionMonitoringRows")];
      currentRows[rowIndex].availableStructures = [];
      setValue("executionMonitoringRows", currentRows);
    }
  };

  // Handle contract change for a specific row
  const handleContractChange = async (selected, rowIndex) => {
    const currentRows = [...getValues("executionMonitoringRows")];
    currentRows[rowIndex].contract_id = selected;
    currentRows[rowIndex].structures = []; // Clear structures when contract changes
    setValue("executionMonitoringRows", currentRows);
    
    if (selected && selected.value) {
      await getStructuresByContractId(selected.value, rowIndex);
    } else {
      currentRows[rowIndex].availableStructures = [];
      setValue("executionMonitoringRows", currentRows);
    }
  };

  // ===================== FORM POPULATION LOGIC =====================

  const populateFormData = useCallback(async (userData) => {
    console.log("Populating form with user data:", userData);
    
    // Basic user info
    Object.entries(userData).forEach(([key, value]) => {
      if (value !== null && value !== undefined) {
        // Handle Select fields with current dropdown options
        if (key === 'department_fk') {
          const dept = departmentOptions.find(d => d.value === value) || 
                      departmentOptions.find(d => d.label === value);
          setValue(key, dept || null);
        } 
        else if (key === 'user_type_fk') {
          const type = userTypeOptions.find(t => t.value === value) || 
                       userTypeOptions.find(t => t.label === value);
          setValue(key, type || null);
        }
        else if (key === 'reporting_to_id_srfk') {
          const reportingTo = dynamicReportingTo.find(r => r.value === value) || 
                             reportingToOptions.find(r => r.value === value);
          setValue(key, reportingTo || null);
        }
        else if (key === 'user_role_name_fk') {
          const role = userRoleOptions.find(r => r.value === value) || 
                       userRoleOptions.find(r => r.label === value);
          setValue(key, role || null);
        }
        else if (key === 'pmis_key_fk') {
          setValue(key, value);
          setPmisKeyFlag(true);
        }
        else if (['user_id', 'user_name', 'designation', 'email_id', 'mobile_number', 
                  'personal_contact_number', 'landline', 'extension'].includes(key)) {
          setValue(key, value);
        }
      }
    });

    // Set user_id as hidden field
    if (userData.user_id) {
      setValue("user_id", userData.user_id);
    }

    // Load existing user image if available
    if (userData.user_image && userData.user_image.trim() !== "") {
      const fullImageUrl = `${API_BASE_URL}/${userData.user_image}`;
      setExistingImageUrl(fullImageUrl);
    }

    // Wait for module lists to load
    await Promise.all([
      fetchContractsList(),
      fetchLandList(),
      fetchUtilityList(),
      fetchRRList()
    ]);

    // Set module checkboxes based on user permissions
    if (moduleOptions.length > 0) {
      let userEnabledModules = [];
      
      if (userData.user_permissions) {
        userEnabledModules = moduleOptions
          .filter(module => {
            const userPermission = userData.user_permissions.find(p => p.module_name === module.module_name);
            return userPermission?.soft_delete_status === 'Active';
          })
          .map(module => module.module_name);
      }
      
      setEnabledModules(userEnabledModules);

      // Set permissions_check values
      const permissionsCheck = moduleOptions.map(module => {
        const isEnabled = userEnabledModules.includes(module.module_name);
        return {
          value: `${module.module_name}_${isEnabled ? 'Active' : 'Inactive'}`,
          moduleName: module.module_name
        };
      });
      
      console.log("Setting permissions_check:", permissionsCheck);
      setValue("permissions_check", permissionsCheck);

      // Set specific module permissions
      if (userData.contractExecutivesList && userEnabledModules.includes('Contracts')) {
        const contractValues = userData.contractExecutivesList.map(ce => ce.contract_id_fk);
        const selectedContracts = contractOptions.filter(opt => contractValues.includes(opt.value));
        setValue("contract_id", selectedContracts);
      }

      if (userData.landExecutivesList && userEnabledModules.includes('Land Acquisition')) {
        const landValues = userData.landExecutivesList.map(le => le.project_id_fk);
        const selectedLand = landOptions.filter(opt => landValues.includes(opt.value));
        setValue("land_work", selectedLand);
      }

      if (userData.utilityExecutivesList && userEnabledModules.includes('Utility Shifting')) {
        const utilityValues = userData.utilityExecutivesList.map(ue => ue.project_id_fk);
        const selectedUtility = utilityOptions.filter(opt => utilityValues.includes(opt.value));
        setValue("us_work", selectedUtility);
      }

      if (userData.rrExecutivesList && userEnabledModules.includes('R&R')) {
        const rrValues = userData.rrExecutivesList.map(re => re.project_id_fk);
        const selectedRR = rrOptions.filter(opt => rrValues.includes(opt.value));
        setValue("rr_work", selectedRR);
      }

      // Set structure permissions for Execution & Monitoring
      if (userData.executivesList && userEnabledModules.includes('Execution & Monitoring')) {
        if (userData.executivesList.length > 0) {
          // If user has existing structure permissions, use them
          const executiveRows = userData.executivesList.map((executive, index) => ({
            contract_id: contractOptions.find(opt => opt.value === executive.contract_id_fk) || null,
            structures: (executive.structureExecutivesList || []).map(se => ({
              value: se.structure_id_fk,
              label: se.structure_name || se.structure_id_fk
            })),
            availableStructures: []
          }));
          
          setValue("executionMonitoringRows", executiveRows);
          
          // Fetch available structures for each row
          executiveRows.forEach(async (row, index) => {
            if (row.contract_id?.value) {
              await getStructuresByContractId(row.contract_id.value, index);
            }
          });
        } else {
          // If no existing data but module is enabled, ensure at least one empty row
          const currentRows = getValues("executionMonitoringRows") || [];
          if (currentRows.length === 0) {
            setValue("executionMonitoringRows", [{
              contract_id: null,
              structures: [],
              availableStructures: []
            }]);
          }
        }
      }
    }

    setUserDataLoaded(true);
  }, [departmentOptions, userTypeOptions, reportingToOptions, userRoleOptions, moduleOptions, contractOptions, landOptions, utilityOptions, rrOptions]);

  // ===================== USE EFFECTS =====================

  // Initial load: fetch dropdowns and user data
  useEffect(() => {
    const initializeForm = async () => {
      // First, fetch all dropdown data
      const dropdownsLoaded = await fetchDropdownData();
      
      if (isEdit) {
        if (state?.user) {
          // Use state data if available
          setUserData(state.user);
        } else if (userId) {
          // Fetch user data from API
          const fetchedUserData = await fetchUserData(userId);
          if (fetchedUserData && dropdownsLoaded) {
            setUserData(fetchedUserData);
          }
        }
      } else {
        // For add mode: Generate PMIS key
        const generatedKey = generatePMISKey();
        setValue("pmis_key_fk", generatedKey, { shouldValidate: true });
        
        // Auto-check the generated key
        setTimeout(() => {
          checkPMISKeyAvailability(generatedKey);
        }, 1000);
      }
    };

    initializeForm();
  }, [isEdit, userId, state]);

  // Populate form when both dropdowns and user data are ready
  useEffect(() => {
    if (userData && !userDataLoaded && !dropdownLoading) {
      // Small delay to ensure React has processed dropdowns
      setTimeout(() => {
        populateFormData(userData);
      }, 100);
    }
  }, [userData, userDataLoaded, dropdownLoading, populateFormData]);

  // Fetch dynamic reporting to when department or user type changes
  useEffect(() => {
    if (watchDepartment && watchUserType) {
      fetchReportingToPersonsList();
    }
  }, [watchDepartment, watchUserType]);

  // Watch PMIS key changes
  useEffect(() => {
    if (watchPmisKey) {
      setPmisKeyError("");
    }
  }, [watchPmisKey]);

  // Select All Contracts logic
  useEffect(() => {
    const contractValues = getValues("contract_id") || [];
    const contractOptionCount = contractOptions.filter(opt => opt.value !== "").length;
    
    if (contractOptionCount > 0) {
      setSelectAllContracts(contractValues.length === contractOptionCount);
    }
  }, [watch("contract_id"), contractOptions]);

  // ===================== MODULE HANDLING =====================

  // Toggle module checkbox
  const toggleModule = (moduleName) => {
    const isEnabled = enabledModules.includes(moduleName);

    if (isEnabled) {
      // Remove module
      setEnabledModules(prev => prev.filter(m => m !== moduleName));
      
      // Clear module access
      setModuleAccess(prev => {
        const newAccess = { ...prev };
        delete newAccess[moduleName];
        return newAccess;
      });

      // Clear form values for this module
      if (moduleName === 'Contracts') {
        setValue("contract_id", []);
      } else if (moduleName === 'Land Acquisition') {
        setValue("land_work", []);
      } else if (moduleName === 'Utility Shifting') {
        setValue("us_work", []);
      } else if (moduleName === 'R&R') {
        setValue("rr_work", []);
      } else if (moduleName === 'Risk') {
        setValue("risk_work", []);
      } else if (moduleName === 'Execution & Monitoring') {
        // Clear all rows and reset to ONE empty row
        setValue("executionMonitoringRows", [{
          contract_id: null,
          structures: [],
          availableStructures: []
        }]);
      }

      // Update permissions_check value
      updatePermissionCheckValue(moduleName, false);
    } else {
      // Add module
      setEnabledModules(prev => [...prev, moduleName]);
      
      // Initialize with empty access
      setModuleAccess(prev => ({
        ...prev,
        [moduleName]: []
      }));

      // Update permissions_check value
      updatePermissionCheckValue(moduleName, true);

      // For Execution & Monitoring: Ensure at least ONE row exists
      if (moduleName === "Execution & Monitoring") {
        const currentRows = getValues("executionMonitoringRows") || [];
        if (currentRows.length === 0) {
          setValue("executionMonitoringRows", [{
            contract_id: null,
            structures: [],
            availableStructures: []
          }]);
        }
      }
    }
  };

  // Update permission value
  const updatePermissionCheckValue = (moduleName, isActive) => {
    const currentPermissions = getValues("permissions_check") || [];
    const newPermission = {
      value: `${moduleName}_${isActive ? 'Active' : 'Inactive'}`,
      moduleName
    };
    
    // Filter out any existing entry for this module
    const filteredPermissions = currentPermissions.filter(p => 
      p && p.moduleName !== moduleName
    );
    
    // Add the new permission
    setValue("permissions_check", [...filteredPermissions, newPermission], { 
      shouldValidate: true 
    });
  };

  // Handle add new row - adds additional row when clicked
  const handleAddRow = () => {
    appendStructureRow({
      contract_id: null,
      structures: [],
      availableStructures: []
    });
  };

  // Handle delete row - enabled for ALL rows including first row
  const handleRemoveRow = (index) => {
    if (structureRows.length > 1) {
      removeStructureRow(index);
    } else {
      // If it's the last row, clear it instead of removing
      const currentRows = [...getValues("executionMonitoringRows")];
      currentRows[index] = {
        contract_id: null,
        structures: [],
        availableStructures: []
      };
      setValue("executionMonitoringRows", currentRows);
    }
  };

  // ===================== FORM VALIDATION & SUBMISSION =====================

  // Custom validation for image
  const validateForm = async () => {
    const isFormValid = await trigger();
    
    // Check if image is present
    const hasImage = imagePreview || existingImageUrl || (watchFileName && watchFileName[0]);
    
    if (!isEdit && !hasImage) {
      setImageError("User image is required");
      return false;
    }
    
    if (imageError) {
      return false;
    }
    
    return isFormValid;
  };

  // Get all permissions to send
  const getAllPermissionsToSend = () => {
    const allModules = moduleOptions || [];
    const currentPermissions = getValues("permissions_check") || [];
    
    // Create a map of current permissions
    const permissionMap = {};
    currentPermissions.forEach(p => {
      if (p && p.value) {
        const [moduleName] = p.value.split('_');
        permissionMap[moduleName] = p.value;
      }
    });
    
    // Return permissions for ALL modules
    const permissionsToSend = allModules.map(module => {
      if (permissionMap[module.module_name]) {
        return permissionMap[module.module_name];
      } else {
        const isEnabled = enabledModules.includes(module.module_name);
        return `${module.module_name}_${isEnabled ? 'Active' : 'Inactive'}`;
      }
    });
    
    return permissionsToSend;
  };

  // Form submission - FIXED VERSION
  const onSubmit = async (data) => {
    try {
      // Validate form including image
      const isFormValid = await validateForm();
      
      if (!isFormValid) {
        alert("Please fill all required fields correctly.");
        return;
      }

      // Check PMIS key flag
      if (!pmisKeyFlag && !isEdit) {
        alert("Please fix PMIS key issues before submitting.");
        return;
      }

      // Show loading
      setLoading(true);

      // Get all permissions to send
      const permissionsToSend = getAllPermissionsToSend();
      console.log("Permissions to send:", permissionsToSend);

      // Prepare data
      const formData = new FormData();
      
      console.log('Submitting form data:', data);
      
      // Add basic user info - FIXED: Skip fileName if no file selected in edit mode
      Object.entries(data).forEach(([key, value]) => {
        // Skip fileName completely if no file is selected (for edit mode)
        if (key === 'fileName') {
          // Only append fileName if there's actually a file
          if (value && value[0] && value[0] instanceof File) {
            console.log("Appending new file:", value[0].name);
            formData.append('fileName', value[0]);
          } else {
            console.log("Skipping fileName - no new file selected");
            // Don't append anything for fileName
          }
        } 
        else if (key === 'contract_id' && Array.isArray(value)) {
          formData.append('contract_id', value.map(v => v.value).join(','));
        }
        else if (key === 'land_work' && Array.isArray(value)) {
          formData.append('land_work', value.map(v => v.value).join(','));
        }
        else if (key === 'us_work' && Array.isArray(value)) {
          formData.append('us_work', value.map(v => v.value).join(','));
        }
        else if (key === 'rr_work' && Array.isArray(value)) {
          formData.append('rr_work', value.map(v => v.value).join(','));
        }
        else if (key === 'executionMonitoringRows' && Array.isArray(value)) {
          // FIXED: Properly handle multiple rows for contract_ids and structures
          const contractIds = [];
          const structures = [];
          
          value.forEach((row) => {
            if (row.contract_id?.value) {
              contractIds.push(row.contract_id.value);
            } else {
              contractIds.push('');
            }
            
            if (row.structures && Array.isArray(row.structures)) {
              structures.push(row.structures.map(s => s.value).join(','));
            } else {
              structures.push('');
            }
          });
          
          // Append as arrays
          formData.append('contract_ids', contractIds);
          formData.append('structures', structures);
        }
        else if (typeof value === 'object' && value !== null) {
          // For Select fields (react-select objects)
          if (value.value !== undefined) {
            formData.append(key, value.value);
          }
        }
        else if (value !== null && value !== undefined && key !== 'fileName') {
          // Skip fileName here too
          formData.append(key, value);
        }
      });

      // IMPORTANT: Ensure user_id is always sent for update
      if (isEdit) {
        const userIdValue = data.user_id || userId;
        if (userIdValue) {
          // Check if user_id was already added
          if (!formData.has('user_id')) {
            formData.append('user_id', userIdValue);
            console.log("Added user_id:", userIdValue);
          }
        }
      }

      // Add permissions_check
      permissionsToSend.forEach(permission => {
        formData.append('permissions_check', permission);
      });

      // Log FormData contents for debugging
      console.log("FormData contents:");
      for (let pair of formData.entries()) {
        console.log(pair[0] + ': ' + pair[1]);
      }

      // Send to appropriate endpoint
      const endpoint = isEdit 
        ? `${API_BASE_URL}/users/update-user`
        : `${API_BASE_URL}/users/add-user`;

      const response = await api.post(endpoint, formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });

      setLoading(false);

      // FIXED: Handle response correctly
      if (response.data.success === "User updated successfully" || 
          response.data.success === "User added successfully" ||
          response.data.success) {
        alert(isEdit ? "✅ User updated successfully!" : "✅ User added successfully!");
        navigate("/admin/users");
      } else if (response.data.error) {
        alert(response.data.error);
      } else {
        alert(response.data.message || "Error saving user");
      }
    } catch (err) {
      console.error("❌ Error saving user:", err);
      setLoading(false);
      
      if (err.response) {
        console.error("Server response error:", err.response);
        console.error("Error data:", err.response.data);
        console.error("Error status:", err.response.status);
        
        if (err.response.data && err.response.data.error) {
          alert(`Error: ${err.response.data.error}`);
        } else {
          alert("Error saving user. Check console for details.");
        }
      } else if (err.request) {
        console.error("No response received:", err.request);
        alert("No response from server. Please check your connection.");
      } else {
        console.error("Error setting up request:", err.message);
        alert("Error: " + err.message);
      }
    }
  };

  // ===================== RENDER LOGIC =====================

  if (loading || dropdownLoading) {
    return (
      <div className={styles.container}>
        <div className="card">
          <div className="formHeading">
            <h2 className="center-align ps-relative">
              {isEdit ? "Loading user data..." : "Loading form data..."}
            </h2>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className={styles.container}>
      <div className="card">
        <div className="formHeading">
          <h2 className="center-align ps-relative">
            {isEdit ? `Update User (${getValues("user_id") || userId || ""})` : "Add User"}
          </h2>
        </div>
        <div className="innerPage">
          <form onSubmit={handleSubmit(onSubmit)} encType="multipart/form-data">
            {/* Hidden user_id for edit */}
            <input type="hidden" {...register("user_id")} />
            
            <div className="form-row">
              {/* Basic User Info Fields */}
              <div className="form-field">
                <label>Name <span className="red">*</span></label>
                <input {...register("user_name", { required: true })} type="text" placeholder="Enter Value"/>
                {errors.user_name && <span className="text-danger">Required</span>}
              </div>
              
              <div className="form-field">
                <label>Designation <span className="red">*</span></label>
                <input {...register("designation", { required: true })} type="text" placeholder="Enter Value"/>
                {errors.designation && <span className="text-danger">Required</span>}
              </div>

              <div className="form-field">
                <label>Department <span className="red">*</span></label>
                <Controller
                  name="department_fk"
                  control={control}
                  rules={{ required: true }}
                  render={({ field }) => (
                    <Select
                      {...field}
                      classNamePrefix="react-select"
                      placeholder={dropdownLoading ? "Loading..." : "Select Department"}
                      isSearchable
                      options={departmentOptions}
                      isLoading={dropdownLoading}
                    />
                  )}
                />
                {errors.department_fk && <span className="text-danger">Required</span>}
              </div>

              <div className="form-field">
                <label>User Type <span className="red">*</span></label>
                <Controller
                  name="user_type_fk"
                  control={control}
                  rules={{ required: true }}
                  render={({ field }) => (
                    <Select
                      {...field}
                      classNamePrefix="react-select"
                      placeholder={dropdownLoading ? "Loading..." : "Select User Type"}
                      isSearchable
                      options={userTypeOptions}
                      isLoading={dropdownLoading}
                    />
                  )}
                />
                {errors.user_type_fk && <span className="text-danger">Required</span>}
              </div>

              <div className="form-field">
                <label>Reporting To <span className="red">*</span></label>
                <Controller
                  name="reporting_to_id_srfk"
                  control={control}
                  rules={{ required: true }}
                  render={({ field }) => (
                    <Select
                      {...field}
                      classNamePrefix="react-select"
                      placeholder="Select Reporting To"
                      isSearchable
                      options={dynamicReportingTo.length > 0 ? dynamicReportingTo : reportingToOptions}
                      isLoading={dropdownLoading}
                    />
                  )}
                />
                {errors.reporting_to_id_srfk && <span className="text-danger">Required</span>}
              </div>

              <div className="form-field">
                <label>User Role <span className="red">*</span></label>
                <Controller
                  name="user_role_name_fk"
                  control={control}
                  rules={{ required: true }}
                  render={({ field }) => (
                    <Select
                      {...field}
                      classNamePrefix="react-select"
                      placeholder={dropdownLoading ? "Loading..." : "Select User Role"}
                      isSearchable
                      options={userRoleOptions}
                      isLoading={dropdownLoading}
                      onChange={(selected) => {
                        field.onChange(selected);
                        // Set user_role_code
                        if (selected?.name) {
                          setValue("user_role_code", selected.name);
                        }
                      }}
                    />
                  )}
                />
                {errors.user_role_name_fk && <span className="text-danger">Required</span>}
                <input type="hidden" {...register("user_role_code")} />
              </div>

              {/* Contact Info Fields */}
              <div className="form-field">
                <label>Email ID <span className="red">*</span></label>
                <input {...register("email_id", { 
                  required: true,
                  pattern: {
                    value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                    message: "Invalid email address"
                  }
                })} type="email" placeholder="Enter Value"/>
                {errors.email_id && <span className="text-danger">{errors.email_id.message || "Required"}</span>}
              </div>

              <div className="form-field">
                <label>Mobile Number</label>
                <input {...register("mobile_number")} type="number" placeholder="Enter Value"/>
              </div>

              <div className="form-field">
                <label>Personal Contact Number</label>
                <input {...register("personal_contact_number")} type="number" placeholder="Enter Value"/>
              </div>

              <div className="form-field">
                <label>Landline</label>
                <input {...register("landline")} type="number" placeholder="Enter Value"/>
              </div>

              <div className="form-field">
                <label>Extension</label>
                <input {...register("extension")} type="number" placeholder="Enter Value"/>
              </div>

              <div className="form-field">
                <label>PMIS KEY</label>
                <input 
                  {...register("pmis_key_fk")} 
                  type="text" 
                  placeholder="PMIS Key" 
                  onBlur={handlePmisKeyBlur}
                  readOnly={isEdit} // FIXED: Changed from "isEdit || true" to just "isEdit"
                />
                {pmisKeyError && (
                  <span className={pmisKeyError.includes("Available") ? styles.pmisKeySuccess : styles.pmisKeyError}>
                    {pmisKeyError}
                  </span>
                )}
              </div>

              {/* User Image Field */}
              <div className="form-field">
                <div className={styles.imageUploadContainer}>
                  <label className={styles.imageUploadLabel}>
                    User Image <span className="red">*</span>
                    {!isEdit && <span className={styles.requiredNote}>(Required for new users)</span>}
                  </label>
                  
                  {/* Image Preview Section */}
                  {(imagePreview || existingImageUrl) && (
                    <div className={styles.imagePreviewContainer}>
                      <div className={styles.imagePreviewWrapper}>
                        <img 
                          src={imagePreview || existingImageUrl} 
                          alt="User Preview" 
                          className={styles.imagePreview}
                          onClick={handleViewImage}
                          style={{ cursor: 'pointer' }}
                        />
                        <div className={styles.imagePreviewOverlay}>
                          <button 
                            type="button" 
                            className={styles.imageActionBtn}
                            onClick={handleViewImage}
                            title="View Image"
                          >
                            <FiEye size={16} />
                          </button>
                          <button 
                            type="button" 
                            className={styles.imageActionBtn}
                            onClick={handleRemoveImage}
                            title="Remove Image"
                          >
                            <FiTrash2 size={16} />
                          </button>
                        </div>
                      </div>
                    </div>
                  )}

                  {/* File Upload Input */}
                  <div className={styles.fileUploadWrapper}>
                    <label 
                      htmlFor="file-user-image" 
                      className={styles.fileUploadLabel}
                      style={{
                        borderColor: imageError ? '#dc3545' : '#ced4da'
                      }}
                    >
                      <RiAttachment2 size={20} style={{ marginRight: "8px" }} />
                      {imagePreview || existingImageUrl ? "Change Image" : "Choose Image"}
                    </label>
                    <input
                      id="file-user-image"
                      type="file"
                      accept="image/*"
                      {...register("fileName", {
                        validate: {
                          required: (value) => {
                            if (!isEdit && !value?.[0] && !existingImageUrl) {
                              return "User image is required";
                            }
                            return true;
                          },
                          fileType: (value) => {
                            if (value?.[0]) {
                              const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/gif', 'image/webp'];
                              if (!allowedTypes.includes(value[0].type)) {
                                return "Please upload a valid image file (JPEG, PNG, GIF, WebP)";
                              }
                            }
                            return true;
                          },
                          fileSize: (value) => {
                            if (value?.[0]) {
                              const maxSize = 5 * 1024 * 1024; // 5MB
                              if (value[0].size > maxSize) {
                                return "Image size should be less than 5MB";
                              }
                            }
                            return true;
                          }
                        }
                      })}
                      ref={(e) => {
                        fileInputRef.current = e;
                        register("fileName").ref(e);
                      }}
                      onChange={(e) => {
                        register("fileName").onChange(e);
                        handleImageChange(e);
                      }}
                      className={styles.fileUploadInput}
                    />
                    
                    {/* Selected file name */}
                    {watchFileName?.[0] && (
                      <p className={styles.selectedFileName}>
                        Selected: {watchFileName[0].name}
                      </p>
                    )}
                    
                    {/* Image validation error */}
                    {imageError && (
                      <span className={styles.imageError}>
                        {imageError}
                      </span>
                    )}
                    
                    {/* File requirements info */}
                    <p className={styles.fileInfo}>
                      Accepted formats: JPEG, PNG, GIF, WebP | Max size: 5MB
                    </p>
                  </div>
                </div>
              </div>
            </div>

            {/* ============================ MODULE CHECKBOXES ============================ */}
            <div className={styles.moduleCheckboxRow}>
              {moduleOptions.map((module) => {
                const moduleKey = module.module_name.replace(/&/g, '').replace(/ /g, '_');
                return (
                  <label key={module.module_name} className={styles.moduleCheckbox}>
                    <input
                      type="checkbox"
                      checked={enabledModules.includes(module.module_name)}
                      onChange={() => toggleModule(module.module_name)}
                      id={moduleKey}
                      className={`${moduleKey}-ch`}
                      defaultChecked={!isEdit}
                    />
                    {module.module_name}
                  </label>
                );
              })}
            </div>

            {/* ============================ MODULE PERMISSION SECTIONS ============================ */}
            <div className={styles.tablesContainer}>
              {/* Contracts Permission */}
              {enabledModules.includes('Contracts') && (
                <div className={styles.permissionCard}>
                  <h3>Contract Permission</h3>
                  <div className={styles.cardInput}>
                    <Controller
                      name="contract_id"
                      control={control}
                      render={({ field }) => (
                        <div>
                          <Select
                            {...field}
                            isMulti
                            classNamePrefix="react-select"
                            placeholder="Select Contracts"
                            options={contractOptions}
                            value={field.value || []}
                            isLoading={contractOptions.length === 0}
                            onChange={(selected) => {
                              field.onChange(selected);
                              // Update select all state
                              const contractOptionCount = contractOptions.filter(opt => opt.value !== "").length;
                              setSelectAllContracts(selected.length === contractOptionCount);
                            }}
                          />
                          <label style={{ marginTop: '10px', display: 'block' }}>
                            <input 
                              type="checkbox" 
                              id="select-all-contracts"
                              className={styles.checkCss}
                              checked={selectAllContracts}
                              onChange={(e) => {
                                const isChecked = e.target.checked;
                                setSelectAllContracts(isChecked);
                                
                                if (isChecked) {
                                  const allContracts = contractOptions.filter(option => option.value !== "");
                                  field.onChange(allContracts);
                                } else {
                                  field.onChange([]);
                                }
                              }}
                            />
                            Select All
                          </label>
                        </div>
                      )}
                    />
                  </div>
                </div>
              )}

              {/* Land Acquisition Permission */}
              {enabledModules.includes('Land Acquisition') && (
                <div className={styles.permissionCard}>
                  <h3>Land Acquisition Permission</h3>
                  <div className={styles.cardInput}>
                    <Controller
                      name="land_work"
                      control={control}
                      render={({ field }) => (
                        <Select
                          {...field}
                          isMulti
                          classNamePrefix="react-select"
                          placeholder="Select Project"
                          options={landOptions}
                          value={field.value || []}
                          isLoading={landOptions.length === 0}
                        />
                      )}
                    />
                  </div>
                </div>
              )}

              {/* Utility Shifting Permission */}
              {enabledModules.includes('Utility Shifting') && (
                <div className={styles.permissionCard}>
                  <h3>Utility Shifting Permission</h3>
                  <div className={styles.cardInput}>
                    <Controller
                      name="us_work"
                      control={control}
                      render={({ field }) => (
                        <Select
                          {...field}
                          isMulti
                          classNamePrefix="react-select"
                          placeholder="Select Project"
                          options={utilityOptions}
                          value={field.value || []}
                          isLoading={utilityOptions.length === 0}
                        />
                      )}
                    />
                  </div>
                </div>
              )}

              {/* R&R Permission */}
              {enabledModules.includes('R&R') && (
                <div className={styles.permissionCard}>
                  <h3>R&R Permission</h3>
                  <div className={styles.cardInput}>
                    <Controller
                      name="rr_work"
                      control={control}
                      render={({ field }) => (
                        <Select
                          {...field}
                          isMulti
                          classNamePrefix="react-select"
                          placeholder="Select Project"
                          options={rrOptions}
                          value={field.value || []}
                        />
                      )}
                    />
                  </div>
                </div>
              )}

              {/* Execution & Monitoring - Multiple Row Structure Permission */}
              {(enabledModules.includes('Execution & Monitoring') || 
                enabledModules.some(m => m.includes('Execution'))) && (
                <div className="form-row">
                  <div className={styles.incrementTableWrapper}>
                    <h3>Structure Permission</h3>
                    <div className={`dataTable w-100 ${styles.tableWrapper}`}>
                      <table className={styles.incrementTable}>
                        <thead>
                          <tr>
                            <th>#</th>
                            <th>Select Contract</th>
                            <th>Structures (Multi Select)</th>
                            <th>Action</th>
                          </tr>
                        </thead>

                        <tbody>
                          {structureRows.map((row, index) => (
                            <tr key={row.id}>
                              <td>{index + 1}</td>

                              <td style={{ width: "550px" }}>
                                <Controller
                                  name={`executionMonitoringRows.${index}.contract_id`}
                                  control={control}
                                  render={({ field }) => (
                                    <Select
                                      {...field}
                                      classNamePrefix="react-select"
                                      placeholder="Select Contract"
                                      options={contractOptions}
                                      value={field.value || null}
                                      onChange={(selected) => handleContractChange(selected, index)}
                                    />
                                  )}
                                />
                              </td>

                              <td style={{ width: "550px" }}>
                                <Controller
                                  name={`executionMonitoringRows.${index}.structures`}
                                  control={control}
                                  render={({ field }) => (
                                    <Select
                                      {...field}
                                      isMulti
                                      closeMenuOnSelect={false}
                                      classNamePrefix="react-select"
                                      placeholder="Select multiple structures"
                                      options={row.availableStructures || []}
                                      value={field.value || []}
                                      isDisabled={!row.contract_id}
                                    />
                                  )}
                                />
                              </td>

                              <td>
                                <button
                                  type="button"
                                  className="btn btn-outline-danger"
                                  onClick={() => handleRemoveRow(index)}
                                >
                                  <MdOutlineDeleteSweep size="26" />
                                </button>
                              </td>
                            </tr>
                          ))}
                        </tbody>
                      </table>
                    </div>
                    <br />

                    <button
                      type="button"
                      className="btn-2 btn-green"
                      onClick={handleAddRow}
                    >
                      <BiListPlus size="24" /> Add Row
                    </button>
                  </div>
                </div>
              )}
            </div>

            {/* ============================ SAVE BUTTONS ============================ */}
            <div className="form-post-buttons">
              <button 
                type="submit" 
                className="btn btn-primary"
                disabled={(!pmisKeyFlag && !isEdit) || loading}
              >
                {loading ? "Saving..." : (isEdit ? "UPDATE" : "ADD")}
              </button>
              <button type="button" className="btn btn-secondary" onClick={() => navigate(-1)}>
                CANCEL
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}