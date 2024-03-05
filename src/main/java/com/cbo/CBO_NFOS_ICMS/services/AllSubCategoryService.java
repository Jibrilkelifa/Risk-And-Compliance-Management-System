package com.cbo.CBO_NFOS_ICMS.services;

import com.cbo.CBO_NFOS_ICMS.exception.UserNotFoundException;
import com.cbo.CBO_NFOS_ICMS.models.AllSubCategory;
import com.cbo.CBO_NFOS_ICMS.repositories.AllSubCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllSubCategoryService {
    private final AllSubCategoryRepository allSubCategoryRepository;

    public AllSubCategoryService(AllSubCategoryRepository allSubCategoryRepository) {
        this.allSubCategoryRepository = allSubCategoryRepository;
    }

    public AllSubCategory addAllSubCategory(AllSubCategory allSubCategory) {

        return allSubCategoryRepository.save(allSubCategory);
    }

    public List<AllSubCategory> findAllSubCategory() {
        return allSubCategoryRepository.findAll();
    }

    public AllSubCategory updateAllSubCategory(AllSubCategory allSubCategory) {
        return allSubCategoryRepository.save(allSubCategory);
    }

    public AllSubCategory findAllSubCategoryById(Long id) {
        return allSubCategoryRepository.findAllSubCategoryById(id)
                .orElseThrow(() -> new UserNotFoundException("allSubCategory by id = " + id + " was not found"));
    }

    public List<AllSubCategory> findAllSubCategoryByCategoryName(String subModuleName, String categoryName) {
        List<AllSubCategory> subCategories = allSubCategoryRepository.findAll();
        subCategories.removeIf(subCategory -> !subCategory.getAllcategory().getName().equals(categoryName) || !subCategory.getAllcategory().getSubModule().getCode().equals(subModuleName));
        return subCategories;
    }

    public void deleteAllSubCategory(Long id) {
        allSubCategoryRepository.deleteById(id);
    }

}
