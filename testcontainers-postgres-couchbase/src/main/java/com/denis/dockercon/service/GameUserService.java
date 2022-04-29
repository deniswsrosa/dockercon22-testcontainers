package com.denis.dockercon.service;

import com.couchbase.client.java.Bucket;
import static com.denis.dockercon.config.CollectionNames.GAME_USER;

import com.couchbase.client.java.Cluster;
import com.denis.dockercon.entity.GameUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameUserService {

  @Autowired
  private Bucket bucket;

  @Autowired
  private Cluster cluster;

  public GameUser save(GameUser user) {
    bucket.collection(GAME_USER).insert(user.getId(), user);
    return user;
  }

  public GameUser get(String id) {
    return bucket.collection(GAME_USER).get(id).contentAs(GameUser.class);
  }

}
