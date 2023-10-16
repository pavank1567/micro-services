package com.stalwart.orderservice.service;

import com.stalwart.orderservice.dao.OrderRepo;
import com.stalwart.orderservice.dto.OrderLineItemsDTO;
import com.stalwart.orderservice.dto.OrderRequest;
import com.stalwart.orderservice.model.Order;
import com.stalwart.orderservice.model.OrderLineItems;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepo orderRepo;

    public OrderService(OrderRepo orderRepo) {
        this.orderRepo = orderRepo;
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
        orderRepo.save(newOrder);
    }

    private OrderLineItems mapToDTO(OrderLineItemsDTO orderLineItemsDTO) {
        return OrderLineItems.builder()
                .price(orderLineItemsDTO.getPrice())
                .skuCode(orderLineItemsDTO.getSkuCode())
                .quantity(orderLineItemsDTO.getQuantity())
                .build();
    }
}
