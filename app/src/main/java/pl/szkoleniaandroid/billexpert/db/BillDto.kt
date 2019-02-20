package pl.szkoleniaandroid.billexpert.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import pl.szkoleniaandroid.billexpert.api.Category

@Entity(tableName = "bill")
class BillDto {

    @PrimaryKey
    var id: String = ""

    var name: String = ""
    var comment: String? = null
    var amount: Double = 0.0
    var category: Category = Category.OTHER

}