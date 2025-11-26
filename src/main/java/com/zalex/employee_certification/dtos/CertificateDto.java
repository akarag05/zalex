package com.zalex.employee_certification.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateDto {
    private String addressTo;
    private String purpose;
    private String issuedOn;
    private String employeeId;
}
