package com.example.grubgamble

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import java.util.Map
import kotlin.collections.indices
import kotlin.collections.remove
import kotlin.random.Random


class Bingo : AppCompatActivity() {
    private var h1 = 0
    private var h2 = 0
    private var h3 = 0
    private var h4 = 0
    private var h5 = 0
    private var v1 = 0
    private var v2 = 0
    private var v3 = 0
    private var v4 = 0
    private var v5 = 0
    private var d1 = 0
    private var d2 = 0
    private var WinCount = 0
    private var count = 0
    private var rowTextView: TextView? = null
    var db: DataBase = DataBase(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        val wdb: SQLiteDatabase = db.getWritableDatabase()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bingo)
        val bundle = intent.extras
        val nameuser = bundle!!.getString("name")
        Log.d("fds", nameuser!!)
        val nme = findViewById<View>(R.id.username) as TextView
        nme.text = nameuser.toString()
        val dte = bundle.getString("date")
        val dte2 = findViewById<View>(R.id.dte) as TextView
        dte2.text = dte
        val counte = findViewById<View>(R.id.rounds) as TextView
        val _b = findViewById<View>(R.id._B) as TextView
        val _i = findViewById<View>(R.id._I) as TextView
        val _n = findViewById<View>(R.id._N) as TextView
        val _g = findViewById<View>(R.id._G) as TextView
        val _o = findViewById<View>(R.id._O) as TextView
        val _1 = findViewById<View>(R.id._1) as TextView
        val _2 = findViewById<View>(R.id._2) as TextView
        val _3 = findViewById<View>(R.id._3) as TextView
        val _4 = findViewById<View>(R.id._4) as TextView
        val _5 = findViewById<View>(R.id._5) as TextView
        val _6 = findViewById<View>(R.id._6) as TextView
        val _7 = findViewById<View>(R.id._7) as TextView
        val _8 = findViewById<View>(R.id._8) as TextView
        val _9 = findViewById<View>(R.id._9) as TextView
        val _10 = findViewById<View>(R.id._10) as TextView
        val _11 = findViewById<View>(R.id._11) as TextView
        val _12 = findViewById<View>(R.id._12) as TextView
        val _13 = findViewById<View>(R.id._13) as TextView
        val _14 = findViewById<View>(R.id._14) as TextView
        val _15 = findViewById<View>(R.id._15) as TextView
        val _16 = findViewById<View>(R.id._16) as TextView
        val _17 = findViewById<View>(R.id._17) as TextView
        val _18 = findViewById<View>(R.id._18) as TextView
        val _19 = findViewById<View>(R.id._19) as TextView
        val _20 = findViewById<View>(R.id._20) as TextView
        val _21 = findViewById<View>(R.id._21) as TextView
        val _22 = findViewById<View>(R.id._22) as TextView
        val _23 = findViewById<View>(R.id._23) as TextView
        val _24 = findViewById<View>(R.id._24) as TextView
        val _25 = findViewById<View>(R.id._25) as TextView
        val LinLay = findViewById<View>(R.id.lin) as LinearLayout
        val data = findViewById<View>(R.id.randomNum) as TextView
        val size = 100
        val list = ArrayList<Int>(size)
        for (i in 1..size) {
            list.add(i)
        }
        val listForB = ArrayList<Int?>(size)
        for (i in 1..15) {
            listForB.add(i)
        }
        val ranbtn = findViewById<View>(R.id.Ranbtn) as Button
        ranbtn.setOnClickListener {
            list.shuffle()
            listForB.shuffle()
            /*listForB[0]?.let { it1 -> list.removeAt(it1) }
            listForB[1]?.let { it1 -> list.removeAt(it1) }
            listForB[2]?.let { it1 -> list.removeAt(it1) }
            listForB[3]?.let { it1 -> list.removeAt(it1) }
            listForB[4]?.let { it1 -> list.removeAt(it1) }*/
            _1.text = listForB[0].toString()
            _2.text = list[0].toString()
            _3.text = list[1].toString()
            _4.text = list[2].toString()
            _5.text = list[3].toString()
            _6.text = listForB[1].toString()
            _7.text = list[4].toString()
            _8.text = list[5].toString()
            _9.text = list[6].toString()
            _10.text = list[7].toString()
            _11.text = listForB[2].toString()
            _12.text = list[8].toString()
            _13.text = list[9].toString()
            _14.text = list[10].toString()
            _15.text = list[11].toString()
            _16.text = listForB[3].toString()
            _17.text = list[12].toString()
            _18.text = list[13].toString()
            _19.text = list[14].toString()
            _20.text = list[15].toString()
            _21.text = listForB[4].toString()
            _22.text = list[16].toString()
            _23.text = list[17].toString()
            _24.text = list[18].toString()
            _25.text = list[19].toString()
            list.shuffle()
        }
        val randomlist = ArrayList<Int>(99)
        randomlist.clear()
        for (i in list.indices) {
            randomlist.add(list[i])
        }
        val b = findViewById<View>(R.id.ranNumbtn) as Button
        b.setOnClickListener {
            count++
            counte.text = count.toString()
            Log.d("randomlist", randomlist.toString())
            var random = 0
            var randomIndex = Random.nextInt(100)
            if (!randomlist.isEmpty()) {
                Log.d("dfs", randomlist[randomIndex].toString())
                random = randomlist[randomIndex]
                data.text = Integer.toString(random)
                //randomlist.removeAt(randomIndex)
            }
            Log.d("randomNumber", Integer.toString(random))
            if (_1.text.toString() == Integer.toString(random)) {
                Log.d("first cell", _1.text.toString())
                _1.setBackgroundResource(R.drawable.circle)
                _1.setTextColor(Color.BLACK)
                h1++
                v1++
                d1++
            } else if (_2.text.toString() == Integer.toString(random)) {
                Log.d("first cell", _2.text.toString())
                _2.setBackgroundResource(R.drawable.circle)
                _2.setTextColor(Color.BLACK)
                h1++
                v2++
            } else if (_3.text.toString() == Integer.toString(random)) {
                Log.d("first cell", _3.text.toString())
                _3.setBackgroundResource(R.drawable.circle)
                _3.setTextColor(Color.BLACK)
                h1++
                v3++
            } else if (_4.text.toString() == Integer.toString(random)) {
                Log.d("first cell", _4.text.toString())
                _4.setBackgroundResource(R.drawable.circle)
                _4.setTextColor(Color.BLACK)
                h1++
                v4++
            } else if (_5.text.toString() == Integer.toString(random)) {
                Log.d("first cell", _5.text.toString())
                _5.setBackgroundResource(R.drawable.circle)
                _5.setTextColor(Color.BLACK)
                h1++
                v5++
                d2++
            } else if (_6.text.toString() == Integer.toString(random)) {
                Log.d("first cell", _6.text.toString())
                _6.setBackgroundResource(R.drawable.circle)
                _6.setTextColor(Color.BLACK)
                h2++
                v1++
            } else if (_7.text.toString() == Integer.toString(random)) {
                Log.d("first cell", _7.text.toString())
                _7.setBackgroundResource(R.drawable.circle)
                _7.setTextColor(Color.BLACK)
                h2++
                d1++
                v2++
            } else if (_8.text.toString() == Integer.toString(random)) {
                Log.d("first cell", _8.text.toString())
                _8.setBackgroundResource(R.drawable.circle)
                _8.setTextColor(Color.BLACK)
                h2++
                v3++
            } else if (_9.text.toString() == Integer.toString(random)) {
                Log.d("first cell", _9.text.toString())
                _9.setBackgroundResource(R.drawable.circle)
                _9.setTextColor(Color.BLACK)
                h2++
                v4++
                d2++
            } else if (_10.text.toString() == Integer.toString(random)) {
                Log.d("first cell", _10.text.toString())
                _10.setBackgroundResource(R.drawable.circle)
                _10.setTextColor(Color.BLACK)
                h2++
                v5++
            } else if (_11.text.toString() == Integer.toString(random)) {
                Log.d("first cell", _11.text.toString())
                _11.setBackgroundResource(R.drawable.circle)
                _11.setTextColor(Color.BLACK)
                h3++
                v1++
            } else if (_12.text.toString() == Integer.toString(random)) {
                Log.d("first cell", _12.text.toString())
                _12.setBackgroundResource(R.drawable.circle)
                _12.setTextColor(Color.BLACK)
                h3++
                v2++
            } else if (_13.text.toString() == Integer.toString(random)) {
                Log.d("first cell", _13.text.toString())
                _13.setBackgroundResource(R.drawable.circle)
                _13.setTextColor(Color.BLACK)
                h3++
                v3++
                d1++
                d2++
            } else if (_14.text.toString() == Integer.toString(random)) {
                Log.d("first cell", _14.text.toString())
                _14.setBackgroundResource(R.drawable.circle)
                _14.setTextColor(Color.BLACK)
                h3++
                v4++
            } else if (_15.text.toString() == Integer.toString(random)) {
                Log.d("first cell", _15.text.toString())
                _15.setBackgroundResource(R.drawable.circle)
                _15.setTextColor(Color.BLACK)
                h3++
                v5++
            } else if (_16.text.toString() == Integer.toString(random)) {
                Log.d("first cell", _16.text.toString())
                _16.setBackgroundResource(R.drawable.circle)
                _16.setTextColor(Color.BLACK)
                h4++
                v1++
            } else if (_17.text.toString() == Integer.toString(random)) {
                Log.d("first cell", _17.text.toString())
                _17.setBackgroundResource(R.drawable.circle)
                _17.setTextColor(Color.BLACK)
                h4++
                v2++
                d2++
            } else if (_18.text.toString() == Integer.toString(random)) {
                Log.d("18 cell", _18.text.toString())
                _18.setBackgroundResource(R.drawable.circle)
                _18.setTextColor(Color.BLACK)
                h4++
                v3++
            } else if (_19.text.toString() == Integer.toString(random)) {
                Log.d("19 cell", _19.text.toString())
                _19.setBackgroundResource(R.drawable.circle)
                _19.setTextColor(Color.BLACK)
                h4++
                v4++
                d1++
            } else if (_20.text.toString() == Integer.toString(random)) {
                Log.d("20 cell", _20.text.toString())
                _20.setBackgroundResource(R.drawable.circle)
                _20.setTextColor(Color.BLACK)
                h4++
                v5++
            } else if (_21.text.toString() == Integer.toString(random)) {
                Log.d("21 cell", _21.text.toString())
                _21.setBackgroundResource(R.drawable.circle)
                _21.setTextColor(Color.BLACK)
                h5++
                v1++
                d2++
            } else if (_22.text.toString() == Integer.toString(random)) {
                Log.d("22 cell", _22.text.toString())
                _22.setBackgroundResource(R.drawable.circle)
                _22.setTextColor(Color.BLACK)
                h5++
                v2++
            } else if (_23.text.toString() == Integer.toString(random)) {
                Log.d("23 cell", _23.text.toString())
                _23.setBackgroundResource(R.drawable.circle)
                _23.setTextColor(Color.BLACK)
                h5++
                v3++
            } else if (_24.text.toString() == Integer.toString(random)) {
                Log.d("24 cell", _24.text.toString())
                _24.setBackgroundResource(R.drawable.circle)
                _24.setTextColor(Color.BLACK)
                h5++
                v4++
            } else if (_25.text.toString() == Integer.toString(random)) {
                Log.d("25 cell", _25.text.toString())
                _25.setBackgroundResource(R.drawable.circle)
                _25.setTextColor(Color.BLACK)
                h5++
                v5++
                d1++
            }
            if (h1 == 5) {
                WinCount++
                h1 = 0
            }
            if (h2 == 5) {
                WinCount++
                h2 = 0
            }
            if (h3 == 5) {
                WinCount++
                h3 = 0
            }
            if (h4 == 5) {
                WinCount++
                h4 = 0
            }
            if (h5 == 5) {
                WinCount++
                h5 = 0
            }
            if (v1 == 5) {
                WinCount++
                v1 = 0
            }
            if (v2 == 5) {
                WinCount++
                v2 = 0
            }
            if (v3 == 5) {
                WinCount++
                v3 = 0
            }
            if (v4 == 5) {
                WinCount++
                v4 = 0
            }
            if (v5 == 5) {
                WinCount++
                v5 = 0
            }
            if (d1 == 5) {
                WinCount++
                d1 = 0
            }
            if (d2 == 5) {
                WinCount++
                d2 = 0
            }
            val arr = intArrayOf(h1, h2, h3, h4, h5, v1, v2, v3, v4, v5, d1, d2)
            Log.d("wincount", Integer.toString(WinCount))
            Log.d("wincount", Arrays.toString(arr))
            if (WinCount >= 1) _b.setTextColor(Color.RED)
            if (WinCount >= 2) {
                _b.setTextColor(Color.RED)
                _i.setTextColor(Color.RED)
            }
            if (WinCount >= 3) {
                _n.setTextColor(Color.RED)
                _b.setTextColor(Color.RED)
                _i.setTextColor(Color.RED)
            }
            if (WinCount >= 4) {
                _g.setTextColor(Color.RED)
                _n.setTextColor(Color.RED)
                _b.setTextColor(Color.RED)
                _i.setTextColor(Color.RED)
            }
            if (WinCount >= 5) {
                _o.setTextColor(Color.RED)
                _g.setTextColor(Color.RED)
                _n.setTextColor(Color.RED)
                _b.setTextColor(Color.RED)
                _i.setTextColor(Color.RED)
                val nameuser = bundle.getString("name")
                val values = ContentValues()
                values.put(List.col2, nameuser)
                wdb.insert(List.table, null, values)
                val intent = Intent(this@Bingo, Winner::class.java)
                startActivity(intent)
                finish()
            }
        }
        val myTextViews = arrayOfNulls<TextView>(25)
        for (i in 0..24) {
            rowTextView = TextView(this)
            //rowTextView.setText("fg"+i);
            LinLay.addView(rowTextView)
            myTextViews[i] = rowTextView
        }
    }
}