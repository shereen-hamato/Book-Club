package com.ebookclub.ebookclub.dto;

import javax.persistence.Column;
import javax.validation.constraints.Size;

public class GenreDto {

    private String name;

    @Size(max= 2000)
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
