package com.cbo.CBO_NFOS_ICMS.repositories.DCQRepository;

import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.OrganizationalUnit;
import com.cbo.CBO_NFOS_ICMS.models.DCQ.DishonoredCheque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Date;

public interface DishonoredChequeRepository extends JpaRepository<DishonoredCheque, Long> {
    void deleteDishonouredChequeById(Long id);
    @Query("SELECT dc FROM DishonoredCheque dc WHERE dc.accountNumber = :accountNumber")
    List<DishonoredCheque> findDishonouredChequeByAccountNumber(@Param("accountNumber") String accountNumber);
    Optional<DishonoredCheque> findDishonouredChequeById(Long id);
    List<DishonoredCheque> findDishonouredChequeByOrganizationalUnit(OrganizationalUnit organizationalUnit);
    List<DishonoredCheque> findByFrequencyGreaterThanEqual(int frequency);
    @Query("SELECT COUNT(dcq) FROM DishonoredCheque dcq WHERE dcq.frequency >= 3 AND dcq.datePresented BETWEEN :startDate AND :endDate")
    int countDishonouredChequesThreeTimesInLastWeek(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}

