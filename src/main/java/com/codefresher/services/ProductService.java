package com.codefresher.services;

import com.codefresher.dto.ProductDTO;
import com.codefresher.entities.Product;
import com.codefresher.entities.Promotion;
import com.codefresher.repositories.ProductRepository;
import com.codefresher.repositories.PromotionRepository;
import com.codefresher.util.Paged;
import com.codefresher.util.Paging;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private CalculateService calculateService;
    @Autowired
    private ModelMapper mapper;

    // add product
    public Long addProduct(ProductDTO productDTO){
        Product product = toEntity(productDTO);
        productRepository.save(product);
        Long productId = product.getProductId();
        product.setCode(product.getCode() + productId);
        productRepository.saveAndFlush(product);
        return productId;
    }

    public void removeProduct(Long id){
        productRepository.deleteById(id);
    }

    public void updateProduct(Long productId, Product data){
        Product product = productRepository.findProductByProductId(productId);
        product.setName(data.getName());
        product.setPrice(data.getPrice());
        data.getProductQuantities().forEach(quantity -> {
            quantity.setProductQuantity(product);
        });
        product.setProductQuantities(data.getProductQuantities());
        productRepository.saveAndFlush(product);
    }

    public List<ProductDTO> getNewestProduct(){
        List<Product> productList = productRepository.findTop8ByProductPromotionIsNullOrderByProductIdDesc();
        List<ProductDTO> productDTOS = listEntityToListDTO(productList);
        return productDTOS;
    }

    public List<Product> getListProduct(){
        return productRepository.findAll();
    }

    public List<ProductDTO> getSaleProduct(){
        List<Product> productList = productRepository.findTop8ByProductPromotionNotNull();
        List<ProductDTO> productDTOS = listEntityToListDTO(productList);
        return productDTOS;
    }

    public Product getProductById(Long productId){
        return productRepository.findProductByProductId(productId);
    }

    public ProductDTO getProductDTOById(Long productId){
        Product product = productRepository.findProductByProductId(productId);
        ProductDTO productDTO = toDTO(product);

        // set price discount
        if(product.getProductPromotion() == null){
            productDTO.setPromote(false);
        }
        else{
            productDTO.setPromote(true);
            productDTO.setDiscount(product.getProductPromotion().getDiscount());
            productDTO.setDiscountPrice(product.getPrice() - product.getPrice()*product.getProductPromotion().getDiscount()/100);
        }

        // set is no color;
        product.getProductQuantities().forEach(data -> {
            if(data.getColor() == null){
                productDTO.setNoColor(true);
                return;
            }
        });
        return productDTO;
    }

    // get list of product not in promotion
    public List<ProductDTO> getProductsNotDiscount(){
        return convertListPNIPtoProductDTO(productRepository.findAllByProductPromotionNull());
    }

    public List<ProductDTO> getListProductByCate(String cate){
        if(cate.equalsIgnoreCase("ALL")){
            cate = "%";
        }
        else{
            cate += "%";
        }
        return getListDTO(productRepository.findAllByCodeLike(cate));
    }

    // remove promotion
    public void removePromotionofListProduct(List<Long> listProductId){
        for(Long productId: listProductId){
            Product product = productRepository.findProductByProductId(productId);
            product.setProductPromotion(null);
            productRepository.save(product);
        }
    }

    public void addPromotionOfListProduct(List<Long> listProductId){
        // last index is promotionId;
        Promotion promotion = promotionRepository.findByPromotionId(listProductId.get(listProductId.size()-1));
        for(int i=0; i<listProductId.size()-1; i++){
            Product product = productRepository.findProductByProductId(listProductId.get(i));
            product.setProductPromotion(promotion);
            productRepository.saveAndFlush(product);
        }
    }

    //search
    public List<ProductDTO> searchProduct(String keyword){
        return getListDTO(productRepository.findAllByNameContainingOrCodeContaining(keyword, keyword));
    }
    public List<ProductDTO> searchPIP(String name, Long promotionId){
        return convertListPIPtoProductDTO(productRepository.getProductIPByName(name, promotionId));
    }
    public List<ProductDTO> searchPNIP(String name){
        return convertListPNIPtoProductDTO(productRepository.getProductNIPByName(name));
    }

    // search by name
    // paging
    public Paged<Product> searchProductGetPage(int pageNumber, String keyword, int sortBy){
        // product each page
        int size = 2;
        PageRequest pageRequest = PageRequest.of(pageNumber-1, size, Sort.by("productId").descending());
        Page<Product> productPage = productRepository.findAllByNameContaining(keyword, pageRequest);
        return new Paged<>(productPage, Paging.of(productPage.getTotalPages(), pageNumber, size));
    }

    public Paged<Product> cateProductGetPage(int pageNumber, String code){
        int size = 4;
        PageRequest pageRequest = PageRequest.of(pageNumber-1, size, Sort.by("productId").ascending());
        code += "%";
        Page<Product> productPage = productRepository.findAllByCodeLike(code, pageRequest);
        System.out.println(productPage.getTotalElements());
        return new Paged<>(productPage, Paging.of(productPage.getTotalPages(), pageNumber, size));
    }

    public Paged<Product> getPageProductByPromotion(int pageNumber, Long promotionId){
        int size = 4;
        PageRequest pageRequest = PageRequest.of(pageNumber-1, size, Sort.by("productId").ascending());

        Page<Product> productPage = productRepository.findAllByProductPromotionPromotionId(promotionId, pageRequest);
        System.out.println(productPage.getTotalElements());
        return new Paged<>(productPage, Paging.of(productPage.getTotalPages(), pageNumber, size));
    }

    public List<ProductDTO> getListDTO(List<Product> products){
        List<ProductDTO> productDTOList = new ArrayList<>();
        for (Product product : products){
            ProductDTO productDTO = toDTO(product);
            productDTO.setImageName(product.getProductImages().get(0).getName());

            if(product.getProductPromotion() != null){
                productDTO.setDiscount(product.getProductPromotion().getDiscount());
                int discountPrice = product.getPrice() - product.getPrice()*product.getProductPromotion().getDiscount()/100;
                productDTO.setDisPriceCurr(calculateService.numberToCurrency(discountPrice));
            }

//            productDTO.setName(product.getName() + ' ' + product.getCode());
            productDTO.setName(product.getName());
            productDTO.setPriceCurr(calculateService.numberToCurrency(product.getPrice()));
            productDTO.setPromote(product.getProductPromotion() != null);

            productDTOList.add(productDTO);

        }
        return productDTOList;
    }

    //commom
    public List<ProductDTO> convertListPNIPtoProductDTO(List<Product> products){
        List<ProductDTO> res = new ArrayList<>();
        products.forEach(product -> {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductId(product.getProductId());
            productDTO.setImageName(product.getProductImages().get(0).getName());
            productDTO.setName(product.getName() + " " + product.getCode().toUpperCase());
            productDTO.setPrice(product.getPrice());
            res.add(productDTO);
        });
        return res;
    }

    public List<ProductDTO> convertListPIPtoProductDTO(List<Product> products){
        List<ProductDTO> res = new ArrayList<>();
        products.forEach(product -> {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setName(product.getName() + " " + product.getCode());
            productDTO.setImageName(product.getProductImages().get(0).getName());
            productDTO.setProductId(product.getProductId());
            productDTO.setPrice(product.getPrice());
            productDTO.setDiscountPrice(product.getPrice() - product.getPrice()*product.getProductPromotion().getDiscount()/100);
            res.add(productDTO);
        });
        return res;
    }

    //mapper
    public ProductDTO toDTO(Product product){
        return mapper.map(product, ProductDTO.class);
    }

    public Product toEntity(ProductDTO productDTO){
        return mapper.map(productDTO, Product.class);
    }

    public List<ProductDTO>  listEntityToListDTO(List<Product> productList){
        List<ProductDTO> productDTOS = new ArrayList<>();
        productList.forEach(product -> {
            ProductDTO productDTO = toDTO(product);
            if (product.getProductImages().size() > 0){
                productDTO.setImageName(product.getProductImages().get(0).getName());
            }
            if (product.getProductPromotion() != null){
                int discountPrice = product.getPrice() - product.getPrice()*product.getProductPromotion().getDiscount()/100;
                productDTO.setDisPriceCurr(calculateService.numberToCurrency(discountPrice));
            }
            productDTO.setPriceCurr(calculateService.numberToCurrency(product.getPrice()));
            productDTOS.add(productDTO);
        });
        return productDTOS;
    }
}
