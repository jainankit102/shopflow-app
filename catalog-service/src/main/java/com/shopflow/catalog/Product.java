package com.shopflow.catalog;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "products")
public class Product {

    @Id
    private String sku;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /**
     * Optimistic locking: two concurrent updates cannot silently overwrite each
     * other.
     */
    @Version
    private Long version;

    protected Product() {
        // for JPA
    }

    public void changePrice(BigDecimal newPrice) {
        if(newPrice == null || newPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price must be non-negative");
        }
        this.price = newPrice;
        this.updatedAt = Instant.now();
    }

    public String getSku() {
        return sku;
    }

    public ProductDto toDto() {
        return new ProductDto(sku, name, description, category, price);
    }  



}
