package com.codefresher.controller;

import com.codefresher.config.AESConfig;
import com.codefresher.entities.Product;
import com.codefresher.services.ProductService;
import com.codefresher.services.PromotionService;
import com.codefresher.services.UserService;
import com.codefresher.util.Paged;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    @Autowired
    PromotionService promotionService;
    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;

    @GetMapping(value = {"/", "/home"})
    public String index(Model model){
        model.addAttribute("promotionList", promotionService.findAll());
        model.addAttribute("newestProductList", productService.getNewestProduct());
        model.addAttribute("saleProductList", productService.getSaleProduct());

//        System.out.println( AESConfig.encrypt("jdbc:mysql://localhost:3306/apicake?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true","Tuyen24032001"));
//        System.out.println( AESConfig.encrypt("root","Tuyen24032001"));
//        System.out.println( AESConfig.encrypt("240301","Tuyen24032001"));
        return "index";
    }

    @GetMapping("/reset")
    public String forgetPass(){
        return "forgetpassword";
    }

    @GetMapping("/search")
    public String search(@RequestParam("q") String keyword,
                         @RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                         @RequestParam(value = "sortBy", required = false, defaultValue = "0") int sortBy,
                         Model model){
//        try {
//            keyword = URLDecoder.decode(keyword, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        Paged<Product> productPaged = productService.searchProductGetPage(pageNumber, keyword, sortBy);
        model.addAttribute("products", productPaged);
        model.addAttribute("productDTOs", productService.getListDTO(productPaged.getPage().toList()));
        model.addAttribute("keyword", keyword);
        return "search";
    }

    @GetMapping("/promotion/{id}")
    public String getPromotion( @PathVariable("id") Long promotionId,
                                @RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                                @RequestParam(value = "sortBy", required = false, defaultValue = "0") int sortBy,
                                Model model){


        Paged<Product> productPaged = productService.getPageProductByPromotion(pageNumber, promotionId);
        model.addAttribute("products", productPaged);
        model.addAttribute("productDTOs", productService.getListDTO(productPaged.getPage().toList()));
        model.addAttribute("promotion", promotionService.getPromotionById(promotionId));
        return "promotion";
    }



}
