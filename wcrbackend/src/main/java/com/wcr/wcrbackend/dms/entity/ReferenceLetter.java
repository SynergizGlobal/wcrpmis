package com.wcr.wcrbackend.dms.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reference_letter")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReferenceLetter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ref_id")
    private Long refId;

    @Column(name = "ref_letters", length = 100, nullable = false)
    private String refLetters;

   
    @OneToMany(mappedBy = "referenceLetter", cascade = CascadeType.ALL)
    private List<CorrespondenceReference> correspondenceReferences;


}
