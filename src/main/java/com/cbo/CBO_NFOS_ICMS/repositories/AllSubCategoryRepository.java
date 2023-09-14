package com.cbo.CBO_NFOS_ICMS.repositories;

import com.cbo.CBO_NFOS_ICMS.models.AllSubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AllSubCategoryRepository extends JpaRepository<AllSubCategory, Long> {
    Optional<AllSubCategory> findAllSubCategoryById(Long id);
}
