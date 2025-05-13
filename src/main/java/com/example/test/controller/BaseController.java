package com.example.test.controller;

import com.example.test.entity.Roles;
import com.example.test.entity.User;
import com.example.test.service.UserService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController
{
    protected final UserService userService;

    public BaseController(UserService userService)
    {
        this.userService = userService;
    }

    protected User getCurrentUser()
    {
        return userService.getCurrentUser();
    }

    protected boolean isAdmin()
    {
        return getCurrentUser().getRole().equals(Roles.ADMIN);
    }

    protected boolean isOwner(User user)
    {
        return user.getId().equals(getCurrentUser().getId());
    }
}
