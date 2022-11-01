package com.codefresher.restcontroller;

import com.codefresher.dto.UserAddressDTO;
import com.codefresher.services.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserAddressRestController {
    @Autowired
    UserAddressService addressService;

    //    update address bby id
    @PutMapping("/address/{id}")
    public void updateUserAddress(@PathVariable("id") Long addressId, @RequestBody UserAddressDTO data){
        addressService.updateAddress(data, addressId);
    }

    // get address by id
    @GetMapping("/address/{id}")
    public UserAddressDTO getUserAddress(@PathVariable("id") Long id){
        return addressService.getAddress(id);
    }

    // add more address
    @PostMapping("/address")
    public void addAddress(@RequestBody UserAddressDTO data){
        System.out.println(data);
        addressService.addAddress(data);
    }

    // delete address by id
    @DeleteMapping("/address/{id}")
    public void delAddress(@PathVariable("id") Long addressId){
        addressService.removeAddress(addressId);
    }
}
