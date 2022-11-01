package com.codefresher.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class OrderItemListDTO {
    private List<OrderItemDTO> orderItemDTOList = new ArrayList<>();

    public void addOrderItem(OrderItemDTO orderItemDTO){
        this.orderItemDTOList.add(orderItemDTO);
    }
}
