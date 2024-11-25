package com.group34.cooked

data class ChatRequest(
    val model: String, // E.g., "gpt-3.5-turbo"
    val messages: List<Message>
)

data class Message(
    val role: String,   // Either "user" or "assistant"
    val content: String // The message text
)
