package com.cbo.CBO_NFOS_ICMS.controllers;


import com.cbo.CBO_NFOS_ICMS.models.AllCategory;
import com.cbo.CBO_NFOS_ICMS.services.AllCategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/allCategory")
public class AllCategoryController {
    private final AllCategoryService allCategoryService;

    public AllCategoryController(AllCategoryService allCategoryService) {
        this.allCategoryService = allCategoryService;
    }
    @GetMapping("/find/{id}")
    public ResponseEntity<AllCategory> getAllCategoryId (@PathVariable("id") Long id) {
        AllCategory allCategory= allCategoryService.findAllCategoryById(id);
        return new ResponseEntity<>(allCategory, HttpStatus.OK);
    }
    @PostMapping("/getAllCategory")
    public List<AllCategory> getAllCategoriesBySubModuleName(@RequestBody Map<String, String> requestBody) {
        String subModuleName = requestBody.get("subModuleName");
        return allCategoryService.findAllCategoryBySubModuleName(subModuleName);
    }
    @PostMapping("/add")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<AllCategory> addAllCategory(@RequestBody AllCategory allCategory) {
        AllCategory newAllCategory =allCategoryService.addAllCategory(allCategory);
        return new ResponseEntity<>(newAllCategory, HttpStatus.CREATED);
    }
    @PutMapping("/update")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<AllCategory> updateAllCategory(@RequestBody AllCategory allCategory) {
        AllCategory updateAllCategory =allCategoryService.updateAllCategory(allCategory);
        return new ResponseEntity<>(updateAllCategory, HttpStatus.CREATED);
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<?> deleteAllCategory(@PathVariable("id") Long id) {
        allCategoryService.deleteAllCategory(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

