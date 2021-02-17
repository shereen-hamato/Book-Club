package com.ebookclub.ebookclub.controller;

import com.ebookclub.ebookclub.dto.BookDto;
import com.ebookclub.ebookclub.dto.GenreDto;
import com.ebookclub.ebookclub.model.Book;
import com.ebookclub.ebookclub.model.Genre;
import com.ebookclub.ebookclub.repo.GenreRepo;
import com.ebookclub.ebookclub.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/genre")
public class GenreController {

    @Autowired
    private GenreRepo genreRepo;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseBody
    public List<GenreDto> getGenres() {
        List<Genre> genres = new ArrayList<>();
        genreRepo.findAll().forEach(genre -> genres.add(genre));
        return genres.stream().map(g->modelMapper.map(g,GenreDto.class)).collect(Collectors.toList());
    }


}
