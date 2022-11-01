package com.codefresher.restcontroller;

import com.codefresher.dto.OrderDTO;
import com.codefresher.dto.OrderItemDTO;
import com.codefresher.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class OrderRestController {
    @Autowired
    OrderService orderService;


    @GetMapping("/order-pending")
    public List<OrderDTO> getListOrderPending(){
        return orderService.getListOrderPending();
    }

    @PostMapping("/order/{orderId}")
    public void updateOrder(@PathVariable("orderId") Long orderId, @RequestParam("action") int action){
        orderService.updateOrderStatus(orderId, action);
    }

    @GetMapping("/order/{orderId}")
    public OrderDTO getListOrder(@PathVariable("orderId") Long id){
        return orderService.getOrderDTOById(id);
    }
    @GetMapping("/order/{orderId}/items")
    public List<OrderItemDTO> getListOrderItem(@PathVariable("orderId") Long id){
        return orderService.getListOrderItemByOrderId(id);
    }
}
