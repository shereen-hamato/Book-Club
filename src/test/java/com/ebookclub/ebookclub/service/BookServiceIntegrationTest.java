package com.ebookclub.ebookclub.service;

import com.ebookclub.ebookclub.dto.BookDto;
import com.ebookclub.ebookclub.model.Book;
import com.ebookclub.ebookclub.model.Genre;
import com.ebookclub.ebookclub.model.User;
import com.ebookclub.ebookclub.repo.GenreRepo;
import com.ebookclub.ebookclub.repo.UserRepo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class BookServiceIntegrationTest {

    private static final String USER_NAME = "user";
    private static final String PASSWORD = "password";
    private static final String TITLE= "faults in our starts";
    private static final String AUTHOR  ="John Green";
    private static final String REVIEW = "light contemporary romance ";
    private static final String ISDN = "R3547HRYY3GFWGHE";
    private static final String IMG_URL = "example.com";
    private static final String GENRE_NAME = "RomanceTest";
    private static final String GENRE_DESCRIPTION= "Romance";


    private User user = new User(USER_NAME, PASSWORD);
    private Genre genre= new Genre(GENRE_NAME, GENRE_DESCRIPTION);
    private BookDto bookDto;

    @Autowired
    private BookService bookService;

    @Autowired
    private GenreRepo genreRepo;

    @Autowired
    private UserRepo userRepo;

    @Before
    public void setupReturnValuesOfMockMethods() {
        genre=genreRepo.save(genre);
        user=userRepo.save(user);
        bookDto = new BookDto(TITLE,AUTHOR,REVIEW,ISDN,IMG_URL, genre.getId());

    }

    @Test
    public void getBooksList() {
        PageRequest pageRequest = PageRequest.of(1, 10);
        assertThat(bookService.getBooksList().size(),is(0));


    }

    @Test
    public void createBook() {


        Book book =bookService.createBook(bookDto,user.getId());

        Book createdBook = bookService.verifyBookAndOwner(book.getId(), user);

        assertThat(createdBook.getBookOwner().getId(), is(user.getId()));
        assertThat(createdBook.getBookGenre().getId(), is(genre.getId()));
        assertThat(createdBook.getTitle(), is(TITLE));
        assertThat(createdBook.getAuthor(), is(AUTHOR));
        assertThat(createdBook.getOverview(), is(REVIEW));
        assertThat(createdBook.getIsdn(), is(ISDN));
    }

    @Test
    public void getBookById() {

        Book book =bookService.createBook(bookDto,user.getId());

        assertThat(bookService.getBookById(book.getId()), is(book));
    }

    @Test
    public void updateBook() {

        Book createdBook =bookService.createBook(bookDto,user.getId());

        bookDto.setAuthor("diff author");
        bookDto.setOverview("diff overview");

        Book book=bookService.updateBook(createdBook.getId(),user,bookDto);



        assertThat(book.getBookOwner().getId(), is(user.getId()));
        assertThat(book.getBookGenre().getId(), is(genre.getId()));
        assertThat(book.getTitle(), is(TITLE));
        assertThat(book.getAuthor(), is("diff author"));
        assertThat(book.getOverview(), is("diff overview"));
        assertThat(book.getIsdn(), is(ISDN));

    }

    @Test
    public void deleteBook() {
        Book book =bookService.createBook(bookDto,user.getId());
        bookService.deleteBook(book.getId(), user);
        PageRequest pageRequest = PageRequest.of(1, 10);
        assertThat(bookService.getBooksList().size(),is(0));

    }
}