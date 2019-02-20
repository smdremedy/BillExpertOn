package pl.szkoleniaandroid.billexpert.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BillDao {

    @Insert
    fun save(billDto: BillDto)

    @Query("SELECT * from bill")
    fun getAllBills(): List<BillDto>

    @Query("SELECT * from bill WHERE amount > :min")
    fun getBigBills(min: Double) : List<BillDto>

    @Delete
    fun delete(billDto: BillDto)

    @Query("SELECT name, amount from bill")
    fun getSimpleBill(): List<SimpleBill>
}

class SimpleBill(val name: String, val amount: Double)