package com.wcr.wcrbackend.dms.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@IdClass(CorrespondenceReferenceId.class)
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name="correspondence_reference")
public class CorrespondenceReference {
    @Id
    @ManyToOne
    @JoinColumn(name = "correspondenceLetter_id")
    private CorrespondenceLetter correspondenceLetter;
    @Id
    @ManyToOne
    @JoinColumn(name = "referenceLetter_id")
    private ReferenceLetter referenceLetter;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
