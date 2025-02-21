package de.m4chat.dao;

import java.util.List;
import java.util.UUID;

import de.m4chat.models.ChatMessage;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

@RegisterBeanMapper(ChatMessage.class)
public interface ChatMessageDao extends BaseDao {

  @SqlUpdate("""
      CREATE TABLE IF NOT EXISTS chatmessage (
        id UUID PRIMARY KEY NOT NULL,
        sessionId UUID NOT NULL REFERENCES chatsession(id) ON DELETE CASCADE,
        type VARCHAR(10) NOT NULL CHECK (type IN ('user', 'response')),
        content TEXT NOT NULL,
        created TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
      );
      CREATE INDEX IF NOT EXISTS idx_chatmessage_sessionId ON chatmessage(sessionId);
      """)
  void createTable();

  @SqlUpdate("INSERT INTO chatmessage (id, sessionId, type, content) VALUES (:id, :sessionId, :type, :content)")
  void insertMessage(@BindBean ChatMessage message);

  @SqlQuery("""
      SELECT id, sessionId, type, content, created
      FROM chatmessage
      WHERE sessionId = :sessionId AND content <> ''
      ORDER BY created ASC
      """)
  List<ChatMessage> getMessagesBySession(@Bind("sessionId") UUID sessionId);
}
