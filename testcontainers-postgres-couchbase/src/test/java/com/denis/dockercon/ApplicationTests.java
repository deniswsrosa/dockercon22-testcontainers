package com.denis.dockercon;

import com.denis.dockercon.entity.Wallet;
import com.denis.dockercon.entity.GameUser;
import com.denis.dockercon.repository.WalletRepository;
import com.denis.dockercon.service.WalletService;
import com.denis.dockercon.service.GameUserService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.couchbase.BucketDefinition;
import org.testcontainers.couchbase.CouchbaseContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

@Testcontainers
@SpringBootTest
class ApplicationTests {

  @Container
  public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>( "postgres:14.2")
    .withUsername("user")
    .withPassword("password")
    .withDatabaseName("test");

  static BucketDefinition bucketDefinition = new BucketDefinition("mybucket");
  @Container
  static CouchbaseContainer couchbase = new CouchbaseContainer("couchbase/server:enterprise-7.0.3")
          .withBucket(bucketDefinition);

  @DynamicPropertySource
  static void properties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.password", postgres::getPassword);
    registry.add("spring.datasource.username", postgres::getUsername);

    registry.add("spring.couchbase.bootstrap-hosts", couchbase::getConnectionString);
    registry.add("spring.couchbase.bucket.user", couchbase::getUsername);
    registry.add("spring.couchbase.bucket.password", couchbase::getPassword);
    registry.add("spring.couchbase.bucket.name", bucketDefinition::getName);
  }

  @Autowired
  private GameUserService gameUserService;
  @Autowired
  private WalletService walletService;
  @Autowired
  private WalletRepository walletRepository;

  @Test
  void saveGameUserTest() {

    GameUser gameUser = new GameUser();
    gameUser.setId(UUID.randomUUID().toString());
    gameUser.setName("denis");
    gameUser.setEmail("denis.rosa@couchbase.com");
    gameUser = gameUserService.save(gameUser);

    Assert.assertEquals(gameUser.getName(), gameUserService.get(gameUser.getId()).getName());
  }

  @Test
  void saveWalletTest() {

    GameUser gameUser = new GameUser();
    gameUser.setId(UUID.randomUUID().toString());
    gameUser.setName("denis2");
    gameUser.setEmail("denis2.rosa@Couchbase.com");
    gameUser = gameUserService.save(gameUser);

    Wallet coin = new Wallet();
    coin.setQuantity(500l);
    coin.setGameUserId(gameUser.getId());
    coin = walletService.save(coin);

    Assert.assertEquals(coin.getQuantity(), walletRepository.getById(coin.getId()).getQuantity());
  }

}
