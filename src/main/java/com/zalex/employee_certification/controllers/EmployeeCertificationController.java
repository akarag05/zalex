package com.zalex.employee_certification.controllers;

import com.zalex.employee_certification.dtos.CertificateDto;
import com.zalex.employee_certification.services.implementation.CertificateService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/certificates")
@AllArgsConstructor
public class EmployeeCertificationController {

    private CertificateService certificateService;

    @PostMapping
    public ResponseEntity<CertificateDto> createCertificate(@RequestBody CertificateDto certificateDto) {
        CertificateDto savedCertificate = certificateService.createCertificate(certificateDto);

        return new ResponseEntity<>(savedCertificate, HttpStatus.CREATED);
    }
}
