package com.khelomore.graphql.service.datafetcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.khelomore.graphql.model.Book;
import com.khelomore.graphql.repository.BookRepository;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

@Component
public class BookTitleDataFetcher implements DataFetcher<Book>{
	@Autowired
	BookRepository bookRepo;
	
	@Override
	public Book get(DataFetchingEnvironment environment) {
		String isn = environment.getArgument("title");
		return bookRepo.findByTitle(isn);
	}
	
}
