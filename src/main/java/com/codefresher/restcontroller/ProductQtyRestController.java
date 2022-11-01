package com.codefresher.restcontroller;

import com.codefresher.dto.ProductQuantityDTO;
import com.codefresher.entities.ProductQuantity;
import com.codefresher.services.ProductQuantityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ProductQtyRestController {
    @Autowired
    ProductQuantityService productQtyService;

    // get quantity of product
    @GetMapping("/product/{id}/quantity")
    public ProductQuantityDTO getProductQty(@PathVariable("id") Long productId){
        return productQtyService.getAllProductQty(productId);
    }

    // add product qty
    @PostMapping("/product/{id}/quantity")
    public void addProductQuantity(@PathVariable("id") Long productId, @RequestBody ProductQuantity quantity){
        productQtyService.addQuantity(productId, quantity);
    }

    @DeleteMapping("/product-quantity/{id}")
    public void removeProductQty(@PathVariable("id") Long id){
        productQtyService.removeProductQuantity(id);
    }

}
