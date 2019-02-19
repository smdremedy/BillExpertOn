package pl.szkoleniaandroid.billexpert

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_bills.*
import kotlinx.android.synthetic.main.bill_item.view.*
import kotlinx.android.synthetic.main.content_bills.*
import pl.szkoleniaandroid.billexpert.api.Bill
import pl.szkoleniaandroid.billexpert.api.BillResponse
import pl.szkoleniaandroid.billexpert.api.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BillsActivity : AppCompatActivity() {

    private lateinit var adapter: ArrayAdapter<Bill>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(loginManager.isNotLoggedIn()){
            goToLogin()
            return
        }


        setContentView(R.layout.activity_bills)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        adapter = object: ArrayAdapter<Bill>(this, android.R.layout.simple_list_item_1) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                Log.d("TAG", "CV:$convertView")
                var view  = convertView ?: LayoutInflater.from(context).inflate(R.layout.bill_item, parent, false)

                val bill = getItem(position)
                view.comment_tv.text = bill.comment
                view.amount_tv.text = bill.amount.toString()

                val vh: ViewHolder = view.tag as? ViewHolder ?: ViewHolder(view.imageView, view.name_tv)
                vh.nameTV.text = bill.name


                Picasso.get()
                    .load("file:///android_asset/${bill.category.name.toLowerCase()}.png")
                    .into(vh.iv)

                view.tag = vh

                return view
            }
        }
        bills_list.adapter = adapter



        bills_list.setOnItemClickListener { parent, view, position, id ->
            val bill = adapter.getItem(position)
            Log.d("TAG", "Clicked:$bill")
            val data = Intent()
            data.putExtra("bill", bill)
            setResult(Activity.RESULT_OK, data)
            finish()
        }

    }

    private fun goToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bills, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.add_action -> {
                startActivity(Intent(this, DetailsActivity::class.java))
                return true
            }
            R.id.refresh_action -> {
                billApi.getBills().enqueue(object: Callback<BillResponse>{
                    override fun onFailure(call: Call<BillResponse>, t: Throwable) {

                    }

                    override fun onResponse(call: Call<BillResponse>, response: Response<BillResponse>) {
                        if(response.isSuccessful) {
                            val billResponse = response.body()
                            Log.d("TAG", billResponse.toString())
                            adapter.addAll(billResponse!!.results)
                        } else {
                            response.errorBody()
                        }
                    }
                })
                return true
            }
            R.id.logout_action -> {
                val builder = AlertDialog.Builder(this)

                builder.setTitle("Logout").setMessage("Are you sure?")
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("OK") { _, _ ->
                        loginManager.logout()

                        goToLogin()
                    }
                builder.create().show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}

class ViewHolder(val iv: ImageView, val nameTV: TextView)
