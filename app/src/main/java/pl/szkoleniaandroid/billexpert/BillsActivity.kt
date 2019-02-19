package pl.szkoleniaandroid.billexpert

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_bills.*
import kotlinx.android.synthetic.main.content_bills.*
import pl.szkoleniaandroid.billexpert.api.Bill
import pl.szkoleniaandroid.billexpert.api.LoginResponse

class BillsActivity : AppCompatActivity() {

    private lateinit var adapter: ArrayAdapter<Bill>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bills)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        adapter = ArrayAdapter<Bill>(this, android.R.layout.simple_list_item_1)
        bills_list.adapter = adapter

        adapter.addAll(
            Array(100, { Bill("bill $it")}).toList()
        )

        bills_list.setOnItemClickListener { parent, view, position, id ->
            val bill = adapter.getItem(position)
            Log.d("TAG", "Clicked:$bill")
            val data = Intent()
            data.putExtra("bill", bill)
            setResult(Activity.RESULT_OK, data)
            finish()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.bills, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }

}
