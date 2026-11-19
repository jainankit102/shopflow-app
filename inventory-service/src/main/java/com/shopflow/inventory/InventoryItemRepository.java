package com.shopflow.inventory;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

public interface InventoryItemRepository extends JpaRepository<InventoryItem, String> {

     /**
     * SELECT ... FOR UPDATE: locks the rows until the transaction ends, so two orders for the last
     * unit cannot both succeed (no overselling).
     * Rows are locked in a fixed order (by sku) so two transactions never wait on each other in a
     * cycle - that is how you avoid deadlocks.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from InventoryItem i where i.sku in :skus order by i.sku")
    List<InventoryItem> lockAllBySku(@Param("skus") Collection<String> skus);
}
