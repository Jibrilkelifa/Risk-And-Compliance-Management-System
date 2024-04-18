package com.cbo.CBO_NFOS_ICMS.repositories.DACGMRepository;

import com.cbo.CBO_NFOS_ICMS.models.DACGM.DailyActivityGapControl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyActivityGapControlRepository extends JpaRepository<DailyActivityGapControl, Long> {
    void deleteDACGMById(Long id);
//    @Query("SELECT a.irregularity.allSubCategory.allcategory.name, COUNT(a) FROM DailyActivityGapControl a GROUP BY a.irregularity.allSubCategory.allcategory.name")
//    List<Object[]> countByCategory();
@Query("SELECT a.irregularity.allSubCategory.allcategory.name, COUNT(a) FROM DailyActivityGapControl a WHERE a.branch.id = :branchId GROUP BY a.irregularity.allSubCategory.allcategory.name")
List<Object[]> countByCategoryAndBranch(@Param("branchId") String branchId);

    @Query("SELECT a.irregularity.allSubCategory.allcategory.name, COUNT(a) FROM DailyActivityGapControl a WHERE a.subProcess.id = :subProcessId GROUP BY a.irregularity.allSubCategory.allcategory.name")
    List<Object[]> countByCategoryAndSubProcess(@Param("subProcessId") Long subProcessId);





    Optional<DailyActivityGapControl> findDACGMById(Long id);

    List<DailyActivityGapControl> findDACGMByBranchId(String id);
    List<DailyActivityGapControl> findDACGMBySubProcessId(Long subProcessId);

    boolean existsByCaseId(String caseId);

    @Query(value = "SELECT MAX(id) FROM DailyActivityGapControl")
    Long findLastAddedDACGMId();


    List<DailyActivityGapControl> findByBranchIdAndActivityStatusAndDateBetween(String branchId, String closed, LocalDate startDate, LocalDate endDate);


}