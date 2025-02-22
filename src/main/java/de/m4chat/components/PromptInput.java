package de.m4chat.components;

import de.m4chat.events.SubmitEvent;

import com.webforj.annotation.InlineStyleSheet;
import com.webforj.component.Composite;
import com.webforj.component.Expanse;
import com.webforj.component.button.Button;
import com.webforj.component.button.ButtonTheme;
import com.webforj.component.element.Element;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;

@InlineStyleSheet(/* css */"""
    .input-component {
        z-index: 3;
        background: var(--dwc-surface-3, #fff);
        border-radius: 1.5em;
        border: 1px solid var(--dwc-color-gray-70);
        max-width: var(--chat-max-width);
        margin: 0 auto;
        gap: 1.25em;
        padding: 1em 1.75em;
        width: 90%;
    }

    @media (max-width: 800px) {
        .prompt-input[contenteditable]{
          font-size: 1.1em;
        }
        .input-component {
          width: unset;
          margin: 0;
        }
    }

    .prompt-input{
      flex: 1;
      max-height: 8rem;
    }

    .prompt-input[contenteditable] {
        accent-color: var(--dwc-color-primary);
        overflow: auto;
        background: var(--dwc-input-background, var(--dwc-color-default-light));
        border: var(--dwc-input-border-width, var(--dwc-border-width)) var(--dwc-input-border-style, var(--dwc-border-style)) var(--dwc-input-border-color, var(--dwc-color-default-dark));
        cursor: var(--dwc-cursor-text);
        border-radius: var(--dwc-border-radius);
        padding: 0.6em;
    }

    .prompt-input[contenteditable]:focus-visible {
      background-color: var(--dwc-input-hover-background, var(--dwc-color-default-light));
      border-color: var(--dwc-input-hover-border-color, var(--dwc-color-primary));
      outline: 1px solid var(--dwc-input-hover-border-color, var(--dwc-color-primary));
      color: var(--dwc-input-hover-color, var(--dwc-color-on-default-text-light));
    }

    .prompt-input[contenteditable]:empty::before {
      content: "Write your message";
      color: var(--dwc-color-gray-35);
    }

    @media (min-width: 768px) {
      .prompt-input[contenteditable]{
        font-size: 1.3em;
      }
    }

    .input-component > dwc-textarea::part(input) {
      --dwc-space: 0.6em;
    }
      """)
public class PromptInput extends Composite<FlexLayout> {
  private FlexLayout self = getBoundComponent();
  private SubmitEvent submitEvent;

  private Element messageTextField;

  public PromptInput() {

    self.addClassName("input-component")
        .setAlignment(FlexAlignment.CENTER)
        .setJustifyContent(FlexJustifyContent.CENTER);

    this.messageTextField = new Element("div")
        .setAttribute("contenteditable", "true")
        .setAttribute("role", "textbox")
        .addClassName("prompt-input");
    self.add(messageTextField);
    this.messageTextField.executeJsAsync("addKeyboardSubmitShortCut()");

    var submitButton = new Button("", ButtonTheme.PRIMARY, e -> {
      this.fireSubmitEvent();
    })
        .setSuffixComponent(TablerIcon.create("send-2"))
        .setExpanse(Expanse.XLARGE)
        .addClassName("submit-button");
    self.add(submitButton);
  }

  private void fireSubmitEvent() {
    if (submitEvent == null) {
      return;
    }
    var textContent = this.messageTextField.getText().trim();
    if (textContent.isEmpty()) {
      return;
    }
    this.submitEvent.onSubmit(textContent);
    this.messageTextField.setText("");
  }

  public void onSubmit(SubmitEvent submitEvent) {
    this.submitEvent = submitEvent;
  }
}
