package com.zalex.employee_certification.services.handlers.implementation;

import com.zalex.employee_certification.entities.Certificate;
import com.zalex.employee_certification.mappers.CertificateMapper;
import com.zalex.employee_certification.repositories.ICertificateRepository;
import com.zalex.employee_certification.services.dms.CreationContextDM;
import com.zalex.employee_certification.services.handlers.interfaces.ICreationHandler;
import com.zalex.employee_certification.services.handlers.interfaces.IHandler;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class CreationHandler implements ICreationHandler<CreationContextDM> {

    private final ICertificateRepository certificateRepository;

    private IHandler<CreationContextDM> next;

    public CreationHandler(ICertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    @Override
    public void setNext(IHandler<CreationContextDM> next) {
        this.next = next;
    }

    @Override
    public boolean hasNext() {
        return this.next != null;
    }

    @Override
    public void handle(CreationContextDM context) {
        try {
            Certificate savedCertificate = certificateRepository.save(context.getCertificate());
            context.setResponseDto(CertificateMapper.mapToCertificateDto(savedCertificate));
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
