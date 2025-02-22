package de.m4chat.components;

import de.m4chat.models.ChatMessage;
import de.m4chat.models.ChatSession;
import de.m4chat.services.ChatSessionService;

import java.util.List;

import com.webforj.Page;
import com.webforj.annotation.InlineStyleSheet;
import com.webforj.component.Component;
import com.webforj.component.Composite;
import com.webforj.component.element.Element;
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
  private ChatSession chatSession;

  private List<ChatMessage> messages;

  public ChatList(ChatSession session, List<ChatMessage> messages) {
    this.chatSession = session;
    this.messages = messages;
    self.addClassName("chat-list");
  }

  @Override
  protected void onCreate(Window window) {
    super.onCreate(window);
    this.drawMessages();
  }

  public void addResponseMessage(ResponseChatItem item) {
    this.addMessage(item);
    Page.getCurrent().executeJsVoidAsync("highlightCode()");
  }

  public void addMessageUserMessage(UserChatItem item) {
    this.addMessage(item);
  }

  private void addMessage(Component component) {
    self.add(component);
    Page.getCurrent().executeJs("scrollToBottom()");
  }

  private void drawMessages() {
    for (ChatMessage chatMessage : messages)
      if (chatMessage.getType().equals("user")) {
        addMessageUserMessage(new UserChatItem(chatMessage.getContent()));
      } else {
        addResponseMessage(new ResponseChatItem(this.chatSession.getId(), chatMessage.getContent()));
      }
  }
}
