package com.cbo.CBO_NFOS_ICMS.repositories.CIPMRepository;

import com.cbo.CBO_NFOS_ICMS.models.CIPM.Status;
<<<<<<< HEAD
=======
import com.cbo.CBO_NFOS_ICMS.models.IFR.CaseStatus;
>>>>>>> a0b69334fa61468010b3649472556044a1ddafbf
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status,Long> {
    Optional<Status> findStatusById(Long id);


}
