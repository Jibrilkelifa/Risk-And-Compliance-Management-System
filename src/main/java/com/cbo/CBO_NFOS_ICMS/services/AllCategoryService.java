package com.cbo.CBO_NFOS_ICMS.services;

import com.cbo.CBO_NFOS_ICMS.exception.UserNotFoundException;
import com.cbo.CBO_NFOS_ICMS.models.AllCategory;
import com.cbo.CBO_NFOS_ICMS.repositories.AllCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllCategoryService {
    private final AllCategoryRepository allCategoryRepository;

    public AllCategoryService(AllCategoryRepository allCategoryRepository) {
        this.allCategoryRepository = allCategoryRepository;
    }

    public AllCategory addAllCategory(AllCategory allCategory) {

        return allCategoryRepository.save(allCategory);
    }

    public List<AllCategory> findAllAllCategory() {
        return allCategoryRepository.findAll();
    }

    public List<AllCategory> findAllCategoryBySubModuleName(String name) {
        List<AllCategory> allCategories = allCategoryRepository.findAll();
        allCategories.removeIf(allCategory -> !allCategory.getSubModule().getCode().equals(name));
        return allCategories;
    }

    public AllCategory updateAllCategory(AllCategory allCategory) {
        return allCategoryRepository.save(allCategory);
    }

    public AllCategory findAllCategoryById(Long id) {
        return allCategoryRepository.findAllCategoryById(id)
                .orElseThrow(() -> new UserNotFoundException("User by id" + id + " was not found"));
    }

    public void deleteAllCategory(Long id) {
        allCategoryRepository.deleteById(id);
    }

}

