package de.m4chat.components;

import org.commonmark.renderer.html.HtmlRenderer;

import com.webforj.annotation.InlineStyleSheet;
import com.webforj.component.Composite;
import com.webforj.component.element.Element;
import com.webforj.component.html.elements.Div;

import java.util.UUID;

import org.commonmark.node.*;
import org.commonmark.parser.Parser;

@InlineStyleSheet(/* css */"""
      .chat-item {
       min-width: 270px;
       padding: 1.2em 1.5em;
       background-color: var(--dwc-surface-3, #fff);
       border-radius: 0.5em;
       margin-right: auto;
       margin-bottom: 2rem;
       max-width: 700px;
       filter: drop-shadow(0 4px 3px rgba(0, 0, 0, 0.1));
      }
    """)
public class ResponseChatItem extends Composite<Div> {

  StringBuffer buffer = new StringBuffer();
  Element container = new Element("div");
  private Element loadingElement = new Element().addClassName("loader-dots");

  Parser parser = Parser.builder().build();
  HtmlRenderer htmlRenderer = HtmlRenderer.builder().escapeHtml(true).build();

  private UUID currentSessionId;

  public ResponseChatItem(UUID sessionId) {
    this.currentSessionId = sessionId;
    var self = getBoundComponent();
    self.addClassName("chat-item");
    this.container.add(loadingElement);
    self.add(this.container);
  }

  public ResponseChatItem(UUID sessionId, String text) {
    var self = getBoundComponent();
    self.addClassName("chat-item");
    self.add(this.container);
    if (text.isEmpty()) {
      this.container.add(loadingElement);
    }
    this.AppendText(currentSessionId, text);
  }

  public void AppendText(UUID sessionId, String text) {
    if (this.currentSessionId != sessionId) {
      return;
    }
    buffer.append(text);
    Node document = parser.parse(buffer.toString());
    this.container.setHtml(this.htmlRenderer.render(document));
    this.container.executeJs("scrollToBottom()");
    this.container.executeJsVoidAsync("highlightCode()");
  }

  public String getText() {
    return this.buffer.toString();
  }

  public UUID getSessionUuid() {
    return this.currentSessionId;
  }

}
