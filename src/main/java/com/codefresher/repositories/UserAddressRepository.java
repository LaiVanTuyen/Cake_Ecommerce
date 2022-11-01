package com.codefresher.repositories;

import com.codefresher.entities.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {

    @Transactional
    @Modifying
    @Query("update UserAddress add set add.receiverName = ?1, add.detail = ?2, add.ward = ?3, " +
            "add.district = ?4, add.province = ?5, add.phoneNumber = ?6 where add.addressId = ?7")
    public void updateAddress(String name, String detail, String ward,
                              String district, String province, String phoneNumber, Long Id);

    public UserAddress findUserAddressByAddressId(Long id);
}