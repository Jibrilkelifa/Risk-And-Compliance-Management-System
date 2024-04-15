package com.cbo.CBO_NFOS_ICMS.services.FinanceService;

import com.cbo.CBO_NFOS_ICMS.exception.UserNotFoundException;
import com.cbo.CBO_NFOS_ICMS.models.Finance.FinanceStatus;
import com.cbo.CBO_NFOS_ICMS.repositories.FinanceRepository.FinanceStatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinanceStatusService {
    private final FinanceStatusRepository financeStatusRepository;

    public FinanceStatusService(
            FinanceStatusRepository financeStatusRepository) {
        this.financeStatusRepository = financeStatusRepository;
    }

    public FinanceStatus findFinanceStatusById(Long id) {
        return financeStatusRepository.findFinanceStatusById(id)
                .orElseThrow(() -> new UserNotFoundException("Finance Status by id = " + id + " was not found"));
    }

    public FinanceStatus addFinanceStatus(FinanceStatus as) {
        return financeStatusRepository.save(as);
    }

    public List<FinanceStatus> findAllFinanceStatus() {
        return financeStatusRepository.findAll();
    }

}