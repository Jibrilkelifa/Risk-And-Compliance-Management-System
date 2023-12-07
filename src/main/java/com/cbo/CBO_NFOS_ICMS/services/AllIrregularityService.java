package com.cbo.CBO_NFOS_ICMS.services;

import com.cbo.CBO_NFOS_ICMS.exception.UserNotFoundException;
import com.cbo.CBO_NFOS_ICMS.models.AllIrregularity;
import com.cbo.CBO_NFOS_ICMS.repositories.AllIrregularityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllIrregularityService {
    private final AllIrregularityRepository allIrregularityRepository;

    public AllIrregularityService(AllIrregularityRepository allIrregularityRepository) {
        this.allIrregularityRepository = allIrregularityRepository;
    }

    public AllIrregularity addIrregularity(AllIrregularity allIrregularity) {

        return allIrregularityRepository.save(allIrregularity);
    }

    public List<AllIrregularity> findAllIrregularity() {
        return allIrregularityRepository.findAll();
    }

    public AllIrregularity updateIrregularity(AllIrregularity allIrregularity) {
        return allIrregularityRepository.save(allIrregularity);
    }

    public AllIrregularity findIrregularityById(Long id) {
        return allIrregularityRepository.findIrregularityById(id)
                .orElseThrow(() -> new UserNotFoundException("User by id" + id + " was not found"));
    }

    public List<AllIrregularity> findAllIrregularityByCategoryNameAndSubCategoryName(String categoryName, String subCategoryName) {
        List<AllIrregularity> irregularities = allIrregularityRepository.findAll();
        irregularities.removeIf(irregularity -> !irregularity.getAllSubCategory().getAllcategory().getName().equals(categoryName) || !irregularity.getAllSubCategory().getName().equals(subCategoryName));
        return irregularities;
    }

    public void deleteIrregularity(Long id) {
        allIrregularityRepository.deleteById(id);
    }
}


