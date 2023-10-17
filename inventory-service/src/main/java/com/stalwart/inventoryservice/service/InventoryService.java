package com.stalwart.inventoryservice.service;

import com.stalwart.inventoryservice.Dao.InventoryRepository;
import com.stalwart.inventoryservice.dto.InventoryRequest;
import com.stalwart.inventoryservice.dto.InventoryResponse;
import com.stalwart.inventoryservice.model.Inventory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<InventoryResponse> isInStock(List<String> skuCode){
        return inventoryRepository.findBySkuCodeIn(skuCode)
                .stream()
                .map(inventory ->
                    InventoryResponse.builder()
                            .skuCode(inventory.getSkuCode())
                            .isInStock(inventory.getQuantity()>0)
                            .build()
                ).collect(Collectors.toList());
    }

    public List<InventoryResponse> areItemsAvailable(List<InventoryRequest> requests) {

        Map<String,Integer> inventoryMap = new HashMap<>();
        for(InventoryRequest item : requests){
            inventoryMap.put(item.getSkuCode(), item.getQuantity());
        }
        return inventoryRepository.findBySkuCodeIn(inventoryMap.keySet())
                .stream()
                .map(inventory ->
                        InventoryResponse.builder()
                                .skuCode(inventory.getSkuCode())
                                .isInStock(inventory.getQuantity() >=
                                        inventoryMap.getOrDefault(inventory.getSkuCode(),1))
                                .build()
                ).collect(Collectors.toList());
    }

    public void placeOrder(List<InventoryRequest> requests) {
        Map<String,Integer> inventoryMap = new HashMap<>();
        for(InventoryRequest item : requests){
            inventoryMap.put(item.getSkuCode(), item.getQuantity());
        }
        List<Inventory> updatedInventoryItems = inventoryRepository.findBySkuCodeIn(inventoryMap.keySet())
                .stream()
                .map(inventory -> Inventory.builder()
                        .id(inventory.getId())
                        .quantity(inventory.getQuantity() -
                                inventoryMap.getOrDefault(inventory.getSkuCode(),0)
                        ).skuCode(inventory.getSkuCode())
                        .build()
                ).collect(Collectors.toList());
        inventoryRepository.saveAll(updatedInventoryItems);

    }
}
