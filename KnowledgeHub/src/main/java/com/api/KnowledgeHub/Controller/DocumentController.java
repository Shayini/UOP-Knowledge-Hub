package com.api.KnowledgeHub.Controller;


import com.api.KnowledgeHub.Elasticsearch.ElasticSearchApplication;
import com.api.KnowledgeHub.Model.Document;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.search.SearchHit;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@EnableAutoConfiguration
@CrossOrigin(origins = "http://localhost:8081")
@RestController //Encapsulating @Controller + @ResponseBody annotations,@RestController means that this class is a Controller.
@RequestMapping(path="/Project/REST-API") //This means URL's start with /Project/REST-API (after Application path).
public class DocumentController extends ElasticSearchApplication {
    private String index = "document";
    private String type = "_doc";

    @PostMapping("/upload") // Map ONLY POST Requests and means URL's start with /signin (after Application path).
    @ResponseBody // @ResponseBody means the returned Integer is the response, not a view Id.
    public IndexResponse upload(@RequestBody Document document) throws IOException { //@RequestBody annotation is used to bind the HTTP request/response body with a domain object in method parameter or return type.
        //Check for index exist & if no, create the index
        if(!indexExist(index)) {
            System.out.println("----------Creating index");
            createIndex(index, type);
        }

//        long count = countDocument(index);
        long ids = 1;
//        System.out.println(documentExist(index, type, Long.toString(ids)));
        //Check for document exist
        for(long count=1; documentExist(index, type, Long.toString(count)); count++) {
            ids = count + 1;
        }
        String id = Long.toString(ids);
//        System.out.println(id);

        //Add new document
        System.out.println("----------Adding new document");
        IndexResponse response = indexDocument(index, type, id, document.getUsername(), document.getTitle(), document.getAuthor(), document.getDocType(), document.getDescription(), document.getFile_name(), document.getFile_path(), document.getText());

        //Refresh
        System.out.println("----------Refreshing index");
        refresh(index);

        return response;
    }

    @GetMapping(value="/multisearch") //Map ONLY GET Requests and means URL's start with /login/{userName}(after Application path).
    public SearchHit[] multiSearch(@RequestParam("input") String input) throws IOException {
        //MultiFieldSearch
        System.out.println("----------Multi Field Searching documents");
        SearchHit[] hit = multiFieldSearch(index, input);
        return hit;
    }

    @GetMapping(value="/search", params = { "field", "value" }) //Map ONLY GET Requests and means URL's start with /login/{userName}(after Application path).
    public SearchHit[] singleSearch(@RequestParam("field") String field, @RequestParam("value") String value) throws IOException {
        //SingleFieldSearch
        System.out.println("----------Single Field Searching documents");
        SearchHit[] hit = search(index, field, value);
        return hit;
    }

    @DeleteMapping(value="/delete")
    public DeleteResponse deleteDocument(@RequestParam("id") String id) throws IOException {
        System.out.println("----------Delete documents");
        DeleteResponse response = delete(index, type, id);
        return response;
    }
}
