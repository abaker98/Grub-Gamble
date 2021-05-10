package com.example.grubgamble

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.random.Random

class SpinFragment : Fragment(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_spin, container, false)

        mListener?.getLocation()// get location permissions

        val restBuilder = AlertDialog.Builder(view.context)
        val builder = AlertDialog.Builder(view.context)
        val restaurants: ArrayList<Restaurant> = ArrayList()
        val uid = FirebaseAuth.getInstance().currentUser.uid
        val db = Firebase.firestore.collection("Users/$uid/Restaurants")

        /*imageView is the spinner in the middle of the wheel
        sector is a variable used to get the degrees of each section of the wheel (45 degrees)*/
        val miniWheel: ImageView = view.findViewById(R.id.miniWheel)
        val sector = 360f / 8f

        /*spinResults is the textView at the top of the spin page that will display where the spinner
        lands*/
        val spinResults: TextView = view.findViewById(R.id.spinResult)

        /*The eight textViews corresponding to the textViews locations around the eight sectors
        of the wheel*/
        val one: TextView = view.findViewById(R.id.one)
        val two: TextView = view.findViewById(R.id.two)
        val three: TextView = view.findViewById(R.id.three)
        val four: TextView = view.findViewById(R.id.four)
        val five: TextView = view.findViewById(R.id.five)
        val six: TextView = view.findViewById(R.id.six)
        val seven: TextView = view.findViewById(R.id.seven)
        val eight: TextView = view.findViewById(R.id.eight)

        val array = arrayOf(one, two, three, four, five, six,
            seven, eight)

        view.findViewById<Button>(R.id.buttonSpin).setOnClickListener {
            //mListener?.getRestaurants() //API request
            //pull restaurants from database into ArrayList<Restaurant>
            db.get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val restaurant = Restaurant()
                        restaurant.rId = document.data["restaurant_id"] as Long
                        restaurant.rName = document.data["restaurant_name"] as String
                        restaurant.rPhone = document.data["restaurant_phone"] as String
                        restaurant.rPrice = document.data["price_range"] as String
                        restaurant.rLatitude = document.data["latitude"] as Double
                        restaurant.rLongitude = document.data["longitude"] as Double
                        restaurant.rCuisine.add(document.data["cuisines"] as String)
                        restaurants.add(restaurant)
                        //Log.d("tag", "onCreateView: $restaurant")
                    }
                    if(restaurants.size < 8) {
                        builder.setTitle("Not Enough Restaurants!")
                        builder.setMessage("Consider increasing distance =]")
                        builder.setPositiveButton(
                            "OK") { dialog, id ->
                        }.show()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("tag", "Error getting documents: ", exception)
                }

            //Starts animation of the imageView(spinner in the middle of the wheel)
            val degree = Random.nextInt(360) + 1080f
            val originalDegree = degree % 360f

            /*RotateAnimation is used to rotate an image using a degreeFrom, degreeTo, PivotX, PivotY
            PivotX and PivotY is the point where you image rotates around, example 0,0 would be the
            top left, 1,1 would be the bottom right */
            val spinTheWheel = RotateAnimation(originalDegree, degree,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5745098039f)

            //duration is the amount of time that the spinner will spin before stopping
            spinTheWheel.duration = 4000
            spinTheWheel.fillAfter = true
            /*interpolator is used to allow for a more smooth stop so on the spinner so
            it decelerates to the finish*/
            spinTheWheel.interpolator = DecelerateInterpolator()

            /*setAnimationListener is used to define what happens when the animation starts,
            * when the animation ends, and what happens when the animation is repeated */
            spinTheWheel.setAnimationListener(object: Animation.AnimationListener{
                //OnAnimationStart set the text of spinResults(top of roulette page) to "  "
                override fun onAnimationStart(animation: Animation){
                    spinResults.text = ""
                }
                /*onAnimationEnd set the text of spinResults(top of roulette page) to what the wheel
                landed on*/
                override fun onAnimationEnd(animation: Animation) {
                    var i = 0
                    do {
                        val end = sector * (i + 1)
                        val start = end - sector

                        if((degree % 360f) >= start && (degree % 360f) < end){
                            if(restaurants.size >= 8) {
                                restBuilder.setTitle(restaurants[i].rName)
                                restBuilder.setMessage(restaurants[i].rCuisine[0]+"\n\n"
                                        + restaurants[i].rPhone +"\t\t"
                                        + restaurants[i].rPrice)
                                val gmmIntentUri = Uri.parse("geo:0, 0?q=" + Uri.encode(
                                    restaurants[i].rName
                                ))
                                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                                mapIntent.setPackage("com.google.android.apps.maps")
                                restBuilder.setPositiveButton("Directions") { dialog, whichButton ->
                                    startActivity(mapIntent)}.show()

                                restaurants.clear()//clears array for new random list
                            }
                        }
                        i++
                    }while(/*spinResults.text == " " && */i < array.size)
                }

                override fun onAnimationRepeat(animation: Animation) {
                    //do nothing
                }
            })
            miniWheel.startAnimation(spinTheWheel)
        }

        view.findViewById<Button>(R.id.buttonFilter).setOnClickListener {
            mListener?.goToFilters() //go to Filters page
        }
        return view
    }

    var mListener: ISpinListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as ISpinListener
    }

    interface ISpinListener {
        fun goToFilters()
        fun getLocation()
        //fun getRestaurants()
    }


}