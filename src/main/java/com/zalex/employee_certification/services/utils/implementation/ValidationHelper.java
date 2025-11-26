package com.zalex.employee_certification.services.utils.implementation;

import com.zalex.employee_certification.constants.CertificateConstants;
import com.zalex.employee_certification.dtos.CertificateDto;
import com.zalex.employee_certification.dtos.CertificateQueryDto;
import com.zalex.employee_certification.services.utils.interfaces.IValidationHelper;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class ValidationHelper implements IValidationHelper {
    @Override
    public Optional<String> getCreationValidationErrors(CertificateDto certificateDto) {
        List<String> errors = new ArrayList<>();

        Optional<String> addressToError = this.getAddressToError(certificateDto.getAddressTo(), true);
        Optional<String> purposeError = this.getPurposeError(certificateDto.getPurpose(), true);
        Optional<String> issuedOnError = this.getIssuedOnError(certificateDto.getIssuedOn(), true);
        Optional<String> employeeIdError = this.getEmployeeIdError(certificateDto.getEmployeeId(), true);

        addressToError.ifPresent(errors::add);
        purposeError.ifPresent(errors::add);
        issuedOnError.ifPresent(errors::add);
        employeeIdError.ifPresent(errors::add);

        if(errors.isEmpty()) {
            return Optional.empty();
        } else {
            return String.join(", ", errors).describeConstable();
        }
    }

    @Override
    public Optional<String> getFilterValidationErrors(CertificateQueryDto certificateQueryDto) {
        List<String> errors = new ArrayList<>();
        Optional<String> addressToError = this.getAddressToError(certificateQueryDto.getAddressTo(), false);
        Optional<String> employeeIdError = this.getEmployeeIdError(certificateQueryDto.getEmployeeId(), false);
        Optional<String> pageError = this.getPageError(certificateQueryDto.getPage(), false);

        addressToError.ifPresent(errors::add);
        employeeIdError.ifPresent(errors::add);
        pageError.ifPresent(errors::add);

        if(errors.isEmpty()) {
            return Optional.empty();
        } else {
            return String.join(", ", errors).describeConstable();
        }
    }

    @Override
    public Optional<String> getAddressToError(String addressTo, boolean isMandatory) {
        if(addressTo == null && isMandatory) {
            return "Address to is a required field".describeConstable();
        } else if(addressTo != null && !isAddressValid(addressTo)) {
            return "Address to is an alphanumeric field".describeConstable();
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> getPurposeError(String purpose, boolean isMandatory) {
        if(purpose == null && isMandatory) {
            return "Purpose is a required field".describeConstable();
        } else if(purpose != null && !isPurposeValid(purpose)) {
            return "Purpose length must be at least 50 characters".describeConstable();
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> getIssuedOnError(String issuedOn, boolean isMandatory) {
        if(issuedOn == null && isMandatory) {
            return "Issued on is a required field".describeConstable();
        } else if(issuedOn != null && !isIssuedOnValid(issuedOn)) {
            return "Issued on must be a valid date in the future. Expected format: MM/dd/yyyy (e.g., 12/09/2022)".describeConstable();
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> getEmployeeIdError(String employeeId, boolean isMandatory) {
        if(employeeId == null && isMandatory) {
            return "Employee id is a required field".describeConstable();
        } else if(employeeId != null && !isEmployeeIdValid(employeeId)) {
            return "Employee id is a numeric field".describeConstable();
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> getPageError(Integer page, boolean isMandatory) {
        if(page == null && isMandatory) {
            return "Page is a required field".describeConstable();
        } else if(page != null && page < 0) {
            return "Page cannot be negative".describeConstable();
        }
        return Optional.empty();
    }

    private boolean isAddressValid(String address) {
        return address.matches(CertificateConstants.ALPHANUMERIC_REGEX);
    }

    private boolean isPurposeValid(String purpose) {
        return purpose.length() > 49;
    }

    private boolean isIssuedOnValid(String issuedOn) {
        if (!issuedOn.matches(CertificateConstants.DATE_FORMAT_REGEX)) {
            return false;
        }
        try {
            CertificateConstants.DATE_FORMAT.setLenient(false);
            Date issuedOnDate = CertificateConstants.DATE_FORMAT.parse(issuedOn);
            Date currentDate = new Date();
            return issuedOnDate.after(currentDate);
        } catch (ParseException e) {
           return false;
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
