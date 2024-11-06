package com.pactworkshop.consumer;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import au.com.dius.pact.core.model.PactSpecVersion; // required for v4.6.x to set pactVersion
import java.util.Collections;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static au.com.dius.pact.consumer.dsl.LambdaDsl.newJsonArrayMinLike;
import static au.com.dius.pact.consumer.dsl.LambdaDsl.newJsonBody;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(PactConsumerTestExt.class)
public class ProductConsumerPactTest {

    @Pact(consumer = "FrontendApplication", provider = "ProductService")
    RequestResponsePact getAllRecords(PactDslWithProvider builder) {
        return builder.given("records exist")
                .uponReceiving("get all records")
                .method("GET")
                .path("/products")
                .matchHeader("Authorization", "Bearer (19|20)\\d\\d-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])T([01][1-9]|2[0123]):[0-5][0-9]")
                .willRespondWith()
                .status(200)
                .headers(headers())
                .body(newJsonArrayMinLike(2, array ->
                        array.object(object -> {
                            object.stringType("uin", "1f518caa773f01a72fdfcd84913632d844ebc853");
                            object.stringType("datatype", "cost");
                            object.stringType("countryCode", "US");
                            object.integerType("applicationId", 83);
                            object.stringType("currencyCode", "USD");
                            object.stringType("applicationRecordId", "29a8b245-4f3e-41e7-ac82-97249ec39aab");
                            object.stringType("organizationId", "6e71cd34ef189372646b6f302f14f6bc9d1fedaa");
                            object.stringType("version", "1.2");
                        })
                ).build())
                .toPact();
    }

    @Pact(consumer = "FrontendApplication", provider = "ProductService")
    RequestResponsePact noRecordsExist(PactDslWithProvider builder) {
        return builder.given("no records exist")
                .uponReceiving("get all records")
                .method("GET")
                .path("/products")
                .matchHeader("Authorization", "Bearer (19|20)\\d\\d-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])T([01][1-9]|2[0123]):[0-5][0-9]")
                .willRespondWith()
                .status(200)
                .headers(headers())
                .body("[]")
                .toPact();
    }

    @Pact(consumer = "FrontendApplication", provider = "ProductService")
    RequestResponsePact getOneRecord(PactDslWithProvider builder) {
        return builder.given("record with uin 1f518caa773f01a72fdfcd84913632d844ebc853 exists")
                .uponReceiving("get record with uin 1f518caa773f01a72fdfcd84913632d844ebc853")
                .method("GET")
                .path("/product/1f518caa773f01a72fdfcd84913632d844ebc853")
                .matchHeader("Authorization", "Bearer (19|20)\\d\\d-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])T([01][1-9]|2[0123]):[0-5][0-9]")
                .willRespondWith()
                .status(200)
                .headers(headers())
                .body(newJsonBody(object -> {
                    object.stringType("uin", "1f518caa773f01a72fdfcd84913632d844ebc853");
                    object.stringType("datatype", "cost");
                    object.stringType("countryCode", "US");
                    object.integerType("applicationId", 83);
                    object.stringType("currencyCode", "USD");
                    object.stringType("applicationRecordId", "29a8b245-4f3e-41e7-ac82-97249ec39aab");
                    object.stringType("organizationId", "6e71cd34ef189372646b6f302f14f6bc9d1fedaa");
                    object.stringType("version", "1.2");
                }).build())
                .toPact();
    }

    @Pact(consumer = "FrontendApplication", provider = "ProductService")
    RequestResponsePact recordDoesNotExist(PactDslWithProvider builder) {
        return builder.given("record with uin 11 does not exist")
                .uponReceiving("get record with uin 11")
                .method("GET")
                .path("/product/11")
                .matchHeader("Authorization", "Bearer (19|20)\\d\\d-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])T([01][1-9]|2[0123]):[0-5][0-9]")
                .willRespondWith()
                .status(404)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "getAllRecords", pactVersion = PactSpecVersion.V3)
    void getAllProducts_whenProductsExist(MockServer mockServer) {
        Product product = new Product("1f518caa773f01a72fdfcd84913632d844ebc853", "cost", "US", 83,
            "USD", "29a8b245-4f3e-41e7-ac82-97249ec39aab",
            "6e71cd34ef189372646b6f302f14f6bc9d1fedaa", "1.2");
        List<Product> expected = Arrays.asList(product, product);

        RestTemplate restTemplate = new RestTemplateBuilder()
                .rootUri(mockServer.getUrl())
                .build();
        List<Product> products = new ProductService(restTemplate).getAllProducts();

        assertEquals(expected, products);
    }

    @Test
    @PactTestFor(pactMethod = "noRecordsExist", pactVersion = PactSpecVersion.V3)
    void getAllProducts_whenNoProductsExist(MockServer mockServer) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .rootUri(mockServer.getUrl())
                .build();
        List<Product> products = new ProductService(restTemplate).getAllProducts();

        assertEquals(Collections.emptyList(), products);
    }

    @Test
    @PactTestFor(pactMethod = "getOneRecord", pactVersion = PactSpecVersion.V3)
    void getProductById_whenProductWithId10Exists(MockServer mockServer) {
        Product expected = new Product("1f518caa773f01a72fdfcd84913632d844ebc853", "cost", "US",
            83, "USD", "29a8b245-4f3e-41e7-ac82-97249ec39aab",
            "6e71cd34ef189372646b6f302f14f6bc9d1fedaa", "1.2");

        RestTemplate restTemplate = new RestTemplateBuilder()
                .rootUri(mockServer.getUrl())
                .build();
        Product product = new ProductService(restTemplate).getProduct("1f518caa773f01a72fdfcd84913632d844ebc853");

        assertEquals(expected, product);
    }

    @Test
    @PactTestFor(pactMethod = "recordDoesNotExist", pactVersion = PactSpecVersion.V3)
    void getProductById_whenProductWithId11DoesNotExist(MockServer mockServer) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .rootUri(mockServer.getUrl())
                .build();

        HttpClientErrorException e = assertThrows(HttpClientErrorException.class,
                () -> new ProductService(restTemplate).getProduct("11"));
        assertEquals(404, e.getStatusCode().value());
    }

    private Map<String, String> headers() {
      Map<String, String> headers = new HashMap<>();
      headers.put("Content-Type", "application/json; charset=utf-8");
      return headers;
    }
}
