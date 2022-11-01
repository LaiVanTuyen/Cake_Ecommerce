package com.codefresher.restcontroller;

import com.codefresher.dto.CheckOutDTO;
import com.codefresher.entities.User;
import com.codefresher.services.CartService;
import com.codefresher.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1")
public class CartRestController {
    @Autowired
    CartService cartService;
    @Autowired
    UserService userService;

    @PostMapping("/cart")
    public String addToCart(@RequestBody CheckOutDTO checkOutDTO, HttpServletResponse response){
        User user = userService.getUserByLogged();
        if(user.getUserId() != null){
            cartService.addToCart(checkOutDTO, user);
        }
        else{
            return "setcookie";
        }
        return "";
    }

    @GetMapping("/cart/{username}/quantity")
    public Long getCartQuantity(@PathVariable("username") String username){
        return cartService.countItem(username);
    }

    @PutMapping("/cart/{id}")
    public void updateQuantity(@PathVariable("id") Long id,
                               @RequestParam("quantity") int number){
        cartService.updateCartItem(id, number);
    }

    @DeleteMapping("/cart/{id}")
    public void deleteCartItem(@PathVariable("id") Long id){
        cartService.deleteCartItem(id);
    }
}
