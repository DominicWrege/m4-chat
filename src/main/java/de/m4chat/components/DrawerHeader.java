package de.m4chat.components;

import com.webforj.component.Composite;
import com.webforj.component.html.elements.H1;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;

public class DrawerHeader extends Composite<FlexLayout> {
  private FlexLayout self = getBoundComponent();

  public DrawerHeader() {
    self.setDirection(FlexDirection.COLUMN);
    self.setSpacing("0px");

    H1 title = new H1("M4 Chat");
    title.setStyle("margin-bottom", "0");
    self.add(title);
  }
}
