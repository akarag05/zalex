package com.zalex.employee_certification.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = false)
public class CertificateQueryDto {
    private String employeeId;
    private String addressTo;
    private Integer page;
    private Boolean isAscending;
}
