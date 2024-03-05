package com.cbo.CBO_NFOS_ICMS.services.FinanceService;

import com.cbo.CBO_NFOS_ICMS.exception.ResourceNotFoundException;
import com.cbo.CBO_NFOS_ICMS.exception.UserNotFoundException;
import com.cbo.CBO_NFOS_ICMS.models.Finance.Finance;
import com.cbo.CBO_NFOS_ICMS.repositories.FinanceRepository.FinanceRepository;
import com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService.BranchService;
import com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService.SubProcessService;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@Service
public class FinanceService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private final SubProcessService subProcessService;
    private final BranchService organizationalUnitService;
    private final FinanceRepository financeRepository;

    public FinanceService(SubProcessService subProcessService, BranchService organizationalUnitService, FinanceRepository financeRepository) {
        this.subProcessService = subProcessService;
        this.organizationalUnitService = organizationalUnitService;
        this.financeRepository = financeRepository;
    }

    public Finance addIFB(Finance finance) {
        return financeRepository.save(finance);
    }

    public List<Finance> findFinance() {
        return financeRepository.findAll();
    }

    public Finance updateFinance(Finance finance) {
        Optional<Finance> optionalFinance = financeRepository.findById(finance.getId());
        if (optionalFinance.isPresent()) {
            Finance existingFinance = optionalFinance.get();
            existingFinance.setStatus(finance.getStatus());
            existingFinance.setFinanceDate(finance.getFinanceDate()); // Add this line
            return financeRepository.save(existingFinance);
        } else {
            throw new ResourceNotFoundException("Finance not found");
        }
    }



    public Finance findFinanceById(Long id) {
        return financeRepository.findFinanceById(id)
                .orElseThrow(() -> new UserNotFoundException("User by id" + id + " was not found"));
    }

    public void deleteFinance(Long id) {
        financeRepository.deleteById(id);
    }

    public List<Finance> findAllFinanceBYBranch(Long branchId) {
        return financeRepository.findFinanceByBranchId(branchId);
    }

    public List<Finance> findAllFinance() {
        return financeRepository.findAll();
    }
    public List<Finance> findAllFinanceSubProcess(Long subProcessId) {
        return financeRepository.findFinanceBySubProcessId(subProcessId);
    }

    public boolean isCaseIdExists(String caseId) {
        return financeRepository.existsByCaseId(caseId);
    }

    public int findFinanceSize() {
        return financeRepository.findAll().size();
    }
}
