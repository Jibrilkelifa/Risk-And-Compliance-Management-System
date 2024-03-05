package com.cbo.CBO_NFOS_ICMS.services.IFBService;

import com.cbo.CBO_NFOS_ICMS.exception.ResourceNotFoundException;
import com.cbo.CBO_NFOS_ICMS.exception.UserNotFoundException;
import com.cbo.CBO_NFOS_ICMS.models.IFB.IFB;
import com.cbo.CBO_NFOS_ICMS.repositories.IFBRepository.IFBRepository;
import com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService.BranchService;
import com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService.SubProcessService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@Service
public class IFBService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private final SubProcessService subProcessService;
    private final BranchService organizationalUnitService;
    private final IFBRepository ifbRepository;

    public IFBService(SubProcessService subProcessService, BranchService organizationalUnitService, IFBRepository ifbRepository) {
        this.subProcessService = subProcessService;
        this.organizationalUnitService = organizationalUnitService;
        this.ifbRepository = ifbRepository;
    }

    public IFB addIFB(IFB iFB) {
        return ifbRepository.save(iFB);
    }

    public List<IFB> findIFB() {
        return ifbRepository.findAll();
    }

    public IFB updateIFB(IFB ifb) {
        Optional<IFB> optionalIFB = ifbRepository.findById(ifb.getId());
        if (optionalIFB.isPresent()) {
            IFB existingIFB = optionalIFB.get();
            existingIFB.setStatus(ifb.getStatus());
            existingIFB.setIfbDate(ifb.getIfbDate()); // Add this line
            return ifbRepository.save(existingIFB);
        } else {
            throw new ResourceNotFoundException("IFB not found");
        }
    }



    public IFB findIFBById(Long id) {
        return ifbRepository.findIFBById(id)
                .orElseThrow(() -> new UserNotFoundException("User by id" + id + " was not found"));
    }

    public void deleteIFB(Long id) {
        ifbRepository.deleteById(id);
    }

    public List<IFB> findAllIFBBYBranch(Long branchId) {
        return ifbRepository.findIFBByBranchId(branchId);
    }

    public List<IFB> findAllIFB() {
        return ifbRepository.findAll();
    }
    public List<IFB> findAllIFBSubProcess(Long subProcessId) {
        return ifbRepository.findIFBBySubProcessId(subProcessId);
    }

    public boolean isCaseIdExists(String caseId) {
        return ifbRepository.existsByCaseId(caseId);
    }

    public int findIFBSize() {
        return ifbRepository.findAll().size();
    }
    public IFB authorizeIFR(Long id, String caseAuthorizer) {
        IFB row = ifbRepository.findById(id).orElseThrow(() -> new UserNotFoundException("IncidentFraudReport by id = " + id + " was not found"));
        row.setAuthorizedBy(caseAuthorizer);
        row.setIsAuthorized(true);
        return ifbRepository.save(row);
    }

}
