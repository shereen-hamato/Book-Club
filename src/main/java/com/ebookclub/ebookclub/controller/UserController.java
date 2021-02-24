package com.ebookclub.ebookclub.controller;

import com.ebookclub.ebookclub.dto.UserDto;
import com.ebookclub.ebookclub.security.LoginUser;
import com.ebookclub.ebookclub.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.OK)
    public String login(@RequestBody @Valid UserDto userDto) {
        return userService.signin(userDto.getUsername(), userDto.getPassword()).orElseThrow(()->
                new HttpServerErrorException(HttpStatus.FORBIDDEN, "Login Failed"));
    }


}