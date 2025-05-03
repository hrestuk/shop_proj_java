package com.example.test.service;

import com.example.test.entity.Order;
import com.example.test.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService
{
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository)
    {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAll()
    {
        return orderRepository.findAll();
    }

    public Optional<Order> getById(Long id)
    {
        return orderRepository.findById(id);
    }

    public Order create(Order order)
    {
        return orderRepository.save(order);
    }

    public Order update(Long id, Order order)
    {
        return orderRepository.findById(id).map(existing -> {
            existing.setOrderDate(order.getOrderDate());
            existing.setTotalAmount(order.getTotalAmount());
            return orderRepository.save(existing);
        }).orElse(null);
    }

    public void delete(Long id)
    {
        orderRepository.deleteById(id);
    }
}
