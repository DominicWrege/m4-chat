package de.m4chat.models;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

public class ChatSession {
  private UUID id;
  private UUID userId;
  private Timestamp created;

  // Default constructor (needed for Jdbi)
  public ChatSession() {
  }

  // Constructor for existing sessions
  public ChatSession(UUID id, UUID userId, Timestamp created) {
    this.id = id;
    this.userId = userId;
    this.created = created;
  }

  // Constructor for new sessions
  public ChatSession(UUID userId) {
    this(UUID.randomUUID(), userId, Timestamp.from(Instant.now()));
  }

  // Getters
  public UUID getId() {
    return id;
  }

  public UUID getUserId() {
    return userId;
  }

  public Timestamp getCreated() {
    return created;
  }

  // Setters
  public void setId(UUID id) {
    this.id = id;
  }

  public void setUserId(UUID userId) {
    this.userId = userId;
  }

  public void setCreated(Timestamp created) {
    this.created = created;
  }

  // Utility Methods
  public String getShortCode() {
    return this.id.toString().substring(0, 3);
  }

}
