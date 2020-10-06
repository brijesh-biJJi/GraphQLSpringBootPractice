package com.khelomore.graphql.repository;

import com.khelomore.graphql.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, String> {

	Book findByTitle(String isn);
}
