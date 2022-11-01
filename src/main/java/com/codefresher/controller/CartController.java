package com.codefresher.controller;

import com.codefresher.dto.OrderItemDTO;
import com.codefresher.dto.OrderItemListDTO;
import com.codefresher.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CartController {
    @Autowired
    CartService cartService;

    @GetMapping("/cart")
    public String getCart(@CookieValue(value = "cartItem", defaultValue = "") String cartItem, Model model){
        List<OrderItemDTO> listItem = cartService.getListOrderItem(cartItem);
        OrderItemListDTO orderItemListDTO = new OrderItemListDTO();
        listItem.forEach(orderItemListDTO::addOrderItem);
        model.addAttribute("listOrderItem", orderItemListDTO);
        model.addAttribute("payTotal", cartService.getPayTotalCurr(listItem));
        return "cart";
    }
}
