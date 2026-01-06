import React, { useState, useEffect } from "react";
import Select from "react-select";
import { useForm, Controller, useFieldArray } from "react-hook-form";
import api from "../../../../api/axiosInstance";
import styles from "./LandAcquisitionProcessForm.module.css";
import { API_BASE_URL } from "../../../../config";

import { RiAttachment2 } from 'react-icons/ri';
import { MdOutlineDeleteSweep } from 'react-icons/md';
import { BiListPlus } from 'react-icons/bi';

export default function LandAcquisitionProcessForm() {

  
  const { register, control, handleSubmit } = useForm({
    defaultValues: {
      privateLands: [{}],
      governmentLands: [{}],
      forestLands: [{}],
    },
  });

  const privateArr = useFieldArray({ control, name: "privateLands" });
  const govtArr = useFieldArray({ control, name: "governmentLands" });
  const forestArr = useFieldArray({ control, name: "forestLands" });

  const onSubmit = (data) => {
    const hasAny =
      hasData(data.privateLands) ||
      hasData(data.governmentLands) ||
      hasData(data.forestLands);

    if (!hasAny) {
      alert("Please enter at least one land detail.");
      return;
    }

    console.log("FINAL JSON PAYLOAD:", data);
  };

  return (
    <div className={styles.container}>
      <div className="card">
            <div className="formHeading">
              <h2 className="center-align ps-relative">
                Land Acquisition Process
              </h2>
            </div>
            <div className="innerPage">

      <form onSubmit={handleSubmit(onSubmit)}>

        {/* ================= HEADER FIELDS ================= */}
        <div className="form-row">
          <div className="form-field">
            <label>Project</label>
            <Controller
              name="project"
              control={control}
              rules={{ required: true }}
              render={({ field }) => (
                <Select
                  {...field}
                  classNamePrefix="react-select"
                  placeholder="Select Value"
                  isSearchable
                />
              )}
            />
            
          </div>

          <div className="form-field">
            <label>Submission of proposal to GM</label>
            <input type="date" {...register("submissionOfProposalToGM")} />
          </div>

          <div className="form-field">
            <label>GM Approval</label>
            <input type="date" {...register("gmApproval")} />
          </div>
        </div>

        {/* ================= PRIVATE LAND ================= */}
        <h6 className="d-flex justify-content-center mt-1 mb-2">Private Land</h6>
       
        <LandTable
          title="Private Land"
          fields={privateArr.fields}
          append={privateArr.append}
          remove={privateArr.remove}
          register={register}
          name="privateLands"
          columns={[
            "district",
            "taluka",
            "village",
            "collector",
            "areaRequired",
          ]}
        />

        {/* ================= GOVERNMENT LAND ================= */}
        <h6 className="d-flex justify-content-center mt-1 mb-2">Government Land</h6>
        <LandTable
          title="Government Land"
          fields={govtArr.fields}
          append={govtArr.append}
          remove={govtArr.remove}
          register={register}
          name="governmentLands"
          columns={[
            "district",
            "taluka",
            "village",
            "areaRequired",
            "dateOfApplication",
          ]}
          dateColumns={["dateOfApplication"]}
        />

        {/* ================= FOREST LAND ================= */}
        <h6 className="d-flex justify-content-center mt-1 mb-2">Forest Land</h6>
        <LandTable
          title="Forest Land"
          fields={forestArr.fields}
          append={forestArr.append}
          remove={forestArr.remove}
          register={register}
          name="forestLands"
          columns={[
            "district",
            "taluka",
            "village",
            "areaRequired",
            "stage1Approval",
            "stage2Approval",
          ]}
          dateColumns={["stage1Approval", "stage2Approval"]}
        />

        {/* ================= ACTIONS ================= */}
        <div className="form-post-buttons">
        <button className="btn btn-primary" >
          Submit
        </button>
        <button className="btn btn-white">CANCEL</button>
      </div>
      </form>
    </div>
    </div>
    </div>
  );
}

/* ================= REUSABLE TABLE ================= */

function LandTable({
  fields,
  append,
  remove,
  register,
  name,
  columns,
  dateColumns = [],
}) {
  return (
    <>
    <div className={`dataTable w-100 ${styles.tableWrapper}`}>
      <table className={styles.incrementTable}>
        <thead>
          <tr>
            {columns.map((c) => (
              <th key={c}>{beautify(c)}</th>
            ))}
            <th>Action</th>
          </tr>
        </thead>

        <tbody>
          {fields.map((field, index) => (
            <tr key={field.id}>
              {columns.map((col) => (
                <td key={col}>
                  <input
                    type={dateColumns.includes(col) ? "date" : "text"}
                    {...register(`${name}.${index}.${col}`)}
                  />
                </td>
              ))}

              <td>
                <button type="button" className="btn btn-outline-danger" onClick={() => remove(index)}>
                  <MdOutlineDeleteSweep
                    size="26"
                  />
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      </div>

      <div className={styles.centerAlign}>
        
        <button
          type="button"
          className="btn-2 btn-green"
          onClick={() =>
            append({
            })
          }
        >
          <BiListPlus
            size="24"
          />
        </button>
      </div>
      
    </>
  );
}

/* ================= HELPERS ================= */

function hasData(arr = []) {
  return arr.some((row) =>
    Object.values(row || {}).some((v) => v && v.toString().trim() !== "")
  );
}

function beautify(text) {
  return text
    .replace(/([A-Z])/g, " $1")
    .replace(/^./, (s) => s.toUpperCase());
}
