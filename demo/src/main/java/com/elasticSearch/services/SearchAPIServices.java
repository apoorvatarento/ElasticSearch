package com.elasticSearch.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.range.ParsedDateRange;
import org.elasticsearch.search.aggregations.bucket.range.RangeAggregator.Range;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.elasticSearch.domain.Person;
import com.elasticSearch.utils.ResourceBuilder;
import com.elasticSearch.utils.RestHighClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.Builder;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service(value="seachApiService")
public class SearchAPIServices {

	@Autowired
	RestHighClient client;
	@Autowired
	ResourceBuilder builder;

	private RestHighLevelClient restClient;

	@Value("${index}")
	String index;
	@Value("${type}")
	String type;

	public List fetchAll() {

		restClient = client.getClient();
		SearchRequest request = new SearchRequest(index).types(type);

		ObjectMapper mapper = new ObjectMapper();
		List<Person> listOfPerson = new ArrayList();

		SearchSourceBuilder builder = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery());
		request.source(builder);
		try {
			SearchResponse response = restClient.search(request);

			SearchHits hits = response.getHits();

			hits.forEach((hit) -> {
				try {

					listOfPerson.add(mapper.readValue(hit.getSourceAsString(), Person.class));

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
			});
			return listOfPerson;

		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}

	}

	public List fetchByName(String name) {

		restClient = client.getClient();

		List founddata = new ArrayList();

		SearchRequest request = new SearchRequest(index).types(type);

		SearchSourceBuilder builder = new SearchSourceBuilder().query(QueryBuilders.commonTermsQuery("name", name));

		request.source(builder);

		try {
			SearchResponse response = restClient.search(request);

			response.getHits().forEach((hit) -> {
				ObjectMapper mapper = new ObjectMapper();

				try {
					founddata.add(mapper.readValue(hit.getSourceAsString(), Person.class));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});

			return founddata;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public Optional<Person> fetchById(String docid) throws IOException {

		restClient = client.getClient();
		ObjectMapper mapper = new ObjectMapper();
		GetRequest request = new GetRequest(index, type, docid);

		GetResponse response;
		response = restClient.get(request);
		restClient.close();

		return Optional.of(builder.addLinks(mapper.readValue(response.getSourceAsString(), Person.class)));

	}

	public ObjectNode fetchHrs() throws IOException {
		restClient = client.getClient();

		ObjectMapper mapper = new ObjectMapper();
		ObjectNode result=mapper.createObjectNode();
		ObjectNode object = mapper.createObjectNode();
		
		SearchRequest request = new SearchRequest(index).types(type);

		SearchSourceBuilder builder = new SearchSourceBuilder().size(0).
				aggregation(AggregationBuilders.terms("by_domain").field("domain.keyword")
						.subAggregation(AggregationBuilders.dateRange("daterange").field("date")
						.addUnboundedFrom(DateTime.parse("12-01-2017", DateTimeFormat.forPattern("dd-MM-yyyy")))
						.subAggregation(AggregationBuilders.sum("total_hrs").field("hrs")).subAggregation(AggregationBuilders.avg("avg_hrs").field("hrs"))));
		
		
	
		request.source(builder);
		SearchResponse response = restClient.search(request);
		System.out.println(response);
		Terms terms = response.getAggregations().get("by_domain");

		terms.getBuckets().forEach((bucket) -> {

			ParsedDateRange range = bucket.getAggregations().get("daterange");
			range.getBuckets().forEach((_bucket) -> {
				Sum sum = _bucket.getAggregations().get("total_hrs");
				Avg avg=_bucket.getAggregations().get("avg_hrs");

				object.put(sum.getName(), sum.getValue());
				object.put(avg.getName(), avg.getValue());
				result.put(bucket.getKeyAsString(), object);
			});

		});
		return result;
	}
}
