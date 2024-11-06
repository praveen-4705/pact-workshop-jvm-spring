package com.pactworkshop.consumer;

import com.github.tomakehurst.wiremock.WireMockServer;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductServiceTest {

    private WireMockServer wireMockServer;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(options().dynamicPort());

        wireMockServer.start();

        RestTemplate restTemplate = new RestTemplateBuilder()
                .rootUri(wireMockServer.baseUrl())
                .build();

        productService = new ProductService(restTemplate);
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void getAllProducts() {
        wireMockServer.stubFor(get(urlPathEqualTo("/products"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[" +
                                "{\"1f518caa773f01a72fdfcd84913632d844ebc853\", \"cost\", \"US\", 83, \"USD\", \"29a8b245-4f3e-41e7-ac82-97249ec39aab\", \"6e71cd34ef189372646b6f302f14f6bc9d1fedaa\", \"1.2\"},"+
                                "{\"6cbbcc8fc23cc3378e4a70ace25ff0d5567f80d7\", \"patent\", \"JP\", 83, \"EUR\", 26f17105-8ba0-4fb9-9d92-0346273c3ac8\", \"6e71cd34ef189372646b6f302f14f6bc9d1fedaa\", \"3.2\"}"+
                                "]")));

        List<Product> expected = Arrays.asList(new Product("1f518caa773f01a72fdfcd84913632d844ebc853", "cost", "US", 83, "USD",
                "29a8b245-4f3e-41e7-ac82-97249ec39aab", "6e71cd34ef189372646b6f302f14f6bc9d1fedaa", "1.2"),
                new Product("6cbbcc8fc23cc3378e4a70ace25ff0d5567f80d7", "patent", "JP", 83, "EUR",
                    "26f17105-8ba0-4fb9-9d92-0346273c3ac8", "6e71cd34ef189372646b6f302f14f6bc9d1fedaa", "3.2"));

        List<Product> products = productService.getAllProducts();

        assertEquals(expected, products);
    }

    @Test
    void getProductById() {
        wireMockServer.stubFor(get(urlPathEqualTo("/product/1f518caa773f01a72fdfcd84913632d844ebc853"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                            {
                                "uin": "1f518caa773f01a72fdfcd84913632d844ebc853",
                                "datatype": "trademark",
                                "countryCode": "EU",
                                "applicationId": 83,
                                "currencyCode": "ABC",
                                "applicationRecordId": "1f85a328-356f-4f39-bae8-bc9e4f7dda61",
                                "organizationId": "6e71cd34ef189372646b6f302f14f6bc9d1fedaa",
                                "version": "3.6"
                            }""".indent(4))));

        Product expected = new Product("1f518caa773f01a72fdfcd84913632d844ebc853", "trademark", "EU", 83, "ABC",
            "1f85a328-356f-4f39-bae8-bc9e4f7dda61", "6e71cd34ef189372646b6f302f14f6bc9d1fedaa", "3.6");

        Product product = productService.getProduct("1f518caa773f01a72fdfcd84913632d844ebc853");

        assertEquals(expected, product);
    }
}
