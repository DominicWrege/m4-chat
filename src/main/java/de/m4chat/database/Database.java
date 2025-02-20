package de.m4chat.database;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.m4chat.dao.ChatMessageDao;
import de.m4chat.dao.ChatSessionDao;
import de.m4chat.dao.UserDao;
import de.m4chat.dao.UserSettingDao;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

public class Database {

  private static final Logger logger = LogManager.getLogger(Database.class);

  private static final String DB_URL = String.format("jdbc:sqlite:%s", System.getenv("DATABASE_FILE"));

  public static Jdbi getJdbi() {
    return Jdbi.create(DB_URL).installPlugin(new SqlObjectPlugin());
  }

  public static void createTable() {
    try {
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException e) {
      logger.error("database -> {}", e.getMessage());
    }

    try {
      var jdbi = getJdbi();
      var daoClasses = List.of(
          UserDao.class,
          UserSettingDao.class,
          ChatSessionDao.class,
          ChatMessageDao.class);

      daoClasses.forEach(daoClass -> jdbi.useExtension(daoClass, dao -> {
        dao.createTable();
      }));

    } catch (Exception e) {
      logger.error("create table -> {}", e.getMessage());
    }
  }
}
