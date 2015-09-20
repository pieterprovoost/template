package be.pieterprovoost.demo.controllers;

import be.pieterprovoost.demo.model.User;
import be.pieterprovoost.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private UserService userService;

    // Returns the currently authenticated user.

    @RequestMapping("/user")
    public User user() {
        return userService.findCurrentUser();
    }

    @RequestMapping("/users")
    public List<User> users() {
        return userService.findAll();
    }

}