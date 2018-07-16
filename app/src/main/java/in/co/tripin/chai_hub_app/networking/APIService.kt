package `in`.co.tripin.chai_hub_app.networking


import `in`.co.tripin.chai_hub_app.POJOs.Bodies.ChangePinBody
import `in`.co.tripin.chai_hub_app.POJOs.Bodies.ForgotPassOTPBody
import `in`.co.tripin.chai_hub_app.POJOs.Bodies.LogInBody
import `in`.co.tripin.chai_hub_app.POJOs.Bodies.ResetPassBody
import `in`.co.tripin.chai_hub_app.POJOs.Responces.*
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.http.*


public interface APIService {

//
//    @GET("search/users")
//    fun search(@Query("q") query: String,
//               @Query("page") page: Int,
//               @Query("per_page") perPage: Int): Observable<Result>

    @POST("v1/user/signIn")
    abstract fun logInUser(@Body body: LogInBody): Observable<Response<LogInResponce>>

    @POST("v1/otp/send")
    abstract fun sendOTPforgotpassword(@Body body: ForgotPassOTPBody): Observable<Response<ForgotPassResponce>>

    @POST("v1/user/password/forget")
    abstract fun resetPassword(@Body body: ResetPassBody): Observable<ResetPassResponce>


    @GET("v2/hub/orders?type=pending")
    abstract fun getPendingOrders(@Header("token")  token :String): Observable<PendingOrdersResponce>

    @GET("v1/tapri/hub")
    abstract fun getHubDetails(@Header("token")  token :String): Observable<MappedHubResponce>


    @POST("v1/user/password/update")
    abstract fun changePIN(@Header("token")  token :String,
                                  @Header("Content-Type")  ContentType :String,
                                  @Body body: ChangePinBody): Observable<ChangePinResponce>

    /**
     * Companion object to create the GithubApiService
     */
    companion object Factory {
        fun create(): APIService {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://192.168.1.21:3055/api/")
                    .build()

            return retrofit.create(APIService::class.java)
        }
    }
}
