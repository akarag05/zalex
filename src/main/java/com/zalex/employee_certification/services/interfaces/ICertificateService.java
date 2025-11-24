package com.zalex.employee_certification.services.interfaces;

import com.zalex.employee_certification.dtos.CertificateDto;

public interface ICertificateService {
    CertificateDto createCertificate(CertificateDto certificateDto);
}
