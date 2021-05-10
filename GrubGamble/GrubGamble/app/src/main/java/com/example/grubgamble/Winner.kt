package com.example.grubgamble

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.grubgamble.R

class Winner : AppCompatActivity() {
    var db: DataBase = DataBase(this)
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        val theView: TableLayout = findViewById(R.id.theTable) as TableLayout
        val rdb: SQLiteDatabase = db.getReadableDatabase()
        val selectQuery = "SELECT  * FROM " + List.table
        try {
            val cursor: Cursor = rdb.rawQuery(selectQuery, null)
            if (cursor != null) {
                cursor.moveToFirst()
                var data: TextView
                var row: TableRow
                var cnt = 0
                do {
                    row = TableRow(this@Winner)
                    row.setPadding(2, 2, 2, 2)
                    if (cnt++ % 2 == 0) row.setBackgroundColor(Color.WHITE)
                    for (x in 0 until cursor.columnCount) {
                        data = TextView(this@Winner)
                        if (x == 0) {
                            data.setTypeface(Typeface.DEFAULT_BOLD)
                            data.setGravity(Gravity.CENTER_HORIZONTAL)
                        }
                        data.setText(cursor.getString(x))
                        row.addView(data)
                    }
                    theView.addView(row)
                } while (cursor.moveToNext())
                theView.setShrinkAllColumns(true)
                theView.setStretchAllColumns(true)
            }
        } catch (ex: Exception) {
        }
    }
}