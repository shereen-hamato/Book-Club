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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BookService {

    @Autowired
    BookRepo bookRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    GenreRepo genreRepo;

    @Autowired
    private RatingRepo ratingRepo;


    /**
     *
     * @param pageable
     * @return list of found books
     */
    public List<Book> getBooksList(
            Pageable pageable) {
        Page<Book> books = bookRepo.findAll(pageable);
        return books.getContent();
    }

    /**
     *
     * @param bookDto
     * @return
     * @throws NoSuchElementException
     */
    public Book createBook(BookDto bookDto, Integer userId) throws NoSuchElementException {
        Genre bookGenre=verifyGenre(bookDto.getGenreId());
        User  bookOwner = userRepo.findById(userId).orElseThrow(()-> new NoSuchElementException());
        Book book =bookRepo.save(new Book(bookDto.getTitle(), bookDto.getAuthor(), bookDto.getOverview(), bookDto.getIsdn(), bookDto.getImageUrl(), bookGenre, bookOwner));
        return book;
    }

    /**
     *
     * @param id
     * @return created book
     */
    public Book getBookById(Integer id) throws NoSuchElementException {
        return bookRepo.findById(id).orElseThrow(()
                -> new NoSuchElementException("Book " + id + " not found"));
    }

    /**
     *
     * @param bookDto
     * @return updated book
     * @throws NoSuchElementException
     */
    public Book updateBook(Integer bookId, User user,  BookDto bookDto) throws NoSuchElementException {
        Book  book=verifyBookAndOwner(bookId, user);
        Genre genre=verifyGenre(bookDto.getGenreId());
        book.setBookGenre(genre);
        book.setAuthor(bookDto.getAuthor());
        book.setIsdn(bookDto.getIsdn());
        book.setOverview(bookDto.getOverview());
        book.setImageUrl(bookDto.getImageUrl());
        book.setTitle(bookDto.getTitle());
        return bookRepo.save(book);
    }

    /**
     *
     * @param bookId
     * @throws NoSuchElementException
     */
    @Transactional
    public void deleteBook(Integer bookId, User user) throws NoSuchElementException {
        verifyBookAndOwner(bookId, user);
        ratingRepo.deleteAll(ratingRepo.findByBookId(bookId));
        bookRepo.deleteById(bookId);
    }
    /**
     * Verify and return the Book given a bookId.
     *
     * @param bookId
     * @return the found Book
     * @throws NoSuchElementException if no Book found.
     */

    Book verifyBook(int bookId) throws NoSuchElementException {
        return bookRepo.findById(bookId).orElseThrow(() ->
                new NoSuchElementException("Book does not exist " + bookId)
        );
    }

    /**
     * Verify and return the Book given a bookId.
     *
     * @param bookId
     * @return the found Book
     * @throws NoSuchElementException if no Book found.
     */

    Book verifyBookAndOwner(int bookId, User user) throws NoSuchElementException {
        return bookRepo.findByIdAndBookOwner(bookId,  user).orElseThrow(() ->
                new NoSuchElementException("Operation is not allowed, userId "+user.getId()+" , bookId "+ bookId)
        );
    }


    /**
     *
     * @param genreId
     * @return
     * @throws NoSuchElementException
     */
    Genre verifyGenre(int genreId) throws NoSuchElementException {
        return genreRepo.findById(genreId).orElseThrow(() ->
                new NoSuchElementException("Genre does not exist " + genreId)
        );
    }


}
