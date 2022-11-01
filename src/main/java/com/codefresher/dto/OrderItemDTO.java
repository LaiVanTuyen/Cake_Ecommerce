package com.codefresher.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class OrderItemDTO {
    private Long quantityId;
    private String color;
    private String size;
    private String productName;
    private int price;
    private int quantity;
    private int amountTotal;
    private Long productId;
    private String imageName;
    private String totalCurrency;
    private Long cartId;
    private int remainQty;
}
