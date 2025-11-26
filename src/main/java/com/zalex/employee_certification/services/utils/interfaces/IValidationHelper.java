package com.zalex.employee_certification.services.utils.interfaces;

import com.zalex.employee_certification.dtos.CertificateDto;
import com.zalex.employee_certification.dtos.CertificateQueryDto;

import java.util.Optional;

public interface IValidationHelper {
    Optional<String> getCreationValidationErrors(CertificateDto certificateDto);
    Optional<String> getFilterValidationErrors(CertificateQueryDto certificateQueryDto);
    Optional<String> getAddressToError(String addressTo, boolean isMandatory);
    Optional<String> getPurposeError(String purpose, boolean isMandatory);
    Optional<String> getIssuedOnError(String issuedOn, boolean isMandatory);
    Optional<String> getEmployeeIdError(String employeeId, boolean isMandatory);
}
