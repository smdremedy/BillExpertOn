package pl.szkoleniaandroid.billexpert

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager

class App : Application() {

    lateinit var loginManager: LoginManager

    override fun onCreate() {
        super.onCreate()
        loginManager = LoginManager(PreferenceManager.getDefaultSharedPreferences(this))
    }

}

val Activity.loginManager: LoginManager
    get() = (this.application as App).loginManager

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