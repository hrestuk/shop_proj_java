package com.example.test.service;

import com.example.test.entity.Order;
import com.example.test.entity.User;
import com.example.test.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest
{
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private Order sampleOrder;

    @BeforeEach
    public void setup()
    {
        User user = User.builder().id(1L).username("usertest").build();

        sampleOrder = Order.builder().id(1L).user(user).orderDate(LocalDateTime.now()).build();
    }

    @Test
    public void createOrder_ShouldReturnSavedOrder()
    {
        when(orderRepository.save(sampleOrder)).thenReturn(sampleOrder);

        Order result = orderService.create(sampleOrder);

        assertThat(result).isNotNull();
        assertThat(result.getUser().getUsername()).isEqualTo("usertest");
        verify(orderRepository, times(1)).save(sampleOrder);
    }

    @Test
    public void getAll_ShouldReturnListOfOrders()
    {
        List<Order> orders = Arrays.asList(sampleOrder);
        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderService.getAll();

        assertThat(result).hasSize(1);
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    public void getById_ShouldReturnOrder()
    {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(sampleOrder));

        Optional<Order> result = orderService.getById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    public void getById_ShouldReturnEmpty_WhenOrderNotFound()
    {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Order> result = orderService.getById(999L);

        assertThat(result).isEmpty();
        verify(orderRepository, times(1)).findById(999L);
    }

    @Test
    public void updateOrder_ShouldReturnUpdatedOrder()
    {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(sampleOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(sampleOrder);

        Order update = Order.builder().user(sampleOrder.getUser()).orderDate(LocalDateTime.now().plusDays(1)).build();

        Order result = orderService.update(1L, update);

        assertThat(result.getOrderDate()).isEqualTo(update.getOrderDate());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    public void update_ShouldReturnNull_WhenOrderNotFound()
    {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        Order updated = orderService.update(999L, sampleOrder);

        assertThat(updated).isNull();
        verify(orderRepository, times(1)).findById(999L);
        verify(orderRepository, never()).save(any());
    }

    @Test
    public void deleteOrder_ShouldCallRepositoryDelete()
    {
        orderService.delete(1L);

        verify(orderRepository, times(1)).deleteById(1L);
    }
}
