package com.stalwart.inventoryservice.Dao;

import com.stalwart.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {
    List<Inventory> findBySkuCodeIn(List<String> skuCode);
    List<Inventory> findBySkuCodeIn(Set<String> skuCode);
}
