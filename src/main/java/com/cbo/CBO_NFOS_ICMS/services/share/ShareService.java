package com.cbo.CBO_NFOS_ICMS.services.share;

import com.cbo.CBO_NFOS_ICMS.exception.ResourceNotFoundException;
import com.cbo.CBO_NFOS_ICMS.exception.UserNotFoundException;

import com.cbo.CBO_NFOS_ICMS.models.share.Share;
import com.cbo.CBO_NFOS_ICMS.repositories.shareRepository.ShareRepository;
import com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService.BranchService;
import com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService.SubProcessService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShareService {

    private final SubProcessService subProcessService;
    private final BranchService branchService;
    private final ShareRepository shareRepository;

    public ShareService(SubProcessService subProcessService, BranchService branchService, ShareRepository shareRepository) {
        this.subProcessService = subProcessService;
        this.branchService = branchService;
        this.shareRepository = shareRepository;
    }

    public Share addShare(Share share) {

        return shareRepository.save(share);
    }

    public Share authorizeShare(Share share) {

        return shareRepository.save(share);
    }


    public List<Share> findAllShare() {
        return shareRepository.findAll();
    }


//    public Share approveActionPlan(Long id, String actionPlanDueDate) {
//        Share row = ShareRepository.findById(id).orElseThrow(() -> new UserNotFoundException("IncidentFraudReport by id = " + id + " was not found"));
//        row.setActionPlanDueDate(actionPlanDueDate);
//
//        row.setActionTaken(true);
//        System.out.println(row.getActionTaken());
//
//        return ShareRepository.save(row);
//    }
    public boolean isCaseIdExists(String caseId) {
        return shareRepository.existsByCaseId(caseId);
    }

    /*    public Share approveActionPlan(Long id, String actionPlanDueDate) {
            Share row = ShareRepository.findById(id).orElseThrow(() -> new UserNotFoundException("IncidentFraudReport by id = " + id + " was not found"));
            row.setActionPlanDueDate(actionPlanDueDate);
            return ShareRepository.save(row);
        }*/
//    public Share escalatePlan(Long id) {
//        Share row = ShareRepository.findById(id).orElseThrow(() -> new UserNotFoundException("IncidentFraudReport by id = " + id + " was not found"));
//        row.setEscalatedByManager(true);
//        return ShareRepository.save(row);
//    }
    public int findShareSize() {
        return shareRepository.findAll().size();
    }

    public Share updateShare(Share share) {
        Optional<Share> optionalShare = shareRepository.findById(share.getId());
        if (optionalShare.isPresent()) {
            Share existingShare = optionalShare.get();
            existingShare.setShareStatus(share.getShareStatus());
            return shareRepository.save(existingShare);
        } else {
            throw new ResourceNotFoundException("Share not found");
        }
    }

    public Share findShareById(Long id) {
        return shareRepository.findShareById(id)
                .orElseThrow(() -> new UserNotFoundException("User by id" + id + " was not found"));
    }
  /*  public int findShareByAccountNumber(String accountNumber) {
        return (ShareRepository.findShareByAccountNumber(accountNumber)).size();
    }
    */


    public void deleteShare(Long id) {
        shareRepository.deleteById(id);
    }

    public List<Share> findAllShareInSpecificOrganizationalUnit(Long id) {
        return shareRepository.findShareByBranchId(id);
    }

    public List<Share> findAllShareInSpecificSubProcess(Long subProcessId) {
//        Branch branch = organizationalUnitService.findBranchById(id);
        return shareRepository.findShareBySubProcessId(subProcessId);
    }


//    public List<Share> findAllShareInSpecificSubProcess(Long id) {
//        SubProcess subProcess = subProcessService.findSubProcessById(id);
//        List<Branch> organizationalUnits = branchService.findBranchBySubProcess(subProcess);
//        List<Share> dchques = new ArrayList<>();
//        for (int i = 0; i < organizationalUnits.size(); i++) {
//            List<Share> clmfjhd = ShareRepository.findShareByBranch(organizationalUnits.get(i));
//            for (int j = 0; j < clmfjhd.size(); j++) {
//                dchques.add(ShareRepository.findShareByBranch(organizationalUnits.get(i)).get(j));
//
//            }
//
//
//        }
//        return dchques;
//    }


   /* public Share updateTableRow(Long id, boolean caseAuthorized) {
        Share row = ShareRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Share  by id" + id + " was not found"));
        row.setCaseAuthorized(caseAuthorized);
        return ShareRepository.save(row);
    }*/

    public void deleteRow(int id) {
        Optional<Share> data = shareRepository.findById((long) id);
        if (data.isPresent()) {

            List<Share> dataList = shareRepository.findAll();
            for (int i = id; i - 1 < dataList.size(); i++) {
                Share d = dataList.get(i - 1);

                d.setId(d.getId() - 1);
                shareRepository.save(d);
            }
            shareRepository.deleteById((long) id);
        }
    }

//    public Share approveShare(Share Share) {
//
//    }
}