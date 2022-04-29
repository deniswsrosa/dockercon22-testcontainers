package com.denis.dockercon;

import com.denis.dockercon.entity.Wallet;
import com.denis.dockercon.entity.GameUser;
import com.denis.dockercon.repository.WalletRepository;
import com.denis.dockercon.repository.GameUserRepository;
import com.denis.dockercon.service.WalletService;
import com.denis.dockercon.service.GameUserService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
class ApplicationTests {

  @Container
  public static PostgreSQLContainer postgres = new PostgreSQLContainer( "postgres:14.2")
    .withUsername("user")
    .withPassword("password")
    .withDatabaseName("test");

  @Autowired
  private GameUserRepository gameUserRepository;
  @Autowired
  private GameUserService gameUserService;
  @Autowired
  private WalletService coinService;
  @Autowired
  private WalletRepository coinRepository;

  @DynamicPropertySource
  static void properties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.password", postgres::getPassword);
    registry.add("spring.datasource.username", postgres::getUsername);
  }

  @Test
  void saveGameUserTest() {

    GameUser gameUser = new GameUser();
    gameUser.setName("denis");
    gameUser.setEmail("denis.rosa@couchbase.com");
    gameUser = gameUserRepository.save(gameUser);

    Assert.assertEquals(gameUser.getName(), gameUserRepository.getById(gameUser.getId()).getName());
  }

  @Test
  void saveWalletTest() {

    GameUser gameUser = new GameUser();
    gameUser.setName("denis2");
    gameUser.setEmail("denis2.rosa@Couchbase.com");
    gameUser = gameUserService.save(gameUser);

    Wallet coin = new Wallet();
    coin.setQuantity(500l);
    coin.setGameUser(gameUserService.get(gameUser.getId()));
    coin = coinService.save(coin);

    Assert.assertEquals(coin.getQuantity(), coinRepository.getById(coin.getId()).getQuantity());
  }

}
