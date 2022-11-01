package com.codefresher.restcontroller;

import com.codefresher.dto.PromotionDTO;
import com.codefresher.services.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ManagePromotionRestController {

    @Autowired
    PromotionService promotionService;

    // add new promotion
    @PostMapping("/manage/promotion")
    public void addPromotion(@ModelAttribute PromotionDTO promotionDTO){
        promotionService.addPromotion(promotionDTO);
    }

    // update promotion
    @PutMapping("manage/promotion/{id}")
    public void updatePromotion(@ModelAttribute PromotionDTO promotionDTO, @PathVariable("id") Long promotionId){
        promotionService.updatePromotion(promotionDTO, promotionId);
    }
}
