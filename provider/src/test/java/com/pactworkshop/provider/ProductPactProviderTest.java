package com.pactworkshop.provider;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.junitsupport.loader.PactBrokerAuth;
import au.com.dius.pact.provider.junitsupport.loader.PactBrokerConsumerVersionSelectors;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import au.com.dius.pact.provider.junitsupport.loader.SelectorBuilder;
import java.util.UUID;
import org.apache.hc.core5.http.HttpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.mockito.Mockito.when;

@Provider("ProductService")
@PactBroker(url = "http://localhost:8000", authentication = @PactBrokerAuth(username = "pact_workshop", password = "pact_workshop"))
//@PactFolder("pacts")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductPactProviderTest {

  @LocalServerPort
  int port;
  @MockBean
  private ProductRepository productRepository;

  @PactBrokerConsumerVersionSelectors
  public static SelectorBuilder consumerVersionSelectors() {
    return new SelectorBuilder().deployedOrReleased().mainBranch().branch("pact-workflow");
  }

  @BeforeEach
  void setUp(PactVerificationContext context) {
    context.setTarget(new HttpTestTarget("localhost", port));
  }

  @TestTemplate
  @ExtendWith(PactVerificationInvocationContextProvider.class)
  void verifyPact(PactVerificationContext context, HttpRequest request) {
    replaceAuthHeader(request);
    context.verifyInteraction();
  }

  private void replaceAuthHeader(HttpRequest request) {
    if (request.containsHeader("Authorization")) {
      String header = "Bearer " + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(new Date());
      request.removeHeaders("Authorization");
      request.addHeader("Authorization", header);
    }
  }

  @State("records exist")
  void toProductsExistState() {
    when(productRepository.fetchAll()).thenReturn(Arrays.asList(
        new Product("1f518caa773f01a72fdfcd84913632d844ebc853", "cost", "US", 83, "USD",
            "29a8b245-4f3e-41e7-ac82-97249ec39aab", "6e71cd34ef189372646b6f302f14f6bc9d1fedaa", "1.2"),
        new Product("6cbbcc8fc23cc3378e4a70ace25ff0d5567f80d7", "patent", "JP", 83, "EUR",
            "26f17105-8ba0-4fb9-9d92-0346273c3ac8", "6e71cd34ef189372646b6f302f14f6bc9d1fedaa", "3.2")));
  }

  @State({"no records exist", "record with uin 11 does not exist"})
  void toNoProductsExistState() {
    when(productRepository.fetchAll()).thenReturn(Collections.emptyList());
  }

  @State("record with uin 1f518caa773f01a72fdfcd84913632d844ebc853 exists")
  void toProductWithIdTenExistsState() {
    when(productRepository.getById("1f518caa773f01a72fdfcd84913632d844ebc853")).thenReturn(
        Optional.of(
            new Product("1f518caa773f01a72fdfcd84913632d844ebc853", "cost", "US", 83, "USD",
                "29a8b245-4f3e-41e7-ac82-97249ec39aab", "6e71cd34ef189372646b6f302f14f6bc9d1fedaa", "3.2")));
  }
}
