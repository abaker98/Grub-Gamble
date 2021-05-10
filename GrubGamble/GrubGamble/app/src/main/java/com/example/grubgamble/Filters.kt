package com.example.grubgamble

import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FieldValue.arrayUnion
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*

class Filters : AppCompatActivity() {
    private lateinit var foodtypeAdapter: TypeAdapter
    private var num: Int = 1


    private fun hideSoftKeyboard(view: View) {
        val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filters)
        foodtypeAdapter = TypeAdapter(ArrayList())

        var choices: ArrayList<String> = ArrayList()

        val rvFoodItems = findViewById<RecyclerView>(R.id.rvFoodItems)
        val btnAddFood = findViewById<Button>(R.id.btnAddFood)
        val btnDelFood = findViewById<Button>(R.id.btnDelFood)
        val etFoodItems = findViewById<EditText>(R.id.etFoodItems)
        val done = findViewById<Button>(R.id.buttonDone)
        val seekBar = findViewById<SeekBar>(R.id.seekBar)
        val distanceText = findViewById<TextView>(R.id.textView3)


        rvFoodItems.adapter = foodtypeAdapter
        rvFoodItems.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.btnAddFood).setOnClickListener {
            hideSoftKeyboard(it)
        }

        btnAddFood.setOnClickListener {
            var foodTitle = etFoodItems.text.toString()
            if(foodTitle.isNotEmpty()) {
                val type = FoodType(foodTitle)
                foodtypeAdapter.addFType(type)
                etFoodItems.text.clear()
                hideSoftKeyboard(it)
            }
        }

        btnDelFood.setOnClickListener {
            foodtypeAdapter.deleteFType()
        }

        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                num = if(progress == 0) {
                    1
                } else {
                    progress //get distance
                }
                distanceText.text = "Distance: $progress Mile(s)"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //do nothing
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //do nothing
            }

        })

        done.setOnClickListener {
            val uid = FirebaseAuth.getInstance().currentUser.uid
            val db = Firebase.firestore.collection("Users").document(uid)
            val restaurants: ArrayList<Restaurant> = ArrayList()
            db.update("cuisines", FieldValue.delete()) //clears old filter
            db.update("distance", num)
            for(i in 0 until foodtypeAdapter.fTypes.size ){ //writes new filter to database
                db.update("cuisines", arrayUnion(foodtypeAdapter.fTypes[i].title.toLowerCase()))
            }
            if(foodtypeAdapter.fTypes.isEmpty()) { //prevents null exception
                db.update("cuisines", arrayUnion(""))
            }

            //*******************************API WORK*****************************************

            //getting latitude, longitude, and filters from firebase
            val subCollectionRef = db.collection("Restaurants")
            //deleting old restaurant collection before populating with new random list
            subCollectionRef.get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        subCollectionRef.document(document.id).delete()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("tag", "Error getting documents: ", exception)
                }

            val client = OkHttpClient() //create client for api call
            db.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val lat = document.data?.get("latitude")
                        val long = document.data?.get("longitude")
                        val miles = document.data?.get("distance")
                        val foods = document.data?.get("cuisines") as ArrayList<*>
                        val apiList: ArrayList<String> = ArrayList()
                        var url = ""

                        //build api request
                        if(foods[0].equals("")) { //no cuisines
                            url = "https://api.documenu.com/v2/restaurants/search/geo?lat=$lat&lon=$long&distance=$miles&size=100&key=ef70659442988cec28616dbc62def51e"
                            val request = Request.Builder().url(url).build()
                            client.newCall(request).enqueue(object : Callback {
                                override fun onFailure(call: Call, e: IOException) {
                                    Log.d("tag", "onFailure: $e")
                                }

                                override fun onResponse(call: Call, response: Response) {
                                    if (response.isSuccessful) {
                                        try {
                                            val total_pages = JSONObject(response.body?.string()).getInt("total_pages")
                                            for (p in 1 until total_pages + 1) {
                                                val eachPage = "$url&page=$p"
                                                val request1 = Request.Builder().url(eachPage).build()
                                                client.newCall(request1).enqueue(object : Callback {
                                                    override fun onFailure(call: Call, e: IOException) {
                                                        Log.d("tag", "onFailure: $e")
                                                    }

                                                    override fun onResponse(call: Call, response: Response) {
                                                        if (response.isSuccessful) {
                                                            try {
                                                                val json = JSONObject(response.body?.string())
                                                                val data = json.getJSONArray("data")
                                                                //Log.d("tag", "onResponse: " + data.length())
                                                                for (i in 0 until data.length()) { //cycle through each restaurant in data array
                                                                    val restaurant = Restaurant()
                                                                    restaurant.rName = (data.getJSONObject(i).getString("restaurant_name"))
                                                                    restaurant.rPhone = (data.getJSONObject(i).getString("restaurant_phone"))
                                                                    restaurant.rPrice = (data.getJSONObject(i).getString("price_range"))
                                                                    restaurant.rId = (data.getJSONObject(i).getLong("restaurant_id"))
                                                                    val cuisines = data.getJSONObject(i).getJSONArray("cuisines")
                                                                    for (c in 0 until cuisines.length()) { //getting all cuisines for each restaurant
                                                                        restaurant.rCuisine.add(cuisines.get(c) as String)
                                                                    }
                                                                    restaurant.rLatitude = data.getJSONObject(i).getJSONObject("geo").get("lat") as Double
                                                                    restaurant.rLongitude = data.getJSONObject(i).getJSONObject("geo").get("lon") as Double
                                                                    restaurants.add(restaurant)
                                                                    //Log.d("tag", "restaurant: $restaurant")
                                                                }
                                                                Log.d("tag", "length of ArrayList<Restaurant>: " + restaurants.size)
                                                                //add random 8 restaurants to firebase
                                                                val random = (0 until restaurants.size).shuffled().take(8).toSet()
                                                                for(i in random.indices) {
                                                                    val cuisines = restaurants[random.elementAt(i)].rCuisine as ArrayList<*>
                                                                    var cuisine = ""
                                                                    for (c in 0 until cuisines.size) { //getting all cuisines for each restaurant
                                                                        cuisine += cuisines[c] as String
                                                                        if(c != cuisines.size-1){
                                                                            cuisine += ", "
                                                                        }
                                                                    }
                                                                    val restDocRef = Firebase.firestore.collection("Users").document(uid).
                                                                    collection("Restaurants").document(restaurants[random.elementAt(i)].rId.toString())
                                                                    val rest = hashMapOf(
                                                                        "restaurant_name" to restaurants[random.elementAt(i)].rName,
                                                                        "restaurant_phone" to restaurants[random.elementAt(i)].rPhone,
                                                                        "price_range" to restaurants[random.elementAt(i)].rPrice,
                                                                        "restaurant_id" to restaurants[random.elementAt(i)].rId,
                                                                        "latitude" to restaurants[random.elementAt(i)].rLatitude,
                                                                        "longitude" to restaurants[random.elementAt(i)].rLongitude,
                                                                        "cuisines" to cuisine
                                                                    )
                                                                    //Log.d("tag", "onResponse: $rest")
                                                                    restDocRef.set(rest)
                                                                }
                                                            } catch (e: JSONException) {
                                                                Log.d("tag", "onResponse: $e")
                                                            }
                                                        }
                                                    }
                                                })
                                            }
                                        } catch (e: JSONException) {
                                            Log.d("tag", "onResponse: $e")
                                        }
                                    }
                                }
                            })
                        } else { //cuisines and distance
                            for(i in 0 until foods.size) {
                                //create ArrayList<String> of api calls for each cuisine
                                apiList.add("https://api.documenu.com/v2/restaurants/search/geo?lat=$lat&lon=$long&distance=$miles&cuisine="+foods[i]+"&size=100&key=ef70659442988cec28616dbc62def51e")
                                val request = Request.Builder().url(apiList[i]).build()
                                client.newCall(request).enqueue(object : Callback {
                                    override fun onFailure(call: Call, e: IOException) {
                                        Log.d("tag", "onFailure: $e")
                                    }

                                    override fun onResponse(call: Call, response: Response) {
                                        if (response.isSuccessful) {
                                            try {
                                                val total_pages = JSONObject(response.body?.string()).getInt("total_pages")
                                                for (p in 1 until total_pages + 1) {
                                                    val eachPage = apiList[i] + "&page=$p"
                                                    val request1 = Request.Builder().url(eachPage).build()
                                                    client.newCall(request1).enqueue(object : Callback {
                                                        override fun onFailure(call: Call, e: IOException) {
                                                            Log.d("tag", "onFailure: $e")
                                                        }

                                                        override fun onResponse(call: Call, response: Response) {
                                                            if (response.isSuccessful) {
                                                                try {
                                                                    val json = JSONObject(response.body?.string())
                                                                    val data = json.getJSONArray("data")
                                                                    Log.d("tag", "onResponse: " + data.length())
                                                                    for (d in 0 until data.length()) { //cycle through each restaurant in data array
                                                                        val restaurant = Restaurant()
                                                                        restaurant.rName = (data.getJSONObject(d).getString("restaurant_name"))
                                                                        restaurant.rPhone = (data.getJSONObject(d).getString("restaurant_phone"))
                                                                        restaurant.rPrice = (data.getJSONObject(d).getString("price_range"))
                                                                        restaurant.rId = (data.getJSONObject(d).getLong("restaurant_id"))
                                                                        val cuisines = data.getJSONObject(d).getJSONArray("cuisines")
                                                                        for (c in 0 until cuisines.length()) { //getting all cuisines for each restaurant
                                                                            restaurant.rCuisine.add(cuisines.get(c) as String)
                                                                        }
                                                                        restaurant.rLatitude = data.getJSONObject(d).getJSONObject("geo").get("lat") as Double
                                                                        restaurant.rLongitude = data.getJSONObject(d).getJSONObject("geo").get("lon") as Double
                                                                        restaurants.add(restaurant)
                                                                        Log.d("tag", "ADDING TO ARRAYLIST: $restaurant")
                                                                    }
                                                                    //add random 8 restaurants to firebase
                                                                    val random = (0 until restaurants.size).shuffled().take(8).toSet()
                                                                    for(r in random.indices) {
                                                                        val cuisines = restaurants[random.elementAt(r)].rCuisine as ArrayList<*>
                                                                        var cuisine = ""
                                                                        for (c in 0 until cuisines.size) { //getting all cuisines for each restaurant
                                                                            cuisine += cuisines[c] as String
                                                                            if(c != cuisines.size-1){
                                                                                cuisine += ", "
                                                                            }
                                                                        }
                                                                        val restDocRef = Firebase.firestore.collection("Users").document(uid).
                                                                        collection("Restaurants").document(restaurants[random.elementAt(r)].rId.toString())
                                                                        val rest = hashMapOf(
                                                                            "restaurant_name" to restaurants[random.elementAt(r)].rName,
                                                                            "restaurant_phone" to restaurants[random.elementAt(r)].rPhone,
                                                                            "price_range" to restaurants[random.elementAt(r)].rPrice,
                                                                            "restaurant_id" to restaurants[random.elementAt(r)].rId,
                                                                            "latitude" to restaurants[random.elementAt(r)].rLatitude,
                                                                            "longitude" to restaurants[random.elementAt(r)].rLongitude,
                                                                            "cuisines" to cuisine
                                                                        )
                                                                        Log.d("tag", "onResponse ADDING TO DATABASE: $rest")
                                                                        restDocRef.set(rest)
                                                                    }
                                                                    //Log.d("tag", "length of ArrayList<Restaurant>: " + restaurants.size)
                                                                } catch (e: JSONException) {
                                                                    Log.d("tag", "onResponse: $e")
                                                                }
                                                            }
                                                        }
                                                    })
                                                }
                                            } catch (e: JSONException) {
                                                Log.d("tag", "onResponse: $e")
                                            }
                                        }
                                    }
                                })
                            }
                        }
                    } else {
                        Log.d("tag", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("tag", "get failed with ", exception)
                }
            //*************************************************************************************

            this.finish() //close activity and return to spin fragment
        }
    }

    class TypeAdapter (
            val fTypes: ArrayList<FoodType>
    ) : RecyclerView.Adapter<TypeAdapter.FTypeViewHolder>() {
        class FTypeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FTypeViewHolder {
            return FTypeViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                            R.layout.item_food,
                            parent,
                            false

                    )
            )
        }

        override fun getItemCount(): Int {
            return fTypes.size
        }

        fun addFType(foodtype: FoodType ) {
            fTypes.add(foodtype)
            notifyItemInserted(fTypes.size - 1)
        }

        fun deleteFType() {
            fTypes.removeAll { foodtype ->
                foodtype.isChecked
            }
            notifyDataSetChanged()
        }

        private fun toggleStrikeThrough(tvFoodTitle: TextView, isChecked: Boolean) {
            if(isChecked) {
                tvFoodTitle.paintFlags = tvFoodTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                tvFoodTitle.paintFlags = tvFoodTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

        override fun onBindViewHolder(holder: FTypeViewHolder, position: Int) {

            val curFType = fTypes[position]
            holder.itemView.apply {
                val tvFoodTitle = findViewById<TextView>(R.id.tvFoodTitle)
                val cbDone = findViewById<CheckBox>(R.id.cbDone)
                tvFoodTitle.text = curFType.title
                cbDone.isChecked = curFType.isChecked
                toggleStrikeThrough(tvFoodTitle, curFType.isChecked)
                cbDone.setOnCheckedChangeListener { _ , isChecked ->
                    toggleStrikeThrough(tvFoodTitle, isChecked)
                    curFType.isChecked = !curFType.isChecked
                }
            }
        }
    }
}


