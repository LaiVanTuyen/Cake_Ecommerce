package com.codefresher.controller;

import com.codefresher.dto.CheckOutDTO;
import com.codefresher.dto.OrderItemListDTO;
import com.codefresher.entities.Order;
import com.codefresher.services.OrderItemService;
import com.codefresher.services.OrderService;
import com.codefresher.services.ProductQuantityService;
import com.codefresher.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CheckOutController {
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    UserService userService;
    @Autowired
    OrderService service;
    @Autowired
    ProductQuantityService productQuantityService;

    @PostMapping("/product/checkout")
    public String productCheckout(@ModelAttribute("quantity") CheckOutDTO checkOutDTO, RedirectAttributes attributes){
        attributes.addFlashAttribute("productCheckout", checkOutDTO);
        return "redirect:/checkout";
    }

    @PostMapping("/cart/checkout")
    public String cartCheckout(@ModelAttribute("quantity") OrderItemListDTO listOrderItemDTO, RedirectAttributes attributes){
        List<CheckOutDTO> checkOutDTOList = service.orderItemToCheckOut(listOrderItemDTO.getOrderItemDTOList());
        attributes.addFlashAttribute("cartCheckout", checkOutDTOList);

        return "redirect:/checkout";
    }

    @GetMapping("/checkout")
    public String checkout(Model model ){
        CheckOutDTO checkOutDTO = (CheckOutDTO) model.getAttribute("productCheckout");
        List<CheckOutDTO> checkOutDTOS = (List<CheckOutDTO>) model.getAttribute("cartCheckout");

        OrderItemListDTO orderItems = new OrderItemListDTO();
        orderItemService.converToListItem(checkOutDTO, checkOutDTOS).forEach(orderItems::addOrderItem);

        model.addAttribute("orderItemList", orderItems);
        model.addAttribute("user", userService.getUserByLogged());
        model.addAttribute("order", new Order());
        return "checkout";
    }

    @PostMapping("/checkout")
    public String saveOrder(@ModelAttribute("orderItemList")  OrderItemListDTO orderItems,
                            @ModelAttribute("order") Order order){
        service.saveOrder(orderItems.getOrderItemDTOList(), order);
        productQuantityService.updateQuantity(orderItems.getOrderItemDTOList(), "sub");
        return "ordersuccess";
    }
}
