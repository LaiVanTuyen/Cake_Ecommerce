package com.codefresher.services;

import com.codefresher.dto.OrderItemDTO;
import com.codefresher.dto.ProductColorQtyDTO;
import com.codefresher.dto.ProductQuantityDTO;
import com.codefresher.dto.ProductSizeQtyDTO;
import com.codefresher.entities.Product;
import com.codefresher.entities.ProductImage;
import com.codefresher.entities.ProductQuantity;
import com.codefresher.repositories.ProductQuantityRepository;
import com.codefresher.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductQuantityService {
    @Autowired
    ModelMapper mapper;
    @Autowired
    ProductQuantityRepository repository;
    @Autowired
    ProductRepository productRepository;

    // save product qty
    public void saveProductQty(List<ProductQuantityDTO> listQty){
        Product product = productRepository.findProductByProductId(listQty.get(0).getProductId());
        List<ProductQuantity> productQuantities = new ArrayList<>();
        listQty.forEach(pq -> {
            ProductQuantity productQuantity = toEntity(pq);
            productQuantity.setQuantityId(null);
            productQuantity.setProductQuantity(product);

            productQuantities.add(productQuantity);
        });
        product.setProductQuantities(productQuantities);
        productRepository.saveAndFlush(product);
    }

    // add a quantity
    public void addQuantity(Long productId, ProductQuantity quantity){
        Product product = productRepository.findProductByProductId(productId);
        quantity.setProductQuantity(product);
        repository.saveAndFlush(quantity);
    }

    // get all quantity
    public ProductQuantityDTO getAllProductQty(Long productId){
        ProductQuantityDTO productQty = new ProductQuantityDTO();
        Product product = productRepository.findProductByProductId(productId);

        // check product has color
        boolean noColor = false;
        for(ProductQuantity data : product.getProductQuantities()){
            if(data.getColor() == null){
                noColor = true;
                break;
            }
        }
        // product has color
        if(noColor){
            productQty.setNoColor(true);
            List<ProductSizeQtyDTO> productSizeList = new ArrayList<>();
            for(ProductQuantity data: product.getProductQuantities()){
                productSizeList.add(new ProductSizeQtyDTO(data.getQuantityId(), data.getSize(), data.getQuantity()));
            }
            productQty.setQuantityBySize(productSizeList);
        }
        // product has not color
        else{
            productQty.setNoColor(false);
            List<ProductColorQtyDTO> productColorList = new ArrayList<>();

            Set<String> colorSet = new HashSet<>();
            for(ProductQuantity data: product.getProductQuantities()){
                colorSet.add(data.getColor());
            }
            for(String color: colorSet){
                // get color list
                ProductColorQtyDTO productColor = new ProductColorQtyDTO();
                List<ProductSizeQtyDTO> productSizeList = new ArrayList<>();
                for(ProductQuantity data: product.getProductQuantities()){
                    if(color.equals(data.getColor())){
                        productSizeList.add(new ProductSizeQtyDTO(data.getQuantityId(), data.getSize(), data.getQuantity()));
                    }
                    productColor.setColor(color);
                    productColor.setListSize(productSizeList);
                }
                // get image color link
                productColor.setImageName(product.getProductImages().get(0).getName());
                for(ProductImage data : product.getProductImages()){
                    if(data.getColor() != null){
                        if(data.getColor().equalsIgnoreCase(color)) {
                            productColor.setImageName(data.getName());
                        }
                    }
                }

                productColorList.add(productColor);
            }
            productQty.setQuantityByColor(productColorList);
        }


        return productQty;
    }

    // remove product quantity
    public void removeProductQuantity(Long id){
        repository.deleteById(id);
    }

    public List<String> listOfSize(Long productId){
        List<String> size = new ArrayList<>();
        for(ProductQuantity data: productRepository.findProductByProductId(productId).getProductQuantities()){
            if(!size.contains(data.getSize())){
                size.add(data.getSize());
            }
        }
        return size;
    }

    public void updateQuantity(List<OrderItemDTO> orderItemDTOList, String action){
        for(OrderItemDTO orderItemDTO : orderItemDTOList){
            ProductQuantity  productQuantity = repository.findProductQuantityByQuantityId(orderItemDTO.getQuantityId());
            if (action.equalsIgnoreCase("sub"))
                productQuantity.setQuantity(productQuantity.getQuantity() - orderItemDTO.getQuantity());
            else
                productQuantity.setQuantity(productQuantity.getQuantity() + orderItemDTO.getQuantity());

            repository.saveAndFlush(productQuantity);
        }
    }
    //mapper
    private ProductQuantityDTO toDTO(ProductQuantity productQuantity){
        return mapper.map(productQuantity, ProductQuantityDTO.class);
    }
    private ProductQuantity toEntity(ProductQuantityDTO productQuantityDTO){
        return mapper.map(productQuantityDTO, ProductQuantity.class);
    }
}
