package com.codefresher.services;

import com.codefresher.dto.UserAddressDTO;
import com.codefresher.entities.UserAddress;
import com.codefresher.repositories.UserAddressRepository;
import com.codefresher.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserAddressService {
    @Autowired
    ModelMapper mapper;
    @Autowired
    UserAddressRepository addressRepository;
    @Autowired
    UserRepository userRepository;

    public void updateAddress(UserAddressDTO data, Long addressId){
        addressRepository.updateAddress(data.getReceiverName(), data.getDetail(), data.getWard(), data.getDistrict(),
                data.getProvince(), data.getPhoneNumber(), addressId);
    }

    // get list of address
    public List<UserAddressDTO> getListAddress(Long userId){
        List<UserAddressDTO> addresList = new ArrayList<>();
        for (UserAddress userAddress : userRepository.findByUserId(userId).getUserAddressList()) {
            addresList.add(toDTO(userAddress));
        }
        return addresList;
    }

    // add a address
    public void addAddress(UserAddressDTO data){
        UserAddress address = toEntity(data);
        address.setUser(userRepository.findByUserId(data.getUserId()));
        addressRepository.saveAndFlush(address);
    }

    // remove address by Id
    public void removeAddress(Long id){
        addressRepository.deleteById(id);
    }

    public UserAddressDTO getAddress(Long addressId){
        return toDTO(addressRepository.findUserAddressByAddressId(addressId));
    }


    // mapper
    private UserAddressDTO toDTO(UserAddress user){
        return mapper.map(user, UserAddressDTO.class);
    }
    private UserAddress toEntity(UserAddressDTO userDTO){
        return mapper.map(userDTO, UserAddress.class);
    }

}
