package com.example.test.controller;

import com.example.test.entity.Order;
import com.example.test.entity.User;
import com.example.test.repository.UserRepository;
import com.example.test.service.OrderService;
import com.example.test.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController extends BaseController
{
    private final OrderService orderService;
    private final UserRepository userRepository;

    public OrderController(OrderService orderService, UserRepository userRepository, UserService userService)
    {
        super(userService);
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Order> getAll()
    {
        User currentUser = getCurrentUser();
        if (isAdmin())
        {
            return orderService.getAll();
        }
        return orderService.getByUsername(currentUser.getUsername());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable Long id)
    {
        return orderService.getById(id)
                .filter(order -> isAdmin() || isOwner(order.getUser()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Order create(@RequestBody Order order)
    {
        User user = getCurrentUser();
        order.setUser(user);
        return orderService.create(order);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> update(@PathVariable Long id, @RequestBody Order order)
    {
        return orderService.getById(id)
                .filter(existing -> isAdmin() || isOwner(existing.getUser()))
                .map(existing -> ResponseEntity.ok(orderService.update(id, order)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id)
    {
        return orderService.getById(id)
                .filter(existing -> isAdmin() || isOwner(existing.getUser()))
                .map(existing -> {
                    orderService.delete(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
