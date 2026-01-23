import React, { useState, useEffect, useCallback, useRef } from "react";
import { useForm, Controller, useFieldArray } from "react-hook-form";
import Select from "react-select";
import { useNavigate, useLocation } from "react-router-dom";
import api from "../../../../api/axiosInstance";
import styles from "./UserForm.module.css";
import { API_BASE_URL } from "../../../../config";

import { RiAttachment2 } from 'react-icons/ri';
import { MdOutlineDeleteSweep } from 'react-icons/md';
import { BiListPlus } from 'react-icons/bi';
import { FiEye, FiTrash2 } from 'react-icons/fi';

export default function UserForm() {
    
    const navigate = useNavigate();
    const { state } = useLocation(); 
    const isEdit = Boolean(state?.user);
    const [action] = useState(isEdit ? "edit" : "add");

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
      
          // Structure permissions (Execution & Monitoring)
          executionMonitoringRows: []
        },
        mode: "onChange"
      });

  // Watch department and user type for dynamic reporting to
  const watchDepartment = watch("department_fk");
  const watchUserType = watch("user_type_fk");
  const watchPmisKey = watch("pmis_key_fk");
  const watchFileName = watch("fileName");

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

  // Handle image view (preview in modal or new tab)
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
  
  // Generate random PMIS key (exactly like JSP)
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

    // Format: AA33HT34MG71 (2 letters + 2 digits repeated)
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

      // Handle response
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
      
      // On any error, assume Available (don't block user)
      setPmisKeyError("Available (check failed)");
      setPmisKeyFlag(true);
      return true;
      
    } finally {
      setIsCheckingPmisKey(false);
    }
  };

  // Handle PMIS key blur (like JSP's blur event)
  const handlePmisKeyBlur = async () => {
    const pmisKey = getValues("pmis_key_fk");
    const existingUserKey = isEdit && state?.user ? state.user.pmis_key_fk : "";
    
    if (pmisKey && pmisKey.trim() !== "" && pmisKey !== existingUserKey) {
      await checkPMISKeyAvailability(pmisKey);
    } else if (pmisKey === existingUserKey) {
      // Same key for existing user = OK
      setPmisKeyFlag(true);
      setPmisKeyError("");
    } else if (!pmisKey || pmisKey.trim() === "") {
      // Empty key
      setPmisKeyError("PMIS key is required");
      setPmisKeyFlag(false);
    }
  };

  // ===================== END PMIS KEY FUNCTIONS =====================

  // ===================== API FUNCTIONS =====================

  // Fetch contracts list using your API endpoint
  const fetchContractsList = async () => {
    try {
      // Prepare the request body (like the User obj in your API)
      const requestBody = {
        // Add any user properties if needed
        user_id: isEdit && state?.user ? state.user.user_id : "",
        department_fk: getValues("department_fk")?.value || ""
      };
      
      const response = await api.post(
        `${API_BASE_URL}/users/ajax/form/add-user-form/getContractsList`,
        requestBody
      );

      if (response.data?.contractsList) {
        // Update dropdown options
        setDropdownOptions(prev => ({
          ...prev,
          contractsList: response.data.contractsList
        }));

        // Convert to Select options
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
      // Prepare the request body
      const requestBody = {
        user_id: isEdit && state?.user ? state.user.user_id : "",
        department_fk: getValues("department_fk")?.value || ""
      };
      
      const response = await api.post(
        `${API_BASE_URL}/users/ajax/form/add-user-form/getLandList`,
        requestBody
      );

      if (response.data?.landList) {
        // Update dropdown options
        setDropdownOptions(prev => ({
          ...prev,
          landList: response.data.landList
        }));

        // Convert to Select options
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
      // Prepare the request body
      const requestBody = {
        user_id: isEdit && state?.user ? state.user.user_id : "",
        department_fk: getValues("department_fk")?.value || ""
      };
      
      const response = await api.post(
        `${API_BASE_URL}/users/ajax/form/add-user-form/getUtilityList`,
        requestBody
      );

      if (response.data?.utilityList) {
        // Update dropdown options
        setDropdownOptions(prev => ({
          ...prev,
          utilityList: response.data.utilityList
        }));

        // Convert to Select options
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
      // Prepare the request body
      const requestBody = {
        user_id: isEdit && state?.user ? state.user.user_id : "",
        department_fk: getValues("department_fk")?.value || ""
      };
      
      const response = await api.post(
        `${API_BASE_URL}/users/ajax/form/add-user-form/getRRList`,
        requestBody
      );

      if (response.data?.rrList) {
        // Update dropdown options
        setDropdownOptions(prev => ({
          ...prev,
          rrList: response.data.rrList
        }));

        // Convert to Select options
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
      setLoading(true);
      
      // First, get the main form data
      const response = await api.post(`${API_BASE_URL}/users/ajax/form/add-user-form`, {});
      
      if (response.data) {
        // Initialize dropdown options with basic data
        const updatedOptions = {
          roles: response.data.roles || [],
          types: response.data.types || [],
          departments: response.data.departments || [],
          reportingToList: response.data.reportingToList || [],
          pmisKeys: response.data.pmisKeys || [],
          moduleList: response.data.moduleList || [],
          contractsList: [], // Will be populated via separate API
          landList: [], // Will be populated via separate API
          utilityList: [], // Will be populated via separate API
          rrList: [], // Will be populated via separate API
          structuresList: response.data.structuresList || [],
        
        };
        
        setDropdownOptions(updatedOptions);

        // Fetch all module lists in parallel for better performance
        await Promise.all([
          fetchContractsList(),
          fetchLandList(),
          fetchUtilityList(),
          fetchRRList(),
     
        ]);

        // Set default module checkboxes (all checked for add, conditional for edit)
        if (!isEdit) {
          const allModules = (response.data.moduleList || []).map(module => module.module_name);
          setEnabledModules(allModules);
          
          // Initialize permissions_check for add mode
          const permissionsCheck = allModules.map(module => ({
            value: `${module}_Active`,
            moduleName: module
          }));
          setValue("permissions_check", permissionsCheck);
        }
      }
    } catch (err) {
      console.error("❌ Error fetching dropdown data:", err);
      alert("Error loading form data");
    } finally {
      setLoading(false);
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

  // Get structures by contract ID (for Execution & Monitoring)
  const getStructuresByContractId = async (contractId, rowIndex) => {
    try {
      const response = await api.post(`${API_BASE_URL}/users/ajax/getStructuresByContractId`, {
        contract_id_fk: contractId
      });

      if (response.data) {
        const structureOptions = response.data.map(structure => ({
          value: structure.structure_id_fk,
          label: structure.structure
        }));

        // Update the specific row with available structures and clear selected structures
        const currentRows = [...getValues("executionMonitoringRows")];
        currentRows[rowIndex].availableStructures = structureOptions;
        currentRows[rowIndex].structures = []; // Clear selected structures when contract changes
        setValue("executionMonitoringRows", currentRows);
      }
    } catch (err) {
      console.error("Error fetching structures:", err);
    }
  };

  // Handle contract change - clear structures
  const handleContractChange = async (selected, rowIndex) => {
    // Update the contract value
    const currentRows = [...getValues("executionMonitoringRows")];
    currentRows[rowIndex].contract_id = selected;
    currentRows[rowIndex].structures = []; // Clear structures when contract changes
    setValue("executionMonitoringRows", currentRows);
    
    // Fetch new structures if a contract is selected
    if (selected && selected.value) {
      await getStructuresByContractId(selected.value, rowIndex);
    } else {
      // If no contract selected, clear available structures
      currentRows[rowIndex].availableStructures = [];
      setValue("executionMonitoringRows", currentRows);
    }
  };

  // ===================== END API FUNCTIONS =====================

  // Fetch dropdown data on component mount
  useEffect(() => {
    fetchDropdownData();
    
    // If editing, populate form with existing user data
    if (isEdit && state?.user) {
      populateFormData(state.user);
    } else {
      // For add mode: Generate PMIS key on load automatically
      const generatedKey = generatePMISKey();
      setValue("pmis_key_fk", generatedKey, { shouldValidate: true });
      
      // Auto-check the generated key
      const timer = setTimeout(() => {
        checkPMISKeyAvailability(generatedKey);
      }, 1000); // 1 second delay
      
      if (structureRows.length === 0) {
        appendStructureRow({
          contract_id: null,
          structures: [],
          availableStructures: []
        });
      }
      
      return () => clearTimeout(timer);
    }
  }, [generatePMISKey, isEdit, state]);

  // Fetch dynamic reporting to when department or user type changes
  useEffect(() => {
    if (watchDepartment && watchUserType) {
      fetchReportingToPersonsList();
    }
  }, [watchDepartment, watchUserType]);

  // Watch PMIS key changes for real-time validation
  useEffect(() => {
    if (watchPmisKey) {
      // Clear error when user starts typing
      setPmisKeyError("");
    }
  }, [watchPmisKey]);

  // Refresh contracts when needed
  useEffect(() => {
    // If editing and user data changes, refresh contracts
    if (isEdit && state?.user) {
      const timer = setTimeout(() => {
        fetchContractsList();
      }, 500);
      
      return () => clearTimeout(timer);
    }
  }, [isEdit, state?.user]);
  
  useEffect(() => {
    const contractValues = getValues("contract_id") || [];
    const contractOptionCount = contractOptions.filter(opt => opt.value !== "").length;
    
    if (contractOptionCount > 0) {
      setSelectAllContracts(contractValues.length === contractOptionCount);
    }
  }, [watch("contract_id"), contractOptions]);
  
  const populateFormData = async (userData) => {
    // Basic user info
    Object.entries(userData).forEach(([key, value]) => {
      if (value !== null && value !== undefined) {
        // Handle Select fields
        if (key === 'department_fk') {
          const dept = dropdownOptions.departments.find(d => d.department === value);
          setValue(key, dept ? { value: dept.department, label: dept.department_name } : null);
        } 
        else if (key === 'user_type_fk') {
          const type = dropdownOptions.types.find(t => t.user_type_fk === value);
          setValue(key, type ? { value: type.user_type_fk, label: type.user_type_fk } : null);
        }
        else if (key === 'reporting_to_id_srfk') {
          const reportingTo = dropdownOptions.reportingToList.find(r => r.user_id === value);
          setValue(key, reportingTo ? { 
            value: reportingTo.user_id, 
            label: reportingTo.designation ? `${reportingTo.designation} - ${reportingTo.user_name}` : reportingTo.user_name 
          } : null);
        }
        else if (key === 'user_role_name_fk') {
          const role = dropdownOptions.roles.find(r => r.user_role_name === value);
          setValue(key, role ? { value: role.user_role_name, label: role.user_role_name } : null);
        }
        else if (key === 'pmis_key_fk') {
          setValue(key, value);
          // For edit mode, set flag to true (like JSP)
          if (isEdit) {
            setPmisKeyFlag(true);
          }
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
      // Assuming user_image contains the image URL or path
      const fullImageUrl = `${API_BASE_URL}/${userData.user_image}`;
      setExistingImageUrl(fullImageUrl);
    }

    // Wait for all module lists to load before setting module permissions
    await Promise.all([
      fetchContractsList(),
      fetchLandList(),
      fetchUtilityList(),
      fetchRRList()
    
    ]);

    // Set module checkboxes based on user permissions
    if (dropdownOptions.moduleList) {
      let userEnabledModules = [];
      
      if (userData.user_permissions) {
        userEnabledModules = dropdownOptions.moduleList
          .filter(module => {
            const userPermission = userData.user_permissions.find(p => p.module_name === module.module_name);
            return userPermission?.soft_delete_status === 'Active';
          })
          .map(module => module.module_name);
      } else if (!isEdit) {
        // For add mode, enable all modules by default
        userEnabledModules = dropdownOptions.moduleList.map(module => module.module_name);
      }
      
      setEnabledModules(userEnabledModules);

      // Set permissions_check values - ALL modules must be included (like JSP)
      const permissionsCheck = dropdownOptions.moduleList.map(module => {
        const isEnabled = userEnabledModules.includes(module.module_name);
        return {
          value: `${module.module_name}_${isEnabled ? 'Active' : 'Inactive'}`,
          moduleName: module.module_name
        };
      });
      
      console.log("Setting permissions_check in populateFormData:", permissionsCheck);
      setValue("permissions_check", permissionsCheck);

      // Set specific module permissions if available
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
        const executiveRows = userData.executivesList.map((executive, index) => ({
          contract_id: contractOptions.find(opt => opt.value === executive.contract_id_fk) || null,
          structures: (executive.structureExecutivesList || []).map(se => ({
            value: se.structure_id_fk,
            label: se.structure_name || se.structure_id_fk
          })),
          availableStructures: [] // Will be populated when contract is selected
        }));
        
        // Set all rows at once
        setValue("executionMonitoringRows", executiveRows);
      } else if (userEnabledModules.includes('Execution & Monitoring')) {
        // If module is enabled but no executive data, check if we need to add a row
        const currentRows = getValues("executionMonitoringRows") || [];
        if (currentRows.length === 0) {
          appendStructureRow({
            contract_id: null,
            structures: [],
            availableStructures: []
          });
        }
      }
    }
  };

  // Toggle module checkbox (JSP's valueChanged function)
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
        setValue("executionMonitoringRows", []);
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

      // Add initial row for Execution & Monitoring if no rows exist
      if (moduleName === "Execution & Monitoring") {
        const currentRows = getValues("executionMonitoringRows") || [];
        if (currentRows.length === 0) {
          appendStructureRow({
            contract_id: null,
            structures: [],
            availableStructures: []
          });
        }
      }
    }
  };

  // Update permission value (like JSP's valueChanged function)
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

  // Handle delete row - ensure at least one row remains
  const handleRemoveRow = (index) => {
    if (structureRows.length > 1) {
      removeStructureRow(index);
    } else {
      // If only one row, just clear its values but keep the row
      const currentRows = [...getValues("executionMonitoringRows")];
      currentRows[index] = {
        contract_id: null,
        structures: [],
        availableStructures: []
      };
      setValue("executionMonitoringRows", currentRows);
    }
  };

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

  // Get all permissions to send (like JSP does)
  const getAllPermissionsToSend = () => {
    const allModules = dropdownOptions.moduleList || [];
    const currentPermissions = getValues("permissions_check") || [];
    
    // Create a map of current permissions for quick lookup
    const permissionMap = {};
    currentPermissions.forEach(p => {
      if (p && p.value) {
        const [moduleName] = p.value.split('_');
        permissionMap[moduleName] = p.value;
      }
    });
    
    // Return permissions for ALL modules (like JSP)
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

  // Form submission (like JSP's addUser/updateUser functions)
  const onSubmit = async (data) => {
    try {
      // Validate form including image
      const isFormValid = await validateForm();
      
      if (!isFormValid) {
        alert("Please fill all required fields correctly.");
        return;
      }

      // Check PMIS key flag (like JSP's flag check)
      if (!pmisKeyFlag && !isEdit) {
        alert("Please fix PMIS key issues before submitting.");
        return;
      }

      // Show loading (like JSP's page-loader)
      setLoading(true);

      // Get all permissions to send
      const permissionsToSend = getAllPermissionsToSend();
      console.log("Permissions to send:", permissionsToSend);

      // Prepare data like JSP does
      const formData = new FormData();
      
      // Debug: Log what we're sending
      console.log('Submitting form data:', data);
      
      // Add basic user info
      Object.entries(data).forEach(([key, value]) => {
        if (key === 'fileName' && value && value[0]) {
          formData.append('fileName', value[0]);
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
          // Format contract_ids and structures like JSP
          value.forEach((row, index) => {
            if (row.contract_id?.value) {
              // Convert commas to ~$~ like JSP does
              const contractValue = row.contract_id.value.split(",").join("~$~");
              formData.append('contract_ids', contractValue);
            }
            if (row.structures && Array.isArray(row.structures)) {
              const structureValues = row.structures.map(s => s.value).join(',');
              formData.append('structures', structureValues);
            }
          });
        }
        else if (typeof value === 'object' && value !== null) {
          // For Select fields (react-select objects)
          if (value.value !== undefined) {
            formData.append(key, value.value);
          }
        }
        else if (value !== null && value !== undefined) {
          formData.append(key, value);
        }
      });

      // CRITICAL: Add permissions_check like JSP does
      // JSP sends multiple fields with same name 'permissions_check'
      permissionsToSend.forEach(permission => {
        formData.append('permissions_check', permission);
      });

      // Debug: Log FormData contents
      console.log('FormData contents:');
      for (let [key, value] of formData.entries()) {
        console.log(key, '=', value);
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

      if (response.data.success) {
        alert(isEdit ? "✅ User updated successfully!" : "✅ User added successfully!");
        navigate("/admin/users");
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

  if (loading) {
    return (
      <div className={styles.container}>
        <div className="card">
          <div className="formHeading">
            <h2 className="center-align ps-relative">
              Loading form data...
            </h2>
          </div>
        </div>
      </div>
    );
  }

  // Convert backend arrays to Select options
  const departmentOptions = (dropdownOptions.departments || []).map(dept => ({
    value: dept.department,
    label: dept.department_name
  }));

  const userTypeOptions = (dropdownOptions.types || []).map(type => ({
    value: type.user_type_fk,
    label: type.user_type_fk
  }));

  const reportingToOptions = (dropdownOptions.reportingToList || []).map(rep => ({
    label: `${rep.designation} - ${rep.user_name}`,
    value: rep.user_id
  }));

  const userRoleOptions = (dropdownOptions.roles || []).map(role => ({
    value: role.user_role_name,
    label: role.user_role_name,
    name: role.user_role_code
  }));

  return (
    <div className={styles.container}>
      <div className="card">
        <div className="formHeading">
          <h2 className="center-align ps-relative">
            {isEdit ? `Update User (${getValues("user_id") || ""})` : "Add User"}
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
                      placeholder="Select Department"
                      isSearchable
                      options={departmentOptions}
                      isLoading={loading}
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
                      placeholder="Select User Type"
                      isSearchable
                      options={userTypeOptions}
                      isLoading={loading}
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
                      options={reportingToOptions}
                      isLoading={loading}
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
                      placeholder="Select User Role"
                      isSearchable
                      options={userRoleOptions}
                      isLoading={loading}
                      onChange={(selected) => {
                        field.onChange(selected);
                        // Set user_role_code like JSP's setUserRoleCode()
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
                  readOnly={isEdit || true} 
                />
                {pmisKeyError && (
                  <span className={pmisKeyError.includes("Available") ? styles.pmisKeySuccess : styles.pmisKeyError}>
                    {pmisKeyError}
                  </span>
                )}
              </div>

              {/* User Image Field with Preview and Validation */}
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
                            // Only validate as required for new users
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
            
              {(dropdownOptions.moduleList || []).map((module) => {
                
                const moduleKey = module.module_name.replace(/&/g, '').replace(/ /g, '_');
                return (
                  <label key={module.module_name} className={styles.moduleCheckbox}>
                    <input
                      type="checkbox"
                      checked={enabledModules.includes(module.module_name)}
                      onChange={() => toggleModule(module.module_name)}
                      id={moduleKey}
                      className={`${moduleKey}-ch`}
                      defaultChecked={!isEdit} // All checked for add mode (like JSP)
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
                                  // Select all options except the placeholder/empty one
                                  const allContracts = contractOptions.filter(option => option.value !== "");
                                  field.onChange(allContracts);
                                } else {
                                  // Deselect all
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

              {/* Execution & Monitoring - Structure Permission Table (Updated Design) */}
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
                                      isDisabled={!row.contract_id} // Disable if no contract selected
                                    />
                                  )}
                                />
                              </td>

                              <td>
                                <button
                                  type="button"
                                  className="btn btn-outline-danger"
                                  onClick={() => handleRemoveRow(index)}
                                  disabled={structureRows.length === 1}
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
                      onClick={() => appendStructureRow({ 
                        contract_id: null, 
                        structures: [], 
                        availableStructures: [] 
                      })}
                    >
                      <BiListPlus size="24" />
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