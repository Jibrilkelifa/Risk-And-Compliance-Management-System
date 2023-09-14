package com.cbo.CBO_NFOS_ICMS.controllers.DCQController;

import com.cbo.CBO_NFOS_ICMS.models.DCQ.ActionTaken;
import com.cbo.CBO_NFOS_ICMS.services.DCQService.ActionTakenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/actionTaken")
public class ActionTakenController {
    private final ActionTakenService actionTakenService;

    public ActionTakenController(ActionTakenService actionTakenService) {
        this.actionTakenService = actionTakenService;
    }
    @GetMapping("/all")
    public ResponseEntity<List<ActionTaken>> getAllActionTakens(){
        List<ActionTaken> actionTakens = actionTakenService.findAllActionTaken();
        return new ResponseEntity<>(actionTakens, HttpStatus.OK);

    }
    @GetMapping("/find/{id}")
    public ResponseEntity<ActionTaken> getActionTakenById (@PathVariable(value ="id") Long id) {
        ActionTaken actionTaken= actionTakenService.findActionTakenById(id);
        return new ResponseEntity<>(actionTaken, HttpStatus.OK);
    }
    @PostMapping("/add")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<ActionTaken> addActionTaken(@RequestBody ActionTaken actionTaken) {
        ActionTaken newActionTaken =actionTakenService.addActionTaken(actionTaken);
        return new ResponseEntity<>(newActionTaken, HttpStatus.CREATED);
    }
    @PutMapping("/update")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<ActionTaken> updateCActionTaken(@RequestBody ActionTaken actionTaken) throws Exception {
        ActionTaken updateActionTaken =actionTakenService.updateActionTaken(actionTaken);
        return new ResponseEntity<>(updateActionTaken, HttpStatus.CREATED);
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<String> deleteActionTaken(@PathVariable("id") Long id) {
        actionTakenService.deleteActionTaken(id);
        return new ResponseEntity<>("Record deleted Successfully",HttpStatus.OK);
    }
}
