package com.codefresher.controller;

import com.codefresher.dto.CheckOutDTO;
import com.codefresher.services.ProductQuantityService;
import com.codefresher.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProductController {
    @Autowired
    ProductService productService;
    @Autowired
    ProductQuantityService productQuantityService;

    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable("id") Long productId, Model model){
        model.addAttribute("product", productService.getProductById(productId));
        model.addAttribute("productDTO", productService.getProductDTOById(productId));
        model.addAttribute("productQtyDTO", productQuantityService.getAllProductQty(productId));
        model.addAttribute("listSize", productQuantityService.listOfSize(productId));
        model.addAttribute("saleProductList", productService.getSaleProduct());
        model.addAttribute("quantity", new CheckOutDTO());
        return "product";
    }
}
