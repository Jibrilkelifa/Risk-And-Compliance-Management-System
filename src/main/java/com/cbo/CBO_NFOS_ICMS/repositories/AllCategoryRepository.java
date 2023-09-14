package com.cbo.CBO_NFOS_ICMS.repositories;

import com.cbo.CBO_NFOS_ICMS.models.AllCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AllCategoryRepository extends JpaRepository<AllCategory, Long> {
    Optional<AllCategory> findAllCategoryById(Long id);
}
