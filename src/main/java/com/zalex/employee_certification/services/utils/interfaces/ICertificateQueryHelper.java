package com.zalex.employee_certification.services.utils.interfaces;

import com.zalex.employee_certification.dtos.CertificateQueryDto;
import com.zalex.employee_certification.entities.Certificate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface ICertificateQueryHelper {
    Specification<Certificate>  constructQuerySpecification(CertificateQueryDto certificateQueryDto);
    Pageable constructQueryPagination(CertificateQueryDto certificateQueryDto, int pageSize);
}
