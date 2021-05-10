package com.example.grubgamble

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {
    fun LoginFragment() {
        // Required empty public constructor
    }

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val editTextEmail = view.findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = view.findViewById<EditText>(R.id.editTextPassword)

        view.findViewById<Button>(R.id.buttonLogin).setOnClickListener {
            var email = editTextEmail.text.toString()
            var password = editTextPassword.text.toString()
            if(email.isNotEmpty() && password.isNotEmpty()){
                signIn(email, password)
            }else{
                Toast.makeText(activity, "Enter valid email/password", Toast.LENGTH_SHORT).show()
            }
        }

        view.findViewById<Button>(R.id.buttonRegister).setOnClickListener {
            mListener?.goToRegistration() //go to register fragment
        }

        view.findViewById<TextView>(R.id.forgotPassword).setOnClickListener {
            mListener?.goToForgotPassword()
        }

        return view
    }
    var mListener: ILoginListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as ILoginListener
    }

    interface ILoginListener {
        fun loginSuccessful()
        fun goToRegistration()
        fun goToForgotPassword()
    }

    private fun signIn(email: String, password: String) {
        auth = Firebase.auth
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("t", "signIn: login successful")
                    mListener!!.loginSuccessful() //go to main fragment
                } else {
                    Log.d("t", "signIn: login failed")
                    Toast.makeText(activity, task.exception!!.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }
}