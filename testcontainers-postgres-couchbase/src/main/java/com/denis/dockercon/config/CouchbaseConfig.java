package com.denis.dockercon.config;


import com.couchbase.client.core.error.CollectionExistsException;
import com.couchbase.client.core.error.IndexExistsException;
import com.couchbase.client.core.msg.kv.DurabilityLevel;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.manager.bucket.BucketSettings;
import com.couchbase.client.java.manager.bucket.BucketType;
import com.couchbase.client.java.manager.collection.CollectionManager;
import com.couchbase.client.java.manager.collection.CollectionSpec;
import com.couchbase.client.java.manager.collection.CreateCollectionOptions;
import com.couchbase.client.java.query.QueryResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CouchbaseConfig {

  @Value("${spring.couchbase.bootstrap-hosts}")
  private String hostName;
  @Value("${spring.couchbase.bucket.user}")
  private String username;
  @Value("${spring.couchbase.bucket.password}")
  private String password;
  @Value("${spring.couchbase.bucket.name}")
  private String bucketName;

  @Bean
  public Cluster getCouchbaseCluster(){
    return Cluster.connect(hostName, username, password);
  }

  @Bean
  public Bucket getCouchbaseBucket(Cluster cluster){

    // Creates the bucket if it does not exist yet
    if (!cluster.buckets().getAllBuckets().containsKey(bucketName)) {
      cluster.buckets().createBucket(
        BucketSettings.create(bucketName)
          .bucketType(BucketType.COUCHBASE)
          .minimumDurabilityLevel(DurabilityLevel.NONE)
          .ramQuotaMB(128));
    }
    createCollection(cluster);
    return cluster.bucket(bucketName);
  }

  //Creates the collection and index if it doesn't exists yet
  private void createCollection(Cluster cluster) {
    CollectionManager collectionManager = cluster.bucket(bucketName).collections();
    try {
      CollectionSpec spec = CollectionSpec.create(CollectionNames.GAME_USER,  cluster.bucket(bucketName).defaultScope().name());
      collectionManager.createCollection(spec);
      System.out.println("Created collection '" + spec.name() + "' in scope '" + spec.scopeName() + "' of bucket '" + bucketName + "'");
      Thread.sleep(1000);
    } catch (CollectionExistsException e){
      System.out.println(String.format("Collection <%s> already exists", CollectionNames.GAME_USER));
    } catch (Exception e) {
      System.out.println(String.format("Generic error <%s>",e.getMessage()));
    }

    try {
      final String query = "CREATE PRIMARY INDEX default_game_user_index ON "+bucketName+"._default."+ CollectionNames.GAME_USER;
      System.out.println(String.format("Creating default_profile_index: <%s>", query));
      final QueryResult result = cluster.query(query);
      for (JsonObject row : result.rowsAsObject()){
        System.out.println(String.format("Index Creation Status %s",row.getObject("meta").getString("status")));
      }
      System.out.println("Created primary index on collection " + CollectionNames.GAME_USER);
      Thread.sleep(1000);
    } catch (IndexExistsException e){
      System.out.println(String.format("Collection's primary index already exists"));
    } catch (Exception e){
      System.out.println(String.format("General error <%s> when trying to create index ",e.getMessage()));
    }
  }
}
