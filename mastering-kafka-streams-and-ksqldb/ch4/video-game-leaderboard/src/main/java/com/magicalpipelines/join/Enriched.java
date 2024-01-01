package com.magicalpipelines.join;

import com.magicalpipelines.model.Product;

public class Enriched implements Comparable<Enriched> {

  private Long playerId;
  private Long productId;
  private String playerName;
  private String gameName;
  private double score;

  public Enriched(ScoreWithPlayer scoreWithPlayer, Product product) {
    this.playerId = scoreWithPlayer.getPlayer().getId();
    this.productId = product.getId();
    this.playerName = scoreWithPlayer.getPlayer().getName();
    this.gameName = product.getName();
    this.score = scoreWithPlayer.getScoreEvent().getScore();
  }

  @Override
  public int compareTo(Enriched o) {
    return Double.compare(o.score, score);
  }

  public Long getPlayerId() {
    return this.playerId;
  }

  public Long getProductId() {
    return this.productId;
  }

  public String getPlayerName() {
    return this.playerName;
  }

  public String getGameName() {
    return this.gameName;
  }

  public Double getScore() {
    return this.score;
  }

  @Override
  public String toString() {
    return "{"
        + " playerId='"
        + getPlayerId()
        + "'"
        + ", playerName='"
        + getPlayerName()
        + "'"
        + ", gameName='"
        + getGameName()
        + "'"
        + ", score='"
        + getScore()
        + "'"
        + "}";
  }
}