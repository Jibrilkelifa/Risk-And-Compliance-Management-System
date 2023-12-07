package com.cbo.CBO_NFOS_ICMS.services.IFRService;

import com.cbo.CBO_NFOS_ICMS.exception.UserNotFoundException;
import com.cbo.CBO_NFOS_ICMS.models.IFR.FraudType;
import com.cbo.CBO_NFOS_ICMS.repositories.IFRRepository.FraudTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FraudTypeService {
    private final FraudTypeRepository fraudTypeRepository;

    public FraudTypeService(FraudTypeRepository fraudTypeRepository) {
        this.fraudTypeRepository = fraudTypeRepository;
    }

    public FraudType addFraudType(FraudType fraudType) {

        return fraudTypeRepository.save(fraudType);
    }

    public List<FraudType> findAllFraudType() {
        return fraudTypeRepository.findAll();
    }

    public FraudType updateFraudType(FraudType fraudType) {
        return fraudTypeRepository.save(fraudType);
    }

    public FraudType findFraudTypeById(Long id) {
        return fraudTypeRepository.findFraudTypeById(id)
                .orElseThrow(() -> new UserNotFoundException("User by id" + id + " was not found"));
    }

    public void deleteFraudType(Long id) {
        fraudTypeRepository.deleteById(id);
    }
}
