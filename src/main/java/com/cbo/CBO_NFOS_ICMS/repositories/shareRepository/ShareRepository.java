package com.cbo.CBO_NFOS_ICMS.repositories.shareRepository;

import com.cbo.CBO_NFOS_ICMS.models.DACGM.DailyActivityGapControl;
import com.cbo.CBO_NFOS_ICMS.models.share.Share;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShareRepository extends JpaRepository<Share, Long> {
    void deleteShareById(Long id);

    Optional<Share> findShareById(Long id);

    List<Share> findShareByBranchId(Long id);
    List<Share> findShareBySubProcessId(Long subProcessId);

    boolean existsByCaseId(String caseId);
}