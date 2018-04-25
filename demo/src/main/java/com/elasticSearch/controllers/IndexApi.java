package com.elasticSearch.controllers;

import static org.mockito.Mockito.doCallRealMethod;

import java.io.File;
import java.io.IOException;


import javax.xml.ws.Response;

import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.elasticSearch.domain.Person;
import com.elasticSearch.domain.UploadFile;
import com.elasticSearch.services.IndexAPIServices;
import com.elasticSearch.utils.ResourceBuilder;
import com.elasticSearch.utils.RestHighClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
public class IndexApi {

	@Autowired
	IndexAPIServices indexService;
	
	@Autowired
	ResourceBuilder builder;

	@PostMapping(value = "/insertSingle")
	public ResponseEntity<?> singleRecord(@RequestBody Person person) throws IOException {

		String sucess = null;
	 Person per=	indexService.insertSingleDocument(person).get();
		

		return new ResponseEntity<>(per, HttpStatus.ACCEPTED);
	}

	@PostMapping("/insertBulk")
	public ResponseEntity<?> insertBulk(@ModelAttribute UploadFile file) throws IllegalStateException, IOException {

	
		BulkResponse response = indexService.insertBulk(file.getFile());

		//return new Response().
		
		return response.hasFailures() ? new ResponseEntity<>("there are failures", HttpStatus.MULTI_STATUS)
				: new ResponseEntity<>("no failures", HttpStatus.ACCEPTED);

	}

}
