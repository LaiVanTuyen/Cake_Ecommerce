package com.codefresher.restcontroller;

import com.codefresher.dto.ProductImageDTO;
import com.codefresher.services.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ProductImageRestController {
    @Autowired
    ProductImageService service;
    @PostMapping("/product-image")
    public void addProductImage(@ModelAttribute ProductImageDTO productImage){
        service.saveProductImage(productImage);
    }
}
