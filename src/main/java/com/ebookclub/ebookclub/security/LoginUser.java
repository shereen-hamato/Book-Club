package com.ebookclub.ebookclub.security;

import com.ebookclub.ebookclub.model.User;
import org.springframework.web.context.annotation.RequestScope;

import java.util.NoSuchElementException;

@RequestScope
public class LoginUser {

    private static User loginUser;

    public LoginUser(){
    }

    public static User get() {
        if(loginUser==null)
            throw new RuntimeException("Login user is not available");
        return loginUser;
    }

    protected void set(User user) {
        loginUser=user;
    }

}
