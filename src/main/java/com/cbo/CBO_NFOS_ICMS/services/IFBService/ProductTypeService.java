package com.cbo.CBO_NFOS_ICMS.services.IFBService;

import com.cbo.CBO_NFOS_ICMS.exception.UserNotFoundException;
import com.cbo.CBO_NFOS_ICMS.models.IFB.ProductType;
import com.cbo.CBO_NFOS_ICMS.repositories.IFBRepository.ProductTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductTypeService {
    private final ProductTypeRepository productTypeRepository;

    public ProductTypeService(ProductTypeRepository productTypeRepository) {
        this.productTypeRepository = productTypeRepository;
    }

    public ProductType addProductType(ProductType productType) {
        return productTypeRepository.save(productType);
    }

    public List<ProductType> findAllProductType() {
        return productTypeRepository.findAll();
    }

    public ProductType updateProductType(ProductType productType) {
        return productTypeRepository.save(productType);
    }

    public ProductType findIFBById(Long id) {
        return productTypeRepository.findProductTypeById(id)
                .orElseThrow(() -> new UserNotFoundException("ProductType by id" + id + " was not found"));
    }

    public void deleteProductType(Long id) {
        productTypeRepository.deleteById(id);
    }

}
