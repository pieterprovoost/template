package be.pieterprovoost.demo.controllers;

import be.pieterprovoost.demo.model.User;
import be.pieterprovoost.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private UserService userService;

    @RequestMapping("/user")
    public User user() {
        User user = userService.findCurrentUser();
        return user;
    }

}