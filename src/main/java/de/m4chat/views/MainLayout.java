package de.m4chat.views;

import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.m4chat.components.DrawerHeader;
import de.m4chat.components.ChatSessionsMenu;
import de.m4chat.services.ChatSessionService;
import de.m4chat.services.OpenAiApiService;
import de.m4chat.services.UserService;
import de.m4chat.services.UserState;

import com.webforj.Page;
import com.webforj.component.Composite;
import com.webforj.component.html.elements.H1;

import com.webforj.component.layout.applayout.AppDrawerToggle;
import com.webforj.component.layout.applayout.AppLayout;

import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.layout.toolbar.Toolbar;
import com.webforj.component.list.ChoiceBox;
import com.webforj.component.list.ListItem;
import com.webforj.router.Router;
import com.webforj.router.annotation.Route;
import com.webforj.router.event.NavigateEvent;

@Route
public class MainLayout extends Composite<AppLayout> {
  private AppLayout self = getBoundComponent();
  private H1 title = new H1("Chat").setStyle("color", "var(--dwc-color-gray-40)");

  private UserState userState = new UserState();

  private static final Logger logger = LogManager.getLogger(MainLayout.class);

  private ChatSessionService chatService = ChatSessionService.getInstance();
  private ChatSessionsMenu chatSessionsMenu;

  public MainLayout() {
    var toolbarContainer = setHeader();
    this.chatSessionsMenu = new ChatSessionsMenu(self, this.userState);
    setDrawer();
    toolbarContainer.add(this.createComboBox());
    Router.getCurrent().addNavigateListener(this::handleNavigation);
  }

  private FlexLayout setHeader() {
    Toolbar toolbar = new Toolbar();
    toolbar.addToStart(new AppDrawerToggle());
    toolbar.addToTitle(title);

    var toolbarContainer = new FlexLayout().setJustifyContent(FlexJustifyContent.END);
    toolbar.add(toolbarContainer);
    self.addToHeader(toolbar);
    return toolbarContainer;
  }

  private ChoiceBox createComboBox() {
    var userService = UserService.getInstance();

    var availableModels = OpenAiApiService.availableModels;

    var comboBox = new ChoiceBox()
        .setMinWidth("140px")
        .insert(availableModels
            .stream()
            .map(model -> new ListItem(model.name(), model.name()))
            .collect(Collectors.toList()));

    comboBox.addSelectListener(event -> availableModels.stream()
        .filter(model -> model.name().equals(event.getSelectedItem().getText()))
        .findFirst()
        .ifPresent(model -> userService.setSetting(this.userState.getUserId(), model)));

    var userId = this.userState.getUserId();
    var selectedModel = userService.getSelectedModel(userId);
    userService.setSetting(userId, selectedModel);
    comboBox.selectKey(selectedModel.name());

    return comboBox;
  }

  private void setDrawer() {
    this.chatSessionsMenu.initComponents();
    self.setDrawerHeaderVisible(true);
    self.addToDrawerTitle(new DrawerHeader());
  }

  private void handleNavigation(NavigateEvent event) {
    var location = event.getLocation();
    var chatIdParameter = location.getQueryParameters().get(ChatSessionsMenu.ChatIdKey);
    logger.info("navigation changed with chatId param: {}", chatIdParameter);

    var sessionId = chatIdParameter.map(UUID::fromString).orElseGet(() -> UUID.randomUUID());

    var session = this.chatService
        .getOrCreateSession(sessionId, this.userState.getUserId());
    logger.info("Using existing session: {}", session.getId());

    this.chatSessionsMenu.rebuildChatMenuItems();
    this.title.setText(String.format("Chat #%s", session.getShortCode()));
    Router.getCurrent().getHistory()
        .replaceState(null, this.chatSessionsMenu.newChatIdLocation(session));

    var component = (ChatView) event.getContext().getComponent();
    component.initComponents(session);
  }
}
