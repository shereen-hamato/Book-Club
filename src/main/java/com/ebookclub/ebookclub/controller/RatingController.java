package com.ebookclub.ebookclub.controller;

import com.ebookclub.ebookclub.dto.RatingDto;
import com.ebookclub.ebookclub.model.Rating;
import com.ebookclub.ebookclub.service.RatingService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.AbstractMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Book Rating Controller
 *
 * Created by Shereen
 */
@RestController
@RequestMapping(path = "/ratings/{bookId}")
public class RatingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RatingController.class);

    @Autowired
    private RatingService ratingService;
    @Autowired
    private ModelMapper modelMapper;


    /**
     * Lookup a the Ratings for a book.
     *
     * @param bookId
     * @param pageable
     * @return
     */
    @GetMapping
    public List<RatingDto> getAllRatingsForBook(@PathVariable(value = "bookId") int bookId, Pageable pageable) {
        LOGGER.info("GET /ratings/{}", bookId);
        List<Rating> ratingPage = ratingService.lookupRatings(bookId);

        return ratingPage.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * Calculate the average Score of a Book.
     *
     * @param bookId
     * @return Tuple of "average" and the average value.
     */
    @GetMapping("/average")
    public AbstractMap.SimpleEntry<String, Double> getAverage(@PathVariable(value = "bookId") int bookId) {
        LOGGER.info("GET /ratings/{}/average", bookId);
        return new AbstractMap.SimpleEntry<String, Double>("average", ratingService.getAverageScore(bookId));
    }

    /**
     * Create a Book Rating.
     *
     * @param bookId
     * @param ratingDto
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RatingDto createRating(@PathVariable(value = "bookId") int bookId, @RequestBody @Validated RatingDto ratingDto) {
        LOGGER.info("POST /ratings/{}", bookId);
        return convertToDto(ratingService.createNew(ratingDto, bookId));
    }

    /**
     * Update rank and comment of a Book Rating
     *
     * @param bookId
     * @param ratingDto
     * @return The modified Rating DTO.
     */
    @PutMapping
    public RatingDto updateWithPut(@PathVariable(value = "bookId") int bookId, @RequestBody @Validated RatingDto ratingDto) {
        LOGGER.info("PUT /ratings/{}", bookId);
        return convertToDto(ratingService.update(ratingDto, bookId));
    }
    /**
     * Update read status, rank or comment of a Book Rating
     *
     * @param bookId
     * @param ratingDto
     * @return The modified Rating DTO.
     */
    @PatchMapping
    public RatingDto updateWithPatch(@PathVariable(value = "bookId") int bookId, @RequestBody @Validated RatingDto ratingDto) {
        LOGGER.info("PATCH /ratings/{}", bookId);
        return convertToDto(ratingService.updateSome(ratingDto, bookId));
    }

    /**
     * Delete a Rating of a book made by a customer
     *
     * @param bookId
     * @param userId
     */
    @DeleteMapping("/{userId}")
    public void delete(@PathVariable(value = "bookId") int bookId, @PathVariable(value = "userId") int userId) {
        LOGGER.info("DELETE /ratings/{}/{}", bookId, userId);
        ratingService.delete(bookId, userId);
    }

    /**
     * Convert the Rating entity to a RatingDto
     *
     * @param rating
     * @return RatingDto
     */
    private RatingDto convertToDto(Rating rating) {
        RatingDto ratingDto= modelMapper.map(rating, RatingDto.class);
        ratingDto.setRankerId(Math.toIntExact(rating.getUser().getId()));
        return ratingDto;
    }





}
