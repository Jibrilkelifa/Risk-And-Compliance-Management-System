package com.cbo.CBO_NFOS_ICMS.controllers.DACGMController;

import com.cbo.CBO_NFOS_ICMS.models.DACGM.DailyActivityGapControl;
import com.cbo.CBO_NFOS_ICMS.services.DACGMService.DailyActivityGapControlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/DACGM")
public class DailyActivityGapController {
    private final DailyActivityGapControlService dACGMService;

    public DailyActivityGapController(DailyActivityGapControlService dACGMService) {
        this.dACGMService = dACGMService;
    }
    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
    public ResponseEntity<List<DailyActivityGapControl>> getAllDACGM(){
        List<DailyActivityGapControl> dACGM =dACGMService.findAllDACGM();
        return new ResponseEntity<>(dACGM, HttpStatus.OK);
    }

    @GetMapping("/findByOrganizationalUnitId/{id}")
    @PreAuthorize("hasAnyRole('ICMS_BRANCH_MANAGER','ICMS_BRANCH')")
    public ResponseEntity<List<DailyActivityGapControl>> getAllDACGMInSpecificOrganizationalUnit(@PathVariable("id") Long id) {
        List<DailyActivityGapControl> dACGM;
        dACGM = dACGMService.findAllDACGMInSpecificOrganizationalUnit(id);
        return new ResponseEntity<>(dACGM, HttpStatus.OK);
    }
    @GetMapping("/findBySubProcessId/{id}")
    @PreAuthorize("hasAnyRole('ICMS_DISTRICT')")
    public @ResponseBody List<DailyActivityGapControl> getAllDACGMInSpecificSubProcess(@PathVariable("id") Long id) {
        List<DailyActivityGapControl> dACGM;
        dACGM= dACGMService.findAllDACGMInSpecificSubProcess(id);
        return dACGM;
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<DailyActivityGapControl> getDACGMId
            (@PathVariable("id") Long id){
        DailyActivityGapControl dACGM = dACGMService.findDACGMById(id);
        return new ResponseEntity<>(dACGM, HttpStatus.OK);
    }
    @PostMapping("/add")
    @PreAuthorize("hasRole('ICMS_BRANCH')")
    public ResponseEntity<DailyActivityGapControl> addDACGM
            (@RequestBody DailyActivityGapControl dACGM){
        DailyActivityGapControl newDailyActivityGapControl = dACGMService.addDACGM(dACGM);
        return new ResponseEntity<>(newDailyActivityGapControl, HttpStatus.CREATED);
    }
    @PutMapping("/update")
    @PreAuthorize("hasRole('ICMS_BRANCH')")
    public ResponseEntity<DailyActivityGapControl> updateDACGM
            (@RequestBody DailyActivityGapControl dACGM){
        DailyActivityGapControl updateDailyActivityGapControl = dACGMService.updateDACGM(dACGM);
        return new ResponseEntity<>(updateDailyActivityGapControl, HttpStatus.CREATED);
    }
//    @PutMapping("/approve/{id}")
//    @PreAuthorize("hasRole('ICMS_BRANCH_MANAGER')")
//    public ResponseEntity<DailyActivityGapControl> approveDACGM
//            (@RequestBody DailyActivityGapControl dACGM){
//        DailyActivityGapControl approvedDailyActivityGapControl = dACGMService.approveDACGM(dACGM);
//        return new ResponseEntity<>(approvedDailyActivityGapControl, HttpStatus.CREATED);
//    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ICMS_BRANCH')")

    public ResponseEntity<?> deleteDACGM (@PathVariable("id") Long id){
        dACGMService.deleteDACGM(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    /*@GetMapping("/getFrequencyForAccountNumber/{accountNumber}")
    public Frequency getDCFrequency
            (@PathVariable("accountNumber") String accountNumber){
        int frequency  = dACGMService.findDACGMByAccountNumber(accountNumber);
        Frequency frequencyCount = new Frequency();
        frequencyCount.setCount(frequency);
        return frequencyCount;
    }*/

}