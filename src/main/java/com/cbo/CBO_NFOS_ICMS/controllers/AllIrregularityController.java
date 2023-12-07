package com.cbo.CBO_NFOS_ICMS.controllers;

import com.cbo.CBO_NFOS_ICMS.models.AllIrregularity;
import com.cbo.CBO_NFOS_ICMS.services.AllIrregularityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/allIrregularity")
public class AllIrregularityController {
    private final AllIrregularityService allIrregularityService;

    public AllIrregularityController(AllIrregularityService allIrregularityService) {
        this.allIrregularityService = allIrregularityService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<AllIrregularity>> getAllIrregularitys() {
        List<AllIrregularity> allIrregularities = allIrregularityService.findAllIrregularity();
        return new ResponseEntity<>(allIrregularities, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<AllIrregularity> getIrregularityId(@PathVariable("id") Long id) {
        AllIrregularity allIrregularity = allIrregularityService.findIrregularityById(id);
        return new ResponseEntity<>(allIrregularity, HttpStatus.OK);
    }

    @PostMapping("/getAllIrregularity")
    public List<AllIrregularity> getAllIrregularitiesByCategoryNameAndSubCategoryName(@RequestBody Map<String, String> requestBody) {
        String categoryName = requestBody.get("categoryName");
        String subCategoryName = requestBody.get("subCategoryName");
        return allIrregularityService.findAllIrregularityByCategoryNameAndSubCategoryName(categoryName, subCategoryName);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<AllIrregularity> addIrregularity(@RequestBody AllIrregularity allIrregularity) {
        AllIrregularity newAllIrregularity = allIrregularityService.addIrregularity(allIrregularity);
        return new ResponseEntity<>(newAllIrregularity, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<AllIrregularity> updateIrregularity(@RequestBody AllIrregularity allIrregularity) {
        AllIrregularity updateAllIrregularity = allIrregularityService.updateIrregularity(allIrregularity);
        return new ResponseEntity<>(updateAllIrregularity, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<?> deleteIrregularity(@PathVariable("id") Long id) {
        allIrregularityService.deleteIrregularity(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
