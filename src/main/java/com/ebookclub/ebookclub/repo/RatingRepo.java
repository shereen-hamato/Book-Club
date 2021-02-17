package com.ebookclub.ebookclub.repo;

import com.ebookclub.ebookclub.model.Book;
import com.ebookclub.ebookclub.model.Rating;
import com.ebookclub.ebookclub.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepo extends PagingAndSortingRepository<Rating, Integer> {
    Page<Rating> findByBookId(Integer id, Pageable pageable);

    List<Rating> findByBookId(Integer id);

    Optional<Rating> findByBookAndUser(Book book, User user);
}
