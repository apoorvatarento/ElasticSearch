package com.elasticSearch.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.elasticSearch.controllers.SearchApi;
import com.elasticSearch.domain.Person;
import com.elasticSearch.utils.RestHighClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service(value="indexService")
public class IndexAPIServices {

	@Autowired
	RestHighClient client;
	
	@Autowired
	SearchAPIServices seachApiService;
	
	private static AtomicLong id=new AtomicLong(1);

	private RestHighLevelClient restClient;

	@Value("${index}")
	String index;
	@Value("${type}")
	String type;

	public Optional<Person> insertSingleDocument(Person person) {
		restClient = client.getClient();
		ObjectMapper mapper = new ObjectMapper();
		try {
			IndexRequest request = new IndexRequest(index,type, String.valueOf(id.getAndIncrement()));
			request.source(mapper.writeValueAsString(person), XContentType.JSON);
			IndexResponse response = restClient.index(request);
			restClient.close();
		String index=	response.getId();
		return seachApiService.fetchById(index);
		
		
			

		} catch (Exception e) {

			e.printStackTrace();
		}
		return null;
	}
	
	public BulkResponse insertBulk(MultipartFile _file) throws IOException {
		
		restClient =client.getClient();
		
		BulkRequest bulkRequest = new BulkRequest();

		BufferedReader br = new BufferedReader(new InputStreamReader(_file.getInputStream()));

		List<Person> listOfNames = br.lines().filter((line)->line.trim().equals("")?false:true).map(objConverter).collect(Collectors.toList());
		listOfNames.forEach((person) -> {

			ObjectMapper obj = new ObjectMapper();
			String entity;

			try {
				entity = obj.writeValueAsString(person);
				
				
				String value= String.valueOf(id.getAndIncrement());
				
				IndexRequest request = new IndexRequest(index, type,value)
						.source(entity, XContentType.JSON);
				bulkRequest.add(request);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
		BulkResponse response=restClient.bulk(bulkRequest);
	
		restClient.close();
return response;
		
		
	}
	
	public static Function<String, Person> objConverter = (line) -> {

		String[] data = line.split(",");
	
		
		Person per = null;
		
			per = new Person(data[0],data[1],Integer.valueOf(data[2]),data[3]);
		

		return per;

	};

}
