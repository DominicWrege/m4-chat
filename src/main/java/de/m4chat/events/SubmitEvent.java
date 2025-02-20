package de.m4chat.events;

@FunctionalInterface
public interface SubmitEvent {
  void onSubmit(String text);
}
