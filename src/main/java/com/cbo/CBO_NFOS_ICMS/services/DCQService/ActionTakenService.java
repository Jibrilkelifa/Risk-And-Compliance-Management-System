package com.cbo.CBO_NFOS_ICMS.services.DCQService;

import com.cbo.CBO_NFOS_ICMS.exception.NoSuchActionTakenExistsException;
import com.cbo.CBO_NFOS_ICMS.exception.UserNotFoundException;
import com.cbo.CBO_NFOS_ICMS.models.DCQ.ActionTaken;
import com.cbo.CBO_NFOS_ICMS.repositories.DCQRepository.ActionTakenRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.util.List;

@Service
public class ActionTakenService {
    private final ActionTakenRepository actionTakenRepository;

    public ActionTakenService(ActionTakenRepository actionTakenRepository) {
        this.actionTakenRepository = actionTakenRepository;
    }
    public ActionTaken addActionTaken(ActionTaken actionTaken){

        return actionTakenRepository.save(actionTaken);
    }
    public List<ActionTaken> findAllActionTaken(){
        return actionTakenRepository.findAll();
    }
    public ActionTaken updateActionTaken(ActionTaken actionTaken) throws Exception {
        try {
            return actionTakenRepository.save(actionTaken);
        } catch (DataAccessException e) {
            throw new Exception("Error accessing data source: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new Exception("Illegal argument passed to method: " + e.getMessage());
        } catch (NullPointerException e) {
            throw new Exception("Null object passed to method: " + e.getMessage());
        } catch (TransactionSystemException e) {
            throw new Exception("Error with transaction system: " + e.getMessage());
        }
    }
    public ActionTaken findActionTakenById(Long id){
        return actionTakenRepository.findActionTakenById(id)
                .orElseThrow(()-> new UserNotFoundException("User by id" + id + " was not found"));
    }
    public void deleteActionTaken(Long id){
        ActionTaken existingActionTaken = actionTakenRepository.findById(id).orElse(null);
        if (existingActionTaken == null)
            throw new NoSuchActionTakenExistsException("No Such ActionTaken exists!!");
        else {
            actionTakenRepository.deleteById(id);
        }
    }

}
