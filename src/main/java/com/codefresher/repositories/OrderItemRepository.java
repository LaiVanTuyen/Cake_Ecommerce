package com.codefresher.repositories;

import com.codefresher.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    public OrderItem findOrderItemByItemId(Long id);
}