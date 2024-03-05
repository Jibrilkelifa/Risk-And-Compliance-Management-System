package com.cbo.CBO_NFOS_ICMS.controllers.IFBController;

import com.cbo.CBO_NFOS_ICMS.models.CIPM.CollateralType;
import com.cbo.CBO_NFOS_ICMS.models.IFB.ProductType;
import com.cbo.CBO_NFOS_ICMS.services.CIPMService.CollateralTypeService;
import com.cbo.CBO_NFOS_ICMS.services.IFBService.ProductTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ProductType")
public class ProductTypeController {
    private final ProductTypeService productTypeService;

    public ProductTypeController(ProductTypeService productTypeService) {
        this.productTypeService = productTypeService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProductType>> getAllProductTypes() {
        List<ProductType> productTypes = productTypeService.findAllProductType();
        return new ResponseEntity<>(productTypes, HttpStatus.OK);
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<ProductType> getProductTypeId(@PathVariable("id") Long id) {
        ProductType productType = productTypeService.findIFBById(id);
        return new ResponseEntity<>(productType, HttpStatus.OK);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<ProductType> addProductType(@RequestBody ProductType productType) {
        ProductType newProductType = productTypeService.addProductType(productType);
        return new ResponseEntity<>(newProductType, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<ProductType> updateProductType(@RequestBody ProductType productType) {
        ProductType updateProductType = productTypeService.updateProductType(productType);
        return new ResponseEntity<>(updateProductType, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ICMS_ADMIN')")
    public ResponseEntity<?> deleteProductType(@PathVariable("id") Long id) {
        productTypeService.deleteProductType(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
