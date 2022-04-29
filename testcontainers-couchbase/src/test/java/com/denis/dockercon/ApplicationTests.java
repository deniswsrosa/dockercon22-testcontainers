package com.denis.dockercon;

import com.denis.dockercon.entity.GameUser;
import com.denis.dockercon.service.GameUserService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.couchbase.BucketDefinition;
import org.testcontainers.couchbase.CouchbaseContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

@Testcontainers
@SpringBootTest
class ApplicationTests {

  static Logger log = LoggerFactory.getLogger(ApplicationTests.class);
  static Network network = Network.newNetwork();

  @Container
  public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>( "postgres:14.2")
          .withUsername("user")
          .withPassword("password")
          .withDatabaseName("test")
          .withNetwork(network)
          .withNetworkAliases("wallet-microservice-db");

  @Container
  public static GenericContainer<?> walletMicroservice = new GenericContainer<>("deniswsrosa/wallet_microservice:latest")
          .withEnv("DATASOURCE_URL","jdbc:postgresql://wallet-microservice-db:5432/test?loggerLevel=OFF")
          .withEnv("DATASOURCE_PASSWORD", container.getPassword())
          .withEnv("DATASOURCE_USERNAME", container.getUsername())
          .withExposedPorts(8080)
          .waitingFor(Wait.forHttp("/api/v1/health"))
          .withNetwork(network)
          .withLogConsumer(new Slf4jLogConsumer(log))
          .dependsOn(container)
          .withNetworkAliases("wallet_microservice");

  static BucketDefinition bucketDefinition = new BucketDefinition("mybucket");
  @Container
  static CouchbaseContainer couchbase = new CouchbaseContainer("couchbase/server:enterprise-7.0.3")
          .withBucket(bucketDefinition);

  @DynamicPropertySource
  static void properties(DynamicPropertyRegistry registry) {
    registry.add("spring.couchbase.bootstrap-hosts", couchbase::getConnectionString);
    registry.add("spring.couchbase.bucket.user", couchbase::getUsername);
    registry.add("spring.couchbase.bucket.password", couchbase::getPassword);
    registry.add("spring.couchbase.bucket.name", bucketDefinition::getName);
  }

  @Autowired
  private GameUserService gameUserService;

  @Test
  void  myTest() throws Exception {

    GameUser gameUser = new GameUser();
    gameUser.setId(UUID.randomUUID().toString());
    gameUser.setName("denis");
    gameUser.setEmail("denis.rosa@couchbase.com");
    gameUser = gameUserService.save(gameUser);
    Assert.assertEquals(gameUser.getName(), gameUserService.get(gameUser.getId()).getName());

    //calling the external microservice
    WalletVO newWallet = createWallet(gameUser.getId(), 500);
    WalletVO vo = getWallet(newWallet.getId());

    Assert.assertEquals(vo.getGameUserId(), gameUser.getId());
  }

  private WalletVO createWallet(String gameUserId, int quantity) throws Exception {

    String body = "{ \"gameUserId\": \""+gameUserId+"\", \"quantity\": "+quantity+" }";
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://"+ walletMicroservice.getHost()+":"+ walletMicroservice.getMappedPort(8080)+"/api/v1/wallet/"))
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .header("Content-Type", "application/json")
            .build();

    HttpResponse<String> response =
            client.send(request, HttpResponse.BodyHandlers.ofString());

    return toWalletVO(response.body());
  }

  private WalletVO getWallet(Long id) throws Exception {
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://"+ walletMicroservice.getHost()+":"+ walletMicroservice.getMappedPort(8080)+"/api/v1/wallet/"+id))
            .build();

    HttpResponse<String> response =
            client.send(request, HttpResponse.BodyHandlers.ofString());

    return toWalletVO(response.body());
  }

  private WalletVO toWalletVO(String body) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(body, WalletVO.class);
  }

}
