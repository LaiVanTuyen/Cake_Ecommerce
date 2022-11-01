package com.codefresher.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class ProductSizeQtyDTO {
    private Long quantityId;
    private String size;
    private int quantity;
}
