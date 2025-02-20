package de.m4chat.views;

import de.m4chat.components.ResponseChatItem;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import de.m4chat.components.ChatList;
import de.m4chat.components.UserChatItem;
import de.m4chat.models.ChatSession;
import de.m4chat.components.PromptInput;
import de.m4chat.services.OpenAiApiService;
import de.m4chat.services.UserService;
import de.m4chat.services.ChatSessionService;

import com.webforj.annotation.InlineStyleSheet;
import com.webforj.component.Composite;

import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.router.NavigationContext;
import com.webforj.router.annotation.Route;
import com.webforj.router.concern.HasFrameTitle;
import com.webforj.router.history.ParametersBag;

@InlineStyleSheet("""
      .chat-view {
        margin: 0 auto;
        overflow-y: hidden;
        flex: 1;
      }
    """)
@Route(value = "/", outlet = MainLayout.class)
public class ChatView extends Composite<FlexLayout> implements HasFrameTitle {

  private static final Logger logger = LogManager.getLogger(ChatView.class);
  private FlexLayout self = getBoundComponent();
  private OpenAiApiService openAiApiService;
  private ChatList chatList;
  private ChatSession currentChatSession;

  private ArrayList<Thread> threads = new ArrayList<>();

  @Override
  public String getFrameTitle(NavigationContext context, ParametersBag parameters) {
    return String.format("Session #%s", this.currentChatSession.getShortCode());
  }

  public void initComponents(ChatSession session) {
    this.currentChatSession = session;
    this.openAiApiService = new OpenAiApiService();
    logger.info("init chat view with sessionId: {}", session.getId());
    this.interruptCurrentThreads();
    self.getComponents().forEach(c -> self.remove(c));
    this.self.setHeight("100%")
        .setDirection(FlexDirection.COLUMN)
        .setSpacing("0")
        .addClassName("chat-view");

    this.chatList = new ChatList(session);
    this.self.add(this.chatList);
    var userInput = new PromptInput();
    this.self.add(userInput);
    userInput.onSubmit(this::handleSubmit);

  }

  private void handleSubmit(String userMessage) {
    var chatDb = ChatSessionService.getInstance();
    chatDb.insertUserMessage(this.currentChatSession.getId(), userMessage);
    this.chatList.addMessage(new UserChatItem(userMessage));

    var chatItem = new ResponseChatItem(this.currentChatSession.getId());
    this.chatList.addMessage(chatItem);
    var model = UserService.getInstance().getSelectedModel(this.currentChatSession.getUserId());
    logger.info("[session: {}] Using Model: {}", chatItem.getSessionUuid(), model);
    logger.info("[session: {}] User Message: {}", chatItem.getSessionUuid(), userMessage);
    var thread = Thread.startVirtualThread(() -> {
      this.openAiApiService.writeMessage(userMessage, model, (response) -> {
        if (Thread.currentThread().isInterrupted()) {
          return;
        }
        chatItem.AppendText(this.currentChatSession.getId(), response);
      });

      ChatSessionService.getInstance()
          .insertResponseMessage(chatItem.getSessionUuid(), chatItem.getText());
    });
    this.threads.add(thread);

  }

  private void interruptCurrentThreads() {
    logger.debug("stop and remove {} threads", this.threads.size());
    for (var thread : this.threads) {
      thread.interrupt();
    }
    this.threads.clear();
  }

}
