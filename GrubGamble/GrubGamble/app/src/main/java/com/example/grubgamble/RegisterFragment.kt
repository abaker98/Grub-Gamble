package com.example.grubgamble

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class RegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    val MIN_PASSWORD_LENGTH = 6


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        val builder = AlertDialog.Builder(view.context)
        val registerUsername = view.findViewById<EditText>(R.id.registerUsername)
        val registerEmail = view.findViewById<EditText>(R.id.registerEmail)
        val registerPassword = view.findViewById<EditText>(R.id.registerPassword)
        val confirmPassword = view.findViewById<EditText>(R.id.confirmPassword)

        view.findViewById<Button>(R.id.buttonCancelSignup).setOnClickListener {
            mListener?.goToLogin() //go back to login fragment
        }

        view.findViewById<Button>(R.id.buttonSignup).setOnClickListener {
            var userName = registerUsername.text.toString()
            var email = registerEmail.text.toString()
            var password = registerPassword.text.toString()
            var confirmPass = confirmPassword.text.toString()
            builder.setTitle("See what happened was...")
            //registration validation
            //if any field is empty
            if(userName.isEmpty() || email.isEmpty() ||  password.isEmpty()) {
                builder.setMessage("Missing fields")
                builder.setPositiveButton(
                        "DUH") { dialog, id ->
                }.show()
            //if passwords do not match
            }else if(!password.equals(confirmPass)) {
                builder.setMessage("Passwords do not match")
                builder.setPositiveButton(
                        "DUH") { dialog, id ->
                }.show()
            //if password length is less than 6 characters
            }else if(registerPassword.text.length < MIN_PASSWORD_LENGTH){
                builder.setMessage("Password must be greater than $MIN_PASSWORD_LENGTH chars")
                builder.setPositiveButton(
                        "DUH") { dialog, id ->
                }.show()
            //if email is not valid
            }else if(!isEmailValid(email)){
                builder.setMessage("Please enter valid email")
                builder.setPositiveButton(
                        "DUH") { dialog, id ->
                }.show()
            }else{
                createAccount(userName, email, password)
            }
        }

        return view
    }

    var mListener: IRegisterListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as IRegisterListener
    }

    interface IRegisterListener {
        fun loginSuccessful()
        fun goToLogin()
    }

    //check if email is valid
    private fun isEmailValid(email: String?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    //create new account
    private fun createAccount(userName: String,email: String, password: String) {
        auth = Firebase.auth
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("t", "createAccount:  successful")
                    setData(userName,email)
                    mListener!!.loginSuccessful() //go to main fragment
                } else {
                    Log.d("t", "createAccount:  failed")
                    Toast.makeText(this.getActivity(), task.exception!!.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    //add data to firestore database for new user
    private fun setData(userName:String,email:String){
        //use account unique ID as the document identifier
        val uid = FirebaseAuth.getInstance().currentUser.uid
        val db = Firebase.firestore
        val user = hashMapOf(
            "uid" to uid,
            "username" to userName,
            "email" to email,
            "points" to 0,
            "latitude" to 0,
            "longitude" to 0,
            "distance" to 1,
            "cuisines" to arrayListOf(""),
            "friends" to hashMapOf("friend_user" to "username")
        )



        db.collection("Users").document(uid)
                .set(user)
                .addOnSuccessListener { Log.d("tag", "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w("tag", "Error writing document", e) }
    }
}