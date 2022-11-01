package com.codefresher.restcontroller;

import com.codefresher.dto.ProductDTO;
import com.codefresher.dto.PromotionDTO;
import com.codefresher.services.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PromotionRestController {
    @Autowired
    PromotionService service;

    @GetMapping("/promotion/{id}")
    public PromotionDTO getPromotion(@PathVariable("id") Long promotionId){
        return service.getPromotionById(promotionId);
    }



    @GetMapping("/promotion/{id}/products")
    public List<ProductDTO> getProductList(@PathVariable("id") Long promotionId){
        return service.getProductList(promotionId);
    }

}
