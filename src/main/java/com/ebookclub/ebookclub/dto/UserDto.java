package com.ebookclub.ebookclub.dto;


import javax.validation.constraints.NotNull;

public class UserDto {


    @NotNull
    private String username;

    @NotNull
    private String password;

    /**
     * Default constructor
     */
    protected UserDto() {
    }

    /**
     * Partial constructor
     * @param username
     * @param password
     */
    public UserDto(String username, String password) {
        this.username = username;
        this.password = password;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
