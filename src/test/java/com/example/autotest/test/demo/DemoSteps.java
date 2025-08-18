package com.example.autotest.test.demo;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchPhraseQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Param;
import io.qameta.allure.Step;
import io.qameta.allure.model.Parameter;
import org.assertj.core.api.Assertions;
import org.awaitility.Awaitility;
import org.awaitility.core.ConditionFactory;
import org.awaitility.core.ConditionTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.autotest.configuration.settings.ElasticSearchSettings;
import com.example.autotest.exception.AutotestException;
import com.example.autotest.support.ExceptionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public final class DemoSteps {

    private static final Logger LOG = LoggerFactory.getLogger(DemoSteps.class);

    private DemoSteps() {
        ExceptionUtils.throwInstantiationException(getClass());
    }

    @Step("Поиск логов в Elasticsearch")
    @SuppressWarnings("unchecked")
    public static Object searchElasticLogs(@Param(mode = Parameter.Mode.HIDDEN) ElasticsearchClient elasticSearchClient,
                                                             @Param(mode = Parameter.Mode.HIDDEN) int responseCode,
                                                             @Param(mode = Parameter.Mode.HIDDEN) String messageId) {
        String sourceName = "http.request.body.content";
        SearchRequest searchRequest = prepareSearchRequest(responseCode, sourceName, messageId);
        SearchResponse<Object> searchResponse = searchLogs(responseCode, elasticSearchClient, searchRequest);

        Map<String, Object> source = (Map<String, Object>) searchResponse.hits().hits().get(0).source();
        for (String key : sourceName.split("\\.")) {
            Object value = Objects.requireNonNull(source, "Не удалось получить значение из 'source' в ответе Elasticsearch").get(key);
            if (value instanceof String val) {
                try {
                    return new ObjectMapper().findAndRegisterModules().readValue(val, Object.class);
                } catch (Exception e) {
                    throw new AutotestException(e);
                }
            } else {
                source = (Map<String, Object>) value;
            }
        }

        throw new AutotestException(String.format("Не удалось найти значение поля '%s' в найденных логах Elasticsearch", sourceName));
    }

    private static SearchRequest prepareSearchRequest(int responseCode, String sourceName, String messageId) {
        List<Query> queries = prepareSearchSubqueries(responseCode, messageId);
        return SearchRequest.of(
                requestBuilder -> requestBuilder.index(ElasticSearchSettings.ELASTICSEARCH_PACKETBEAT_INDEX)
                        .size(1)
                        .source(s -> s.filter(f -> f.includes(sourceName)))
                        .query(Query.of(
                                        queryBuilder -> queryBuilder.bool(
                                                boolQueryBuilder -> boolQueryBuilder.filter(queries)
                                        )
                                )
                        )
        );
    }

    private static List<Query> prepareSearchSubqueries(int responseCode, String messageId) {
        List<Query> queries = new ArrayList<>();

        queries.add(Query.of(queryBuilder -> queryBuilder.matchPhrase(
                MatchPhraseQuery.of(
                        matchPhraseQueryBuilder -> matchPhraseQueryBuilder.field("response.code")
                                .query(String.valueOf(responseCode))
                )
        )));
        queries.add(Query.of(queryBuilder -> queryBuilder.matchPhrase(
                MatchPhraseQuery.of(
                        matchPhraseQueryBuilder -> matchPhraseQueryBuilder.field("method")
                                .query("post")
                )
        )));
        queries.add(Query.of(queryBuilder -> queryBuilder.matchPhrase(
                MatchPhraseQuery.of(
                        matchPhraseQueryBuilder -> matchPhraseQueryBuilder.field("project.key")
                                .query("EXAMPLE")
                )
        )));

        if (responseCode == 200) {
            queries.add(Query.of(queryBuilder -> queryBuilder.multiMatch(
                    MultiMatchQuery.of(
                            multiMatchQueryBuilder -> multiMatchQueryBuilder.lenient(true)
                                    .query(messageId)
                                    .type(TextQueryType.Phrase)
                    )
            )));
        }

        return queries;
    }

    private static SearchResponse<Object> searchLogs(int responseCode, ElasticsearchClient elasticSearchClient, SearchRequest searchRequest) {
        ConditionFactory conditionFactory;
        Predicate<? super SearchResponse<Object>> predicate;
        if (responseCode == 200) {
            conditionFactory = Awaitility.given()
                    .timeout(60, TimeUnit.SECONDS)
                    .pollInterval(5, TimeUnit.SECONDS);
            predicate = response -> response != null && !response.hits().hits().isEmpty();
        } else {
            conditionFactory = Awaitility.given()
                    .atLeast(30, TimeUnit.SECONDS);
            predicate = response -> true;
        }

        LOG.info("\nОтправлен запрос:\n{}", searchRequest);
        SearchResponse<Object> searchResponse = null;
        try {
            searchResponse = conditionFactory
                    .pollInSameThread()
                    .alias("Ожидание логов elasticsearch")
                    .until(() -> {
                        try {
                            return elasticSearchClient.search(searchRequest, Object.class);
                        } catch (ElasticsearchException e) {
                            LOG.info("Логи не появились");
                            return null;
                        }
                    }, predicate);
        } catch (ConditionTimeoutException e){
            LOG.info("\nПолучен ответ:\n{}", searchResponse);
            throw e;
        }

        if (responseCode == 200) {
            Assertions.assertThat(searchResponse.hits().hits()).hasSize(1);
        } else {
            Assertions.assertThat(searchResponse.hits().hits()).isEmpty();
        }

        return searchResponse;
    }
}
