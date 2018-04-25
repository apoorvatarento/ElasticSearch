package com.elasticSearch.utils;

import org.apache.http.HttpHost;

import org.elasticsearch.client.RestClient;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Component;


@Component
public class RestHighClient {

	private RestHighLevelClient client;

	public RestHighLevelClient getClient() {
		this.client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
		return client;
	}

	public void setClient(RestHighLevelClient client) {
		
		this.client = client;
	}
	
	

}
