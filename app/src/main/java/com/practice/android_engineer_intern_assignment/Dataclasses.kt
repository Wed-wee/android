data class Message(
    val id: Int,
    val thread_id: Int,
    val user_id: Int?,
    val agent_id: Int?,
    val body: String,
    val timestamp: String
)

data class Threads(
    val id: Int,
    val body: String,
    val timestamp: String,
    val user_id: Int
)

data class MessageRequest(
    val thread_id: Int,
    val body: String
)

data class LoginResponse(
    val auth_token: String
)
