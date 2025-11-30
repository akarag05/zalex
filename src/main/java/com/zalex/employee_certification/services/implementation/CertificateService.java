package com.zalex.employee_certification.services.implementation;

import com.zalex.employee_certification.dtos.CertificateDto;
import com.zalex.employee_certification.dtos.CertificateQueryDto;
import com.zalex.employee_certification.entities.Certificate;
import com.zalex.employee_certification.mappers.CertificateMapper;
import com.zalex.employee_certification.repositories.ICertificateRepository;
import com.zalex.employee_certification.services.commands.implementation.CreationCommand;
import com.zalex.employee_certification.services.dms.CreationContextDM;
import com.zalex.employee_certification.services.utils.interfaces.ICertificateQueryHelper;
import com.zalex.employee_certification.services.interfaces.ICertificateService;
import com.zalex.employee_certification.services.utils.interfaces.IValidationHelper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE;

@Service
@AllArgsConstructor
public class CertificateService implements ICertificateService {

    private ICertificateRepository certificateRepository;
    private ICertificateQueryHelper certificateQueryHelper;
    private IValidationHelper validationHelper;
    private CreationCommand creationCommand;

    @Override
    public CertificateDto createCertificate(CertificateDto certificateDto) {
        CreationContextDM context = new CreationContextDM();
        context.setRequestDto(certificateDto);
        return creationCommand.execute(context);
    }

    @Override
    public CertificateDto getCertificateById(Long id, String employeeIdString) {
        Optional<String> userIdValidationError = validationHelper.getEmployeeIdError(employeeIdString, true);
        if(userIdValidationError.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, userIdValidationError.get());
        }

        Optional<Certificate> dbResult = certificateRepository.findById(id);
        Integer employeeId = null;
        try {
            employeeId = Integer.parseInt(employeeIdString);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Something went wrong when parsing employeeId: " + employeeIdString);
        }

        if(dbResult.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Document does not exist.");
        }
        if(dbResult.get().getEmployeeId().equals(employeeId)) {
            return CertificateMapper.mapToCertificateDto(dbResult.get());
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Document does not exist for user.");
    }

    @Override
    public List<CertificateDto> getAllCertificates(CertificateQueryDto certificateQueryDto) {

        Optional<String> validationResult = validationHelper.getFilterValidationErrors(certificateQueryDto);

        if (validationResult.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, validationResult.get());
        }

        Specification<Certificate> spec = certificateQueryHelper.constructQuerySpecification(certificateQueryDto);
        Pageable pageable = certificateQueryHelper.constructQueryPagination(certificateQueryDto, DEFAULT_PAGE_SIZE);

        Page<Certificate> certificatePage = certificateRepository.findAll(spec, pageable);

        return certificatePage.getContent().stream()
            .map(CertificateMapper::mapToCertificateDto)
            .toList();
    }

    @Override
    public CertificateDto updateCertificate(Long id, String purpose) {
        Optional<String> validationResult = validationHelper.getPurposeError(purpose, true);
        if (validationResult.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, validationResult.get());
        }

        Optional<Certificate> findByIdResult = certificateRepository.findById(id);
        if(findByIdResult.isPresent()) {
            Certificate patchCertificate = findByIdResult.get();
            patchCertificate.setPurpose(purpose);
            Certificate patchResult = certificateRepository.save(patchCertificate);
            return CertificateMapper.mapToCertificateDto(patchResult);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Document does not exist.");
        }
    }
}
