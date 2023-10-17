package com.stalwart.inventoryservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Inventory {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "inventory_seq"
    )
    @SequenceGenerator(
            name = "inventory_seq",
            sequenceName = "inventory_sequence",
            allocationSize = 1
    )
    private Long id;

    private String skuCode;

    private Integer quantity;
}
