package de.m4chat.services;

import de.m4chat.dao.ChatMessageDao;
import de.m4chat.dao.ChatSessionDao;
import de.m4chat.database.Database;
import de.m4chat.models.ChatMessage;
import de.m4chat.models.ChatSession;

import java.util.List;
import java.util.UUID;

import com.basis.server.local.generic.vkeyed.Page;

public class ChatSessionService {

  private final ChatSessionDao chatSessionDao;
  private final ChatMessageDao chatMessageDao;

  private static volatile ChatSessionService instance;

  private ChatSessionService() {
    var jdbi = Database.getJdbi();
    this.chatSessionDao = jdbi.onDemand(ChatSessionDao.class);
    this.chatMessageDao = jdbi.onDemand(ChatMessageDao.class);
  }

  public static ChatSessionService getInstance() {
    if (instance == null) {
      synchronized (ChatSession.class) {
        if (instance == null) {
          instance = new ChatSessionService();
        }
      }
    }
    return instance;
  }

  public ChatSession createSession(UUID userId) {
    var session = new ChatSession(userId);
    chatSessionDao.insertChatSession(session);
    return session;
  }

  public void clearSessionForUser(UUID userId) {
    chatSessionDao.clearSessionForUser(userId);
  }

  private void insertMessage(ChatMessage message) {
    if (message.getContent().isBlank()) {
      return;
    }
    chatMessageDao.insertMessage(message);
  }

  public void insertUserMessage(UUID sessionId, String content) {
    this.insertMessage(new ChatMessage(sessionId, "user", content));
  }

  public void insertResponseMessage(UUID sessionId, String content) {
    this.insertMessage(new ChatMessage(sessionId, "response", content));
  }

  public ChatSession getOrCreateSession(UUID sessionId, UUID userId) {
    return chatSessionDao.getOrCreateSession(sessionId, userId);
  }

  public List<ChatSession> getSessionsForUser(UUID userId) {
    return chatSessionDao.getChatSessionsForUser(userId);
  }

  public List<ChatMessage> getMessageForSession(UUID sessionId) {
    return chatMessageDao.getMessagesBySession(sessionId)
        .stream()
        .filter(item -> !item.getContent().isBlank())
        .toList();
  }
}
