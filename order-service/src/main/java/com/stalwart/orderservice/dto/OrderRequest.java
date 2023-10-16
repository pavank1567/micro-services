package com.stalwart.orderservice.dto;

import java.util.List;

public record OrderRequest(
        List<OrderLineItemsDTO> orderLineItemsDTOList
) {
}
