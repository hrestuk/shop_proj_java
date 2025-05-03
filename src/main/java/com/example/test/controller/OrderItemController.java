package com.example.test.controller;

import com.example.test.entity.OrderItem;
import com.example.test.service.OrderItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-items")
public class OrderItemController
{

    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService)
    {
        this.orderItemService = orderItemService;
    }

    @GetMapping
    public List<OrderItem> getAll() {
        return orderItemService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItem> getById(@PathVariable Long id)
    {
        return orderItemService.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public OrderItem create(@RequestBody OrderItem orderItem)
    {
        return orderItemService.create(orderItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderItem> update(@PathVariable Long id, @RequestBody OrderItem orderItem)
    {
        OrderItem updated = orderItemService.update(id, orderItem);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id)
    {
        orderItemService.delete(id);
    }
}
