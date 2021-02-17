package com.ebookclub.ebookclub.repo;

import com.ebookclub.ebookclub.model.Genre;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepo extends PagingAndSortingRepository<Genre, Integer> {
}
