package com.practice.android_engineer_intern_assignment

import Message
import MessageRequest
import MessagesAdapter
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class ConversationActivity : AppCompatActivity() {

    private lateinit var authToken: String
    private var threadId by Delegates.notNull<Int>()
    private lateinit var messagesRecyclerView: RecyclerView
    private lateinit var messageInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.conversation)

        // Get the authToken and threadId from the intent extras
        authToken = intent.getStringExtra("auth_token") ?: ""
        threadId = intent.getIntExtra("thread_id", 0)  // default value 0 if not found

        if (threadId == 0) {
            // Handle case where threadId is 0 or invalid (optional)
            Toast.makeText(this, "Invalid thread ID", Toast.LENGTH_SHORT).show()
            finish() // close the activity or handle the error gracefully
            return
        }

        messagesRecyclerView = findViewById(R.id.messagesRecyclerView)
        messageInput = findViewById(R.id.messageInput)

        messagesRecyclerView.layoutManager = LinearLayoutManager(this)

        val sendButton: Button = findViewById(R.id.sendButton)
        sendButton.setOnClickListener {
            val messageBody = messageInput.text.toString()
            if (messageBody.isNotEmpty()) {
                sendMessage(threadId, messageBody)
            }
        }

        loadMessages(threadId)
    }


    private fun loadMessages(threadId: Int) {
        RetrofitInstance.apiService.getMessages(authToken).enqueue(object : Callback<List<Message>> {
            override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
                if (response.isSuccessful) {
                    val messages = response.body()?.filter { it.thread_id == threadId } ?: emptyList()
                    messagesRecyclerView.adapter = MessagesAdapter(messages)
                }
            }

            override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                Toast.makeText(this@ConversationActivity, "Failed to load messages", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendMessage(threadId: Int, messageBody: String) {
        val messageRequest = MessageRequest(threadId, messageBody)

        RetrofitInstance.apiService.sendMessage(authToken, messageRequest).enqueue(object : Callback<Message> {
            override fun onResponse(call: Call<Message>, response: Response<Message>) {
                if (response.isSuccessful) {
                    messageInput.text.clear()
                    loadMessages(threadId) // Refresh messages
                } else {
                    // Log the error response
                    Toast.makeText(this@ConversationActivity, "Failed to send message: ${response.message()}", Toast.LENGTH_SHORT).show()
                    Log.e("SendMessageError", "Error: ${response.code()} - ${response.message()}")
                    // Log the response body for debugging
                    response.errorBody()?.let {
                        Log.e("SendMessageError", "Error body: ${it.string()}")
                    }
                }
            }

            override fun onFailure(call: Call<Message>, t: Throwable) {
                Toast.makeText(this@ConversationActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("SendMessageError", "Error: ${t.message}")
            }
        })
    }

}
