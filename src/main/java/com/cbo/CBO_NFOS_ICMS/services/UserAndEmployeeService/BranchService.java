package com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService;

import com.cbo.CBO_NFOS_ICMS.exception.UserNotFoundException;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.Branch;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.SubProcess;
import com.cbo.CBO_NFOS_ICMS.repositories.UserAndEmployeeRepository.BranchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchService {
    private final BranchRepository branchRepository;

    public BranchService(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }
    public Branch addBranch(Branch branch){

        return branchRepository.save(branch);
    }
    public List<Branch> findAllBranch(){
        return branchRepository.findAll();
    }
    public Branch  updateBranch(Branch branch){
        return branchRepository.save(branch);
    }
    public Branch findBranchById(Long id){
        return branchRepository.findBranchById(id)
                .orElseThrow(()-> new UserNotFoundException("Branch by id" + id + " was not found"));
    }

    public List<Branch> findBranchBySubProcess(SubProcess subProcess){
        return branchRepository.findBranchBySubProcess(subProcess);
    }
    public String deleteBranch(Long id){

        branchRepository.deleteById(id);
        return  "Record deleted Succesfully";
    }

    public Branch findBranchByName(String name) {
        return branchRepository.findBranchByName(name).orElseThrow(()-> new UserNotFoundException("Branch by name=" + name + " was not found"));
    }
}
