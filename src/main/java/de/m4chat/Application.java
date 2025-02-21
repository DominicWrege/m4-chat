package de.m4chat;

import de.m4chat.database.Database;

import com.webforj.App;
import com.webforj.annotation.AppProfile;
import com.webforj.annotation.AppTheme;
import com.webforj.annotation.AppTitle;
import com.webforj.annotation.JavaScript;
import com.webforj.annotation.Routify;
import com.webforj.annotation.StyleSheet;

@Routify(packages = "de.m4chat.views")
@StyleSheet("ws://app.css")
// @StyleSheet("ws://highlight.min.css")
// @StyleSheet("ws://highlight-dark.min.css")
@JavaScript("ws://highlight.min.js")
@JavaScript("ws://app.js")
@AppTitle("M4 Chat")
@AppTheme("system")
@AppProfile(name = "M4 Chat", shortName = "M4 Chat")
public class Application extends App {

  public Application() {
    Database.createTable();
  }
}
