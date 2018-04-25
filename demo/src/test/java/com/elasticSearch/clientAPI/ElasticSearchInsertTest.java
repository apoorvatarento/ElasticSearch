package com.elasticSearch.clientAPI;


import static org.junit.Assert.assertEquals;

import org.elasticsearch.action.index.IndexResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.elasticSearch.controllers.IndexApi;
import com.elasticSearch.domain.Person;
import com.elasticSearch.utils.RestHighClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class) // launch spring test context framework
@WebMvcTest(IndexApi.class) // focuses on IndexApi controller only
public class ElasticSearchInsertTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	RestHighClient client;

	@Test
	public void insertData() throws Exception {
		Person per = new Person();
		ObjectMapper mapper = new ObjectMapper();

	RequestBuilder requestBuilder=	MockMvcRequestBuilders.post("/insertSingle").accept(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(per)).contentType(MediaType.APPLICATION_JSON);

MvcResult result=mockMvc.perform(requestBuilder).andReturn();
MockHttpServletResponse response=result.getResponse();
assertEquals(HttpStatus.CREATED.value(), response.getStatus());

	
	}

}
