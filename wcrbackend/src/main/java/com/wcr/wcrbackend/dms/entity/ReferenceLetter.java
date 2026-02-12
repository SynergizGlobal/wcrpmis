package com.wcr.wcrbackend.dms.entity;

import jakarta.persistence.*;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

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
