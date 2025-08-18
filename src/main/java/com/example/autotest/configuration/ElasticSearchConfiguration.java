package com.example.autotest.configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.example.autotest.configuration.settings.ElasticSearchSettings;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.Base64;

@Configuration
@SuppressWarnings("unused")
public class ElasticSearchConfiguration {

    @Bean
    @Lazy
    public ElasticsearchClient elasticSearchClient(ElasticsearchTransport elasticsearchTransport) {
        return new ElasticsearchClient(elasticsearchTransport);
    }

    @Bean
    @Lazy
    public ElasticsearchTransport elasticsearchTransport(RestClient restClient, JacksonJsonpMapper jacksonJsonpMapper) {
        return new RestClientTransport(restClient, jacksonJsonpMapper);
    }

    @Bean
    @Lazy
    public JacksonJsonpMapper jacksonJsonpMapper() {
        return new JacksonJsonpMapper(new ObjectMapper()
                .findAndRegisterModules()
                .configure(SerializationFeature.INDENT_OUTPUT, false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL));
    }

    @Bean
    @Lazy
    public RestClient restClient() {
        String authInfo = String.format("%s:%s", ElasticSearchSettings.ELASTICSEARCH_LOGIN, ElasticSearchSettings.ELASTICSEARCH_PASSWORD);
        String authHeaderValue = "Basic " + Base64.getEncoder().encodeToString(authInfo.getBytes());
        return RestClient.builder(HttpHost.create(ElasticSearchSettings.ELASTICSEARCH_URL))
                .setDefaultHeaders(new Header[]{
                        new BasicHeader("Authorization", authHeaderValue),
                        new BasicHeader("Content-type", "application/json")
                })
                .setHttpClientConfigCallback(
                        httpClientBuilder -> httpClientBuilder.addInterceptorLast(
                                (HttpResponseInterceptor) (response, context) -> response.addHeader("X-Elastic-Product", "Elasticsearch")
                        )
                )
                .build();
    }
}
