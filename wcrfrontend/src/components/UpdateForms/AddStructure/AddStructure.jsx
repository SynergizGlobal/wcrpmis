import React, { useContext, useState, useEffect } from "react";
import { RefreshContext } from "../../../context/RefreshContext";
import Select from "react-select";
import styles from "./AddStructure.module.css";
import { CirclePlus } from "lucide-react";
import api from "../../../api/axiosInstance";
import { Outlet, useNavigate, useLocation } from "react-router-dom"; 
import { API_BASE_URL } from "../../../config";

import { MdEditNote } from "react-icons/md";

export default function AddStructure() {
  const location = useLocation();
  const navigate = useNavigate();
  const { refresh } = useContext(RefreshContext);
  const [search, setSearch] = useState("");
   const [page, setPage] = useState(1);
   const [perPage, setPerPage] = useState(10);
   const [projects, setProjects] = useState([]);
   const [allProjectSummaries, setAllProjectSummaries] = useState([]);

  const [filters, setFilters] = useState({
    project: "",
  });
  
  const filteredProjects = filters.project
    ? allProjectSummaries.filter(p => p.projectId === filters.project)
    : allProjectSummaries;
	
	const searchedProjects = filteredProjects.filter(p => {
	  const text = search.toLowerCase();

	  const matchProject = p.projectId.toLowerCase().includes(text);

	  const matchStructureTypes = p.structureTypes
	    .map(t => `${t.structureType} ${t.count}`)
	    .join(" ")
	    .toLowerCase()
	    .includes(text);

	  return matchProject || matchStructureTypes;
	});

  // pagination values computed from filteredProjects
  const totalRecords = searchedProjects.length;
  const totalPages = Math.ceil(totalRecords / perPage);

  const paginatedRows = searchedProjects.slice(
    (page - 1) * perPage,
    page * perPage
  );
  
  useEffect(() => {
    api.get(`${API_BASE_URL}/structures/allProjectSummaries`)
      .then(res => {
        setAllProjectSummaries(res.data);
        console.log("All project summaries:", res.data);
      })
      .catch(err => console.error("Error loading summaries:", err));
  }, [refresh, location]);

  
  useEffect(() => {
    api.get(`${API_BASE_URL}/projects/api/getProjects`)
      .then(res => {
		console.log("Projects API Response:", res.data);
        setProjects(res.data);
      })
      .catch(err => console.error(err));
  }, [refresh, location]);
  

  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setFilters({ ...filters, [name]: value });
  };
  
  const handleClearFilters = () => {
     setFilters({ project: "" });  
     setSearch("");               
     setPage(1);                   
   };

  const handleAdd = () => navigate("addstructureform");
  const handleEdit = async (projectId) => {
    try {
      if (!projectId) {
        console.error(" No projectId for edit");
        return;
      }

      // Fetch full structure data for selected project
      const res = await api.get(`${API_BASE_URL}/structures/full/${projectId}`);

	  // Create typeOptions for dropdown
	  const typeOptions = res.data.structureTypes.map(t => ({
	    value: t.type,
	    label: t.type
	  }));

	  navigate("addstructureform", {
	    state: { 
	      structureData: res.data,
	      typeOptions: typeOptions
	    },
	  });

    } catch (error) {
      console.error("Error loading structure for edit:", error);
    }
  };

  const isStructureForm = location.pathname.endsWith("/addstructureform");

  return (
    <div className={styles.container}>
      {/* Top Bar */}
      { !isStructureForm &&(
      <div className="pageHeading">
        <h2>Structure</h2>
        <div  className="rightBtns">
          <button className="btn btn-primary" onClick={handleAdd}>
            <CirclePlus size={16} /> Add
          </button>
        </div>
      </div>
      )}
      
      {/* Filters */}
      {!isStructureForm && (
        
        <div className="innerPage">
		<div className={styles.filterRow}>
		  {Object.keys(filters).map((key) => {

			const options = [
			  { value: "", label: "Select Project" },
			  ...projects.map((p) => ({
			    value: p.project_id,
			    label: `${p.project_id} - ${p.project_name}`
			  }))
			];

		    return (
		      <div className={styles.filterOptions} key={key}>
		        <Select
		          options={options}
				  value={filters[key] ? options.find(opt => opt.value === filters[key]) : null}
				  onChange={(selectedOption) => {
					console.log(" Selected Project:", selectedOption);
				    handleFilterChange({
				      target: { name: key, value: selectedOption.value },
				    });
				  }
		          }
		          placeholder="Select Project"
		          isSearchable
		          styles={{
		            control: (base) => ({
		              ...base,
		              minHeight: "32px",
		              borderColor: "#ced4da",
		              boxShadow: "none",
		              "&:hover": { borderColor: "#86b7fe" },
		            }),
		            menu: (base) => ({ ...base, zIndex: 9999 }),
		          }}
		        />
		      </div>
		    );
		  })}

		  <button className="btn btn-2 btn-primary" onClick={handleClearFilters}>Clear Filters</button>
		</div>
		  {/* Search + Entries Row */}
		         <div className={styles.tableTopRow}>
		  	<div className="showEntriesCount">
		  	              <label>Show </label>
		  	              <select
		  	                value={perPage}
		  	                onChange={(e) => { setPerPage(Number(e.target.value)); setPage(1); }}
		  	              >
		  	                {[5, 10, 20, 50, 100].map((size) => (
		  	                  <option key={size} value={size}>{size}</option>
		  	                ))}
		  	              </select>
		  	              <span> entries</span>
		  	            </div>
						<div className={styles.searchWrapper}>
						  <input
						    type="text"
						    placeholder="Search project"
						    value={search}
						    onChange={(e) => {
						      setPage(1);
						      setSearch(e.target.value);
						    }}
						    className={styles.searchInput}
						  />
						  <span className={styles.searchIcon}>🔍</span>
						</div>
		         </div>
          <div className={`dataTable ${styles.tableWrapper}`}>
            <table className={styles.projectTable}>
              <thead>
                <tr>
                  <th>Project </th>
                  <th>Structures</th>
                  <th>Action</th>
                </tr>
              </thead>
			  <tbody>
			    {searchedProjects.length === 0 ? (
			      // CASE 1: No matching projects (search or filter result empty)
			      <tr>
			        <td colSpan="3">
			          No details found
			        </td>
			      </tr>
			    ) : (
			      // CASE 2: Display all rows (filtered OR searched)
			      paginatedRows.map((item, index) => (
			        <tr key={index}>
			          <td>{item.projectId}</td>

			          <td>
			            {item.structureTypes?.length > 0 ? (
			              item.structureTypes.map((st, i) => (
			                <div key={i}>{st.structureType} - {st.count}</div>
			              ))
			            ) : (
			              <span style={{ color: "red", fontStyle: "italic" }}>
			                No Structures for this project
			              </span>
			            )}
			          </td>

			          <td>
			            <button
			              className="btn btn-sm btn-outline-primary"
			              onClick={() => handleEdit(item.projectId)}
			            >
			              <MdEditNote size={28} />
			            </button>
			          </td>
			        </tr>
			      ))
			    )}
			  </tbody>
            </table>
          </div>
		  {/* PAGINATION */}
		  <div className={styles.paginationBar}>
		    <span>
		      {totalRecords === 0
		        ? "0 - 0"
		        : `${(page - 1) * perPage + 1} - ${Math.min(page * perPage, totalRecords)}`}
		      {" "}of {totalRecords}
		    </span>

		    <div className={styles.paginationBtns}>
		      <button
		        onClick={() => page > 1 && setPage(page - 1)}
		        disabled={page === 1}
		      >
		        {"<"}
		      </button>

		      <button className={styles.activePage}>{page}</button>

		      <button
		        onClick={() => page < totalPages && setPage(page + 1)}
		        disabled={page === totalPages}
		      >
		        {">"}
		      </button>
		    </div>
		  </div>
        </div>
      )}
        <Outlet />
    </div>
  );
}
