package com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService;

import com.cbo.CBO_NFOS_ICMS.exception.UserNotFoundException;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.Branch;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.Branch;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.SubProcess;
import com.cbo.CBO_NFOS_ICMS.repositories.UserAndEmployeeRepository.BranchRepository;
import com.cbo.CBO_NFOS_ICMS.repositories.UserAndEmployeeRepository.BranchRepository;
import com.cbo.CBO_NFOS_ICMS.repositories.UserAndEmployeeRepository.SubProcessRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SubProcessService {


    private final SubProcessRepository subProcessRepository;
    private  final BranchRepository organizationalUnitRepository;

    public SubProcessService(SubProcessRepository subProcessRepository, BranchRepository organizationalUnitRepository) {
        this.subProcessRepository = subProcessRepository;
        this.organizationalUnitRepository = organizationalUnitRepository;
    }
    public SubProcess addSubProcess(SubProcess subProcess){

        return subProcessRepository.save(subProcess);
    }
    public List<SubProcess> findAllSubProcess(){
        return subProcessRepository.findAll();
    }
    public SubProcess  updateSubProcess(SubProcess subProcess){
        return subProcessRepository.save(subProcess);
    }
    public SubProcess findSubProcessById(Long id){
        return subProcessRepository.findSubProcessById(id)
                .orElseThrow(()-> new UserNotFoundException("SubProcess by id" + id + " was not found"));
    }
    public void deleteSubProcess(Long id){
        subProcessRepository.deleteById(id);
    }

    public List<Branch> findAllBranch(SubProcess subProcess) {
        return organizationalUnitRepository.findAllBySubProcess(subProcess);
    }

    public SubProcess findSubProcessByName(String name) {
        return subProcessRepository.findSubProcessByName(name).orElseThrow(()-> new UserNotFoundException("SubProcess by name = " + name + " was not found"));
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
}
