package com.khelomore.graphql.service;



import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.khelomore.graphql.service.datafetcher.AllBooksDataFetcher;
import com.khelomore.graphql.service.datafetcher.BookDataFetcher;
import com.khelomore.graphql.service.datafetcher.BookTitleDataFetcher;
import com.khelomore.graphql.model.Book;
import com.khelomore.graphql.repository.BookRepository;

import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

@Service
public class GraphQLService {
	
	private GraphQL graphQL;
	
	@Value("classpath:books.graphql")
	Resource schemaResource;

	@Autowired
	BookRepository bookRepo;
	
	@Autowired
	private AllBooksDataFetcher allBooksDataFetcher;

	@Autowired
	private BookDataFetcher bookDataFetcher;

	@Autowired
	private BookTitleDataFetcher bookTitleDataFetcher;
	
	//This is called when graphql serice innitialized
	@PostConstruct
	private void loadSchema() throws IOException {
		//Load Books Into the Book Repository
		loadDataIntoHSQL();
		
		//Get the schema
		File schemaFile = schemaResource.getFile();
		
		//parse schema
		TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schemaFile);
		RuntimeWiring wiring = buildRuntimeWiring();
		GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry, wiring);
		graphQL = GraphQL.newGraphQL(schema).build();
	}

	

	private RuntimeWiring buildRuntimeWiring() {
			return RuntimeWiring.newRuntimeWiring()
					.type("Query", typeWiring -> 
						typeWiring.dataFetcher("allBooks", allBooksDataFetcher)
								  .dataFetcher("book", bookDataFetcher)
					              .dataFetcher("bookTitle", bookTitleDataFetcher))
								  .build();
					
					
	}
	
	public GraphQL getGraphQL() {return graphQL;}
	
	private void loadDataIntoHSQL() {
		Stream.of(
				new Book("123","Immortals Of Meluha","Westland Press",
						new String[] {
								"Amish Tripathi"
						}, "February 2010"),
				new Book("456","Death"," Penguin Ananda",
						new String[] {
								"Amish Tripathi"
						}, "February 2020"),
				new Book("789","Secrets Of Nagas","Westland Press",
						new String[] {
								"Amish Tripathi"
						}, "August 2011")
				).forEach(book ->{
			bookRepo.save(book);
		});
	}
}
