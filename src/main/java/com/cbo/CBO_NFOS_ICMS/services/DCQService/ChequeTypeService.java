package com.cbo.CBO_NFOS_ICMS.services.DCQService;

import com.cbo.CBO_NFOS_ICMS.exception.UserNotFoundException;
import com.cbo.CBO_NFOS_ICMS.models.DCQ.ChequeType;
import com.cbo.CBO_NFOS_ICMS.repositories.DCQRepository.ChequeTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChequeTypeService {
    private final ChequeTypeRepository chequeTypeRepository;

    public ChequeTypeService(ChequeTypeRepository chequeTypeRepository) {
        this.chequeTypeRepository = chequeTypeRepository;
    }
    public ChequeType addChequeType(ChequeType ChequeType){

        return chequeTypeRepository.save(ChequeType);
    }
    public List<ChequeType> findAllChequeType(){
        return chequeTypeRepository.findAll();
    }
    public ChequeType updateChequeType(ChequeType ChequeType){
        return chequeTypeRepository.save(ChequeType);
    }
    public ChequeType findChequeTypeById(Long id){
        return chequeTypeRepository.findChequeTypeById(id)
                .orElseThrow(()-> new UserNotFoundException("User by id" + id + " was not found"));
    }
    public void deleteChequeType(Long id){
        chequeTypeRepository.deleteById(id);
    }

}
