package com.zalex.employee_certification.services.interfaces;

import com.zalex.employee_certification.dtos.CertificateDto;
import com.zalex.employee_certification.dtos.CertificateQueryDto;

import java.util.List;

public interface ICertificateService {
    CertificateDto createCertificate(CertificateDto certificateDto);

    CertificateDto getCertificateById(Long id, String employeeIdString);

    List<CertificateDto> getAllCertificates(CertificateQueryDto certificateQueryDto);

    CertificateDto updateCertificate(Long id, String purpose);
}
