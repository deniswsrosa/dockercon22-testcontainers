package com.denis.dockercon;


import com.couchbase.client.java.Bucket;
import com.denis.dockercon.config.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.couchbase.BucketDefinition;
import org.testcontainers.couchbase.CouchbaseContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;

@Testcontainers
@SpringBootTest
public class CouchbaseExampleTest {

  static BucketDefinition bucketDefinition = new BucketDefinition("mybucket");

  @Container
  public static PostgreSQLContainer container = new PostgreSQLContainer( "postgres:14.2")
    .withUsername("duke")
    .withPassword("password")
    .withDatabaseName("test");

  @Container
  static CouchbaseContainer couchbase = new CouchbaseContainer("couchbase/server:enterprise-7.0.3")
    .withBucket(bucketDefinition);

  @Autowired
  private Bucket bucket;

  @DynamicPropertySource
  static void properties(DynamicPropertyRegistry registry) {
    registry.add("spring.couchbase.bootstrap-hosts", couchbase::getConnectionString);
    registry.add("spring.couchbase.bucket.user", couchbase::getUsername);
    registry.add("spring.couchbase.bucket.password", couchbase::getPassword);
    registry.add("spring.couchbase.bucket.name", bucketDefinition::getName);

    registry.add("spring.datasource.url", container::getJdbcUrl);
    registry.add("spring.datasource.password", container::getPassword);
    registry.add("spring.datasource.username", container::getUsername);
  }


  @Test
  public void testPostgreSQLModule() throws Exception {

    User user = new User();
    user.setUsername("test");
    user.setId("123");

    bucket.defaultCollection().insert(user.getId(), user);

    Assert.assertEquals(user.getUsername(), bucket.defaultCollection().get(user.getId()).contentAs(User.class).getUsername());

  }
}
