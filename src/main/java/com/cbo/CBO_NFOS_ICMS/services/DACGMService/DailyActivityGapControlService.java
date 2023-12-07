package com.cbo.CBO_NFOS_ICMS.services.DACGMService;

import com.cbo.CBO_NFOS_ICMS.exception.ResourceNotFoundException;
import com.cbo.CBO_NFOS_ICMS.exception.UserNotFoundException;
import com.cbo.CBO_NFOS_ICMS.models.DACGM.DailyActivityGapControl;
import com.cbo.CBO_NFOS_ICMS.repositories.DACGMRepository.DailyActivityGapControlRepository;
import com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService.BranchService;
import com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService.SubProcessService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DailyActivityGapControlService {

    private final SubProcessService subProcessService;
    private final BranchService branchService;
    private final DailyActivityGapControlRepository dACGMRepository;

    public DailyActivityGapControlService(SubProcessService subProcessService, BranchService branchService, DailyActivityGapControlRepository dACGMRepository) {
        this.subProcessService = subProcessService;
        this.branchService = branchService;
        this.dACGMRepository = dACGMRepository;
    }

    public DailyActivityGapControl addDACGM(DailyActivityGapControl dACGM) {

        return dACGMRepository.save(dACGM);
    }

    public DailyActivityGapControl authorizeDACGM(DailyActivityGapControl dACGM) {

        return dACGMRepository.save(dACGM);
    }


    public List<DailyActivityGapControl> findAllDACGM() {
        return dACGMRepository.findAll();
    }


    public DailyActivityGapControl approveActionPlan(Long id, String actionPlanDueDate) {
        DailyActivityGapControl row = dACGMRepository.findById(id).orElseThrow(() -> new UserNotFoundException("IncidentFraudReport by id = " + id + " was not found"));
        row.setActionPlanDueDate(actionPlanDueDate);

        row.setActionTaken(true);
        System.out.println(row.getActionTaken());

        return dACGMRepository.save(row);
    }

    public DailyActivityGapControl escalatePlan(Long id) {
        DailyActivityGapControl row = dACGMRepository.findById(id).orElseThrow(() -> new UserNotFoundException("IncidentFraudReport by id = " + id + " was not found"));
        row.setEscalatedByManager(true);
        return dACGMRepository.save(row);
    }

    public int findDACGMSize() {
        return dACGMRepository.findAll().size();
    }

    public DailyActivityGapControl updateDACGM(DailyActivityGapControl dACGM) {
        Optional<DailyActivityGapControl> optionalDACGM = dACGMRepository.findById(dACGM.getId());
        if (optionalDACGM.isPresent()) {
            DailyActivityGapControl existingDACGM = optionalDACGM.get();
            existingDACGM.setActivityStatus(dACGM.getActivityStatus());
            return dACGMRepository.save(existingDACGM);
        } else {
            throw new ResourceNotFoundException("DailyActivityGapControl not found");
        }
    }

    public DailyActivityGapControl findDACGMById(Long id) {
        return dACGMRepository.findDACGMById(id)
                .orElseThrow(() -> new UserNotFoundException("User by id" + id + " was not found"));
    }
  /*  public int findDACGMByAccountNumber(String accountNumber) {
        return (dACGMRepository.findDACGMByAccountNumber(accountNumber)).size();
    }
    */


    public void deleteDACGM(Long id) {
        dACGMRepository.deleteById(id);
    }

    public List<DailyActivityGapControl> findAllDACGMInSpecificOrganizationalUnit(Long id) {
        return dACGMRepository.findDACGMByBranchId(id);
    }

    public List<DailyActivityGapControl> findAllDACGMInSpecificSubProcess(Long subProcessId) {
//        Branch branch = organizationalUnitService.findBranchById(id);
        return dACGMRepository.findDACGMBySubProcessId(subProcessId);
    }


//    public List<DailyActivityGapControl> findAllDACGMInSpecificSubProcess(Long id) {
//        SubProcess subProcess = subProcessService.findSubProcessById(id);
//        List<Branch> organizationalUnits = branchService.findBranchBySubProcess(subProcess);
//        List<DailyActivityGapControl> dchques = new ArrayList<>();
//        for (int i = 0; i < organizationalUnits.size(); i++) {
//            List<DailyActivityGapControl> clmfjhd = dACGMRepository.findDACGMByBranch(organizationalUnits.get(i));
//            for (int j = 0; j < clmfjhd.size(); j++) {
//                dchques.add(dACGMRepository.findDACGMByBranch(organizationalUnits.get(i)).get(j));
//
//            }
//
//
//        }
//        return dchques;
//    }


   /* public DACGM updateTableRow(Long id, boolean caseAuthorized) {
        DACGM row = dACGMRepository.findById(id).orElseThrow(() -> new UserNotFoundException("DACGM  by id" + id + " was not found"));
        row.setCaseAuthorized(caseAuthorized);
        return dACGMRepository.save(row);
    }*/

    public void deleteRow(int id) {
        Optional<DailyActivityGapControl> data = dACGMRepository.findById((long) id);
        if (data.isPresent()) {

            List<DailyActivityGapControl> dataList = dACGMRepository.findAll();
            for (int i = id; i - 1 < dataList.size(); i++) {
                DailyActivityGapControl d = dataList.get(i - 1);

                d.setId(d.getId() - 1);
                dACGMRepository.save(d);
            }
            dACGMRepository.deleteById((long) id);
        }
    }

//    public DailyActivityGapControl approveDACGM(DailyActivityGapControl dACGM) {
//
//    }
}