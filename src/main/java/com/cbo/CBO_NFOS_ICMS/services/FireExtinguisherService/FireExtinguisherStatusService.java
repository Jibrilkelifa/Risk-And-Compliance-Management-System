package com.cbo.CBO_NFOS_ICMS.services.FireExtinguisherService;

import com.cbo.CBO_NFOS_ICMS.exception.UserNotFoundException;
import com.cbo.CBO_NFOS_ICMS.models.Finance.FinanceStatus;
import com.cbo.CBO_NFOS_ICMS.repositories.FinanceRepository.FinanceStatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FireExtinguisherStatusService {
    private final FinanceStatusRepository financeStatusRepository;

    public FireExtinguisherStatusService(FinanceStatusRepository financeStatusRepository) {
        this.financeStatusRepository = financeStatusRepository;
    }

    public FinanceStatus addStatus(FinanceStatus status) {

        return financeStatusRepository.save(status);
    }

    public List<FinanceStatus> findAllStatus() {
        return financeStatusRepository.findAll();
    }

    public FinanceStatus updateStatus(FinanceStatus status) {
        return financeStatusRepository.save(status);
    }

    public FinanceStatus findStatusById(Long id) {
        return financeStatusRepository.findStatusById(id)
                .orElseThrow(() -> new UserNotFoundException("User by id" + id + " was not found"));
    }

    public void deleteStatus(Long id) {
        financeStatusRepository.deleteById(id);
    }
}
