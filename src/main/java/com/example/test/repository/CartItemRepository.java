package com.example.test.repository;

import com.example.test.entity.CartItem;
import com.example.test.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long>
{
    List<CartItem> findByUser_Username(String username);
}
