package com.cbo.CBO_NFOS_ICMS.repositories.UserAndEmployeeRepository;

import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.Branch;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.SubProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    Optional<Branch> findBranchById(Long id);


//    List<Branch> findBranchBySubProcess(SubProcess subProcess);

//    @Query("SELECT b FROM Branch b WHERE b.subProcess = (:subProcess)")
//    List<Branch> findAllBySubProcess(SubProcess subProcess);

//    Optional<Branch> findBranchByName(String name);
}
