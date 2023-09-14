package com.cbo.CBO_NFOS_ICMS.services.IFRService;

import com.cbo.CBO_NFOS_ICMS.exception.UserNotFoundException;
import com.cbo.CBO_NFOS_ICMS.models.IFR.CaseStatus;
import com.cbo.CBO_NFOS_ICMS.repositories.IFRRepository.CaseStatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CaseStatusService {
    private final CaseStatusRepository caseStatusRepository;

    public CaseStatusService(CaseStatusRepository caseStatusRepository) {
        this.caseStatusRepository = caseStatusRepository;
    }
    public CaseStatus addCaseStatus(CaseStatus caseStatus){

        return caseStatusRepository.save(caseStatus);
    }
    public List<CaseStatus> findAllCaseStatus(){
        return caseStatusRepository.findAll();
    }
    public CaseStatus updateCaseStatus(CaseStatus caseStatus){
        return caseStatusRepository.save(caseStatus);
    }
    public CaseStatus findCaseStatusById(Long id){
        return caseStatusRepository.findCaseStatusById(id)
                .orElseThrow(()-> new UserNotFoundException("User by id" + id + " was not found"));
    }
    public void deleteCaseStatus(Long id){
        caseStatusRepository.deleteById(id);
    }
}
