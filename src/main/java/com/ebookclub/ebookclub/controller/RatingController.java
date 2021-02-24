package com.ebookclub.ebookclub.controller;

import com.ebookclub.ebookclub.dto.RatingDto;
import com.ebookclub.ebookclub.model.Rating;
import com.ebookclub.ebookclub.security.LoginUser;
import com.ebookclub.ebookclub.service.RatingService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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


    private RatingService ratingService;

    private ModelMapper modelMapper;


    @Autowired
    public RatingController(RatingService ratingService, ModelMapper modelMapper) {
        this.ratingService = ratingService;
        this.modelMapper = modelMapper;
    }




    /**
     * Lookup a the Ratings for a book.
     *
     * @param bookId
     * @return
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RatingDto> getAllRatingsForBook(@PathVariable(value = "bookId") int bookId) {
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
    @ResponseStatus(HttpStatus.OK)
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
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
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
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
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
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public RatingDto updateWithPatch(@PathVariable(value = "bookId") int bookId, @RequestBody @Validated RatingDto ratingDto) {
        LOGGER.info("PATCH /ratings/{}", bookId);
        return convertToDto(ratingService.updateSome(ratingDto, bookId));
    }

    /**
     * Delete a Rating of a book made by a customer
     *
     * @param bookId
     */
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "", authorizations = { @Authorization(value="jwtToken") })
    public void delete(@PathVariable(value = "bookId") int bookId) {
        LOGGER.info("DELETE /ratings/{}/{}", bookId, LoginUser.get().getId());
        ratingService.delete(bookId);
    }

    /**
     * Convert the Rating entity to a RatingDto
     *
     * @param rating
     * @return RatingDto
     */
    private RatingDto convertToDto(Rating rating) {
        RatingDto ratingDto= modelMapper.map(rating, RatingDto.class);
        return ratingDto;
    }





}
