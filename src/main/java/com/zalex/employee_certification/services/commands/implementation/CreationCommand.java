package com.zalex.employee_certification.services.commands.implementation;

import com.zalex.employee_certification.dtos.CertificateDto;
import com.zalex.employee_certification.services.commands.interfaces.ICreationCommand;
import com.zalex.employee_certification.services.dms.CreationContextDM;
import com.zalex.employee_certification.services.handlers.interfaces.ICreationHandler;
import com.zalex.employee_certification.services.handlers.interfaces.IValidateCreationHandler;
import org.springframework.stereotype.Component;

@Component
public class CreationCommand implements ICreationCommand {

    private final IValidateCreationHandler<CreationContextDM> validateCreationHandler;
    private final ICreationHandler<CreationContextDM> creationHandler;

    public CreationCommand(IValidateCreationHandler<CreationContextDM> validateCreationHandler,
                          ICreationHandler<CreationContextDM> creationHandler) {
        this.validateCreationHandler = validateCreationHandler;
        this.creationHandler = creationHandler;
    }

    @Override
    public CertificateDto execute(CreationContextDM context) {
        validateCreationHandler.setNext(creationHandler);
        validateCreationHandler.handle(context);
        return context.getResponseDto();
    }
}
