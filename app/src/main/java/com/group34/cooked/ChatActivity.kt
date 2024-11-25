package com.group34.cooked

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call


class ChatActivity : AppCompatActivity() {
    private lateinit var inputField: EditText
    private lateinit var sendButton: Button
    private lateinit var chatOutput: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        inputField = findViewById(R.id.inputField)
        sendButton = findViewById(R.id.sendButton)
        chatOutput = findViewById(R.id.chatOutput)

        sendButton.setOnClickListener {
            val userMessage = inputField.text.toString().trim()
            if (userMessage.isNotEmpty()) {
                sendMessageToChatGPT(userMessage)
                inputField.text.clear()
            }
        }
    }

    private fun sendMessageToChatGPT(message: String) {
        // Append the user message to the chat output
        chatOutput.append("You: $message\n")

        // Create Retrofit instance
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ChatGPTService::class.java)

        // Prepare request
        val messages = listOf(Message("user", message))
        val request = ChatRequest("gpt-3.5turbo", messages)

        // Make the API call
        service.sendMessage(request).enqueue(object : retrofit2.Callback<ChatResponse> {
            override fun onResponse(call: Call<ChatResponse>, response: retrofit2.Response<ChatResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val reply = response.body()?.choices?.get(0)?.message?.content ?: "No reply"
                    chatOutput.append("ChatGPT: $reply\n")
                } else {
                    val errorBody = response.errorBody()?.string() ?: "No error body"
                    chatOutput.append("ChatGPT: Failed to fetch response.\n")
                    Log.e("ChatGPTError", "Error response: $errorBody")
                }
            }


            override fun onFailure(call: Call<ChatResponse>, t: Throwable) {
                chatOutput.append("ChatGPT: Error - ${t.message}\n")
            }
        })
    }

}
