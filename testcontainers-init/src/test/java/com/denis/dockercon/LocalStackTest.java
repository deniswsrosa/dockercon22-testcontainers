package com.denis.dockercon;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;

import static org.testcontainers.containers.Container.ExecResult;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service;

@Testcontainers
public class LocalStackTest {

  @Container
  static LocalStackContainer container = new LocalStackContainer(DockerImageName.parse("localstack/localstack:0.14.2"))
    .withServices(Service.S3, Service.SQS);

  @BeforeAll
  static void initContainer() throws IOException, InterruptedException {
    container.execInContainer("awslocal", "s3api", "create-bucket", "--bucket", "gameassets");
    container.execInContainer("awslocal", "sqs", "create-queue", "--queue-name", "gameassets");
  }

  @Test
  void accessS3BucketTest() {
    AmazonS3 s3Client = AmazonS3ClientBuilder
      .standard()
      .withCredentials(container.getDefaultCredentialsProvider())
      .withEndpointConfiguration(container.getEndpointConfiguration(Service.S3))
      .build();
    System.out.println(s3Client.getBucketLocation("gameassets"));
  }

}
