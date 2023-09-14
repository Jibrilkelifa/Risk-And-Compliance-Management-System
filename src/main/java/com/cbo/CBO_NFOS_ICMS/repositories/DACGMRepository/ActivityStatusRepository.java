package com.cbo.CBO_NFOS_ICMS.repositories.DACGMRepository;

import com.cbo.CBO_NFOS_ICMS.models.DACGM.ActivityStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivityStatusRepository extends JpaRepository<ActivityStatus, Long> {
    Optional<ActivityStatus> findActivityStatusById(Long id);
}
