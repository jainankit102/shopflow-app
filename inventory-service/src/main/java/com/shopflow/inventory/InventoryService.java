package com.shopflow.inventory;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

    private final InventoryItemRepository inventoryItemRepository;
    private final ReservationRepository reservationRepository;

    public InventoryService(InventoryItemRepository inventoryItemRepository,
            ReservationRepository reservationRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
        this.reservationRepository = reservationRepository;
    }

    @Transactional(readOnly = true)
    public List<StockView> availability(List<String> skus) {
        Map<String, InventoryItem> itemFound = inventoryItemRepository.findAllById(skus).stream().collect(Collectors.toMap(InventoryItem::getSku, Function.identity()));
        
        return skus.stream().distinct().map(sku -> {
            InventoryItem item = itemFound.get(sku);
            if (item == null) {
                return new StockView(sku, 0, 0);
            }
            return new StockView(sku, item.getAvailable(), item.getReserved());
        }).toList();
    }

    @Transactional(readOnly = true)
    public StockView getInventory(String sku) {
        InventoryItem item = inventoryItemRepository.findById(sku).orElse(null);
        if (item == null) {
            return new StockView(sku, 0, 0);
        }
        return new StockView(sku, item.getAvailable(), item.getReserved());
    }

    public record StockView(String sku, int available, int reserved) {
    }
}
