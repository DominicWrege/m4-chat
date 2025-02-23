package de.m4chat.components;

import org.commonmark.renderer.html.HtmlRenderer;

import com.webforj.annotation.InlineStyleSheet;
import com.webforj.component.Composite;
import com.webforj.component.element.Element;
import com.webforj.component.html.elements.Div;

import org.commonmark.node.*;
import org.commonmark.parser.Parser;

@InlineStyleSheet(/* css */"""
      .response-chat-item {
       min-width: 270px;
       padding: 1.2em 1.5em;
       background-color: var(--dwc-surface-3, #fff);
       border-radius: 0.5em;
       margin-right: auto;
       margin-left: 0.5em;
       max-width: 700px;
       filter: drop-shadow(0 4px 3px rgba(0, 0, 0, 0.1));
      }
    """)
public class ResponseChatItem extends Composite<Div> {

  StringBuffer buffer = new StringBuffer();
  Element container = new Element("div");
  private Element loadingElement = new Element().addClassName("loader-dots");

  private Parser parser = Parser.builder().build();
  private static HtmlRenderer htmlRenderer = HtmlRenderer.builder().escapeHtml(true).build();
  private final String cssClassName = "response-chat-item";

  public ResponseChatItem() {
    var self = getBoundComponent();
    self.addClassName(this.cssClassName);
    this.container.add(loadingElement);
    self.add(this.container);
  }

  public ResponseChatItem(String text) {
    var self = getBoundComponent();
    self.addClassName(this.cssClassName);
    self.add(this.container);
    if (text.isEmpty()) {
      this.container.add(loadingElement);
    }
    this.AppendText(text);
  }

  public void AppendText(String text) {
    buffer.append(text);
    Node document = parser.parse(buffer.toString());
    this.container.setHtml(htmlRenderer.render(document));
    this.container.executeJs("scrollToBottom()");
  }

  public void highlightCode() {
    this.container.executeJs("highlightCode()");
  }

  public String getText() {
    return this.buffer.toString();
  }

}
