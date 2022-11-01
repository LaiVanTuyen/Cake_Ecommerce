package com.codefresher.services;

import com.codefresher.dto.CheckOutDTO;
import com.codefresher.dto.OrderItemDTO;
import com.codefresher.entities.Cart;
import com.codefresher.entities.Product;
import com.codefresher.entities.ProductQuantity;
import com.codefresher.entities.User;
import com.codefresher.repositories.CartRepository;
import com.codefresher.repositories.ProductQuantityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {
    @Autowired
    CartRepository repository;
    @Autowired
    UserService userService;
    @Autowired
    ProductQuantityRepository quantityRepository;
    @Autowired
    CalculateService calculateService;
    public void addToCart(CheckOutDTO checkOutDTO, User user){
        ProductQuantity productQuantity = quantityRepository.findProductQuantityByQuantityId(checkOutDTO.getQuantityId());
        Cart cart = new Cart();
        cart.setCartUser(user);
        cart.setQuantity(checkOutDTO.getQuantity());
        cart.setCartProductQuantity(productQuantity);
        repository.saveAndFlush(cart);
    }

    public void deleteCartItem(Long id){
        repository.deleteById(id);
    }

    public void updateCartItem(Long id, int number){
        Cart cart = repository.findCartByCartId(id);
        cart.setQuantity(cart.getQuantity() + number);
        repository.saveAndFlush(cart);
    }

    public Long countItem(String username){
        return repository.countByCartUserUsername(username);
    }

    public List<OrderItemDTO> getListOrderItem(String cookieData){
        List<OrderItemDTO> orderItemDTOList = new ArrayList<>();
        User user = userService.getUserByLogged();
        if(user.getUserId()!= null){
            List<Cart> carts = repository.findAllByCartUserUserId(user.getUserId());
            carts.forEach(data -> {
                OrderItemDTO orderItemDTO = new OrderItemDTO();
                orderItemDTO = getOrderItemDTO(data.getCartProductQuantity().getQuantityId(), data.getQuantity());
                orderItemDTO.setCartId(data.getCartId());
                orderItemDTOList.add(orderItemDTO);
            });
        }
        else{
            if(cookieData.length() > 0){
                for(String data : cookieData.trim().split("and")){
                    Long quantityId = Long.parseLong(data.split("-")[0]);
                    int quantity = Integer.parseInt(data.split("-")[1]);
                    OrderItemDTO orderItemDTO = new OrderItemDTO();
                    orderItemDTO = getOrderItemDTO(quantityId, quantity);
                    orderItemDTO.setCartId(-1L);
                    orderItemDTOList.add(orderItemDTO);
                }
            }
        }
        return orderItemDTOList;
    }

    public String getPayTotalCurr(List<OrderItemDTO> orderItemDTOList){
        int total = 0;
        for(OrderItemDTO data : orderItemDTOList){
            total += data.getPrice()*data.getQuantity();
        }

        return calculateService.numberToCurrency(total);
    }

    private OrderItemDTO getOrderItemDTO(Long quantityId, int quantity){
        OrderItemDTO orderItem = new OrderItemDTO();

        ProductQuantity productQuantity = quantityRepository.findProductQuantityByQuantityId(quantityId);
        Product product = productQuantity.getProductQuantity();

        orderItem.setQuantityId(quantityId);
        orderItem.setQuantity(quantity);
        orderItem.setSize(productQuantity.getSize());
        orderItem.setColor(productQuantity.getColor());
        orderItem.setRemainQty(productQuantity.getQuantity());
        orderItem.setPrice(product.getPrice());
        orderItem.setProductName(product.getName() + ' ' + product.getCode().toUpperCase());
        orderItem.setImageName(product.getProductImages().get(0).getName());

        orderItem.setTotalCurrency(calculateService.numberToCurrency(orderItem.getPrice()*orderItem.getQuantity()));

        return orderItem;
    }

}
