package com.shopflow.catalog;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopflow.common.web.ApiException;


@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public ProductDto get(String sku) {
        ProductDto product = productRepository.findById(sku).map(Product::toDto)
                .orElseThrow(() -> ApiException.notFound("Product not found", sku));
        return product;
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getAll(List<String> skus) {
        return skus.stream().distinct().map(this::get).toList();
    }

    @Transactional(readOnly = true)
    public Page<ProductDto> list(int page, int size) {
        int safeSize = Math.min(Math.max(size, 1), 100);
        return productRepository.findAll(PageRequest.of(Math.max(page, 0), safeSize, Sort.by("sku"))).map(Product::toDto);
    }

    @Transactional
    public ProductDto changePrice(String sku, BigDecimal newPrice) {
        Product product = productRepository.findById(sku)
                .orElseThrow(() -> ApiException.notFound("Product not found", sku));
        try {
            product.changePrice(newPrice);
            productRepository.save(product);
        } catch (IllegalArgumentException e) {
            throw ApiException.badRequest("Invalid price", e.getMessage());
        }
        return product.toDto();
    }

}   
