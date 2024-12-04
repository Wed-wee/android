package com.practice.android_engineer_intern_assignment

import LoginResponse
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var errorTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText) // This is unused now
        loginButton = findViewById(R.id.loginButton)
        errorTextView = findViewById(R.id.errorTextView)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()

            if (email.isEmpty()) {
                errorTextView.text = "Please enter a valid email"
                errorTextView.visibility = TextView.VISIBLE
            } else {
                // Call loginUser function
                loginUser(email)
            }
        }
    }

    private fun loginUser(email: String) {
        val password = email.reversed()  // Reverse the email to use as the password

        // Create a map with credentials for login
        val credentials = mapOf("username" to email, "password" to password)

        // Make the login API call
        RetrofitInstance.apiService.login(credentials).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val authToken = response.body()?.auth_token
                    if (!authToken.isNullOrEmpty()) {
                        // Pass the auth_token to the next Activity
                        val intent = Intent(this@LoginActivity, ThreadsActivity::class.java) // Replace ThreadsActivity with ConversationActivity if needed
                        intent.putExtra("auth_token", authToken)
                        startActivity(intent)
                        finish() // Optional: Close LoginActivity after successful login
                    } else {
                        errorTextView.text = "Auth token is missing"
                        errorTextView.visibility = TextView.VISIBLE
                    }
                } else {
                    errorTextView.text = "Invalid credentials"
                    errorTextView.visibility = TextView.VISIBLE
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                errorTextView.text = "Login failed. Try again."
                errorTextView.visibility = TextView.VISIBLE
            }
        })
    }
}
