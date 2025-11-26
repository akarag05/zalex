package com.zalex.employee_certification.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateQueryDto {
    private String employeeId;
    private String addressTo;
    private Integer page;
    private Boolean isAscending;
}
