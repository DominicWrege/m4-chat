package de.m4chat.services;

import java.util.Optional;
import java.util.UUID;

import de.m4chat.components.UserSetting;
import de.m4chat.dao.UserDao;
import de.m4chat.dao.UserSettingDao;
import de.m4chat.database.Database;
import de.m4chat.models.ModelInfo;
import de.m4chat.models.User;

public class UserService {
  private final UserDao userDao;
  private final UserSettingDao userSettingDao;

  private static volatile UserService instance;

  private UserService() {
    var jdbi = Database.getJdbi();
    this.userDao = jdbi.onDemand(UserDao.class);
    this.userSettingDao = jdbi.onDemand(UserSettingDao.class);
  }

  public static UserService getInstance() {
    if (instance == null) {
      synchronized (UserService.class) {
        if (instance == null) {
          instance = new UserService();
        }
      }
    }
    return instance;
  }

  public User getOrCreateUser(UUID userId) {
    return this.userDao.getOrCreateUser(userId);
  }

  public ModelInfo getSelectedModel(UUID userId) {
    var setting = UserService.getInstance().getSetting(userId);

    return setting
        .flatMap(item -> OpenAiApiService.findModelByName(item.getModel()))
        .orElseGet(OpenAiApiService.availableModels::getFirst);
  }

  public Optional<UserSetting> getSetting(UUID uuid) {
    return this.userSettingDao.getSettingForUser(uuid);
  }

  public void setSetting(UUID userId, ModelInfo model) {
    this.userSettingDao.insertOrUpdate(new UserSetting(userId, model.name()));
  }

}
