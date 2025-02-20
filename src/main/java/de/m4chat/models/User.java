package de.m4chat.models;

import java.sql.Timestamp;
import java.util.UUID;

public class User {
  private UUID id;

  private Timestamp created;

  public User() {
    this.id = UUID.randomUUID();
  }

  public User(UUID id) {
    this.id = id;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Timestamp getCreated() {
    return this.created;
  }

  public void setCreated(Timestamp created) {
    this.created = created;
  }

  @Override
  public String toString() {
    return "User{id=" + id + "}";
  }
}
