package com.zalex.employee_certification.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "zalex-certificate")
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address_to", nullable = false)
    private String addressTo;
    @Column(name = "purpose", nullable = false)
    private String purpose;
    @Column(name = "issued_on", nullable = false)
    private Date issuedOn;
    @Column(name = "employee_id", nullable = false)
    private Integer employeeId;
}
