package com.zalex.employee_certification.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CertificateDto {
    private String addressTo;
    private String purpose;
    private String issuedOn;
    private String employeeId;
}
