package com.codefresher.controller;

import com.codefresher.entities.Product;
import com.codefresher.services.ProductService;
import com.codefresher.util.Paged;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CategoryController {

    @Autowired
    ProductService productService;
    
    @GetMapping("/GATEAUX-kem-tuoi")
    public String getCateAP(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                            @RequestParam(value = "sortBy", required = false, defaultValue = "0") int sortBy,
                            Model model){
        Paged<Product> productPaged = productService.cateProductGetPage(pageNumber, "GATEAUXkemtuoi");
        model.addAttribute("products", productPaged);
        model.addAttribute("productDTOs", productService.getListDTO(productPaged.getPage().toList()));
        model.addAttribute("title", "GATEAUX Kem Tươi");
        return "category";
    }

    @GetMapping("/GATEAUX-kem-bo")
    public String getCateAPL(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                             @RequestParam(value = "sortBy", required = false, defaultValue = "0") int sortBy,
                             Model model){
        Paged<Product> productPaged = productService.cateProductGetPage(pageNumber, "GATEAUXkembo");
        model.addAttribute("products", productPaged);
        model.addAttribute("productDTOs", productService.getListDTO(productPaged.getPage().toList()));
        model.addAttribute("title", "GATEAUX Kem bơ");

        return "category";
    }

    @GetMapping("/VALENTINE-trai-tim")
    public String getCateASM(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                             @RequestParam(value = "sortBy", required = false, defaultValue = "0") int sortBy,
                             Model model){
        Paged<Product> productPaged = productService.cateProductGetPage(pageNumber, "VALENTINEtraitim");
        model.addAttribute("products", productPaged);
        model.addAttribute("productDTOs", productService.getListDTO(productPaged.getPage().toList()));
        model.addAttribute("title", "Bánh VALENTINE Trái Tim");

        return "category";
    }

    @GetMapping("/banh-my")
    public String getCateQJ(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                            @RequestParam(value = "sortBy", required = false, defaultValue = "0") int sortBy,
                            Model model){
        Paged<Product> productPaged = productService.cateProductGetPage(pageNumber, "banhmy");
        model.addAttribute("products", productPaged);
        model.addAttribute("productDTOs", productService.getListDTO(productPaged.getPage().toList()));
        model.addAttribute("title", "Bánh Mỳ");

        return "category";
    }

    @GetMapping("/banh-man")
    public String getCateQA(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                            @RequestParam(value = "sortBy", required = false, defaultValue = "0") int sortBy,
                            Model model){
        Paged<Product> productPaged = productService.cateProductGetPage(pageNumber, "banhman");
        model.addAttribute("products", productPaged);
        model.addAttribute("productDTOs", productService.getListDTO(productPaged.getPage().toList()));
        model.addAttribute("title", "Bánh Mặn");

        return "category";
    }

//    @GetMapping("/quan-jogger")
//    public String getCateQJG(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
//                             @RequestParam(value = "sortBy", required = false, defaultValue = "0") int sortBy,
//                             Model model){
//        Paged<Product> productPaged = productService.cateProductGetPage(pageNumber, "QJG");
//        model.addAttribute("products", productPaged);
//        model.addAttribute("productDTOs", productService.getListDTO(productPaged.getPage().toList()));
//        model.addAttribute("title", "Quần jogger");
//
//        return "category";
//    }

    @GetMapping("/cookies")
    public String getCateGD(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                            @RequestParam(value = "sortBy", required = false, defaultValue = "0") int sortBy,
                            Model model){
        Paged<Product> productPaged = productService.cateProductGetPage(pageNumber, "cookies");
        model.addAttribute("products", productPaged);
        model.addAttribute("productDTOs", productService.getListDTO(productPaged.getPage().toList()));
        model.addAttribute("title", "COOKIES");

        return "category";
    }

    @GetMapping("/minicake")
    public String getCateTV(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
                            @RequestParam(value = "sortBy", required = false, defaultValue = "0") int sortBy,
                            Model model){
        Paged<Product> productPaged = productService.cateProductGetPage(pageNumber, "minicake");
        model.addAttribute("products", productPaged);
        model.addAttribute("productDTOs", productService.getListDTO(productPaged.getPage().toList()));
        model.addAttribute("title", "MINI CAKE");

        return "category";
    }

//    @GetMapping("/that-lung")
//    public String getCateTL(@RequestParam(value = "page", required = false, defaultValue = "1") int pageNumber,
//                            @RequestParam(value = "sortBy", required = false, defaultValue = "0") int sortBy,
//                            Model model){
//        Paged<Product> productPaged = productService.cateProductGetPage(pageNumber, "TLU");
//        model.addAttribute("products", productPaged);
//        model.addAttribute("productDTOs", productService.getListDTO(productPaged.getPage().toList()));
//        model.addAttribute("title", "Thắt lưng");
//
//        return "category";
//    }
}
