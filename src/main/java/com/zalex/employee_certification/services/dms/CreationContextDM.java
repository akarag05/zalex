package com.zalex.employee_certification.services.dms;

import com.zalex.employee_certification.dtos.CertificateDto;
import com.zalex.employee_certification.entities.Certificate;
import lombok.Data;

@Data
public class CreationContextDM {
    private CertificateDto requestDto;
    private Certificate certificate;
    private CertificateDto responseDto;
}
