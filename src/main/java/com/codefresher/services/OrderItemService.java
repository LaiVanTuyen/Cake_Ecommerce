package com.codefresher.services;

import com.codefresher.dto.CheckOutDTO;
import com.codefresher.dto.OrderItemDTO;
import com.codefresher.entities.OrderItem;
import com.codefresher.entities.Product;
import com.codefresher.entities.ProductQuantity;
import com.codefresher.repositories.OrderItemRepository;
import com.codefresher.repositories.ProductQuantityRepository;
import com.codefresher.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderItemService {
    @Autowired
    OrderItemRepository repository;
    @Autowired
    ProductQuantityRepository proQtyRepo;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ModelMapper mapper;

    public List<OrderItemDTO> converToListItem(CheckOutDTO checkOutDTO, List<CheckOutDTO> checkOutDTOS){
        List<OrderItemDTO> orderItems = new ArrayList<>();
        if(checkOutDTO != null){
            orderItems.add(setInforToOrderItemDTO(checkOutDTO));
        }
        if(checkOutDTOS != null){
            checkOutDTOS.forEach(data -> {
                orderItems.add(setInforToOrderItemDTO(data));
            });
        }
        return orderItems;
    }

    public List<OrderItem> listDTOtoEntity(List<OrderItemDTO> orderItemDTOList){
        List<OrderItem> orderItems = new ArrayList<>();
        orderItemDTOList.forEach(data -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setColor(data.getColor());
            orderItem.setSize(data.getSize());
            orderItem.setPrice(data.getPrice());
            orderItem.setQuantity(data.getQuantity());
            orderItem.setQuantityItem(proQtyRepo.findProductQuantityByQuantityId(data.getQuantityId()));
            orderItems.add(orderItem);
        });
        return orderItems;
    }

    private OrderItemDTO setInforToOrderItemDTO(CheckOutDTO checkOutDTO){
        ProductQuantity productQuantity = proQtyRepo.findProductQuantityByQuantityId(checkOutDTO.getQuantityId());
        Product product = productQuantity.getProductQuantity();

        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setColor(productQuantity.getColor());


        if(product.getProductPromotion() == null){
            orderItemDTO.setPrice(product.getPrice());
        }
        else{
            orderItemDTO.setPrice(product.getPrice() - product.getPrice()*product.getProductPromotion().getDiscount()/100);
        }
        orderItemDTO.setSize(productQuantity.getSize());
        orderItemDTO.setQuantity(checkOutDTO.getQuantity());
        orderItemDTO.setProductId(product.getProductId());
        orderItemDTO.setAmountTotal(product.getPrice() * checkOutDTO.getQuantity());
        orderItemDTO.setImageName(product.getProductImages().get(0).getName());
        orderItemDTO.setProductName(product.getName() + ' ' + product.getCode());
        orderItemDTO.setQuantityId(checkOutDTO.getQuantityId());
        orderItemDTO.setRemainQty(productQuantity.getQuantity());
        return orderItemDTO;
    }
    // mapper
    private OrderItem toEntity(OrderItemDTO orderItemDTO){
        OrderItem orderItem = new OrderItem();
        return null;
    }
}
