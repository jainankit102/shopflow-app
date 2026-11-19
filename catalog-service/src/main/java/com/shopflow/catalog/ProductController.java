package com.shopflow.catalog;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Page<ProductDto> list(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return productService.list(page, size);
    }   

    @GetMapping("/{sku}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable String sku) {
        ProductDto product = productService.get(sku);
        return ResponseEntity.ok(product);
    }

     public record PriceChange(@NotNull @DecimalMin(value = "0.01") BigDecimal price) {
    }

    @PutMapping("/{sku}/changePrice")
    public ProductDto changePrice(@PathVariable String sku, @RequestBody PriceChange body) {
        return productService.changePrice(sku, body.price());
        // return ResponseEntity.ok(updatedProduct);
    }

}
