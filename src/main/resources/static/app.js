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
    .querySelectorAll(".chat-list pre code:not(.hljs)")
    .forEach(hljs.highlightElement);
}
