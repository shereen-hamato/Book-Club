package com.ebookclub.ebookclub.controller;

import com.ebookclub.ebookclub.dto.BookDto;
import com.ebookclub.ebookclub.model.Book;
import com.ebookclub.ebookclub.repo.UserRepo;
import com.ebookclub.ebookclub.security.LoginUser;
import com.ebookclub.ebookclub.service.BookService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "/book")
public class BookController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RatingController.class);

    @Autowired
    private BookService bookService;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping
    @ResponseBody
    public List<BookDto> getBooks(Pageable pageable) {
        List<Book> books = bookService.getBooksList(pageable);
        return books.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public BookDto createBook(@RequestBody @Validated BookDto bookDto) {
        LOGGER.info("User {} CREATE /book",  LoginUser.get().getUsername());
        System.out.println(LoginUser.get().getId());
        System.out.println(bookDto.getAuthor());
        Book book = bookService.createBook(bookDto, LoginUser.get().getId());
        //System.out.println(book.getAuthor());
        return convertToDto(book);
    }

    @GetMapping(value = "/{id}")
    @ResponseBody
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public BookDto getBook(@PathVariable("id") Integer id) {
        return convertToDto(bookService.getBookById(id));
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public BookDto updateBook(@PathVariable("id") Integer id, @RequestBody @Validated BookDto bookDto) {
        LOGGER.info("User {} UPDATE /book/{}", LoginUser.get().getUsername(), id);
        return convertToDto(bookService.updateBook(id,LoginUser.get(), bookDto));

    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public void deleteBook(@PathVariable("id") Integer id) {
        LOGGER.info("User {} DELETE /book/{}",  LoginUser.get().getUsername(), id);
         bookService.deleteBook(id, LoginUser.get());

    }


    private BookDto convertToDto(Book book) {
        BookDto bookDto = modelMapper.map(book,BookDto.class);
        return bookDto;
    }
}