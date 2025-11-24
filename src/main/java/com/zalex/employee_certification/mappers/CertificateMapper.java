package com.zalex.employee_certification.mappers;

import com.zalex.employee_certification.constants.CertificateConstants;
import com.zalex.employee_certification.dtos.CertificateDto;
import com.zalex.employee_certification.entities.Certificate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CertificateMapper {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

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
                issuedOnDate = CertificateConstants.DATE_FORMAT.parse(certificateDto.getIssuedOn());
            } catch (ParseException e) {
                throw new IllegalArgumentException("Invalid date format. Expected format: M/d/yyyy (e.g., 12/9/2022)", e);
            }
        }
        
        Integer employeeId = null;
        if (certificateDto.getEmployeeId() != null && !certificateDto.getEmployeeId().isEmpty()) {
            try {
                employeeId = Integer.parseInt(certificateDto.getEmployeeId());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid employeeId format. Expected integer.", e);
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
