package com.codefresher.restcontroller;

import com.codefresher.dto.UserAddressDTO;
import com.codefresher.dto.UserDTO;
import com.codefresher.services.OrderService;
import com.codefresher.services.UserAddressService;
import com.codefresher.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserRestController {

    @Autowired
    UserService userService;
    @Autowired
    UserAddressService addressService;
    @Autowired
    OrderService orderService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/user/{id}/infor")
    public UserDTO getUser(@PathVariable("id") Long id){
        return userService.getUserInfor(id);
    }

    @PostMapping("/user/infor/{id}")
    public void updateUserInfor(@RequestBody UserDTO userDTO){
        userService.updateUserInfor(userDTO);
    }

    @GetMapping("/user/checkpass")
    public boolean checkPassword(@RequestParam Long id, @RequestParam String password){
        return userService.checkPassword(password, id);
    }

    @PostMapping("/user/changepass")
    public void updatePass(@RequestBody UserDTO userDTO){
        userService.updatePassword(userDTO);
    }

    @GetMapping("/user/{id}/address")
    public List<UserAddressDTO> getAddressList(@PathVariable("id") Long id){
        return addressService.getListAddress(id);
    }
    @GetMapping("/user/forgetpass")
    public boolean forgetPassword(@RequestParam String email){
        return userService.resetPassword(email);
    }

    @GetMapping("/user/role")
    public Collection<? extends GrantedAuthority> getUserRole(){
        return userService.getUserRole();
    }
    @GetMapping("/user/logged")
    public String getUserLogged(){
        return userService.getLoggedUser();
    }


}