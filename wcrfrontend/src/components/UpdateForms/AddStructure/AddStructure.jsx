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
  const [structures, setStructures] = useState([]);
  const [search, setSearch] = useState("");
   const [page, setPage] = useState(1);
   const [perPage, setPerPage] = useState(10);
   const [projects, setProjects] = useState([]);
   const [structureSummary, setStructureSummary] = useState([]);
   const [displayRows, setDisplayRows] = useState([]);
   const [allProjectSummaries, setAllProjectSummaries] = useState([]);

  const [filters, setFilters] = useState({
    project: "",
  });

  useEffect(() => {
    fetchStructures();
  }, [refresh, location]);

  const fetchStructures = async () => {
    try {
      const res = await api.get(`${API_BASE_URL}/Structures`, { withCredentials: true });
      setStructures(res.data || []);
    } catch (err) {
      console.error("Error fetching Structure:", err);
    }
  };
  
  useEffect(() => {
    api.get(`${API_BASE_URL}/projects/api/getProjects`)
      .then(res => {
		console.log("Projects API Response:", res.data);
        setProjects(res.data);
      })
      .catch(err => console.error(err));
  }, [refresh, location]);


  useEffect(() => {
	fetchStructureSummary();
  }, [refresh, location])
  const fetchStructureSummary = async (projectId) => {
    if (!projectId) {
      setStructureSummary([]);
      return;
    }

    try {
      const res = await api.get(`${API_BASE_URL}/structures/summary/${projectId}`);
	  console.log(" Structure summary API response:", res.data);
      setStructureSummary(res.data);
    } catch (err) {
      console.error("Error fetching structure summary:", err);
    }
  };



  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setFilters({ ...filters, [name]: value });
  };
  
  const filteredStructure = structures.filter((s) => {
      const text = search.toLowerCase();

      return (
        s.project?.toLowerCase().includes(text) ||
        s.Structures?.toLowerCase().includes(text) 
      );
    });
  
  // Pagination logic
    const totalRecords = filteredStructure.length;
    const totalPages = Math.ceil(totalRecords / perPage);

    // Slice data according to page & perPage
    const paginatedStructures = filteredStructure.slice(
      (page - 1) * perPage,
      page * perPage
    );

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
		          value={options.find((opt) => opt.value === filters[key])}
				  onChange={(selectedOption) => {
					console.log(" Selected Project:", selectedOption);
				    handleFilterChange({
				      target: { name: key, value: selectedOption.value },
				    });

				    // Load summary for selected project
				    fetchStructureSummary(selectedOption.value);
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

		  <button className="btn btn-2 btn-primary">Clear Filters</button>
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
		               placeholder="Search"
		               value={search}
		               onChange={(e) => setSearch(e.target.value)}
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
			  <tr> 
			  <td> {filters.project ? filters.project : "Please select a project"} 
			  </td> 
			  <td> 
			  {structureSummary.length > 0 ? ( 
				structureSummary.map((item, i) => ( 
				<div key={i}> 
				{item.structureType} - {item.count} 
				</div> 
			     )) 
			      ) : ( 
				<span>No structure types</span> )} 
			  </td> 
			  <td>
			   {filters.project && ( 
				<button className="btn btn-sm btn-outline-primary" 
				onClick={() => handleEdit(filters.project)} > 
				<MdEditNote size={28} /> </button> )} 
				</td> 
				</tr> 
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
