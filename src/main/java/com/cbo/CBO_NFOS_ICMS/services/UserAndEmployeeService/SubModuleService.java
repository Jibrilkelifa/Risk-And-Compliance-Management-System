package com.cbo.CBO_NFOS_ICMS.services.UserAndEmployeeService;

import com.cbo.CBO_NFOS_ICMS.exception.UserNotFoundException;
import com.cbo.CBO_NFOS_ICMS.models.SubModule;
import com.cbo.CBO_NFOS_ICMS.repositories.SubModuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubModuleService {
    private final SubModuleRepository subModuleRepository;

    public SubModuleService(SubModuleRepository subModuleRepository) {
        this.subModuleRepository = subModuleRepository;
    }
    public SubModule addSubModule(SubModule subModule){
        return subModuleRepository.save(subModule);
    }
    public List<SubModule> findAllSubModule(){
        return subModuleRepository.findAll();
    }
    public SubModule updateSubModule(SubModule subModule){
        return subModuleRepository.save(subModule);
    }
    public SubModule findSubModuleById(Long id){
        return subModuleRepository.findSubModuleById(id)
                .orElseThrow(()-> new UserNotFoundException("SubModule by id = " + id + " was not found"));
    }
    public void deleteSubModule(Long id){
        subModuleRepository.deleteById(id);
    }
}
