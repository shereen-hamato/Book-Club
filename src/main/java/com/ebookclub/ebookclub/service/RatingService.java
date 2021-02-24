package com.ebookclub.ebookclub.service;

import com.ebookclub.ebookclub.dto.RatingDto;
import com.ebookclub.ebookclub.model.Book;
import com.ebookclub.ebookclub.model.Rating;
import com.ebookclub.ebookclub.model.User;
import com.ebookclub.ebookclub.repo.RatingRepo;
import com.ebookclub.ebookclub.repo.UserRepo;
import com.ebookclub.ebookclub.security.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.OptionalDouble;


/**
 * Book Rating Service
 * <p>
 * Created by Shereen.
 */
@Service
public class RatingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RatingService.class);

    @Autowired
    private RatingRepo ratingRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BookService bookService;


    /**
     * Create a new Book Rating in the database
     *
     * @throws NoSuchElementException if no Book found.
     */
    public Rating createNew(RatingDto ratingDto, Integer bookId) {

        try {
            Rating rating = verifyBookAndUser(bookService.verifyBook(bookId), LoginUser.get());
            rating.setRank(ratingDto.getRank());
            rating.setComment(ratingDto.getComment());
            rating.setReadStatus(ratingDto.getRead());
            LOGGER.info("Rating for book {} of user {} already existed", bookId, LoginUser.get().getId());
            return ratingRepo.save(rating);
        } catch (NoSuchElementException e) {
            LOGGER.info("Create Rating for book {} of user {}", bookId, LoginUser.get().getId());
        }
        return ratingRepo.save(new Rating(bookService.verifyBook(bookId), LoginUser.get(), ratingDto.getComment(), ratingDto.getRank(), ratingDto.getRead()));
    }

    /**
     * Get a page of book ratings for a book.
     *
     * @param bookId   book identifier
     * @return Page of BookRatings
     * @throws NoSuchElementException if no Book found.
     */
    public List<Rating> lookupRatings(int bookId) throws NoSuchElementException {
        LOGGER.info("Lookup Rating for book {}", bookId);
        return ratingRepo.findByBookId(bookService.verifyBook(bookId).getId());
    }

    /**
     * Update some of the elements of a Book Rating.
     *
     * @return Book Rating Domain Object
     * @throws NoSuchElementException if no Book found.
     */
    public Rating update(RatingDto ratingDto, Integer bookId) throws NoSuchElementException {
        LOGGER.info("Update  Rating for book {} of user {}", bookId, LoginUser.get().getId());
        Rating rating = verifyBookAndUser(bookService.verifyBook(bookId), LoginUser.get());
        rating.setRank(ratingDto.getRank());
        rating.setComment(ratingDto.getComment());
        rating.setReadStatus(ratingDto.getRead());
        return ratingRepo.save(rating);
    }

    /**
     * Update all of the elements of a Book Rating.
     *
     * @return Book Rating Domain Object
     * @throws NoSuchElementException if no Book found.
     */
    public Rating updateSome(RatingDto ratingDto, Integer bookId)
            throws NoSuchElementException {
        LOGGER.info("Update read status for book {} of user {}", bookId, LoginUser.get().getId());
        Rating rating = verifyBookAndUser(bookService.verifyBook(bookId), LoginUser.get());
        if (ratingDto.getRank() != null) {
            rating.setRank(ratingDto.getRank());
        }
        if (ratingDto.getComment() != null) {
            rating.setComment(ratingDto.getComment());
        }
        if (ratingDto.getRead() != null) {
            rating.setReadStatus(ratingDto.getRead());
        }
        return ratingRepo.save(rating);
    }

    /**
     * Delete a Book Rating.
     *
     * @param bookId book identifier
     * @throws NoSuchElementException if no Book found.
     */
    public void delete(int bookId) throws NoSuchElementException {
        LOGGER.info("Delete Rating for book {} and user {}", bookId, LoginUser.get().getId());
        Rating rating = verifyBookAndUser(bookService.verifyBook(bookId), LoginUser.get());
        ratingRepo.delete(rating);
    }

    /**
     * Get the average rank of a book.
     *
     * @param bookId book identifier
     * @return average rank as a Double.
     * @throws NoSuchElementException
     */
    public Double getAverageScore(int bookId) throws NoSuchElementException {
        LOGGER.info("Get average rank of book {}", bookId);
        List<Rating> ratings = ratingRepo.findByBookId(bookService.verifyBook(bookId).getId());
        OptionalDouble average = ratings.stream().mapToInt((rating) -> rating.getRank()).average();
        return average.isPresent() ? average.getAsDouble() : null;
    }


    /**
     * Verify and return the Rating for a particular bookId and Customer
     *
     * @param book
     * @param ranker
     * @return the found Rating
     * @throws NoSuchElementException if no Rating found
     */
    public Rating verifyBookAndUser(Book book, User ranker) throws NoSuchElementException {
        return ratingRepo.findByBookAndUser(book, ranker).orElseThrow(() ->
                new NoSuchElementException("Book-Rating pair for request("
                        + book.getId() + " for user" + ranker.getId()));
    }


}

