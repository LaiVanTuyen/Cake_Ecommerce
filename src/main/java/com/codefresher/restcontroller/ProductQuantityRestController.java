package com.codefresher.restcontroller;

import com.codefresher.dto.ProductQuantityDTO;
import com.codefresher.services.ProductQuantityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ProductQuantityRestController {
    @Autowired
    ProductQuantityService service;

    @PostMapping("/product-quantity")
    public void addProductQty(@RequestBody List<ProductQuantityDTO> listQty){
        service.saveProductQty(listQty);
    }
}
