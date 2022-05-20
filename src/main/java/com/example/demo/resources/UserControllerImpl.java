package com.example.demo.resources;

import com.example.demo.model.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(value = "/api/user")
public class UserControllerImpl {

    @GetMapping
    public User getUser(String id) {
        return new User("1", "Ulrich");
    }
}
