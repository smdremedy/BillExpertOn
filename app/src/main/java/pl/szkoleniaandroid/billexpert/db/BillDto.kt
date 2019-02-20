package pl.szkoleniaandroid.billexpert.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import pl.szkoleniaandroid.billexpert.api.Category

@Entity(tableName = "bill")
@TypeConverters(Converters::class)
class BillDto {

    @PrimaryKey
    var id: String = ""

    var name: String = ""
    var comment: String? = null
    var amount: Double = 0.0
    var category: Category = Category.OTHER

}

class Converters {

    @TypeConverter
    fun fromCategory(category: Category): Int = category.ordinal

    @TypeConverter
    fun toCategory(ordinal: Int): Category = Category.values()[ordinal]

}