package pl.szkoleniaandroid.billexpert

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import pl.szkoleniaandroid.billexpert.api.BillApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class App : Application() {

    lateinit var loginManager: LoginManager
    lateinit var billApi: BillApi

    override fun onCreate() {
        super.onCreate()
        loginManager = LoginManager(PreferenceManager.getDefaultSharedPreferences(this))

        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("https://parseapi.back4app.com")
            .build()

        billApi = retrofit.create(BillApi::class.java)

    }

}

val Activity.loginManager: LoginManager
    get() = (this.application as App).loginManager

val Activity.billApi: BillApi
    get() = (this.application as App).billApi

class LoginManager(val preferences: SharedPreferences) {


    fun save(token: String, userId: String) {

        preferences.edit()
            .putString(LoginActivity.TOKEN_KEY, token)
            .putString(LoginActivity.USER_ID_KEY, userId)
            .apply()
    }

    fun logout() {

        preferences.edit().clear().apply()
    }

    fun isNotLoggedIn(): Boolean {
        return preferences.getString(LoginActivity.TOKEN_KEY, "").isEmpty()
    }

}