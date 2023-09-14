package com.cbo.CBO_NFOS_ICMS.services.CIPMService;

import com.cbo.CBO_NFOS_ICMS.exception.UserNotFoundException;
import com.cbo.CBO_NFOS_ICMS.models.CIPM.InsuranceCoverageType;
import com.cbo.CBO_NFOS_ICMS.repositories.CIPMRepository.InsuranceCoverageTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class InsuranceCoverageTypeService {
    private final InsuranceCoverageTypeRepository insuranceCoverageTypeRepository;

    public InsuranceCoverageTypeService(InsuranceCoverageTypeRepository insuranceCoverageTypeRepository) {
        this.insuranceCoverageTypeRepository = insuranceCoverageTypeRepository;
    }
    public InsuranceCoverageType addInsuranceCoverageType(InsuranceCoverageType insuranceCoverageType){

        return insuranceCoverageTypeRepository.save(insuranceCoverageType);
    }
    public List<InsuranceCoverageType> findAllInsuranceCoverageType(){
        return insuranceCoverageTypeRepository.findAll();
    }
    public InsuranceCoverageType updateInsuranceCoverageType(InsuranceCoverageType insuranceCoverageType){
        return insuranceCoverageTypeRepository.save(insuranceCoverageType);
    }
    public InsuranceCoverageType findInsuranceCoverageTypeById(Long id){
        return insuranceCoverageTypeRepository.findInsuranceCoverageTypeById(id)
                .orElseThrow(()-> new UserNotFoundException("InsuranceCoverageType by id" + id + " was not found"));
    }
    public void deleteInsuranceCoverageType(Long id){
        insuranceCoverageTypeRepository.deleteById(id);
    }
}
