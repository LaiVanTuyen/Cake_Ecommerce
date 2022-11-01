package com.codefresher.repositories;

import com.codefresher.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
     User findByUsername(String username);
     User findByUserId(Long id);
     User findByEmail(String email);

     @Transactional
     @Modifying
     @Query("update User user set user.fullname = ?1, user.phoneNumber = ?2 where user.userId = ?3")
     void setUserInforById(String fullname, String phoneNumber, Long userId);

     @Transactional
     @Modifying
     @Query("update User user set user.password = ?1 where user.userId = ?2")
     void updatePassword(String pass, Long userId);
}