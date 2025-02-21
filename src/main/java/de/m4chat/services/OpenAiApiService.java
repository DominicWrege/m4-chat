package de.m4chat.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.m4chat.events.ChatResponseEvent;
import de.m4chat.models.ChatMessage;
import de.m4chat.models.ModelInfo;

import io.github.sashirestela.cleverclient.client.OkHttpClientAdapter;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.base.RealtimeConfig;
import io.github.sashirestela.openai.domain.chat.Chat;
import io.github.sashirestela.openai.domain.chat.ChatMessage.SystemMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import okhttp3.OkHttpClient;

public class OpenAiApiService {

  private ArrayList<UserMessage> userMessages;
  private static final Logger logger = LogManager.getLogger(OpenAiApiService.class);

  public OpenAiApiService(List<ChatMessage> userMessages) {
    this.userMessages = userMessages
        .stream()
        .filter(m -> m.getType().equals("user"))
        .map(m -> UserMessage.of(m.getContent()))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  public static List<ModelInfo> availableModels = Arrays.asList(
      new ModelInfo("gpt-4-turbo", true, 0.2),
      new ModelInfo("gpt-4", true, 0.2),
      new ModelInfo("gpt-3.5-turbo", true, 0.2),
      new ModelInfo("o1-preview", false, 1.0),
      new ModelInfo("o1-mini", false, 1.0));

  public static Optional<ModelInfo> findModelByName(String name) {
    return availableModels.stream()
        .filter(model -> model.name().equals(name))
        .findFirst();
  }

  private static SimpleOpenAI openAI = configureOpenAI();

  private static SimpleOpenAI configureOpenAI() {
    String apiKey = System.getenv("OPENAI_API_KEY");

    if (apiKey == null || apiKey.isBlank()) {
      var errorMessage = "Missing environment variable: OPENAI_API_KEY";
      logger.error(errorMessage);
      throw new IllegalStateException(errorMessage);
    }

    var builder = SimpleOpenAI.builder()
        .apiKey(apiKey)
        .clientAdapter(new OkHttpClientAdapter(new OkHttpClient()));

    for (ModelInfo model : availableModels) {
      builder.realtimeConfig(RealtimeConfig.of(model.name()));
    }

    return builder.build();
  }

  public static boolean isModelInList(String modelName) {
    return availableModels.stream()
        .anyMatch(model -> model.name().equalsIgnoreCase(modelName));
  }

  public static Optional<ModelInfo> getModelByName(String modelName) {
    return availableModels.stream()
        .filter(model -> model.name().equalsIgnoreCase(modelName))
        .findFirst();
  }

  public void writeMessage(String message, ModelInfo model, ChatResponseEvent event) {
    this.userMessages.add(UserMessage.of(message));
    try {
      var chatRequestBuilder = ChatRequest.builder()
          .model(model.name())
          .messages(this.userMessages)
          .temperature(model.temperature())
          .maxCompletionTokens(750);

      if (model.supportsSystemMessages()) {
        chatRequestBuilder.message(SystemMessage
            .of("You are an helpful friendly assistant. Your are an expert in computer science but also in other topics. Help the user as much as you can. Strive to be helpful and informative while keeping your answers brief and to the point."));
      }

      var futureChat = OpenAiApiService.openAI.chatCompletions().createStream(chatRequestBuilder.build());
      var chatResponse = futureChat.join();
      chatResponse.filter(chatResp -> chatResp.getChoices().size() > 0 &&
          chatResp.firstContent() != null)
          .map(Chat::firstContent)
          .forEach(word -> {
            event.onResponse(word);
            logger.trace("{}, chat response: {}", word, model);
          });
    } catch (Exception exception) {
      event.onResponse("An error has occurred");
      logger.debug("openai api error: {}", exception.getMessage());
    }
  }

}
