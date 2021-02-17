package com.ebookclub.ebookclub.repo;

import com.ebookclub.ebookclub.model.Book;
import com.ebookclub.ebookclub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepo extends JpaRepository<Book, Integer> {
    Optional<Book> findByIdAndBookOwner(int id, User bookOwner);
}
