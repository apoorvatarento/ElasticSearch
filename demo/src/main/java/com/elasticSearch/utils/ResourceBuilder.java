package com.elasticSearch.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.elasticSearch.controllers.IndexApi;
import com.elasticSearch.controllers.SearchApi;
import com.elasticSearch.domain.Person;

@Component
public class ResourceBuilder {

	
	@Autowired
	private EntityLinks links;
	
	public Person addLinks(Person per) {
		
		ResponseEntity<?> fetchByName=ControllerLinkBuilder.methodOn(SearchApi.class).fetchByName(per.getName());
	//	ResponseEntity<?> fetchById=ControllerLinkBuilder.methodOn(SearchApi.class).fetchById()
		Link LinkName=ControllerLinkBuilder.linkTo(fetchByName).withRel("fetchByName");
	//	Link LinkId=ControllerLinkBuilder.linkTo(fetchById).
		per.add(LinkName);
		
		return per;
		
	}
}
