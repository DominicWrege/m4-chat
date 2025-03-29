# 🗨️ M4 Chat 

An other **ChatGPT** UI built with [webforj](https://webforj.com/), **Java 21** and **SQlite**.

## ✈ How to Run

⚠️ Important: Set Environment Variables First!

To get started with this archetype, open the project in your IDE and run the following command to start the development server:

```bash
mvn jetty:run
```

### Running Integration Tests

To run the integration tests, use the following command:

```bash
mvn verify
```

## Environment Variables

You need an OpenAI API key to use this application. Get one from OpenAI and set it before running the app.
This application uses SQLite for storing chat history. Ensure that the Path to DATABASE_FILE environment variable is correctly set to a writable SQLite database file.

```
OPENAI_API_KEY=xxxxxxxxxxx
DATABASE_FILE=./database.db
```

## 🏗 Build & Run with Docker

```bash
docker build -t m4-chat-app:1.0 .
docker run -p 8080:8080 \
    -e OPENAI_API_KEY="your-api-key" \
    -e DATABASE_FILE="/data/m4-chat.db" \
    m4-chat-app:1.0
```

or pull existing [docker image](https://hub.docker.com/repository/docker/dominicwrege/m4-chat/general)

```bash
docker pull dominicwrege/m4-chat:latest
```

## 📌 Features

✅ **AI-Powered Chat** with OpenAI's ChatGPT API  
✅ **Simple and Lightweight** design  
✅ **Database Persistence** for chat history  
✅ **Dockerized** for easy deployment

---

## Documentation

For more information, check out our comprehensive documentation [here](https://documentation.webforj.com). You can read more about available [components](https://documentation.webforj.com/docs/components/overview), [app basics](https://documentation.webforj.com/docs/intro/basics), and much more.
