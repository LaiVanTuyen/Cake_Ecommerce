package com.codefresher.controller;

import com.codefresher.services.CalculateService;
import com.codefresher.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class OrderController {
    @Autowired
    OrderService service;
    @Autowired
    CalculateService calculateService;

}
