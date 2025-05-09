package de.m4chat.components;

import de.m4chat.models.ChatMessage;
import java.util.List;

import com.webforj.Page;
import com.webforj.annotation.InlineStyleSheet;
import com.webforj.component.Component;
import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;

@InlineStyleSheet(/* css */"""
      .chat-list {
        flex: 1;
        overflow-y: auto;
        overflow-x: hidden;
        padding: 0 1rem;
        margin: 0 auto;
        width: 90%;
        max-width: var(--chat-max-width);
      }

      .chat-list > div{
        margin-bottom: 1.5rem;
      }

      @media (max-width: 1030px) {
        .chat-list {
          margin: unset;
          width: unset;
          padding: 0;
        }
      }
    """)
public class ChatList extends Composite<Div> {
  private Div self = getBoundComponent();

  public ChatList() {
    self.addClassName("chat-list");
    this.setVisible(false);
  }

  private void setVisible(boolean value) {
    self.setStyle("visibility", value ? "visible" : "hidden");
  }

  public void addResponseMessage(ResponseChatItem item) {
    this.addMessageWithScrollToBottom(item);
    this.highlightCode();
  }

  public void addMessageUserMessage(UserChatItem item) {
    this.addMessageWithScrollToBottom(item);
  }

  private void addMessage(Component component) {
    self.add(component);
  }

  private void addMessageWithScrollToBottom(Component component) {
    self.add(component);
    this.scrollToBottom();
  }

  private void scrollToBottom() {
    Page.getCurrent().executeJsVoidAsync("scrollToBottom()");
  }

  private void scrollToBottomInstant() {
    Page.getCurrent().executeJsVoidAsync("scrollToBottom('instant')");
  }

  private void highlightCode() {
    Page.getCurrent().executeJsVoidAsync("highlightCode()");
  }

  public void drawMessages(List<ChatMessage> messages) {
    for (ChatMessage chatMessage : messages) {
      if (chatMessage.getType().equals("user")) {
        addMessage(new UserChatItem(chatMessage.getContent()));
      } else {
        addMessage(new ResponseChatItem(chatMessage.getContent()));
      }
    }
    this.highlightCode();
    this.scrollToBottomInstant();
    this.setVisible(true);
  }
}
