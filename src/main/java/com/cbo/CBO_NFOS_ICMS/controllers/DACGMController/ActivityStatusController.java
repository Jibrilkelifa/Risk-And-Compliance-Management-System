package com.cbo.CBO_NFOS_ICMS.controllers.DACGMController;

import com.cbo.CBO_NFOS_ICMS.models.DACGM.ActivityStatus;
import com.cbo.CBO_NFOS_ICMS.services.DACGMService.ActivityStatusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ActivityStatus")
public class ActivityStatusController {
    private final ActivityStatusService activityStatusService;

    public ActivityStatusController(ActivityStatusService activityStatusService) {
        this.activityStatusService = activityStatusService;
    }

    @GetMapping("/getAll")

    public ResponseEntity<List<ActivityStatus>> getAllActivityStatus() {
        List<ActivityStatus> activitiesStatus = activityStatusService.findAllActivityStatus();
        return new ResponseEntity<>(activitiesStatus, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<ActivityStatus> getAllDACGMInSpecificOrganizationalUnit(@PathVariable("id") Long id) {
        ActivityStatus activityStatus = activityStatusService.findActivityStatusById(id);
        return new ResponseEntity<>(activityStatus, HttpStatus.OK);
    }

}