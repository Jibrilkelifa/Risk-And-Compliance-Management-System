package com.cbo.CBO_NFOS_ICMS.repositories.IFBRepository;



import com.cbo.CBO_NFOS_ICMS.models.IFB.IfbStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IfbStatusRepository extends JpaRepository<IfbStatus, Long> {

    Optional<IfbStatus> findStatusById(Long id);
}
