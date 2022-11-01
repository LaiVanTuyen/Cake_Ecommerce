package com.codefresher.repositories;

import com.codefresher.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    public Long countByCartUserUsername(String username);
    public List<Cart> findAllByCartUserUserId(Long id);
    public Cart findCartByCartId(Long id);
}