package com.codefresher.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class ProductColorQtyDTO {
    private String color;
    private String imageName;
    private List<ProductSizeQtyDTO> listSize;
}

