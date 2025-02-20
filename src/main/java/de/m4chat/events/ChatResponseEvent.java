package de.m4chat.events;

@FunctionalInterface
public interface ChatResponseEvent {
  void onResponse(String text);
}
