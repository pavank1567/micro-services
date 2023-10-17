package com.stalwart.orderservice.service;

import com.stalwart.orderservice.dao.OrderRepo;
import com.stalwart.orderservice.dto.InventoryRequest;
import com.stalwart.orderservice.dto.InventoryResponse;
import com.stalwart.orderservice.dto.OrderLineItemsDTO;
import com.stalwart.orderservice.dto.OrderRequest;
import com.stalwart.orderservice.exceptions.ItemsNAException;
import com.stalwart.orderservice.model.Order;
import com.stalwart.orderservice.model.OrderLineItems;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepo orderRepo;

    private final WebClient.Builder webClientBuilder;

    private final String INVENTORY_URL = "http://inventory-service/api/inventory";

    private final String INVENTORY_POST_URL = "http://inventory-service/api/inventory/order";

    public OrderService(OrderRepo orderRepo, WebClient.Builder webClientBuilder) {
        this.orderRepo = orderRepo;
        this.webClientBuilder = webClientBuilder;
    }

    public void placeOrder(OrderRequest request) {
        List<OrderLineItems> orderLineItemsList =
                request.orderLineItemsDTOList()
                                .stream()
                                .map(this::mapToDTO)
                                .collect(Collectors.toList());
        Order newOrder = Order.builder()
                .orderLineItems(orderLineItemsList)
                .orderNumber(UUID.randomUUID())
                .build();


        //preparing list of skucodes
        List<String> skuCodes = orderLineItemsList.stream()
                .map(OrderLineItems::getSkuCode)
                .collect(Collectors.toList());

        //preparing list of inventoryRequests
        List<InventoryRequest> requests = orderLineItemsList.stream()
                .map(orderLineItems -> InventoryRequest.builder()
                        .skuCode(orderLineItems.getSkuCode())
                        .quantity(orderLineItems.getQuantity())
                        .build())
                .collect(Collectors.toList());

        //get request only using skucodes
        InventoryResponse[] inventoryRes =
                webClientBuilder.build().get()
                .uri(INVENTORY_URL,
                        uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes)
                                .build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        //post reqpuest using skucodes and quantity

        InventoryResponse[] invPostRes = webClientBuilder.build().post()
                .uri(INVENTORY_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requests)
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

//        boolean isAllInStock = Arrays.stream(inventoryRes).sequential().allMatch(InventoryResponse::isInStock);

        boolean isAllInStock = Arrays.stream(invPostRes).sequential().allMatch(InventoryResponse::isInStock);

        if(isAllInStock) {
            orderRepo.save(newOrder);
            webClientBuilder.build().post()
                    .uri(INVENTORY_POST_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requests)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();

        }
        else
            throw new ItemsNAException("Items out of stock");
    }

    private OrderLineItems mapToDTO(OrderLineItemsDTO orderLineItemsDTO) {
        return OrderLineItems.builder()
                .price(orderLineItemsDTO.getPrice())
                .skuCode(orderLineItemsDTO.getSkuCode())
                .quantity(orderLineItemsDTO.getQuantity())
                .build();
    }
}
