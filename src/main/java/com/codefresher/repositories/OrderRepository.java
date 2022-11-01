package com.codefresher.repositories;

import com.codefresher.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findOrderByOrderId(Long id);
    List<Order> findAllByUserOrderUserId(Long userId);

    List<Order> findAllByStatusEquals(int status);
}