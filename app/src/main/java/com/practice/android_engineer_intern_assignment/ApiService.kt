
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("api/login")
    fun login(@Body credentials: Map<String, String>): Call<LoginResponse>

    @GET("api/messages")
    fun getMessages(@Header("X-Branch-Auth-Token") authToken: String): Call<List<Message>>

    @POST("api/messages")
    fun sendMessage(@Header("X-Branch-Auth-Token") authToken: String, @Body message: MessageRequest): Call<Message>
}
