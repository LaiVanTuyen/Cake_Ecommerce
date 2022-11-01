package com.codefresher.services;

import com.codefresher.dto.ProductDTO;
import com.codefresher.dto.PromotionDTO;
import com.codefresher.entities.Product;
import com.codefresher.entities.Promotion;
import com.codefresher.repositories.ProductRepository;
import com.codefresher.repositories.PromotionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PromotionService {
    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private StorageFileService storageFileService;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ModelMapper mapper;


    public List<Promotion> findAll(){
        return promotionRepository.findTop3ByOrderByPromotionIdDesc();
    }

    // save promotion
    public void addPromotion(PromotionDTO promotionDTO){
        Promotion promotion = toEntity(promotionDTO);
        String url = storageFileService.storageFile(promotionDTO.getImage());
        promotion.setImgName(url);
        promotionRepository.save(promotion);
        deleteFirstPromotion();
    }

    // delete first promotion in database
    public void deleteFirstPromotion(){
        if (productRepository.findAll().size() > 3){
            Promotion promotion = promotionRepository.findTop1ByOrderByPromotionIdAsc();
            storageFileService.deleteFile(promotion.getImgName());
            promotionRepository.delete(promotion);
        }
    }

    // get a list of promotion;
    public List<Promotion> getAllPromotion(){
        return promotionRepository.findAll();
    }

    //get promoition by ID
    public PromotionDTO getPromotionById(Long promotionId){
        return toDTO(promotionRepository.findByPromotionId(promotionId));
    }

    // update promotion
    public void updatePromotion(PromotionDTO promotionDTO, Long promotionId){
        Promotion promotion = promotionRepository.findByPromotionId(promotionId);
        promotion.setTitle(promotionDTO.getTitle());
        promotion.setDescription(promotionDTO.getDescription());
        promotion.setDiscount(promotionDTO.getDiscount());
        if(promotionDTO.getImage().isEmpty()){
            promotionRepository.save(promotion);
        }
        else{
            storageFileService.deleteFile(promotion.getImgName());
            String url = storageFileService.storageFile(promotionDTO.getImage());
            promotion.setImgName(url);
            promotionRepository.save(promotion);
        }
    }

    //get product on promotion
    public List<ProductDTO> getProductList(Long promotionId){
        List<Product> products = promotionRepository.findByPromotionId(promotionId).getPromotionProducts();
        List<ProductDTO> res = new ArrayList<>();
        products.forEach(product -> {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductId(product.getProductId());
            productDTO.setImageName(product.getProductImages().get(0).getName());
//            productDTO.setName(product.getName() + " " + product.getCode().toUpperCase());
            productDTO.setName(product.getName());
            productDTO.setPrice(product.getPrice());
            productDTO.setDiscountPrice(product.getPrice() - product.getPrice()*product.getProductPromotion().getDiscount()/100);
            res.add(productDTO);
        });
        return res;
    }

    // model mapper
    public PromotionDTO toDTO(Promotion promotion){
        return mapper.map(promotion, PromotionDTO.class);
    }

    public Promotion toEntity(PromotionDTO promotionDTO){
        return mapper.map(promotionDTO, Promotion.class);
    }
}
