package de.m4chat.components;

import de.m4chat.models.ChatSession;
import de.m4chat.services.ChatSessionService;

import com.webforj.annotation.InlineStyleSheet;
import com.webforj.component.Component;
import com.webforj.component.Composite;
import com.webforj.component.html.elements.Div;
import com.webforj.component.window.Window;

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
  private ChatSession chatSession;

  public ChatList(ChatSession chatSession) {
    this.chatSession = chatSession;
    self.addClassName("chat-list");
  }

  @Override
  protected void onCreate(Window window) {
    super.onCreate(window);
    this.drawMessages();
  }

  public void addMessage(Component item) {
    self.add(item);
  }

  private void drawMessages() {
    ChatSessionService
        .getInstance()
        .getMessageForSession(chatSession.getId()).stream()
        .map(item -> item.getType().equals("user")
            ? new UserChatItem(item.getContent())
            : new ResponseChatItem(this.chatSession.getId(), item.getContent()))
        .forEach(this::addMessage);
  }
}
