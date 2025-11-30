package com.zalex.employee_certification.services.commands.interfaces;

import com.zalex.employee_certification.dtos.CertificateDto;
import com.zalex.employee_certification.services.dms.CreationContextDM;

public interface ICreationCommand extends  ICommand<CreationContextDM, CertificateDto> {
}
