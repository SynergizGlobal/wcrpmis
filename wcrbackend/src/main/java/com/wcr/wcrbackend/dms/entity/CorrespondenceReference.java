package com.wcr.wcrbackend.dms.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

//@IdClass(CorrespondenceReferenceId.class)
//@Entity
//@Data
//@EqualsAndHashCode(callSuper = false)
//@Table(name="correspondence_reference")
//public class CorrespondenceReference {
//    @Id
//    @ManyToOne
//    @JoinColumn(name = "correspondenceLetter_id")
//    private CorrespondenceLetter correspondenceLetter;
//    @Id
//    @ManyToOne
//    @JoinColumn(name = "referenceLetter_id")
//    private ReferenceLetter referenceLetter;
//    @CreationTimestamp
//    private LocalDateTime createdAt;
//}
@Entity
@Table(name = "correspondence_reference")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CorrespondenceReference {

    @EmbeddedId
    private CorrespondenceReferenceId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("correspondenceId")   
    @JoinColumn(name = "correspondence_letter_id", nullable = false)
    private CorrespondenceLetter correspondenceLetter;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("refId")
    @JoinColumn(name = "reference_letter_id", nullable = false)
    private ReferenceLetter referenceLetter;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}