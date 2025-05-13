package com.example.test.repository;

import com.example.test.entity.Order;
import com.example.test.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>
{
    List<Order> findByUser_Username(String username);
}
