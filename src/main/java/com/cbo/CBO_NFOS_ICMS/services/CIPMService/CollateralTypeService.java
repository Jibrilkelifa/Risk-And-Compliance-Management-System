package com.cbo.CBO_NFOS_ICMS.services.CIPMService;

import com.cbo.CBO_NFOS_ICMS.exception.UserNotFoundException;
import com.cbo.CBO_NFOS_ICMS.models.CIPM.CollateralType;
import com.cbo.CBO_NFOS_ICMS.repositories.CIPMRepository.CollateralTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollateralTypeService {
    private final CollateralTypeRepository collateralTypeRepository;

    public CollateralTypeService(CollateralTypeRepository collateralTypeRepository) {
        this.collateralTypeRepository = collateralTypeRepository;
    }

    public CollateralType addCollateralType(CollateralType collateralType) {

        return collateralTypeRepository.save(collateralType);
    }

    public List<CollateralType> findAllCollateralType() {
        return collateralTypeRepository.findAll();
    }

    public CollateralType updateCollateralType(CollateralType collateralType) {
        return collateralTypeRepository.save(collateralType);
    }

    public CollateralType findCollateralTypeById(Long id) {
        return collateralTypeRepository.findCollateralTypeById(id)
                .orElseThrow(() -> new UserNotFoundException("CollateralType by id" + id + " was not found"));
    }

    public void deleteCollateralType(Long id) {
        collateralTypeRepository.deleteById(id);
    }

}
