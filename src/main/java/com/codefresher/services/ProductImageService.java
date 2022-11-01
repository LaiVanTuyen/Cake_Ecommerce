package com.codefresher.services;

import com.codefresher.dto.ProductImageDTO;
import com.codefresher.entities.Product;
import com.codefresher.entities.ProductImage;
import com.codefresher.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductImageService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    StorageFileService storageFileService;
    // save product Image
    public void saveProductImage(ProductImageDTO productImageDTO){
        List<ProductImage> productImages = new ArrayList<>();
        int listImgSize = productImageDTO.getImageFile().length;
        Product product = productRepository.findProductByProductId(productImageDTO.getProductId());
        for(int i=0; i<listImgSize; i++){
            ProductImage productImage = new ProductImage();
            if(productImageDTO.getColors() != null){
                String color = productImageDTO.getColors().get(i);
                productImage.setColor(color);
            }
            MultipartFile file = productImageDTO.getImageFile()[i];
            String url = storageFileService.storageFile(file);
            productImage.setName(url);
            productImage.setProductImage(product);
            productImages.add(productImage);

        }
        product.setProductImages(productImages);
        productRepository.saveAndFlush(product);
    }
}
