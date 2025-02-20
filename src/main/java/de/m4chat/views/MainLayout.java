package de.m4chat.views;

import java.util.stream.Collectors;

import de.m4chat.components.DrawerHeader;
import de.m4chat.components.ChatSessionsMenu;
import de.m4chat.services.OpenAiApiService;
import de.m4chat.services.UserService;
import de.m4chat.services.UserState;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.H1;

import com.webforj.component.layout.applayout.AppDrawerToggle;
import com.webforj.component.layout.applayout.AppLayout;

import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.layout.toolbar.Toolbar;
import com.webforj.component.list.ChoiceBox;
import com.webforj.component.list.ListItem;
import com.webforj.router.annotation.Route;

@Route
public class MainLayout extends Composite<AppLayout> {
  private AppLayout self = getBoundComponent();
  private H1 title = new H1("Chat");

  private UserState userState = new UserState();

  public MainLayout() {
    var toolbarContainer = setHeader();
    setDrawer();
    toolbarContainer.add(this.createComboBox());
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
        .insert(availableModels.stream()
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
    new ChatSessionsMenu(self, this.userState).initComponents();
    self.setDrawerHeaderVisible(true);
    self.addToDrawerTitle(new DrawerHeader());
  }

}
