package com.api.KnowledgeHub;

import com.api.KnowledgeHub.Elasticsearch.ElasticSearchApplication;
import com.api.KnowledgeHub.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

//Using Spring’s support for embedding the Tomcat servlet container as the HTTP runtime, instead of deploying to an external instance.
@SpringBootApplication //@SpringBootApplication encapsulates @Configuration, @EnableAutoConfiguration, and @ComponentScan annotations with their default attributes.

/*
@SpringBootApplication annotation is used to mark a configuration class that declares one or
 more @Bean methods and also triggers auto-configuration and component scanning.
*/

@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class KnowledgeHubApplication extends ElasticSearchApplication { //This is the main class for this Spring Application.

	public static void main(String[] args) {

		//Start Elasticsearch
		startConnect();

		SpringApplication.run(KnowledgeHubApplication.class, args);

	}

}
