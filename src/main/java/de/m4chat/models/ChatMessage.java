package de.m4chat.models;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class ChatMessage {
  private UUID id;
  private UUID sessionId;
  private String type;
  private String content;
  private Timestamp created;

  public ChatMessage() {
  }

  // Constructor for existing messages
  public ChatMessage(UUID id, UUID sessionId, String type, String content, Timestamp created) {
    this.id = id;
    this.sessionId = sessionId;
    this.type = type;
    this.content = content;
    this.created = created;
  }

  // Constructor for new messages
  public ChatMessage(UUID sessionId, String type, String content) {
    this(UUID.randomUUID(), sessionId, type, content, Timestamp.from(Instant.now()));
  }

  public static ChatMessage UserMessage(UUID sessionId, String content) {
    return new ChatMessage(sessionId, "user", content);
  }

  public static ChatMessage ResponseMessage(UUID sessionId, String content) {
    return new ChatMessage(sessionId, "user", content);
  }

  // Getters
  public UUID getId() {
    return id;
  }

  public UUID getSessionId() {
    return sessionId;
  }

  public String getType() {
    return type;
  }

  public String getContent() {
    return content;
  }

  public Timestamp getCreated() {
    return created;
  }

  // Setters
  public void setId(UUID id) {
    this.id = id;
  }

  public void setSessionId(UUID sessionId) {
    this.sessionId = sessionId;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void setCreated(Timestamp created) {
    this.created = created;
  }
}
