package de.m4chat.components;

import java.util.UUID;

public class UserSetting {
  private UUID userId;
  private String model;

  public UserSetting() {
  }

  public UserSetting(UUID userId, String model) {
    this.userId = userId;
    this.model = model;
  }

  public UUID getUserId() {
    return userId;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  @Override
  public String toString() {
    return "UserSetting{" +
        "userId=" + userId +
        ", model='" + model + '\'' +
        '}';
  }
}
