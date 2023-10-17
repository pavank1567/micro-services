package com.stalwart.inventoryservice.controller;

import com.stalwart.inventoryservice.dto.InventoryRequest;
import com.stalwart.inventoryservice.dto.InventoryResponse;
import com.stalwart.inventoryservice.service.InventoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode){

        return inventoryService.isInStock(skuCode);
    }

    @PostMapping
    public List<InventoryResponse> areItemsAvailable(@RequestBody List<InventoryRequest> requests){
        return inventoryService.areItemsAvailable(requests);
    }

    @PostMapping("/order")
    public void placeOrder(@RequestBody List<InventoryRequest> requests){
        inventoryService.placeOrder(requests);
    }
}
