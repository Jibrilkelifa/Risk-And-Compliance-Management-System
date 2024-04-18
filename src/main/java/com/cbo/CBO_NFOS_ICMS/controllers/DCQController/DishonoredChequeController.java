package com.cbo.CBO_NFOS_ICMS.controllers.DCQController;

import com.cbo.CBO_NFOS_ICMS.models.CIPM.Frequency;
import com.cbo.CBO_NFOS_ICMS.models.DCQ.DishonoredCheque;
import com.cbo.CBO_NFOS_ICMS.services.DCQService.DishonoredChequeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/DCQ")
public class DishonoredChequeController {
    private final DishonoredChequeService dishonoredChequeService;

    public DishonoredChequeController(DishonoredChequeService dishonoredChequeService) {
        this.dishonoredChequeService = dishonoredChequeService;
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN','ICMS_BANKING_OPERATION')")
    public ResponseEntity<List<DishonoredCheque>> getAllDishonouredCheque() {
        List<DishonoredCheque> DishonoredCheque = dishonoredChequeService.findAllDishonouredCheque();
        return new ResponseEntity<>(DishonoredCheque, HttpStatus.OK);
    }

    @GetMapping("/findByOrganizationalUnitId/{id}")
    @PreAuthorize("hasAnyRole('ICMS_BRANCH_MANAGER','ICMS_BRANCH_IC')")
    public ResponseEntity<List<DishonoredCheque>> getAllDishouneredChequeInSpecificOrganizationalUnit(@PathVariable("id") String branchId) {
        List<DishonoredCheque> DishounoredCheque;
        DishounoredCheque = dishonoredChequeService.findAllDishonouredChequeInSpecificOrganizationalUnit(branchId);
        return new ResponseEntity<>(DishounoredCheque, HttpStatus.OK);
    }

    @GetMapping("/findBySubProcessId/{id}")
    @PreAuthorize("hasAnyRole('ICMS_DISTRICT_IC','ICMS_DISTRICT_DIRECTOR')")
    public @ResponseBody List<DishonoredCheque> getAllDishonouredChequeInSpecificSubProcess(@PathVariable("id") Long subProcessId) {
        List<DishonoredCheque> dishonoredCheque;
        dishonoredCheque = dishonoredChequeService.findAllDishonouredChequeInSpecificSubProcess(subProcessId);
        return dishonoredCheque;
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<DishonoredCheque> getDishonouredChequeId(@PathVariable("id") Long id) {
        DishonoredCheque DishonoredCheque = dishonoredChequeService.findDishonouredChequeById(id);
        return new ResponseEntity<>(DishonoredCheque, HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ICMS_BRANCH_MANAGER')")
    public ResponseEntity<DishonoredCheque> addDishonouredCheque(@RequestBody DishonoredCheque DishonoredCheque) {
        DishonoredCheque newDishonoredCheque = dishonoredChequeService.addDishonouredCheque(DishonoredCheque);
        return new ResponseEntity<>(newDishonoredCheque, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ICMS_BRANCH_MANAGER')")
    public ResponseEntity<DishonoredCheque> updateDishonouredCheque
            (@RequestBody DishonoredCheque DishonoredCheque) {
        DishonoredCheque updateDishonoredCheque = dishonoredChequeService.updateDishonouredCheque(DishonoredCheque);
        return new ResponseEntity<>(updateDishonoredCheque, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ICMS_BRANCH_IC')")

    public ResponseEntity<?> deleteDishonouredCheque(@PathVariable("id") Long id) {
        dishonoredChequeService.deleteDishonouredCheque(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getFrequencyForAccountNumber/{accountNumber}")
    public Frequency getDCFrequency(@PathVariable("accountNumber") String accountNumber) {
        int frequency = dishonoredChequeService.findDishonouredChequeByAccountNumber(accountNumber);
        Frequency frequencyCount = new Frequency();
        frequencyCount.setCount(frequency);
        return frequencyCount;
    }

    @GetMapping("/getTotalDishonouredChequeCount")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
    public ResponseEntity<Integer> getTotalDishonouredChequeCountForYear() {
        List<DishonoredCheque> dishonoredCheques = dishonoredChequeService.findAllDishonouredCheque();
        int count = 0;
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
        for (DishonoredCheque dcq : dishonoredCheques) {
            String datePresented = dcq.getDatePresented();
            try {
                LocalDate date = LocalDate.parse(datePresented, formatter);
                if (date.getYear() == currentDate.getYear()) {
                    count++;
                }
            } catch (DateTimeParseException e) {
                // handle parse exception
            }
        }
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/three-times-in-last-week/count")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
    public ResponseEntity<Integer> countDishonouredChequesThreeTimesInLastWeek() {
        int count = dishonoredChequeService.countDishonouredChequesThreeTimesInLastWeek();
        return ResponseEntity.ok(count);
    }

/*    @GetMapping("/three-times-in-last-week-list")
    @PreAuthorize("hasAnyRole('ICMS_ADMIN')")
    public ResponseEntity<List<DishonoredCheque>> getDishonouredChequesThreeTimesInLastWeek() {
        List<DishonoredCheque> dishonoredCheques = dishonoredChequeService.getDishonouredChequesThreeTimesInLastWeek();
        return ResponseEntity.ok(dishonoredCheques);
    }*/

    }

