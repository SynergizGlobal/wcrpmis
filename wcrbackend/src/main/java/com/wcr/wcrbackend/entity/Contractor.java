package com.wcr.wcrbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "contractor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contractor {

    @Id
    @Column(name = "contractor_id")
    private String contractorId;

    @Column(name = "contractor_name")
    private String contractorName;

    @Column(name = "contractor_specilization_fk")
    private String specilaization;

    @Column(name = "address")
    private String address;

    @Column(name = "primary_contact_name")
    private String primaryContact;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email_id")
    private String email;

    @Column(name = "pan_number")
    private String panNumber;

    @Column(name = "gst_number")
    private String gstNumber;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "account_number")
    private String accountNo;

    @Column(name = "ifsc_code")
    private String ifscCode;

    @Column(name = "bank_address")
    private String bankAddress;

    @Column(name = "remarks")
    private String remarks;
    
    @Column(name = "contractor_short_code")
    private String contractorShortCode;
}