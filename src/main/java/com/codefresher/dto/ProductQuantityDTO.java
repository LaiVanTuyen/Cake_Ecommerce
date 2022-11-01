package com.codefresher.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class ProductQuantityDTO {
    // for product
    private boolean noColor;
    private List<ProductColorQtyDTO> quantityByColor;
    private List<ProductSizeQtyDTO> quantityBySize;

    // for add product quantity
    private Long productId;
    private String size;
    private String color;
    private int quantity;
}
