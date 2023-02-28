package com.example.secondhandmarketwebapp.controller;

import com.example.secondhandmarketwebapp.entity.User;
import com.example.secondhandmarketwebapp.exception.InvalidUserException;
import com.example.secondhandmarketwebapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class SignUpController {
    private UserService userService;
    @Autowired
    public SignUpController(UserService userService) {
        this.userService = userService;
    }
    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<String> signUp(@RequestBody User user) {
        return userService.signUp(user);
    }

    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<String> handleInvalidUserException(InvalidUserException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getErrorMessage());
    }
}