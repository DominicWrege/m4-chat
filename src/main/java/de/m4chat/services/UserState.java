package de.m4chat.services;

import java.util.UUID;

import com.webforj.webstorage.LocalStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.m4chat.models.User;

public class UserState {

  private static final Logger logger = LogManager.getLogger(UserState.class);
  private final User user;

  public UserState() {
    var keyName = "userId";
    var localStorage = LocalStorage.getCurrent();

    var userId = parseUUID(localStorage.get(keyName));
    this.user = UserService.getInstance().getOrCreateUser(userId);
    localStorage.setItem(keyName, this.user.getId().toString());
    logger.info("user state {}", this.user);
  }

  private UUID parseUUID(String userId) {
    if (userId == null || userId.isBlank()) {
      return UUID.randomUUID();
    }
    try {
      return UUID.fromString(userId);
    } catch (IllegalArgumentException e) {
      return UUID.randomUUID();
    }
  }

  public UUID getUserId() {
    return this.user.getId();
  }

}
