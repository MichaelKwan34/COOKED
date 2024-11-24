package com.group34.cooked

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ChatGPTService {
    @Headers(
        "Authorization: Bearer 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",
        "Content-Type: application/json"
    )
    @POST("v1/chat/completions")
    fun sendMessage(@Body request: ChatRequest): Call<ChatResponse>
}


