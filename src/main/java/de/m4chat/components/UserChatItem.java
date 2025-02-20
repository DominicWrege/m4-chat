package de.m4chat.components;

import com.webforj.annotation.InlineStyleSheet;
import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;

@InlineStyleSheet(/* css */"""
    .user-chat-item {
      padding: 1em 1.4em;
      background: var(--dwc-color-default-90);
      margin-left: auto;
      border-radius: 1em;
      margin-bottom: 2rem;
      max-width: 325px;
      filter: drop-shadow(0 4px 3px rgba(0, 0, 0, 0.1));
      """)
public class UserChatItem extends Composite<Div> {

  public UserChatItem(String text) {
    var self = getBoundComponent();
    self.addClassName("user-chat-item");
    self.setText(text);
  }
}
