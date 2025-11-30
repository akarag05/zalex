package com.zalex.employee_certification.services.handlers.implementation;

import com.zalex.employee_certification.entities.Certificate;
import com.zalex.employee_certification.mappers.CertificateMapper;
import com.zalex.employee_certification.services.dms.CreationContextDM;
import com.zalex.employee_certification.services.handlers.interfaces.IHandler;
import com.zalex.employee_certification.services.handlers.interfaces.IValidateCreationHandler;
import com.zalex.employee_certification.services.utils.interfaces.IValidationHelper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class ValidateCreationHandler implements IValidateCreationHandler<CreationContextDM> {

    private final IValidationHelper validationHelper;

    private IHandler<CreationContextDM> next;

    public ValidateCreationHandler(IValidationHelper validationHelper) {
        this.validationHelper = validationHelper;
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
        Optional<String> validationResult = validationHelper.getCreationValidationErrors(context.getRequestDto());

        if (validationResult.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, validationResult.get());
        }

        Certificate certificate = CertificateMapper.mapToCertificate(context.getRequestDto());
        context.setCertificate(certificate);

        if(hasNext()) {
            this.next.handle(context);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Handler " + this.getClass().getName() + " missing next handler.");
        }
    }
}
