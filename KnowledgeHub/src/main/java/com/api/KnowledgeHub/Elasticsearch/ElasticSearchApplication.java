package com.api.KnowledgeHub.Elasticsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.admin.indices.refresh.RefreshResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class ElasticSearchApplication {

	private static RestHighLevelClient client = null;
	private static boolean exists = true;


	public static void startConnect() {
		System.out.println("----------Welcome to ElasticSearch");

		client = new RestHighLevelClient(
				RestClient.builder(
						new HttpHost("localhost", 9200, "http"),
						new HttpHost("localhost", 9201, "http")));
	}


	private static void stopConnect() throws IOException {
		client.close();
	}


	public static boolean indexExist(String index) throws IOException {
		GetIndexRequest getIndexRequest = new GetIndexRequest();
		getIndexRequest.indices(index);

		exists = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
		System.out.println("----------Index already exist: " + exists);

		return exists;
	}


	public static void createIndex(String index, String type) throws IOException {
		CreateIndexRequest createIndexRequest = new CreateIndexRequest(index);

		XContentBuilder builder = XContentFactory.jsonBuilder();
		builder.startObject();
		{
			builder.startObject(type);
			{
				builder.startObject("properties");
				{
					builder.startObject("user");
					{
						builder.field("type", "text");
					}
					builder.endObject();
					builder.startObject("title");
					{
						builder.field("type", "text");
					}
					builder.endObject();
					builder.startObject("author");
					{
						builder.field("type", "text");
					}
					builder.endObject();
					builder.startObject("description");
					{
						builder.field("type", "text");
					}
					builder.endObject();
					builder.startObject("documentType");
					{
						builder.field("type", "text");
					}
					builder.endObject();
					builder.startObject("file_name");
					{
						builder.field("type", "text");
					}
					builder.endObject();
					builder.startObject("file_path");
					{
						builder.field("type", "text");
					}
					builder.endObject();
					builder.startObject("data");
					{
						builder.field("type", "text");
					}
					builder.endObject();
				}
				builder.endObject();
			}
			builder.endObject();
		}
		builder.endObject();
		createIndexRequest.mapping(type, builder);

		/*
		Map<String, Object> jsonMap = new HashMap<>();
			Map<String, Object> doc = new HashMap<>();

				Map<String, Object> properties = new HashMap<>();

					Map<String, Object> user = new HashMap<>();
					user.put("type", "text");
					Map<String, Object> title = new HashMap<>();
					title.put("type", "text");
					Map<String, Object> author = new HashMap<>();
					author.put("type", "text");
					Map<String, Object> reference = new HashMap<>();
					reference.put("type", "text");

				properties.put("user", user);
				properties.put("title", title);
				properties.put("author", author);
				properties.put("reference", reference);

			doc.put("properties", properties);

		jsonMap.put(type, doc);
		request.mapping(type, jsonMap);
		 */

		/*
		request.mapping(type,
        "{\n" +
        "  \"_doc\": {\n" +
        "    \"properties\": {\n" +
        "      \"user\": {\n" +
        "        \"type\": \"text\"\n" +
        "      }\n" +
        "      \"title\": {\n" +
        "        \"type\": \"text\"\n" +
        "      }\n" +
        "      \"author\": {\n" +
        "        \"type\": \"text\"\n" +
        "      }\n" +
        "      \"reference\": {\n" +
        "        \"type\": \"text\"\n" +
        "      }\n" +
        "    }\n" +
        "  }\n" +
        "}",
        XContentType.JSON);
		 */

		CreateIndexResponse createIndexResponse = client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
	}


	public static boolean documentExist(String index, String type, String id) throws IOException {
		GetRequest getRequest = new GetRequest(index, type, id);
		getRequest.fetchSourceContext(new FetchSourceContext(false));
		getRequest.storedFields("_none_");

		boolean exists = client.exists(getRequest, RequestOptions.DEFAULT);

		return exists;
	}


	public static long countDocument(String index) throws IOException {
		CountRequest countRequest = new CountRequest(index)
				.indicesOptions(IndicesOptions.lenientExpandOpen())
				.preference("_local");

		CountResponse countResponse = client.count(countRequest, RequestOptions.DEFAULT);

		long count = countResponse.getCount();

		return count;
	}


	public static IndexResponse indexDocument(String index, String type, String id, String user, String title, String author, String docType, String description, String file_name, String file_path, String data) throws IOException {
		//Index
		IndexRequest indexRequest = new IndexRequest(index, type, id)
				.source("user", user,
						"title", title,
						"author", author,
						"documentType", docType,
						"description", description,
						"file_name", file_name,
						"file_path", file_path,
						"data", data)
				.opType(DocWriteRequest.OpType.CREATE);				//can't update

		//Index Response	(try - create new document, catch - check whether the document is already created)
		IndexResponse indexResponse = null;
		try {
			indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);

			String _index = indexResponse.getIndex();
			String _type = indexResponse.getType();
			String _id = indexResponse.getId();
			long _version = indexResponse.getVersion();
			String _result = "";
			if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
				_result = "CREATED";
			} else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
				_result = "UPDATED";
			}
			ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
			if (shardInfo.getTotal() != shardInfo.getSuccessful()) {

			}
			if (shardInfo.getFailed() > 0) {
				for (ReplicationResponse.ShardInfo.Failure failure :
						shardInfo.getFailures()) {
					String reason = failure.reason();
				}
			}

			System.out.println("----------index : " + _index);
			System.out.println("----------type : " + _type);
			System.out.println("----------id : " + _id);
			System.out.println("----------version : " + _version);
			System.out.println("----------result : " + _result);
		} catch(ElasticsearchException e) {
			if (e.status() == RestStatus.CONFLICT) {
				System.out.println("----------Document is already created...Can't update");
			}
		}

		return indexResponse;
	}


	public static void refresh(String index) throws IOException {
		try {
			RefreshRequest refresh = new RefreshRequest(index);
			RefreshResponse refreshResponse = client.indices().refresh(refresh, RequestOptions.DEFAULT);
		} catch(ElasticsearchException e) {
			if(e.status() == RestStatus.NOT_FOUND) {
				System.out.println("----------No index to refresh");
			}
		}
	}


	public static DeleteResponse delete(String index, String type, String id) throws IOException {
		DeleteRequest request = new DeleteRequest(index, type, id);

		DeleteResponse deleteResponse = client.delete(request, RequestOptions.DEFAULT);

		System.out.println(deleteResponse);

		return deleteResponse;
	}


	public static SearchHit[] search(String index, String fieldName, String value) throws IOException {
		QueryBuilder matchQueryBuilder = QueryBuilders.matchPhrasePrefixQuery(fieldName, value).analyzer("standard");
																							//Search queries are created using QueryBuilder objects.
																							//Search in one field with phrase_prefix type
		HighlightBuilder highlightBuilder = new HighlightBuilder();									//Highlighting search results can be achieved by setting a HighlightBuilder on the SearchSourceBuilder.
		HighlightBuilder.Field highlightTitle = new HighlightBuilder.Field(fieldName);				//Create a field highlighter for the title field
		highlightTitle.highlighterType("unified");													//Set the field highlighter type
		highlightBuilder.field(highlightTitle);														//Add the field highlighter to the highlight builder


		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();						//Most options controlling the search behavior can be set on the SearchSourceBuilder, which contains more or less the equivalent of the options in the search request body of the Rest API.
		sourceBuilder.query(matchQueryBuilder);
		sourceBuilder.from(0);
		sourceBuilder.size(100);
		sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
		sourceBuilder.highlighter(highlightBuilder);												//Add the highlight builder to source builder


		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices(index);
		searchRequest.source(sourceBuilder);

		SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);


		//To get access to the returned documents
		SearchHits hits = searchResponse.getHits();											//Retrieving SearchHits

		long totalHits = hits.getTotalHits();												//The SearchHits provides global information about all hits, like total number of hits or the maximum score
		float maxScore = hits.getMaxScore();
		System.out.println("----------totalHits : " + totalHits);
		System.out.println("----------maxScore : " + maxScore + "\n");

		SearchHit[] searchHits = hits.getHits();											//Nested inside the SearchHits are the individual search results that can be iterated over
		for (SearchHit hit : searchHits) {
			String indexname = hit.getIndex();													//The SearchHit provides access to basic information like index, type, docId and score of each search hit
			String type = hit.getType();
			String id = hit.getId();
			float score = hit.getScore();
			long version = hit.getVersion();

			Map<String, Object> sourceAsMap = hit.getSourceAsMap();							//Furthermore, it lets you get back the document source, either as a simple JSON-String or as a map of key/value pairs.
			String user = (String) sourceAsMap.get("user");
			String title = (String) sourceAsMap.get("title");
			String author = (String) sourceAsMap.get("author");
			//OR
			//String sourceAsString = hit.getSourceAsString();
			//System.out.println("----------sourceAsString : " + sourceAsString);

			Map<String, HighlightField> highlightFields = hit.getHighlightFields();					//Retrieving Highlighting
			HighlightField highlight = highlightFields.get(fieldName);								//Get the highlighting for the title field
			Text[] fragments = highlight.fragments();												//Get one or many fragments containing the highlighted field content
			String fragmentString = fragments[0].string();


			//?? List<Object> users = (List<Object>) sourceAsMap.get("user");
			//?? Map<String, Object> innerObject = (Map<String, Object>) sourceAsMap.get("innerObject");

			System.out.println("----------index : " + indexname);
			System.out.println("----------type : " + type);
			System.out.println("----------id : " + id);
			System.out.println("----------score : " + score);
			System.out.println("----------version : " + version);
			System.out.println("----------user : " + user);
			System.out.println("----------title : " + title);
			System.out.println("----------author : " + author);
			System.out.println("----------fragmentString : " + fragmentString + "\n");

		}

		return searchHits;
	}


	public static SearchHit[] multiFieldSearch(String index, String value) throws IOException {
		QueryBuilder matchQueryBuilder = QueryBuilders.multiMatchQuery(value, "user", "author", "title","documentType", "description", "file_name", "data").type("phrase_prefix");
																							//Search queries are created using QueryBuilder objects.
																							//Search in multi fields with phrase_prefix type

		HighlightBuilder highlightBuilder = new HighlightBuilder();									//Highlighting search results can be achieved by setting a HighlightBuilder on the SearchSourceBuilder.
		HighlightBuilder.Field highlightUser = new HighlightBuilder.Field("user");			//Create a field highlighter for the user field
		highlightUser.highlighterType("unified");													//Set the field highlighter type
		highlightBuilder.field(highlightUser);														//Add the field highlighter to the highlight builder
		HighlightBuilder.Field highlightTitle = new HighlightBuilder.Field("title");
		highlightTitle.highlighterType("unified");
		highlightBuilder.field(highlightTitle);
		HighlightBuilder.Field highlightAuthor = new HighlightBuilder.Field("author");
		highlightAuthor.highlighterType("unified");
		highlightBuilder.field(highlightAuthor);
		HighlightBuilder.Field highlightDocumentType = new HighlightBuilder.Field("documentType");
		highlightDocumentType.highlighterType("unified");
		highlightBuilder.field(highlightDocumentType);
		HighlightBuilder.Field highlightDescription = new HighlightBuilder.Field("description");
		highlightDescription.highlighterType("unified");
		highlightBuilder.field(highlightDescription);
		HighlightBuilder.Field highlightFileName = new HighlightBuilder.Field("file_name");
		highlightFileName.highlighterType("unified");
		highlightBuilder.field(highlightFileName);
		HighlightBuilder.Field highlightData = new HighlightBuilder.Field("data");
		highlightFileName.highlighterType("unified");
		highlightBuilder.field(highlightData);

		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();						//Most options controlling the search behavior can be set on the SearchSourceBuilder, which contains more or less the equivalent of the options in the search request body of the Rest API.
		sourceBuilder.query(matchQueryBuilder);
		sourceBuilder.highlighter(highlightBuilder);												//Add the highlight builder to source builder
		sourceBuilder.from(0);
		sourceBuilder.size(100);
		sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));


		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices(index);
		searchRequest.source(sourceBuilder);

		SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);


		//To get access to the returned documents
		SearchHits hits = searchResponse.getHits();											//Retrieving SearchHits

		long totalHits = hits.getTotalHits();												//The SearchHits provides global information about all hits, like total number of hits or the maximum score
		float maxScore = hits.getMaxScore();
		System.out.println("----------totalHits : " + totalHits);
		System.out.println("----------maxScore : " + maxScore + "\n");

		SearchHit[] searchHits = hits.getHits();											//Nested inside the SearchHits are the individual search results that can be iterated over
		for (SearchHit hit : searchHits) {
			String indexname = hit.getIndex();													//The SearchHit provides access to basic information like index, type, docId and score of each search hit
			String type = hit.getType();
			String id = hit.getId();
			float score = hit.getScore();
			long version = hit.getVersion();

			Map<String, Object> sourceAsMap = hit.getSourceAsMap();							//Furthermore, it lets you get back the document source, either as a simple JSON-String or as a map of key/value pairs.
			String user = (String) sourceAsMap.get("user");
			String title = (String) sourceAsMap.get("title");
			String author = (String) sourceAsMap.get("author");
			//OR
			//String sourceAsString = hit.getSourceAsString();
			//System.out.println("----------sourceAsString : " + sourceAsString);

			Map<String, HighlightField> highlightFields = hit.getHighlightFields();					//Retrieving Highlighting
			HighlightField highlightUserField = highlightFields.get("user");						//Get the highlighting for the user field
			HighlightField highlightTitleField = highlightFields.get("title");						//Get the highlighting for the title field
			HighlightField highlightAuthorField = highlightFields.get("author");					//Get the highlighting for the author field
			String fragmentString = null;
			Text[] fragments;
			try {
				if(highlightUserField.fragments() != null) {
					System.out.println("user");
					fragments = highlightUserField.fragments();                                   	//Get one or many fragments containing the highlighted field content
					fragmentString = fragments[0].string();
//					for (Text text: fragments){
//						fragmentString += text;
//					}
				}
				if(highlightTitleField.fragments() != null) {
					System.out.println("title");
					fragments = highlightTitleField.fragments();                                   	//Get one or many fragments containing the highlighted field content
					fragmentString = fragments[0].string();
//					for (Text text: fragments){
//						fragmentString += text;
//					}
				}
				if(highlightAuthorField.fragments() != null) {
					System.out.println("author");
					fragments = highlightAuthorField.fragments();                                	//Get one or many fragments containing the highlighted field content
					fragmentString = fragments[0].string();
//					for (Text text: fragments){
//						fragmentString += text;
//					}
				}
			} catch (NullPointerException e) {

			}



			//?? List<Object> users = (List<Object>) sourceAsMap.get("user");
			//?? Map<String, Object> innerObject = (Map<String, Object>) sourceAsMap.get("innerObject");

			System.out.println("----------index : " + indexname);
			System.out.println("----------type : " + type);
			System.out.println("----------id : " + id);
			System.out.println("----------score : " + score);
			System.out.println("----------version : " + version);
			System.out.println("----------user : " + user);
			System.out.println("----------title : " + title);
			System.out.println("----------author : " + author);
			System.out.println("----------fragmentString : " + fragmentString + "\n");
		}

		return searchHits;
	}




	public static void main(String[] args) throws IOException {

        System.out.println("----------Welcome to ElasticSearch");
		//StartUp
		startConnect();


		//Check for index exist & if no, create the index
		if(!indexExist("post")) {
			System.out.println("----------Creating index");
			createIndex("post", "_doc");
		}


		//Check for document exist & if no, add a document
		System.out.println("----------Adding new document");
		indexDocument("post", "_doc", "8", "Kapil R kapil", "UOP Knowledge HUB", "Thenu", "Report", "www.wikipedia.com", "aaaa", "sss", "egsr");
		indexDocument("post", "_doc", "1", "Thaksha", "HUB", "Thakshajini", "Proposal", "www.wikipedia.com", "bbbb", "ddd", "setgb");


		//Refresh
		System.out.println("----------Refreshing index");
		refresh("post");


		//Search
		System.out.println("----------Searching documents");
		search("post", "title", "KnowLedgE hU");

		System.out.println("----------Searching documents");
		search("post", "user", "ThAk");

		System.out.println("----------Searching documents");
		search("post", "title", "hU");


		//MultiFieldSearch
		System.out.println("----------Multi Field Searching documents");
		multiFieldSearch("post", "uop k");












		//End
		stopConnect();

		//SpringApplication.run(ElasticSearchApplication.class, args);
	}

}
