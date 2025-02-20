package de.m4chat.dao;

import java.util.UUID;

import de.m4chat.models.User;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.jdbi.v3.sqlobject.transaction.Transaction;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;

@RegisterBeanMapper(User.class)
public interface UserDao extends BaseDao {
  @SqlUpdate("""
      CREATE TABLE IF NOT EXISTS user (
        id TEXT PRIMARY KEY,
        created TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
      )""")
  void createTable();

  @SqlUpdate("INSERT INTO user (id) VALUES (:id)")
  void insertUser(@BindBean User user);

  @SqlQuery("SELECT id FROM user WHERE id = :id")
  User getUser(@Bind("id") UUID id);

  @Transaction
  default User getOrCreateUser(UUID id) {
    User user = getUser(id);
    if (user == null) {
      insertUser(new User(id));
      return new User(id);
    }
    return user;
  }

}
