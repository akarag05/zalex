package com.zalex.employee_certification.services.implementation;

import com.zalex.employee_certification.constants.CertificateConstants;
import com.zalex.employee_certification.dtos.CertificateDto;
import com.zalex.employee_certification.dtos.CertificateQueryDto;
import com.zalex.employee_certification.entities.Certificate;
import com.zalex.employee_certification.mappers.CertificateMapper;
import com.zalex.employee_certification.repositories.CertificateRepository;
import com.zalex.employee_certification.services.interfaces.ICertificateService;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    @Override
    public CertificateDto getCertificateById(Long id, Integer userId) {
        Optional<Certificate> dbResult = certificateRepository.findById(id);

        if(dbResult.isPresent() && dbResult.get().getEmployeeId().equals(userId)) {
            return CertificateMapper.mapToCertificateDto(dbResult.get());
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Document does not exist for user.");
    }

    @Override
    public List<CertificateDto> getAllCertificates(CertificateQueryDto certificateQueryDto) {

        Specification<Certificate> spec = buildSpecification(certificateQueryDto);

        Sort sort = buildSorting(certificateQueryDto);

        int pageNumber = certificateQueryDto.getPage() != null && certificateQueryDto.getPage() >= 0 
            ? certificateQueryDto.getPage() 
            : 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Certificate> certificatePage = certificateRepository.findAll(spec, pageable);

        return certificatePage.getContent().stream()
            .map(CertificateMapper::mapToCertificateDto)
            .toList();
    }

    @Override
    public CertificateDto updateCertificate(Long id, Integer userId, String purpose) {
        String error = "";
        if(purpose == null) {
            error = "Purpose is a required field";
        } else if(!isPurposeValid(purpose)) {
            error = "Purpose length must be at least 50 characters";
        }
        if(!error.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, error);
        }

        CertificateDto findByIdResult = getCertificateById(id, userId);

        Certificate patchCertificate = CertificateMapper.mapToCertificate(findByIdResult);
        patchCertificate.setPurpose(purpose);
        Certificate patchResult = certificateRepository.save(patchCertificate);
        return CertificateMapper.mapToCertificateDto(patchResult);
    }

    private String validateInput(CertificateDto certificateDto) {

        List<String> errors = new ArrayList<>();
        if(certificateDto.getAddressTo() == null) {
            errors.add("Address to is a required field");
        } else if(!isAddressValid(certificateDto.getAddressTo())) {
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
        return address.matches(CertificateConstants.ALPHANUMERIC_REGEX);
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

    private Specification<Certificate> buildSpecification(CertificateQueryDto queryDto) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filter by employeeId (exact match)
            if (queryDto.getEmployeeId() != null && !queryDto.getEmployeeId().isEmpty()) {
                try {
                    Integer employeeId = Integer.parseInt(queryDto.getEmployeeId());
                    predicates.add(criteriaBuilder.equal(root.get("employeeId"), employeeId));
                } catch (NumberFormatException e) {
                    // If employeeId is not a valid integer, return no results
                    predicates.add(criteriaBuilder.disjunction());
                }
            }

            // Filter by addressTo (contains words)
            if (queryDto.getAddressTo() != null && !queryDto.getAddressTo().isEmpty()) {
                String searchTerm = "%" + queryDto.getAddressTo().toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("addressTo")),
                        searchTerm
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static Sort buildSorting(CertificateQueryDto certificateQueryDto) {
        if(certificateQueryDto.getIsAscending() != null) {
            return Sort.by(certificateQueryDto.getIsAscending()
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC, "issuedOn");
        }
        return Sort.by(Sort.Direction.DESC, "issuedOn");
    }
}
