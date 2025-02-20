function scrollChat(behavior = "smooth") {
  const chatList = document.querySelector("div.chat-list");
  if (chatList) {
    chatList.scrollTo({ top: chatList.scrollHeight, behavior });
  }
}
