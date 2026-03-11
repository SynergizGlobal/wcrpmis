package com.wcr.wcrbackend.dms.entity; 

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "folders")
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    
    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<SubFolder> subFolders = new ArrayList<>();

    @Column(name = "parent_id")
    private Long parentId;

    // Constructors
    public Folder() {}

    public Folder(String name, Long parentId) {
        this.name = name;
        this.parentId = parentId;
    }

    // Getters & Setters
    public Long getId() { 
    	return id; 
    }
    public void setId(Long id) { 
    	this.id = id; 
    }

    public String getName() { 
    	return name; 
    }
    public void setName(String name) { 
    	this.name = name;
    }

    public List<SubFolder> getSubFolders() { 
    	return subFolders; 
    }
    public void setSubFolders(List<SubFolder> subFolders) { 
    	this.subFolders = subFolders; 
    }
    public Long getParentId() { 
    	return parentId; 
    }
    public void setParentId(Long parentId) { 
    	this.parentId = parentId; 
    }
    
 }

