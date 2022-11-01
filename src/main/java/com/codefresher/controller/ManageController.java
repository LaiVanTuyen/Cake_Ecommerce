package com.codefresher.controller;

import com.codefresher.services.OrderService;
import com.codefresher.services.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ManageController {

    @Autowired
    PromotionService service;
    @Autowired
    OrderService orderService;

    @GetMapping("/manage/promotion")
    public String getManagePromotion(Model model){
        model.addAttribute("promotionList", service.getAllPromotion());
        return "managepromotion";
    }

    @GetMapping("/manage")
    public String getManageHome(){
        return "managehome";
    }

    @GetMapping("/manage/product")
    public String getMangeProduct(){
        return "manageproduct";
    }

    @GetMapping("/manage/order")
    public String getManageOrder(Model model){
        model.addAttribute("orderList", orderService.getListOrderPending());
        return "manageorder";
    }
}
