package org.example.fourtreesproject.company.repository;

import org.example.fourtreesproject.company.model.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    @Query("SELECT c FROM Company c JOIN FETCH c.user u WHERE u.idx = :userIdx")
    Optional<Company> findByUserIdx(Long userIdx);
}
