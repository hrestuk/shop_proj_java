package com.example.test.service;

import com.example.test.entity.OrderItem;
import com.example.test.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderItemService
{

    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository)
    {
        this.orderItemRepository = orderItemRepository;
    }

    public List<OrderItem> getAll()
    {
        return orderItemRepository.findAll();
    }

    public Optional<OrderItem> getById(Long id)
    {
        return orderItemRepository.findById(id);
    }

    public OrderItem create(OrderItem orderItem)
    {
        return orderItemRepository.save(orderItem);
    }

    public OrderItem update(Long id, OrderItem orderItem)
    {
        return orderItemRepository.findById(id).map(existing -> {
            existing.setQuantity(orderItem.getQuantity());
            existing.setPriceAtPurchase(orderItem.getPriceAtPurchase());
            return orderItemRepository.save(existing);
        }).orElse(null);
    }

    public void delete(Long id)
    {
        orderItemRepository.deleteById(id);
    }
}
