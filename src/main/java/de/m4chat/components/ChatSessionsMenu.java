package de.m4chat.components;

import java.util.HashMap;
import java.util.UUID;

import de.m4chat.models.ChatSession;
import de.m4chat.services.ChatSessionService;
import de.m4chat.services.UserState;
import de.m4chat.util.DateTimeUtil;
import de.m4chat.views.ChatView;

import com.webforj.Page;
import com.webforj.component.Expanse;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.html.elements.H3;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.applayout.AppLayout;
import com.webforj.component.layout.appnav.AppNav;
import com.webforj.component.layout.appnav.AppNavItem;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexContentAlignment;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.router.Router;

import com.webforj.router.history.Location;
import com.webforj.router.history.ParametersBag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChatSessionsMenu {

  private static final Logger logger = LogManager.getLogger(ChatSessionsMenu.class);

  private AppLayout appLayout;
  private AppNav appNav = new AppNav();
  private UserState userState;
  private ChatSessionService chatService = ChatSessionService.getInstance();

  private final String ChatIdKey = "chatId";

  public ChatSessionsMenu(AppLayout appLayout, UserState userState) {
    this.userState = userState;
    this.appLayout = appLayout;
  }

  public void initComponents() {
    this.setNewChatButton();
    this.appLayout.addToDrawer(this.appNav);
    this.setDeleteAllButton();
    this.UseNavigationListener();
  }

  private void setNewChatButton() {
    var flexContainer = new FlexLayout()
        .setJustifyContent(FlexJustifyContent.BETWEEN)
        .setAlignContent(FlexContentAlignment.CENTER)
        .setAlignment(FlexAlignment.CENTER)
        .setPadding("1em");
    var header = new H3("Your Chats").setStyle("margin", "0");
    flexContainer.add(header);
    var newChatButton = new Button("New", event -> {
      this.createAndNavigateToNewChatSession();
    })
        .setSuffixComponent(TablerIcon.create("plus"))
        .setTheme(ButtonTheme.OUTLINED_DEFAULT)
        .setExpanse(Expanse.MEDIUM);

    flexContainer.add(newChatButton);
    this.appLayout.addToDrawer(flexContainer);
  }

  private void setDeleteAllButton() {
    var button = new Button("Delete All", event -> {
      this.chatService.clearSessionForUser(this.userState.getUserId());
      this.createAndNavigateToNewChatSession();
    })
        .addClassName("delete-button")
        .setSuffixComponent(TablerIcon.create("trash"))
        .setTheme(ButtonTheme.OUTLINED_DANGER)
        .setExpanse(Expanse.MEDIUM);
    this.appLayout.addToDrawerFooter(button);
    this.appLayout.setDrawerFooterVisible(true);
  }

  public void createAndNavigateToNewChatSession() {
    var session = this.chatService.createSession(this.userState.getUserId());
    this.rebuildChatMenuItems();
    Router.getCurrent().navigate(this.newChatIdLocation(session));
  }

  private void rebuildChatMenuItems() {
    this.appNav.getComponents().forEach(this.appNav::remove);
    var sortedChats = this.chatService.getSessionsForUser(this.userState.getUserId());
    for (var chat : sortedChats) {
      var title = String.format("Chat %s #%s",
          DateTimeUtil.formatDate(chat.getCreated()), chat.getShortCode());
      AddMenuChatItem(chat, title);
    }
  }

  private void AddMenuChatItem(ChatSession session, String title) {
    var item = new AppNavItem(title, ChatView.class, TablerIcon.create("message"));
    var map = new HashMap<String, String>();
    map.put(ChatIdKey, session.getId().toString());
    item.setQueryParameters(ParametersBag.of(map));
    appNav.addItem(item);
  }

  private Location newChatIdLocation(ChatSession session) {
    return new Location(String.format("/?%s=%s", ChatIdKey,
        session.getId()));
  }

  private void UseNavigationListener() {
    Router.getCurrent().addNavigateListener(event -> {
      var location = event.getLocation();
      var chatIdParameter = location.getQueryParameters().get(ChatIdKey);
      logger.info("navigation changed with chatId param: {}", chatIdParameter);

      var sessionId = chatIdParameter.map(UUID::fromString).orElseGet(() -> UUID.randomUUID());

      var session = this.chatService
          .getOrCreateSession(sessionId, this.userState.getUserId());
      logger.info("Using existing session: {}", session.getId());

      this.rebuildChatMenuItems();

      Router.getCurrent().getHistory()
          .replaceState(null, this.newChatIdLocation(session));

      var component = (ChatView) event.getContext().getComponent();
      component.initComponents(session);
      Page.getCurrent().executeJs("scrollToBottom('instant')");
    });
  }

}
