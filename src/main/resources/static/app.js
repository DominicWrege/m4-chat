function scrollChat(behavior = "smooth") {
  const chatList = document.querySelector("div.chat-list");
  if (chatList) {
    chatList.scrollTo({ top: chatList.scrollHeight, behavior });
  }
}

const themeLink = document.createElement("link");
themeLink.rel = "stylesheet";
document.head.appendChild(themeLink);

function updateTheme() {
  const darkMode = window.matchMedia("(prefers-color-scheme: dark)").matches;
  themeLink.href = darkMode
    ? "/static/highlight-dark.min.css"
    : "/static/highlight.min.css";
  window.hljs.highlightAll();
}

updateTheme();

window
  .matchMedia("(prefers-color-scheme: dark)")
  .matchMedia("(prefers-color-scheme: light)")
  .addEventListener("change", updateTheme);
