package pl.szkoleniaandroid.billexpert

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_login.*
import pl.szkoleniaandroid.billexpert.api.Bill
import pl.szkoleniaandroid.billexpert.api.BillApi
import pl.szkoleniaandroid.billexpert.api.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class LoginActivity : AppCompatActivity(), LoginView {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        login_button.setOnClickListener {
            tryToLogin()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.loginView = this
    }

    override fun onStop() {
        super.onStop()
        viewModel.loginView = null
    }

    private fun tryToLogin() {
        val username = username_et.text.toString()
        val password = password_et.text.toString()

        var isValid = true
        if(username.isEmpty()) {
            username_layout.error = getString(R.string.username_empty_error)
            isValid = false
        }

        if(password.isEmpty()) {
            password_layout.error = getString(R.string.password_empty_error)
            isValid = false
        }
        if(isValid) {
            viewModel.login(username, password)
        }
    }

    override fun goToMain(body: LoginResponse) {
        //finish()
        val intent = Intent(this, BillsActivity::class.java)
        intent.putExtra("body", body)
        startActivityForResult(intent,1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode != Activity.RESULT_CANCELED && data != null) {
            val bill = data.getSerializableExtra("bill") as Bill
            Log.d("TAG", "Bill:$bill")
        }
    }
}


interface LoginView {
    fun goToMain(body: LoginResponse)
}

class LoginViewModel : ViewModel() {

    var loginView: LoginView? = null

    private var loginCall: Call<LoginResponse>? = null

    fun login(username: String, password: String) {


        val retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("https://parseapi.back4app.com")
            .build()

        val billApi = retrofit.create(BillApi::class.java)

        if(loginCall == null) {
            loginCall = billApi.getLogin(username, password)
            loginCall!!.enqueue(object : Callback<LoginResponse> {
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    loginCall = null
                }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if(response.isSuccessful) {
                        val body = response.body()!!
                        loginView?.goToMain(body)
                    }
                    loginCall = null
                }

            })
        }

    }

    override fun onCleared() {
        super.onCleared()
        loginCall?.cancel()
        loginCall = null
    }
}
