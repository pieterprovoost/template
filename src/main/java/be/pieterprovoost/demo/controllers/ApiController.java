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
public class ApiController {

    @Autowired
    private UserService userService;

    @RequestMapping("/resource")
    public Map<String,Object> home() {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("id", UUID.randomUUID().toString());
        model.put("content", "Hello World");
        return model;
    }

    @RequestMapping("/currentuser")
    public User user() {
        User user = userService.findCurrentUser();
        return user;
    }

}