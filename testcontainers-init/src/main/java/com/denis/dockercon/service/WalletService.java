package com.denis.dockercon.service;

import com.denis.dockercon.entity.Wallet;
import com.denis.dockercon.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

  @Autowired
  private WalletRepository walletRepository;

  public Wallet save(Wallet coin) {
    return walletRepository.save(coin);
  }

  public Wallet get(long id) {
    return walletRepository.getById(id);
  }
}
