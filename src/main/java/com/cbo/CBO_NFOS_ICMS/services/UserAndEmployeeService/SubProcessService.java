package com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService;

import com.cbo.CBO_NFOS_ICMS.exception.NoSuchUserExistsException;
import com.cbo.CBO_NFOS_ICMS.exception.ResourceNotFoundException;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.SubProcess;
import com.cbo.CBO_NFOS_ICMS.repositories.UserAndEmployeeRepository.SubProcessRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubProcessService {
    private final SubProcessRepository subProcessRepository;

    public SubProcessService(SubProcessRepository subProcessRepository) {
        this.subProcessRepository = subProcessRepository;
    }

public SubProcess addSubProcess(SubProcess subProcessDTO) {
    SubProcess savedSubProcess = null;
    if (subProcessDTO.getId() != null) {
        savedSubProcess = subProcessRepository.findById(subProcessDTO.getId()).orElse(null);
    }
    if (savedSubProcess != null)
        throw new ResourceNotFoundException("SubProcess with id "+savedSubProcess.getId()+" already exist.");
    else {
        savedSubProcess = new SubProcess();
        savedSubProcess.setId(subProcessDTO.getId());
        savedSubProcess.setName(subProcessDTO.getName());
        subProcessRepository.save(savedSubProcess);
        return savedSubProcess;
    }
}


public List<SubProcess> findAllSubProcess(){
    return subProcessRepository.findAll();
}

public SubProcess updateSubProcess(SubProcess subProcessDTO){

    SubProcess oldSubProcess = subProcessRepository.findById(subProcessDTO.getId()).orElse(null);
    if (oldSubProcess == null)
        throw new NoSuchUserExistsException("No such subProcess exists!");
    else {
        oldSubProcess.setName(subProcessDTO.getName());
        subProcessRepository.save(oldSubProcess);
        return oldSubProcess;
    }
}


public SubProcess findSubProcessById(Long id){
    return subProcessRepository.findById(id).orElseThrow(() -> new NoSuchUserExistsException("No SubProcess is present with ID = " + id));
}

public String deleteSubProcess(Long id){

    SubProcess existingSubProcess = subProcessRepository.findById(id).orElse(null);
    if (existingSubProcess == null)
        throw new NoSuchUserExistsException("No such subProcess exists!");
    else {
        subProcessRepository.deleteById(id);
        return "subProcess Record deleted Successfully";
    }
  }

    public String[] findAllDistricts() {
        List<SubProcess> allSubProcesses = subProcessRepository.findAll();
        allSubProcesses.removeIf(subProcess -> !subProcess.getName().toLowerCase().contains(" district"));
        String[] subProcessNames = new String[allSubProcesses.size()];
        for(int i = 0; i < allSubProcesses.size(); i++) {
            subProcessNames[i] = allSubProcesses.get(i).getName();
        }
        return subProcessNames;
    }

//    public SubProcess findSubProcessByName(String name) {
//        return subProcessRepository.findSubProcessByName(name);
//    }
}
