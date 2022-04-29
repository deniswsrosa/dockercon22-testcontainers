package com.denis.dockercon;

public class WalletVO {
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
