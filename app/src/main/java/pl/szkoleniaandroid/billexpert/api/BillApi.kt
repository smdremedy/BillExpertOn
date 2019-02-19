package pl.szkoleniaandroid.billexpert.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import java.io.Serializable

interface BillApi {

    @Headers(
        "X-Parse-Application-Id: RRQfzogXeuQI2VzK0bqEgn02IElfm3ifCUf1lNQX",
        "X-Parse-REST-API-Key: mt4btJUcnmVaEJGzncHqkogm0lDM3n2185UNSjiX",
        "X-Parse-Revocable-Session: 1"
    )
    @GET("login")
    fun getLogin(@Query("username") username: String,
                 @Query("password") password: String): Call<LoginResponse>

    @Headers(
        "X-Parse-Application-Id: RRQfzogXeuQI2VzK0bqEgn02IElfm3ifCUf1lNQX",
        "X-Parse-REST-API-Key: mt4btJUcnmVaEJGzncHqkogm0lDM3n2185UNSjiX"
    )
    @GET("classes/Bill")
    fun getBills() : Call<BillResponse>
}


data class LoginResponse(
    val sessionToken: String,
    val objectId: String
) : Serializable


enum class Category {
    OTHER,
    BILLS,
    CAR,
    CHEMISTRY,
    CLOTHES,
    COSMETICS,
    ELECTRONICS,
    ENTERTAINMENT,
    FOOD,
    FURNITURE,
    GROCERIES,
    HEALTH,
    SHOES,
    SPORT,
    TOYS,
    TRAVEL
}

data class BillResponse(val results: List<Bill>)

data class Bill(
    val objectId: String,
    val amount: Double,
    val category: Category,
    val comment: String,
    val date: String,
    val userId: String,
    val name: String
) : Serializable
