package com.elasticSearch.controllers;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.elasticSearch.domain.Person;
import com.elasticSearch.services.SearchAPIServices;
import com.elasticSearch.utils.ResourceBuilder;

@RestController

public class SearchApi {

	@Autowired
	SearchAPIServices services;


	@GetMapping("/fetch")
	public ResponseEntity<List<Person>> fetchAll() {

		List data = services.fetchAll();

		if (data.isEmpty()) {
			return null;
		} else {
			return new ResponseEntity<List<Person>>(data, HttpStatus.ACCEPTED);
		}

	}

	@GetMapping("/fetchByName/{name}")
	public ResponseEntity<?> fetchByName(@PathVariable(value = "name") String name) {

		List data = services.fetchByName(name);

		return data.isEmpty() ? new ResponseEntity<>("no data found", HttpStatus.NOT_FOUND)
				: new ResponseEntity<>(data, HttpStatus.FOUND);

	}
	
	@GetMapping("/fetchById/{id}")
	public ResponseEntity<?> fetchById(@PathVariable(value="id") String id) throws IOException{
		Optional<Person> per=services.fetchById(id);
		return per.isPresent()? new ResponseEntity<>(per.get(), HttpStatus.FOUND):new ResponseEntity<>("not found",HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/fetchHrs")
	public ResponseEntity<?> fetchHrs() throws IOException{
		services.fetchHrs();
		 return new ResponseEntity<>(services.fetchHrs(), HttpStatus.ACCEPTED);
	}

}
