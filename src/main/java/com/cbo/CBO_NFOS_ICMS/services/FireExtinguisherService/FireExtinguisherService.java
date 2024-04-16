package com.cbo.CBO_NFOS_ICMS.services.FireExtinguisherService;

import com.cbo.CBO_NFOS_ICMS.exception.ResourceNotFoundException;
import com.cbo.CBO_NFOS_ICMS.exception.UserNotFoundException;
import com.cbo.CBO_NFOS_ICMS.models.CIPM.Status;
import com.cbo.CBO_NFOS_ICMS.models.Finance.Finance;
import com.cbo.CBO_NFOS_ICMS.models.FireExtinguisher.FireExtinguisher;
import com.cbo.CBO_NFOS_ICMS.repositories.FinanceRepository.FinanceRepository;
import com.cbo.CBO_NFOS_ICMS.repositories.FireExtinguisherRepository.FireExtinguisherRepository;
import com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService.BranchService;
import com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService.SubProcessService;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
public class FireExtinguisherService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private final SubProcessService subProcessService;
    private final BranchService organizationalUnitService;
    private final FireExtinguisherRepository fireExtinguisherRepository;

    public FireExtinguisherService(SubProcessService subProcessService, BranchService organizationalUnitService, FireExtinguisherRepository fireExtinguisherRepository) {
        this.subProcessService = subProcessService;
        this.organizationalUnitService = organizationalUnitService;
        this.fireExtinguisherRepository = fireExtinguisherRepository;
    }

    public FireExtinguisher addFireExtinguisher(FireExtinguisher fireExtinguisher) {
        return fireExtinguisherRepository.save(fireExtinguisher);
    }


    public List<FireExtinguisher> findFireExtinguisher() {
        return fireExtinguisherRepository.findAll();
    }

    public FireExtinguisher updateFireExtinguisher(FireExtinguisher fireExtinguisher) {
        Optional<FireExtinguisher> optionalFireExtinguisher = fireExtinguisherRepository.findById(fireExtinguisher.getId());
        if (optionalFireExtinguisher.isPresent()) {
            FireExtinguisher existingFireExtinguisher = optionalFireExtinguisher.get();
            existingFireExtinguisher.setStatus(fireExtinguisher.getStatus());
            return fireExtinguisherRepository.save(existingFireExtinguisher);
        } else {
            throw new ResourceNotFoundException("FireExtinguisher not found");
        }
    }



    public FireExtinguisher findFireExtinguisherById(Long id) {
        return fireExtinguisherRepository.findFireExtinguisherById(id)
                .orElseThrow(() -> new UserNotFoundException("User by id" + id + " was not found"));
    }

    public void deleteFireExtinguisher(Long id) {
        fireExtinguisherRepository.deleteById(id);
    }

    public List<FireExtinguisher> findAllFireExtinguisherBYBranch(String branchId) {
        return fireExtinguisherRepository.findFireExtinguisherByBranchId(branchId);
    }

    public List<FireExtinguisher> findAllFireExtinguisher() {
        return fireExtinguisherRepository.findAll();
    }
    public List<FireExtinguisher> findAllFireExtinguisherSubProcess(Long subProcessId) {
        return fireExtinguisherRepository.findFireExtinguisherBySubProcessId(subProcessId);
    }

    public int findFireExtinguisherSize() {
        return fireExtinguisherRepository.findAll().size();
    }
}
