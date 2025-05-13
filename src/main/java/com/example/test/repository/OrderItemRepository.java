package com.example.test.repository;

import com.example.test.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long>
{
    List<OrderItem> findByOrder_User_Username(String username);
}
