package com.cbo.CBO_NFOS_ICMS.services.IFBService;

import com.cbo.CBO_NFOS_ICMS.exception.UserNotFoundException;

import com.cbo.CBO_NFOS_ICMS.models.IFB.IfbStatus;
import com.cbo.CBO_NFOS_ICMS.repositories.CIPMRepository.StatusRepository;
import com.cbo.CBO_NFOS_ICMS.repositories.IFBRepository.IfbStatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IfbStatusService {
    private final IfbStatusRepository ifbStatusRepository;

    public IfbStatusService(IfbStatusRepository ifbStatusRepository) {
        this.ifbStatusRepository = ifbStatusRepository;
    }

    public IfbStatus addStatus(IfbStatus status) {

        return ifbStatusRepository.save(status);
    }

    public List<IfbStatus> findAllStatus() {
        return ifbStatusRepository.findAll();
    }

    public IfbStatus updateStatus(IfbStatus status) {
        return ifbStatusRepository.save(status);
    }

    public IfbStatus findStatusById(Long id) {
        return ifbStatusRepository.findStatusById(id)
                .orElseThrow(() -> new UserNotFoundException("User by id" + id + " was not found"));
    }

    public void deleteStatus(Long id) {
        ifbStatusRepository.deleteById(id);
    }
}
