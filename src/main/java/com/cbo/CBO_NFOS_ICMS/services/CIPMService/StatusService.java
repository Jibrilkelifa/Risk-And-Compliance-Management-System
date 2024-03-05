package com.cbo.CBO_NFOS_ICMS.services.CIPMService;

import com.cbo.CBO_NFOS_ICMS.exception.UserNotFoundException;

import com.cbo.CBO_NFOS_ICMS.models.CIPM.Status;
import com.cbo.CBO_NFOS_ICMS.repositories.CIPMRepository.StatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusService {
    private final StatusRepository statusRepository;

    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public Status addStatus(Status status) {

        return statusRepository.save(status);
    }

    public List<Status> findAllStatus() {
        return statusRepository.findAll();
    }

    public Status updateStatus(Status status) {
        return statusRepository.save(status);
    }

    public Status findStatusById(Long id) {
        return statusRepository.findStatusById(id)
                .orElseThrow(() -> new UserNotFoundException("User by id" + id + " was not found"));
    }

    public void deleteStatus(Long id) {
        statusRepository.deleteById(id);
    }
}
