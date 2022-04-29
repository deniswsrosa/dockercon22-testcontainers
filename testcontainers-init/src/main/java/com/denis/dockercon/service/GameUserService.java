package com.denis.dockercon.service;

import com.denis.dockercon.entity.GameUser;
import com.denis.dockercon.repository.GameUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameUserService {

  @Autowired
  private GameUserRepository gameUserRepository;

  public GameUser save(GameUser user) {
    return gameUserRepository.save(user);
  }

  public GameUser get(long id) {
    return gameUserRepository.getById(id);
  }

}
