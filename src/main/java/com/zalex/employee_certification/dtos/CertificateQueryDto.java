package com.zalex.employee_certification.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CertificateQueryDto {
    private String employeeId;
    private String addressTo;
    private Integer page;
    private Boolean isAscending;
}
