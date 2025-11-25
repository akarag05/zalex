package com.zalex.employee_certification.controllers;

import com.zalex.employee_certification.dtos.CertificateDto;
import com.zalex.employee_certification.dtos.CertificateQueryDto;
import com.zalex.employee_certification.dtos.UpdatePurposeDto;
import com.zalex.employee_certification.services.implementation.CertificateService;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

    @GetMapping("{id}")
    public ResponseEntity<CertificateDto> getCertificateById(@PathVariable("id") Long id,
                                                             @RequestHeader("userId") String userIdString) {
        try {
            Integer userId = Integer.parseInt(userIdString);
            CertificateDto foundCertificate = certificateService.getCertificateById(id, userId);
            return new ResponseEntity<>(foundCertificate, HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid user");
        }
    }

    @PatchMapping("{id}")
    public ResponseEntity<CertificateDto> updatePurpose(@PathVariable("id") Long id,
                                                        @RequestHeader("userId") String userIdString,
                                                        @RequestBody UpdatePurposeDto updatePurposeDto) {

        try {
            Integer userId = Integer.parseInt(userIdString);
            CertificateDto foundCertificate = certificateService.updateCertificate(id, userId, updatePurposeDto.getPurpose());
            return new ResponseEntity<>(foundCertificate, HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid user");
        }
    }

    @PostMapping("/all")
    public ResponseEntity<List<CertificateDto>> getCertificates(@RequestBody(required = false) CertificateQueryDto certificateQueryDto) {
        if (certificateQueryDto == null) {
            certificateQueryDto = new CertificateQueryDto();
        }
        List<CertificateDto> filterResult = certificateService.getAllCertificates(certificateQueryDto);
        return new ResponseEntity<>(filterResult, HttpStatus.OK);
    }
}
