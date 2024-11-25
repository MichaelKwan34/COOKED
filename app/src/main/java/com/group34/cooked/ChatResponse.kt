package com.group34.cooked

data class ChatResponse(
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)
