package com.cbo.CBO_NFOS_ICMS.controllers;


import com.cbo.CBO_NFOS_ICMS.models.AllSubCategory;
import com.cbo.CBO_NFOS_ICMS.services.AllSubCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/allSubCategory")
public class AllSubCategoryController {
    private final AllSubCategoryService allSubCategoryService;

    public AllSubCategoryController(AllSubCategoryService allSubCategoryService) {
        this.allSubCategoryService = allSubCategoryService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<AllSubCategory>> getAllSubCategorys() {
        List<AllSubCategory> allSubCategorys = allSubCategoryService.findAllSubCategory();
        return new ResponseEntity<>(allSubCategorys, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<AllSubCategory> getAllSubCategoryId(@PathVariable("id") Long id) {
        AllSubCategory allSubCategory = allSubCategoryService.findAllSubCategoryById(id);
        return new ResponseEntity<>(allSubCategory, HttpStatus.OK);
    }

    @PostMapping("/getAllSubCategory")
    public List<AllSubCategory> getAllSubCategoriesBySubModuleNameAndCategoryName(@RequestBody Map<String, String> requestBody) {
        String subModuleName = requestBody.get("subModuleName");
        String categoryName = requestBody.get("categoryName");
        System.out.println("subModuleName = " + subModuleName);
        System.out.println("categoryName = " + categoryName);
        return allSubCategoryService.findAllSubCategoryByCategoryName(subModuleName, categoryName);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<AllSubCategory> addAllSubCategory(@RequestBody AllSubCategory allSubCategory) {
        AllSubCategory newAllSubCategory = allSubCategoryService.addAllSubCategory(allSubCategory);
        return new ResponseEntity<>(newAllSubCategory, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<AllSubCategory> updateAllSubCategory(@RequestBody AllSubCategory allSubCategory) {
        AllSubCategory updateAllSubCategory = allSubCategoryService.updateAllSubCategory(allSubCategory);
        return new ResponseEntity<>(updateAllSubCategory, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<?> deleteAllSubCategory(@PathVariable("id") Long id) {
        allSubCategoryService.deleteAllSubCategory(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

