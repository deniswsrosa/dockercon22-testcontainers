package com.denis.dockercon.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wallet")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer","handler"})
public class Wallet implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long quantity;
  private String gameUserId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getQuantity() {
    return quantity;
  }

  public void setQuantity(Long quantity) {
    this.quantity = quantity;
  }

  public String getGameUserId() {
    return gameUserId;
  }

  public void setGameUserId(String gameUserId) {
    this.gameUserId = gameUserId;
  }
}
