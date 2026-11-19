package com.shopflow.catalog;

import java.math.BigDecimal;

public record ProductDto(String sku, String name, String description, String category, BigDecimal price){
    
}