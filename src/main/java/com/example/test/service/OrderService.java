package com.example.test.service;

import com.example.test.entity.*;
import com.example.test.repository.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService
{
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, UserRepository userRepository, CartItemRepository cartItemRepository, ProductRepository productRepository)
    {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    public List<Order> getAll()
    {
        return orderRepository.findAll();
    }

    public Optional<Order> getById(Long id)
    {
        return orderRepository.findById(id);
    }

    public List<Order> getByUsername(String username)
    {
        return orderRepository.findByUser_Username(username);
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

    // Метод для оформлення замовлення
    public Order checkout(String username)
    {
        List<CartItem> cartItems = cartItemRepository.findByUser_Username(username);

        if (cartItems.isEmpty())
        {
            throw new IllegalArgumentException("Cart is empty");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(calculateTotalAmount(cartItems));
        order = orderRepository.save(order);

        for (CartItem cartItem : cartItems)
        {
            // Створення OrderItem для кожного CartItem
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(cartItem.getProduct().getPrice() * cartItem.getQuantity());
            orderItemRepository.save(orderItem);

            // Оновлення кількості товару в продукті
            Product product = cartItem.getProduct();
            int newQuantity = product.getQuantity() - cartItem.getQuantity();
            if (newQuantity < 0) {
                throw new IllegalArgumentException("Not enough stock for product: " + product.getName());
            }
            product.setQuantity(newQuantity);
            productRepository.save(product);
        }

        // Видалення всіх елементів з кошика після оформлення замовлення
        cartItemRepository.deleteAll(cartItems);
        return order;
    }

    private double calculateTotalAmount(List<CartItem> cartItems)
    {
        return cartItems.stream()
                .mapToDouble(cartItem -> cartItem.getProduct().getPrice() * cartItem.getQuantity())
                .sum();
    }
}
