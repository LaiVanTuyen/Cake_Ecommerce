package com.codefresher.controller;

import com.codefresher.dto.OrderItemDTO;
import com.codefresher.entities.User;
import com.codefresher.entities.UserAddress;
import com.codefresher.services.CalculateService;
import com.codefresher.services.OrderService;
import com.codefresher.services.UserService;
import com.codefresher.validator.AccountValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    OrderService orderService;
    @Autowired
    CalculateService calculateService;
    @Autowired
    AccountValidator accountValidator;


    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/signup")
    public String signup(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("address", new UserAddress());
        return "signup";
    }

    @PostMapping("/signup")
    public String registerSuccess(@Valid @ModelAttribute("user") User user, BindingResult result,
                                  @ModelAttribute("address") UserAddress userAddress){
        accountValidator.validate(user, result);
        if(result.hasErrors()){
            return "signup";
        }
        userService.saveUser(user, userAddress);
        return "redirect:/login";
    }


    @GetMapping("/user/profile")
    public String getUserHome(Model model){
        String username = userService.getUserLogged().getName();
        User user = userService.findUserByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("orderList", orderService.getListOrderByUserId(user.getUserId()));
        return "profile";
    }
    @PostMapping("/updateInforUser")
    public String updateInforUser(){

        return "redirect:/user/profile";
    }

    @GetMapping("/user/order/{orderId}")
    public String getOrderItem(@PathVariable("orderId") Long orderId, Model model){
        List<OrderItemDTO> orderItemDTOList = orderService.getListOrderItemByOrderId(orderId);
        int paytotal = 0;
        for(OrderItemDTO data : orderItemDTOList){
            paytotal += (data.getPrice()*data.getQuantity());
        }
        model.addAttribute("items", orderItemDTOList);
        model.addAttribute("total", calculateService.numberToCurrency(paytotal));
        model.addAttribute("orderId", orderId);
        return "orderitem";
    }
}
