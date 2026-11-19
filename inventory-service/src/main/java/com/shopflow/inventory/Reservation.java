package com.shopflow.inventory;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity 
@Table (name = "reservations")
public class Reservation {

    public enum Status { RESERVED, RELEASED, COMMITTED }

    @Id
    private UUID id;

    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private String sku;

    @Column (nullable = false, name = "created_at")
    private Instant createdAt;

    @Column (nullable = false, name = "updated_at")
    private Instant updatedAt;

    @Enumerated(EnumType.STRING)
    @Column (nullable = false)
    private Status status;

    protected Reservation() {
    }

    public Reservation(String orderId, String sku, int quantity) {
        this.id = UUID.randomUUID();
        this.orderId = orderId;
        this.sku = sku;
        this.quantity = quantity;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.status = Status.RESERVED;
    }

    public void markReleased() {
        if (status != Status.RESERVED) {
            throw new IllegalStateException("Reservation is not in RESERVED state");
        }
        status = Status.RELEASED;
        updatedAt = Instant.now();
    }

    public void markCommitted() {
        if (status != Status.RESERVED) {
            throw new IllegalStateException("Reservation is not in RESERVED state");
        }
        status = Status.COMMITTED;
        updatedAt = Instant.now();
    }

    public int getQuantity() {
        return quantity;
    }
    

}
