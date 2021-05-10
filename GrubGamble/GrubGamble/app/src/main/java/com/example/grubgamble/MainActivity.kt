/** GrubGamble Mobile Application Project
 *  Austin Baker, Hamza Yousafzai, Jaquille Hinkson, Garrett Love, Nicolas Tucker
 *  ITSC-4155 Spring 2021
 *
 *  GrubGamble app icon attributed to https://www.flaticon.com/
 */

package com.example.grubgamble

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(),LoginFragment.ILoginListener,RegisterFragment.IRegisterListener,
        MainFragment.IMainListener, MinigameFragment.IMiniListener, SpinFragment.ISpinListener,
            ForgotPassword.IForgotListener {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var PERMISSION_ID = 77

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //if user is not logged in
        if (FirebaseAuth.getInstance().currentUser == null) {

            supportFragmentManager.beginTransaction()
                .add(R.id.contentView, LoginFragment())
                .commit()
        } else { //user is already logged in
            supportFragmentManager.beginTransaction()
                .add(R.id.contentView, MainFragment())
                .commit()
        }
    }


    override fun loginSuccessful() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.contentView, MainFragment())
            .commit()
    }

    override fun goToRegistration() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.contentView, RegisterFragment())
            .commit()
    }

    override fun goToLogin() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.contentView, LoginFragment())
            .commit()
    }

    override fun goToTicTacToe() {
        val intent = Intent(this, TicTacToe::class.java)
        startActivity(intent)
    }

    override fun goToRandomNumberGuess() {
        val intent = Intent(this, RandomNumberGuess::class.java)
        startActivity(intent)
    }

    override fun goToBingo() {
        val intent = Intent(this, BingoActivity::class.java)
        startActivity(intent)
    }

    override fun goToRoulette() {
        val intent = Intent(this, RouletteActivity::class.java)
        startActivity(intent)
    }

    override fun goToFilters() {
        val intent = Intent(this, Filters::class.java)
        startActivity(intent)
    }

    override fun goToForgotPassword() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.contentView, ForgotPassword())
            .commit()
    }

    override fun getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            ), PERMISSION_ID
            )
        }
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    // Got last known location. In some rare situations this can be null.
                    val uid = FirebaseAuth.getInstance().currentUser.uid
                    val db = Firebase.firestore
                    val updateLocation = hashMapOf(
                            "latitude" to location?.latitude,
                            "longitude" to location?.longitude
                    )
                    Log.d("tag", "getLocation: ${location?.latitude}, ${location?.longitude}")
                    db.collection("Users").document(uid)
                            .update(updateLocation as Map<String, Any>)
                            .addOnSuccessListener {
                                Log.d("tag", "DocumentSnapshot successfully written!")
                            }
                            .addOnFailureListener { e -> Log.w("tag", "Error writing document", e) }
                }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            77 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED
                ) {
                    if ((ContextCompat.checkSelfPermission(
                                    this, Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                == PackageManager.PERMISSION_GRANTED)
                    ) {
                        getLocation()
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
}




