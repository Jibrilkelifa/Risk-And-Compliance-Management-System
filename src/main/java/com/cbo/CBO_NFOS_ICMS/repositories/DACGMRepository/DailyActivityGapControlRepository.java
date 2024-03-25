package com.cbo.CBO_NFOS_ICMS.repositories.DACGMRepository;

import com.cbo.CBO_NFOS_ICMS.models.DACGM.DailyActivityGapControl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DailyActivityGapControlRepository extends JpaRepository<DailyActivityGapControl, Long> {
    void deleteDACGMById(Long id);

    Optional<DailyActivityGapControl> findDACGMById(Long id);

    List<DailyActivityGapControl> findDACGMByBranchId(String id);
    List<DailyActivityGapControl> findDACGMBySubProcessId(Long subProcessId);

    boolean existsByCaseId(String caseId);
}