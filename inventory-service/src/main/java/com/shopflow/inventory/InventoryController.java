package com.shopflow.inventory;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController 
@RequestMapping ("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/availability")
    public List<InventoryService.StockView> availability(@RequestParam List<String> skus) {
        // Implement the logic to check if the requested quantity of the product with the given SKU is available in inventory
        return inventoryService.availability(skus);
    }

    @GetMapping ("/sku")
    public InventoryService.StockView getInventory(@RequestParam String sku) {
        return inventoryService.getInventory(sku);
    }

}
