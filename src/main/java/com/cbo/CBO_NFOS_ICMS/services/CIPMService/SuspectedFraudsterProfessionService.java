package com.cbo.CBO_NFOS_ICMS.services.CIPMService;

import com.cbo.CBO_NFOS_ICMS.exception.UserNotFoundException;
import com.cbo.CBO_NFOS_ICMS.models.CIPM.SuspectedFraudsterProfession;
import com.cbo.CBO_NFOS_ICMS.repositories.CIPMRepository.SuspectedFraudsterProfessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuspectedFraudsterProfessionService {
    private final SuspectedFraudsterProfessionRepository suspectedFraudsterProfessionRepository;

    public SuspectedFraudsterProfessionService(SuspectedFraudsterProfessionRepository suspectedFraudsterProfessionRepository) {
        this.suspectedFraudsterProfessionRepository = suspectedFraudsterProfessionRepository;
    }

    public SuspectedFraudsterProfession addSuspectedFraudsterProfession(SuspectedFraudsterProfession SuspectedFraudsterProfession) {

        return suspectedFraudsterProfessionRepository.save(SuspectedFraudsterProfession);
    }

    public List<SuspectedFraudsterProfession> findAllSuspectedFraudsterProfession() {
        return suspectedFraudsterProfessionRepository.findAll();
    }

    public SuspectedFraudsterProfession updateSuspectedFraudsterProfession(SuspectedFraudsterProfession SuspectedFraudsterProfession) {
        return suspectedFraudsterProfessionRepository.save(SuspectedFraudsterProfession);
    }

    public SuspectedFraudsterProfession findSuspectedFraudsterProfessionById(Long id) {
        return suspectedFraudsterProfessionRepository.findSuspectedFraudsterProfessionById(id)
                .orElseThrow(() -> new UserNotFoundException("SuspectedFraudsterProfession by id" + id + " was not found"));
    }

    public void deleteSuspectedFraudsterProfession(Long id) {
        suspectedFraudsterProfessionRepository.deleteById(id);
    }

}
