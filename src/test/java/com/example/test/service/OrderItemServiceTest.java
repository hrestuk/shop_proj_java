package com.example.test.service;

import com.example.test.entity.OrderItem;
import com.example.test.entity.Product;
import com.example.test.entity.Order;
import com.example.test.repository.OrderItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderItemServiceTest
{
    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private OrderItemService orderItemService;

    private OrderItem sampleItem;

    @BeforeEach
    public void setup()
    {
        Product product = Product.builder().id(1L).name("TestProduct").price(100).quantity(50).build();
        Order order = Order.builder().id(1L).build();

        sampleItem = OrderItem.builder().id(1L).product(product).order(order).quantity(2).build();
    }

    @Test
    public void createOrderItem_ShouldReturnSavedItem()
    {
        when(orderItemRepository.save(sampleItem)).thenReturn(sampleItem);

        OrderItem result = orderItemService.create(sampleItem);

        assertThat(result).isNotNull();
        assertThat(result.getQuantity()).isEqualTo(2);
        verify(orderItemRepository, times(1)).save(sampleItem);
    }

    @Test
    public void getAll_ShouldReturnListOfOrderItems()
    {
        when(orderItemRepository.findAll()).thenReturn(Arrays.asList(sampleItem));

        List<OrderItem> result = orderItemService.getAll();

        assertThat(result).hasSize(1);
        verify(orderItemRepository, times(1)).findAll();
    }

    @Test
    public void getById_ShouldReturnItem()
    {
        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(sampleItem));

        Optional<OrderItem> result = orderItemService.getById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1L);
        verify(orderItemRepository).findById(1L);
    }

    @Test
    public void getById_ShouldReturnEmpty_WhenNotFound()
    {
        when(orderItemRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<OrderItem> result = orderItemService.getById(999L);

        assertThat(result).isEmpty();
        verify(orderItemRepository).findById(999L);
    }

    @Test
    public void update_ShouldReturnUpdatedItem()
    {
        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(sampleItem));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(sampleItem);

        OrderItem updatedItem = OrderItem.builder().product(sampleItem.getProduct()).order(sampleItem.getOrder()).quantity(5).build();

        OrderItem result = orderItemService.update(1L, updatedItem);

        assertThat(result.getQuantity()).isEqualTo(5);
        verify(orderItemRepository).save(any(OrderItem.class));
    }

    @Test
    public void update_ShouldReturnNull_WhenItemNotFound()
    {
        when(orderItemRepository.findById(999L)).thenReturn(Optional.empty());

        OrderItem result = orderItemService.update(999L, sampleItem);

        assertThat(result).isNull();
        verify(orderItemRepository, times(1)).findById(999L);
        verify(orderItemRepository, never()).save(any());
    }

    @Test
    public void delete_ShouldCallRepository()
    {
        orderItemService.delete(1L);

        verify(orderItemRepository, times(1)).deleteById(1L);
    }
}
