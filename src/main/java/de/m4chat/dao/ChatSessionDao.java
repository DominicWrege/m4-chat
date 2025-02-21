package de.m4chat.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import de.m4chat.models.ChatSession;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;

@RegisterBeanMapper(ChatSession.class)
public interface ChatSessionDao extends BaseDao {
  @SqlUpdate("""
      CREATE TABLE IF NOT EXISTS chatsession (
        id UUID PRIMARY KEY NOT NULL,
        userId UUID NOT NULL,
        created TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
        FOREIGN KEY (userId) REFERENCES user(id) ON DELETE CASCADE
      )""")
  void createTable();

  @SqlUpdate("INSERT INTO chatsession (id, userId) VALUES (:id, :userId)")
  void insertChatSession(@BindBean ChatSession chatSession);

  @SqlUpdate("DELETE from chatsession where userId = :userId")
  void clearSessionForUser(@Bind("userId") UUID userId);

  @SqlQuery("SELECT id, userId, created FROM chatsession WHERE userId = :userId ORDER BY created DESC")
  List<ChatSession> getChatSessionsForUser(@Bind("userId") UUID userId);

  @SqlQuery("SELECT id, userId, created  FROM chatsession WHERE id = :sessionId")
  Optional<ChatSession> getChatSession(@Bind("sessionId") UUID sessionId);

  @SqlQuery("""
          SELECT id, userId, created
          FROM chatsession
          WHERE userId = :userId
          ORDER BY created DESC
          LIMIT 1
      """)
  Optional<ChatSession> getLatestSessionForUser(@Bind("userId") UUID userId);

  @Transaction
  default ChatSession getOrCreateSession(UUID sessionId, UUID userId) {
    return getChatSession(sessionId)
        .or(() -> getLatestSessionForUser(userId))
        .orElseGet(() -> {
          ChatSession newSession = new ChatSession(userId);
          insertChatSession(newSession);
          return newSession;
        });

  }

}
