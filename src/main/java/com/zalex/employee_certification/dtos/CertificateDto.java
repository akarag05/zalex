package com.zalex.employee_certification.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = false)
public class CertificateDto {
    private String addressTo;
    private String purpose;
    private String issuedOn;
    private String employeeId;
}
