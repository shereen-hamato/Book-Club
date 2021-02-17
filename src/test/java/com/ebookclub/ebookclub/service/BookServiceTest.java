package com.ebookclub.ebookclub.service;

import com.ebookclub.ebookclub.dto.BookDto;
import com.ebookclub.ebookclub.model.Book;
import com.ebookclub.ebookclub.model.Genre;
import com.ebookclub.ebookclub.model.User;
import com.ebookclub.ebookclub.repo.BookRepo;
import com.ebookclub.ebookclub.repo.GenreRepo;
import com.ebookclub.ebookclub.repo.RatingRepo;
import com.ebookclub.ebookclub.repo.UserRepo;
import com.ebookclub.ebookclub.security.LoginUser;
import mockit.MockUp;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {

    private static final int USER_ID = 999;
    private static final int GENRE_ID = 1000;
    private static final int BOOK_ID = 998;
    private static final String TITLE= "faults in our starts";
    private static final String AUTHOR  ="John Green";
    private static final String REVIEW = "light contemporary romance ";
    private static final String ISDN = "R3547HRYY3GFWGHE";

    private User user = new User(null, null);
    private Genre genre= new Genre();
    private Book book = new Book(TITLE,AUTHOR,REVIEW,ISDN,null, genre,user);

    private BookDto bookDto = new BookDto(TITLE,AUTHOR,REVIEW,ISDN,null, GENRE_ID);

    @Mock
    private BookRepo bookRepo;

    @Mock
    private GenreRepo genreRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private RatingRepo ratingRepo;


    @InjectMocks
    private BookService bookService;

    @Before
    public void setupReturnValuesOfMockMethods() {
        user.setId(USER_ID);
        genre.setId(GENRE_ID);
        when(genreRepo.findById(GENRE_ID)).thenReturn(Optional.of(genre));
        when(userRepo.findById(USER_ID)).thenReturn(Optional.of(user));
        when( bookRepo.findByIdAndBookOwner(BOOK_ID,user)).thenReturn(Optional.of(book));

        new MockUp<LoginUser>() {
            @mockit.Mock
            public User get() {
                return user;
            }
        };

    }

    @Test
    public void getBooksList() {
        Pageable pageable = mock(Pageable.class);
        Page page = mock(Page.class);
        when(bookRepo.findAll(pageable)).thenReturn(page);
        assertThat(bookService.getBooksList(pageable),is(page.getContent()));


    }

    @Test
    public void createBook() {

        ArgumentCaptor<Book> BookCaptor = ArgumentCaptor.forClass(Book.class);

        bookService.createBook(bookDto,USER_ID);

        verify(bookRepo).save(BookCaptor.capture());


        assertThat(BookCaptor.getValue().getBookOwner().getId(), is(USER_ID));
        assertThat(BookCaptor.getValue().getBookGenre().getId(), is(GENRE_ID));
        assertThat(BookCaptor.getValue().getTitle(), is(TITLE));
        assertThat(BookCaptor.getValue().getAuthor(), is(AUTHOR));
        assertThat(BookCaptor.getValue().getOverview(), is(REVIEW));
        assertThat(BookCaptor.getValue().getIsdn(), is(ISDN));


    }

    @Test
    public void getBookById() {

        when(bookRepo.findById(BOOK_ID)).thenReturn(Optional.of(book));

        assertThat(bookService.getBookById(BOOK_ID), is(book));
    }

    @Test
    public void updateBook() {


        ArgumentCaptor<Book> BookCaptor = ArgumentCaptor.forClass(Book.class);
        bookDto.setAuthor("diff author");
        bookDto.setOverview("diff overview");
        bookService.updateBook(BOOK_ID, user,bookDto);


        verify(bookRepo).save(BookCaptor.capture());


        assertThat(BookCaptor.getValue().getBookOwner().getId(), is(USER_ID));
        assertThat(BookCaptor.getValue().getBookGenre().getId(), is(GENRE_ID));
        assertThat(BookCaptor.getValue().getTitle(), is(TITLE));
        assertThat(BookCaptor.getValue().getAuthor(), is("diff author"));
        assertThat(BookCaptor.getValue().getOverview(), is("diff overview"));
        assertThat(BookCaptor.getValue().getIsdn(), is(ISDN));

    }

    @Test
    public void deleteBook() {
        bookService.deleteBook(BOOK_ID, user);

        //verify bookRepository.delete invoked
        verify(ratingRepo).deleteAll(new ArrayList<>());
        verify(bookRepo).deleteById(BOOK_ID);
    }
}