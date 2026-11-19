package com.shopflow.inventory;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table (name = "inventory_items")
public class InventoryItem {
    @Id
    private String sku;

    @Column(nullable = false)
    private int available;

    @Column(nullable = false)
    private int reserved;

    @Column(nullable = false, name = "updated_at")
    private Instant updatedAt;

    @Version
    private long version;

    protected InventoryItem() {
    }

    public boolean canReserve(int quantity) {
        return available >= quantity;
    }

    public void reserve(int quantity) {
        if (!canReserve(quantity)) {
            throw new IllegalArgumentException("Not enough available inventory to reserve");
        }
        available -= quantity;
        reserved += quantity;
        updatedAt = Instant.now();
    }

    public void release(int quantity) {
        if (reserved < quantity) {
            throw new IllegalArgumentException("Not enough reserved inventory to release");
        }
        reserved -= quantity;
        available += quantity;
        updatedAt = Instant.now();
    }

    public void commit(int quantity) {
        if (reserved < quantity) {
            throw new IllegalArgumentException("Not enough reserved inventory to commit");
        }
        reserved -= quantity;
        updatedAt = Instant.now();
    }

    public void restock(int quantity) {
        available += quantity;
        updatedAt = Instant.now();
    }

    public String getSku() {
        return sku;
    }

    public int getQuantity() {
        return available + reserved;
    }

    public int getAvailable() {
        return available;
    }

    public int getReserved() {
        return reserved;
    }
    




}