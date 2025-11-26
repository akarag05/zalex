package com.zalex.employee_certification.mappers;

import com.zalex.employee_certification.constants.CertificateConstants;
import com.zalex.employee_certification.dtos.CertificateDto;
import com.zalex.employee_certification.entities.Certificate;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.util.Date;

public class CertificateMapper {
    public static CertificateDto mapToCertificateDto(Certificate certificate) {
        String issuedOnString = certificate.getIssuedOn() != null 
                ? CertificateConstants.DATE_FORMAT.format(certificate.getIssuedOn())
                : null;
        
        return new CertificateDto(
                certificate.getAddressTo(),
                certificate.getPurpose(),
                issuedOnString,
                certificate.getEmployeeId() != null ? certificate.getEmployeeId().toString() : null
        );
    }

    public static Certificate mapToCertificate(CertificateDto certificateDto) {
        Date issuedOnDate = null;
        if (certificateDto.getIssuedOn() != null && !certificateDto.getIssuedOn().isEmpty()) {
            try {
                CertificateConstants.DATE_FORMAT.setLenient(false);
                issuedOnDate = CertificateConstants.DATE_FORMAT.parse(certificateDto.getIssuedOn());
            } catch (ParseException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date format. Expected format: MM/dd/yyyy (e.g., 12/09/2022)");
            }
        }
        
        Integer employeeId = null;
        if (certificateDto.getEmployeeId() != null && !certificateDto.getEmployeeId().isEmpty()) {
            try {
                employeeId = Integer.parseInt(certificateDto.getEmployeeId());
            } catch (NumberFormatException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid employeeId format. Expected integer.");
            }
        }
        
        return new Certificate(
                null,
                certificateDto.getAddressTo(),
                certificateDto.getPurpose(),
                issuedOnDate,
                employeeId
        );
    }
}
