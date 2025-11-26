package com.zalex.employee_certification.services.utils.implementation;

import com.zalex.employee_certification.dtos.CertificateQueryDto;
import com.zalex.employee_certification.entities.Certificate;
import com.zalex.employee_certification.services.utils.interfaces.ICertificateQueryHelper;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CertificateQueryHelper implements ICertificateQueryHelper {
    @Override
    public Specification<Certificate> constructQuerySpecification(CertificateQueryDto certificateQueryDto) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (certificateQueryDto.getEmployeeId() != null && !certificateQueryDto.getEmployeeId().isEmpty()) {
                try {
                    Integer employeeId = Integer.parseInt(certificateQueryDto.getEmployeeId());
                    predicates.add(criteriaBuilder.equal(root.get("employeeId"), employeeId));
                } catch (NumberFormatException e) {
                    predicates.add(criteriaBuilder.disjunction());
                }
            }

            if (certificateQueryDto.getAddressTo() != null && !certificateQueryDto.getAddressTo().isEmpty()) {
                String searchTerm = "%" + certificateQueryDto.getAddressTo().toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("addressTo")),
                        searchTerm
                ));
            }

            if(predicates.isEmpty()) {
                return null;
            } else {
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }
        };
    }

    private Sort constructQuerySorting(CertificateQueryDto certificateQueryDto) {
        if(certificateQueryDto.getIsAscending() != null) {
            return Sort.by(certificateQueryDto.getIsAscending()
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC, "issuedOn");
        }
        return Sort.by(Sort.Direction.DESC, "issuedOn");
    }

    @Override
    public Pageable constructQueryPagination(CertificateQueryDto certificateQueryDto, int pageSize) {
        Sort sort = this.constructQuerySorting(certificateQueryDto);

        int pageNumber = certificateQueryDto.getPage() != null && certificateQueryDto.getPage() >= 0
                ? certificateQueryDto.getPage()
                : 0;

        return PageRequest.of(pageNumber, pageSize, sort);
    }
}
