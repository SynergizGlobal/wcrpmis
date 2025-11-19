import React, { useEffect } from "react";
import { useForm, Controller, useFieldArray } from "react-hook-form";
import Select from "react-select";
import { useNavigate, useLocation } from "react-router-dom";
import api from "../../../../api/axiosInstance";
import styles from './ContractorForm.module.css';
import { API_BASE_URL } from "../../../../config";

export default function ContractorForm() {
  const navigate = useNavigate();
  const { state } = useLocation(); // passed when editing
  const isEdit = Boolean(state?.contractor);

  // React Hook Form setup
  const {
    register,
    control,
    handleSubmit,
    setValue,
    watch,
    formState: { errors },
  } = useForm({
    defaultValues: {
      contractorName: "",
      panNumber: "",
      specilaization: "",
      address: "",
      primaryContact: "",
      phoneNumber: "",
      email: "",
      gstNumber: "",
      bankName: "",
      accountNo: "",
      ifscCode: "",
      bankAddress: "",
      remarks: "",
    }
  });

  // Prefill form if editing
  useEffect(() => {
    if (isEdit && state.contractor) {
      Object.entries(state.contractor).forEach(([key, value]) => {
        if (key !== "specilaization") {
          setValue(key, value);
        }
      });

      // Fix react-select value when editing
      if (state.contractor.specilaization) {
        setValue("specilaization", {
          value: state.contractor.specilaization,
          label: state.contractor.specilaization
        });
      }
    }
  }, [isEdit, setValue, state]);

  // Submit
  const onSubmit = async (data) => {
    try {
      const payload = {
        ...data,
        specilaization: data.specilaization?.value || data.specilaization
      };

      if (!isEdit) {
        // CREATE
        await api.post(`${API_BASE_URL}/contractors`, payload);
      } else {
        // UPDATE
        const updatedData = { ...payload };
        delete updatedData.contractorId;

        await api.put(
          `${API_BASE_URL}/contractors/${state.contractor.contractorId}`,
          updatedData
        );
      }

      alert("Contractor details saved successfully!");
      navigate("/updateforms/Contractor");

    } catch (err) {
      console.error("Error saving contractor:", err);
      alert("Error saving contractor details");
    }
  };


  return (
      <div className={`${styles.container} container-padding`}>
        <div className="card">
          <div className="formHeading">
            <h2 className="center-align ps-relative">
              {isEdit ? "Edit Contractor" : "Add Contractor"}
            </h2>
          </div>

          <div className="innerPage">
            <form onSubmit={handleSubmit(onSubmit)}>

              {/* PAN + Specialization */}
              <div className="form-row">
                <div className="form-field">
                  <label>PAN Number <span className="red">*</span></label>
                  <input {...register("panNumber", { required: true, pattern: {value: /^[A-Z]{5}[0-9]{4}[A-Z]{1}$/, message: "Invalid PAN format (e.g., ABCDE1234F)"}
				   })} type="text"  maxLength={10} placeholder="Enter Number"
				   onInput={(e) => (e.target.value = e.target.value.toUpperCase())}/>
				   {errors.panNumber && (
				     <span className="red">{errors.panNumber.message}</span>
					 )}
                </div>

                <div className="form-field">
                  <label>Specialization <span className="red">*</span></label>
                    <Controller
                      name="specilaization"
                      control={control}
                      rules={{ required: "Specialization is required"}}
                      render={({ field }) => (
                        <Select
                          {...field}
                          classNamePrefix="react-select"
                          options={[
                            { value: "Construction", label: "Construction" },
                            { value: "Consultancy", label: "Consultancy" },
                            { value: "Design Consultancy", label: "Design Consultancy" },
                            { value: "OEM", label: "OEM" },
                            { value: "Project Management Consultancy", label: "Project Management Consultancy" },
                          ]}
                          placeholder="Select specialization"
                          isSearchable
                          isClearable
                          onChange={(selected) => field.onChange(selected)}
                        />
                      )}
                    />
					{errors.specilaization && (
					   <span className="red">{errors.specilaization.message}</span>
					 )}
                </div>
              </div>

              {/* Contractor Name */}
              <div className="form-row">
                <div className="form-field">
                  <label>Contractor Name <span className="red">*</span></label>
                  <input {...register("contractorName", { required: "contractor name is Required" })} type="text" placeholder="Enter Contractor Name" />
				  {errors.contractorName && (
				      <span className="red">{errors.contractorName.message}</span>
				    )}
                </div>
              </div>

              {/* Address */}
              <div className="form-row">
                <div className="form-field">
                  <label>Address </label>
                  <textarea
                    {...register("address")}
                    rows="3"
                    maxLength={200}
                  ></textarea>
                  <div style={{ textAlign: "right" }}>
                    {watch("address")?.length || 0}/200
                  </div>
                </div>
              </div>

              {/* Contact Details */}
              <div className="form-row">
                <div className="form-field">
                  <label>Primary Contact </label>
				  <input
				    {...register("primaryContact", {
					   validate: (value) =>
					       value === "" || /^[0-9]{10}$/.test(value) || "Must be 10 digits"
					   })}
				    maxLength={10}
				    placeholder="Enter number"
				    onInput={(e) => e.target.value = e.target.value.replace(/\D/g, "")}  
				  />
				  {errors.primaryContact && <span className="red">{errors.primaryContact.message}</span>}
                </div>

                <div className="form-field">
                  <label>Phone Number </label>
				  <input
				    {...register("phoneNumber", {
						validate: (value) =>
						 value === "" || /^[0-9]{10}$/.test(value) || "Must be 10 digits"
						})}
				    type="text"
				    maxLength={10}
				    placeholder="Enter phone number"
				    onInput={(e) => e.target.value = e.target.value.replace(/\D/g, "")}
				  />
				  {errors.phoneNumber && <span className="red">{errors.phoneNumber.message}</span>}
                </div>

                <div className="form-field">
                  <label>Email </label>
                  <input {...register("email")} type="text" placeholder="Enter Value"/>
                </div>
              </div>

              {/* GST, Bank, IFSC, Account */}
              <div className="form-row flex-4">
                <div className="form-field">
                  <label>GST Number </label>
				  <input
				  {...register("gstNumber", {
				     validate: (value) =>
				       value === "" ||
				       /^[0-9]{2}[A-Z]{5}[0-9]{4}[A-Z]{1}[1-9A-Z]{1}Z[0-9A-Z]{1}$/.test(value) ||
				       "Invalid GST format"
				   })}
				   maxLength={15} 
				   placeholder="Enter Value"
				    onInput={(e) => (e.target.value = e.target.value.toUpperCase())}
					/>
					{errors.gstNumber && <span className="red">{errors.gstNumber.message}</span>}
                </div>

                <div className="form-field">
                  <label>Bank Name</label>
                  <input {...register("bankName")} type="text" placeholder="Enter Value"/>
                </div>

                <div className="form-field">
                  <label>IFSC Code</label>
                  <input {...register("ifscCode")} type="text" placeholder="Enter Value"/>
                </div>

                <div className="form-field">
                  <label>Account Number</label>
                  <input {...register("accountNo")} type="text" placeholder="Enter Value"/>
                </div>
              </div>

              {/* Bank Address */}
              <div className="form-row">
                <div className="form-field">
                  <label>Bank Address</label>
                  <textarea {...register("bankAddress")} rows="3" maxLength={200}></textarea>
                </div>
              </div>

              {/* Remarks */}
              <div className="form-row">
                <div className="form-field">
                  <label>Remarks</label>
                  <textarea {...register("remarks")} rows="3" maxLength={200}></textarea>
                </div>
              </div>

              {/* Buttons */}
              <div className="form-post-buttons">
                <button type="submit" className="btn btn-primary">
                  {isEdit ? "UPDATE" : "ADD"}
                </button>

                <button
                  type="button"
                  className="btn btn-white"
                  onClick={() => navigate(-1)}
                >
                  Cancel
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    );
  }
