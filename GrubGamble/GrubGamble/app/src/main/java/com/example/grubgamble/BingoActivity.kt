package com.example.grubgamble

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class BingoActivity : AppCompatActivity() {
    lateinit var calendarLayout: RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bingo_main)
        calendarLayout = findViewById<View>(R.id.calendarLayout) as RelativeLayout
        val btn = findViewById<View>(R.id.btn_date) as Button
        val selectedDate = findViewById<View>(R.id.calendarView) as DatePicker
        val date =
            selectedDate.year.toString() + "-" + (selectedDate.month + 1) + "-" + selectedDate.dayOfMonth
        val dt = findViewById<View>(R.id.textView_date) as TextView
        dt.text = date
        btn.setOnClickListener {
            calendarLayout!!.visibility = View.VISIBLE
            val date2 =
                selectedDate.year.toString() + "-" + (selectedDate.month + 1) + "-" + selectedDate.dayOfMonth
            dt.text = date2
        }
        val gobtn = findViewById<View>(R.id.btnNxt) as Button
        gobtn.setOnClickListener {
            val name = findViewById<View>(R.id.editText_name) as EditText
            val intent = Intent(this, Bingo::class.java)
            val bundle = Bundle()
            val date2 =
                selectedDate.year.toString() + "-" + (selectedDate.month + 1) + "-" + selectedDate.dayOfMonth
            bundle.putString("name", name.text.toString())
            bundle.putString("date", date2)
            intent.putExtras(bundle)
            startActivity(intent)
            finish()
        }
    }
}