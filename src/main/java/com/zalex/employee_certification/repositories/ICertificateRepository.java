package com.zalex.employee_certification.repositories;

import com.zalex.employee_certification.entities.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ICertificateRepository extends JpaRepository<Certificate, Long>, JpaSpecificationExecutor<Certificate> {
}
