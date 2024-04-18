package com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService;

import com.cbo.CBO_NFOS_ICMS.exception.NoSuchUserExistsException;
import com.cbo.CBO_NFOS_ICMS.models.UserAndEmployee.Branch;
import com.cbo.CBO_NFOS_ICMS.repositories.UserAndEmployeeRepository.BranchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchService {
    private final BranchRepository branchRepository;

    public BranchService(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }


//public Branch addBranch(Branch branchDTO) {
//    Branch savedBranch = null;
//    if (branchDTO.getId() != null) {
//        savedBranch = branchRepository.findById(branchDTO.getId()).orElse(null);
//    }
//    if (savedBranch == null) {
//        savedBranch = new Branch();
//        savedBranch.setId(branchDTO.getId());
//        savedBranch.setCode(branchDTO.getCode());
//        savedBranch.setName(branchDTO.getName());
//        savedBranch.setMnemonic(branchDTO.getMnemonic());
//        savedBranch.setLocation(branchDTO.getLocation());
//        savedBranch.setTelephone(branchDTO.getTelephone());
//        branchRepository.save(savedBranch);
//    }
//    return savedBranch;
//}


public List<Branch> findAllBranch(){
    return branchRepository.findAll();
}


//public Branch updateBranch(Branch branchDTO){
//
//    Branch oldBranch = branchRepository.findById(branchDTO.getId()).orElse(null);
//    if (oldBranch == null)
//        throw new NoSuchUserExistsException("No such branch exists!");
//    else {
//        oldBranch.setCode(branchDTO.getCode());
//        oldBranch.setName(branchDTO.getName());
//        oldBranch.setMnemonic(branchDTO.getMnemonic());
//        oldBranch.setLocation(branchDTO.getLocation());
//        oldBranch.setTelephone(branchDTO.getTelephone());
//        branchRepository.save(oldBranch);
//        return oldBranch;
//    }
//}


public Branch findBranchById(String id){
    return branchRepository.findBranchById(Long.valueOf(id)).orElseThrow(() -> new NoSuchUserExistsException("NO Branch PRESENT WITH ID = " + id));
}

public String deleteBranch(Long id){

    Branch existingBranch = branchRepository.findById(id).orElse(null);
    if (existingBranch == null)
        throw new NoSuchUserExistsException("No such branch exists!");
    else {
        branchRepository.deleteById(id);
        return "Record deleted Successfully";
    }
  }


}
