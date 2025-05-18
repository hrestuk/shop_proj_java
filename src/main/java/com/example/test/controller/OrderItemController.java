package com.example.test.controller;

import com.example.test.entity.OrderItem;
import com.example.test.service.OrderItemService;
import com.example.test.service.OrderService;
import com.example.test.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order-items")
public class OrderItemController extends BaseController
{
    private final OrderItemService orderItemService;
    private final OrderService orderService;

    public OrderItemController(OrderItemService orderItemService, OrderService orderService, UserService userService)
    {
        super(userService);
        this.orderItemService = orderItemService;
        this.orderService = orderService;
    }

    @GetMapping
    public List<OrderItem> getAll()
    {
        if (isAdmin())
        {
            return orderItemService.getAll();
        }

        return orderItemService.getByUsername(getCurrentUser().getUsername());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItem> getById(@PathVariable Long id)
    {
        return orderItemService.getById(id)
                .filter(item -> isAdmin() || isOwner(item.getOrder().getUser()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}")
    public ResponseEntity<OrderItem> update(@PathVariable Long id, @RequestBody OrderItem orderItem)
    {
        return orderItemService.getById(id)
                .filter(existing -> isAdmin() || isOwner(existing.getOrder().getUser()))
                .map(existing -> ResponseEntity.ok(orderItemService.update(id, orderItem)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id)
    {
        return orderItemService.getById(id)
                .filter(existing -> isAdmin() || isOwner(existing.getOrder().getUser()))
                .map(existing -> {
                    orderItemService.delete(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
