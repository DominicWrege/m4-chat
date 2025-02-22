function scrollToBottom(behavior = "smooth") {
  const chatList = document.querySelector("div.chat-list");
  if (chatList) {
    chatList.scrollTo({ top: chatList.scrollHeight, behavior });
  }
}

let themeLink = null;

function supportTheme() {
  if (!themeLink) {
    themeLink = document.createElement("link");
    themeLink.rel = "stylesheet";
    document.head.appendChild(themeLink);
  }

  function updateTheme() {
    const darkMode = window.matchMedia("(prefers-color-scheme: dark)").matches;
    themeLink.href = darkMode
      ? "/static/highlight-dark.min.css"
      : "/static/highlight.min.css";
  }

  updateTheme();
  window
    .matchMedia("(prefers-color-scheme: dark)")
    .addEventListener("change", updateTheme);
}

function highlightCode() {
  if (!hljs) {
    return;
  }
  document
    .querySelectorAll("div.chat-list pre code:not(.hljs)")
    .forEach(hljs.highlightElement);
}

function addKeyboardSubmitShortCut() {
  const input = document.querySelector("div.prompt-input");
  const button = document.querySelector(".submit-button");
  console.debug(button);
  input?.addEventListener("keydown", (event) => {
    if (event.key === "Enter" && !event.shiftKey) {
      button.click();
    }
  });
}
