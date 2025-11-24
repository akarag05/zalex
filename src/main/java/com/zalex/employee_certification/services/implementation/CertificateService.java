package com.zalex.employee_certification.services.implementation;

import com.zalex.employee_certification.constants.CertificateConstants;
import com.zalex.employee_certification.dtos.CertificateDto;
import com.zalex.employee_certification.entities.Certificate;
import com.zalex.employee_certification.mappers.CertificateMapper;
import com.zalex.employee_certification.repositories.CertificateRepository;
import com.zalex.employee_certification.services.interfaces.ICertificateService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class CertificateService implements ICertificateService {

    private CertificateRepository certificateRepository;

    @Override
    public CertificateDto createCertificate(CertificateDto certificateDto) {
        String validationResult = validateInput(certificateDto);
        
        if (!validationResult.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, validationResult);
        }

        Certificate certificate = CertificateMapper.mapToCertificate(certificateDto);

        Certificate savedCertificate = certificateRepository.save(certificate);

        return CertificateMapper.mapToCertificateDto(savedCertificate);
    }

    private String validateInput(CertificateDto certificateDto) {

        List<String> errors = new ArrayList<>();
        if(certificateDto.getAddressTo() == null) {
            errors.add("Address to is a required field");
        } else if(isAddressValid(certificateDto.getAddressTo())) {
            errors.add("Address to is an alphanumeric field");
        }

        if(certificateDto.getPurpose() == null) {
            errors.add("Purpose is a required field");
        } else if(!isPurposeValid(certificateDto.getPurpose())) {
            errors.add("Purpose length must be at least 50 characters");
        }

        if(certificateDto.getIssuedOn() == null) {
            errors.add("Issued on is a required field");
        } else if(!isIssuedOnValid(certificateDto.getIssuedOn())) {
            errors.add("Issued on must be a date in the future");
        }


        if(certificateDto.getEmployeeId() == null) {
            errors.add("Employee id is a required field");
        } else if(!isEmployeeIdValid(certificateDto.getEmployeeId())) {
            errors.add("Employee id is a numeric field");
        }

        return String.join(", ", errors);
    }

    private boolean isAddressValid(String address) {
        return !address.chars().allMatch(Character::isLetterOrDigit);
    }

    private boolean isPurposeValid(String purpose) {
        return purpose.length() > 49;
    }

    private boolean isIssuedOnValid(String issuedOn) {
        try {
            Date issuedOnDate = CertificateConstants.DATE_FORMAT.parse(issuedOn);
            Date currentDate = new Date();
            return issuedOnDate.after(currentDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isEmployeeIdValid(String employeeId) {
        try {
            Integer.parseInt(employeeId);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
