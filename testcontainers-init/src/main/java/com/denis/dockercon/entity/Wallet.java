package com.denis.dockercon.entity;

import javax.persistence.*;

@Entity
@Table(name = "wallet")
public class Wallet {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long quantity;
  @OneToOne
  @JoinColumn(name="game_user_id")
  private GameUser gameUser;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public GameUser getGameUser() {
    return gameUser;
  }

  public void setGameUser(GameUser gameUser) {
    this.gameUser = gameUser;
  }

  public Long getQuantity() {
    return quantity;
  }

  public void setQuantity(Long quantity) {
    this.quantity = quantity;
  }
}
