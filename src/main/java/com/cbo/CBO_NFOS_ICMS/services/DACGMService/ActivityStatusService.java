package com.cbo.CBO_NFOS_ICMS.services.DACGMService;

import com.cbo.CBO_NFOS_ICMS.exception.UserNotFoundException;
import com.cbo.CBO_NFOS_ICMS.models.DACGM.ActivityStatus;
import com.cbo.CBO_NFOS_ICMS.repositories.DACGMRepository.ActivityStatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityStatusService {
    private final ActivityStatusRepository activityStatusRepository;

    public ActivityStatusService(
            ActivityStatusRepository activityStatusRepository) {
        this.activityStatusRepository = activityStatusRepository;
    }

    public ActivityStatus findActivityStatusById(Long id) {
        return activityStatusRepository.findActivityStatusById(id)
                .orElseThrow(() -> new UserNotFoundException("Activity Status by id = " + id + " was not found"));
    }

    public ActivityStatus addActivityStatus(ActivityStatus as) {
        return activityStatusRepository.save(as);
    }

    public List<ActivityStatus> findAllActivityStatus() {
        return activityStatusRepository.findAll();
    }

}