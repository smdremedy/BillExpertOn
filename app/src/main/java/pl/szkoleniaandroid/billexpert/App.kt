package pl.szkoleniaandroid.billexpert

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.room.Room
import androidx.room.RoomDatabase
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import pl.szkoleniaandroid.billexpert.api.BillApi
import pl.szkoleniaandroid.billexpert.db.BillDao
import pl.szkoleniaandroid.billexpert.db.BillsDatabase
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class App : Application() {

    lateinit var loginManager: LoginManager
    lateinit var billApi: BillApi
    lateinit var billDao: BillDao

    override fun onCreate() {
        super.onCreate()
        loginManager = LoginManager(PreferenceManager.getDefaultSharedPreferences(this))

        if(BuildConfig.DEBUG) {

            Stetho.initializeWithDefaults(this)

        }
        val client = OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("https://parseapi.back4app.com")
            .build()

        billApi = retrofit.create(BillApi::class.java)

        val db = Room.databaseBuilder(this, BillsDatabase::class.java, "bills.db").build()

        billDao = db.getBillDao()
    }

}

val Activity.loginManager: LoginManager
    get() = (this.application as App).loginManager

val Activity.billApi: BillApi
    get() = (this.application as App).billApi

val Activity.billDao: BillDao
    get() = (this.application as App).billDao

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