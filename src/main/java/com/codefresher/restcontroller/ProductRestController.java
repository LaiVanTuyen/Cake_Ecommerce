package com.codefresher.restcontroller;

import com.codefresher.dto.ProductDTO;
import com.codefresher.entities.Product;
import com.codefresher.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ProductRestController {
    @Autowired
    ProductService service;

    // get list product
    @GetMapping("/products")
    public List<ProductDTO> getListProduct(){
        return service.getListDTO(service.getListProduct());
    }

    // remove product
    @DeleteMapping("/product/{id}")
    public void removeProduct(@PathVariable("id") Long id){
        service.removeProduct(id);
    }

    // search product
    @GetMapping("/products/search")
    public List<ProductDTO> searchProduct(@RequestParam("keyword") String keyword){
        return service.searchProduct(keyword);
    }

    // get list list product by category
    @GetMapping("/product/category/{cate}")
    public List<ProductDTO> getProductByCate(@PathVariable("cate") String cate){
        return service.getListProductByCate(cate);
    }

    @PutMapping("/product/{id}")
    public void updateProduct(@PathVariable("id") Long id, @RequestBody Product product){
        service.updateProduct(id, product);
    }

    // get product
    @GetMapping("/product/{productId}")
    public Product getProduct(@PathVariable("productId") Long id){
        Product product = service.getProductById(id);
        product.getProductQuantities().forEach(data -> {
            data.setProductQuantityCartList(null);
            data.setProductQuantity(null);
        });
        if(product.getProductPromotion() != null) {
            product.setProductPromotion(null);
        }
        product.getProductImages().forEach(data -> {
            data.setProductImage(null);
        });
        return product;
    }

    @GetMapping("/products-notdiscount")
    public List<ProductDTO> getProductNotDiscount(){
        return service.getProductsNotDiscount();
    }

    // add product
    @PostMapping(path = "/product", consumes = "application/json")
    public Long addProduct(@RequestBody ProductDTO productDTO){
        return service.addProduct(productDTO);
    }

    @PostMapping("/products-addpromotion")
    public void addPromotion(@RequestParam("listProductId") List<Long> productIdList){
        System.out.println(productIdList);
        service.addPromotionOfListProduct(productIdList);
    }
    @PostMapping("/products-removepromotion")
    public void removePromotion(@RequestParam("listProductId") List<Long> productIdList){
        service.removePromotionofListProduct(productIdList);
    }

    // ip: in promotion
    @GetMapping("/products-ip")
    public List<ProductDTO> searchPIPByName(@RequestParam("name") String name, @RequestParam("promotionId") Long id){
        return service.searchPIP(name, id);
    }

    @GetMapping("/products-nip")
    public List<ProductDTO> searchPNIPByName(@RequestParam("name") String name){
        return service.searchPNIP(name);
    }
}
