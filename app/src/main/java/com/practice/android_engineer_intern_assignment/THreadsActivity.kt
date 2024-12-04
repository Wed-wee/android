package com.practice.android_engineer_intern_assignment

import Message
import Threads
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ThreadsActivity : AppCompatActivity() {

    private lateinit var threadsRecyclerView: RecyclerView
    private lateinit var authToken: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_threads)

        authToken = intent.getStringExtra("auth_token") ?: ""

        threadsRecyclerView = findViewById(R.id.threadsRecyclerView)
        threadsRecyclerView.layoutManager = LinearLayoutManager(this)

        RetrofitInstance.apiService.getMessages(authToken).enqueue(object : Callback<List<Message>> {
            override fun onResponse(call: Call<List<Message>>, response: Response<List<Message>>) {
                if (response.isSuccessful) {
                    val threads = response.body()?.groupBy { it.thread_id }?.map { entry ->
                        val lastMessage = entry.value.last() // Get the most recent message in the thread
                        lastMessage.user_id?.let {
                            Threads(
                                entry.key,                // The thread_id (Int)
                                lastMessage.body,         // The body of the last message
                                lastMessage.timestamp,    // Timestamp of the last message
                                it      // The user_id of the last message
                            )
                        }
                    } ?: emptyList()

                    threadsRecyclerView.adapter = ThreadsAdapter(threads) { thread ->
                        val intent = Intent(this@ThreadsActivity, ConversationActivity::class.java)
                        intent.putExtra("auth_token", authToken)
                        intent.putExtra("thread_id", thread.id)
                        startActivity(intent)
                    }
                }

            }

            override fun onFailure(call: Call<List<Message>>, t: Throwable) {
                Toast.makeText(this@ThreadsActivity, "Failed to load threads", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
