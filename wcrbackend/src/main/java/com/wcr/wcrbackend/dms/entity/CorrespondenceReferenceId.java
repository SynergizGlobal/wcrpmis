package com.wcr.wcrbackend.dms.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Embeddable
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class CorrespondenceReferenceId implements Serializable {
//	
//    @Column(name = "correspondence_id")
//    private Long correspondenceLetter;
//    @Column(name = "referenceLetter_id")
//    private Long referenceLetter;
//    
//}
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CorrespondenceReferenceId implements Serializable {

    @Column(name = "correspondence_id")
    private Long correspondenceId;

    @Column(name = "reference_letter_id")
    private Long referenceLetterId;
}