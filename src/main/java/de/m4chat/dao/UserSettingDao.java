package de.m4chat.dao;

import java.util.Optional;
import java.util.UUID;

import de.m4chat.components.UserSetting;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

@RegisterBeanMapper(UserSetting.class)
public interface UserSettingDao extends BaseDao {
    @SqlUpdate("""
              CREATE TABLE IF NOT EXISTS userSetting (
                  userId UUID NOT NULL,
                  model TEXT NOT NULL,
                  UNIQUE (userId),
                  FOREIGN KEY (userId) REFERENCES user(id) ON DELETE CASCADE
              );

              CREATE INDEX IF NOT EXISTS idx_userSetting_userId ON userSetting(userId);
            """)
    void createTable();

    @SqlUpdate("""
                INSERT INTO userSetting (userId, model)
                VALUES (:userId, :model)
                ON CONFLICT (userId)
                DO UPDATE SET model = EXCLUDED.model;
            """)
    void insertOrUpdate(@BindBean UserSetting userSetting);

    @SqlQuery("""
                SELECT * FROM userSetting WHERE userId = :userId LIMIT 1
            """)
    Optional<UserSetting> getSettingForUser(@Bind("userId") UUID userId);
}
