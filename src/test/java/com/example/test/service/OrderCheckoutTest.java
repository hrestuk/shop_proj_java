package com.example.test.service;


import com.example.test.entity.*;
import com.example.test.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderCheckoutTest
{
    @Mock private OrderRepository orderRepository;
    @Mock private OrderItemRepository orderItemRepository;
    @Mock private UserRepository userRepository;
    @Mock private CartItemRepository cartItemRepository;
    @Mock private ProductRepository productRepository;

    @InjectMocks private OrderService orderService;

    private User user;
    private Product product;
    private CartItem cartItem;

    @BeforeEach
    public void setup()
    {
        user = User.builder().id(1L).username("usertest").build();
        product = Product.builder().id(1L).name("TestProduct").price(100).quantity(10).build();
        cartItem = CartItem.builder().id(1L).user(user).product(product).quantity(2).build();
    }

    @Test
    public void checkout_ShouldCreateOrder_WhenCartIsValid()
    {
        when(cartItemRepository.findByUser_Username("usertest")).thenReturn(List.of(cartItem));
        when(userRepository.findByUsername("usertest")).thenReturn(Optional.of(user));
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));
        when(orderItemRepository.save(any(OrderItem.class))).thenAnswer(i -> i.getArgument(0));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        Order order = orderService.checkout("usertest");

        assertThat(user.getUsername()).isEqualTo("usertest");
        assertThat(order).isNotNull();
        assertThat(order.getTotalAmount()).isEqualTo(200);
        verify(orderRepository).save(any(Order.class));
        verify(orderItemRepository).save(any(OrderItem.class));
        verify(productRepository).save(any(Product.class));
        verify(cartItemRepository).deleteAll(List.of(cartItem));
    }

    @Test
    public void checkout_ShouldThrow_WhenCartIsEmpty()
    {
        when(cartItemRepository.findByUser_Username("usertest")).thenReturn(List.of());

        assertThatThrownBy(() -> orderService.checkout("usertest"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Cart is empty");
    }

    @Test
    public void checkout_ShouldThrow_WhenUserNotFound()
    {
        when(cartItemRepository.findByUser_Username("usertest")).thenReturn(List.of(cartItem));
        when(userRepository.findByUsername("usertest")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.checkout("usertest"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    public void checkout_ShouldThrow_WhenProductStockIsInsufficient()
    {
        product.setQuantity(1); // менше, ніж у cartItem

        when(cartItemRepository.findByUser_Username("usertest")).thenReturn(List.of(cartItem));
        when(userRepository.findByUsername("usertest")).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> orderService.checkout("usertest"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Not enough stock for product");
    }
}
