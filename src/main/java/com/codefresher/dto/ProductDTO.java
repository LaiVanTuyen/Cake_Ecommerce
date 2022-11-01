package com.codefresher.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class ProductDTO {
    private Long productId;
    private String name;
    private int price;
    private int discountPrice;
    private int discount;
    private String code;
    private boolean isPromote;
    private boolean noColor;
    private String imageName;
    private String priceCurr;
    private String disPriceCurr;
}
