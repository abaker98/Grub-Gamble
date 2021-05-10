/**
 * ForgotPassword Fragment image: https://rent.partyheadphones.com/customer-reset-password/
 */

package com.example.grubgamble

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_forgot_password, container, false)

        val recoverEmail = view.findViewById<EditText>(R.id.recoverEmail)

        view.findViewById<Button>(R.id.buttonRecover).setOnClickListener {
            var email = recoverEmail.text.toString()
            var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
            mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        mListener?.goToLogin()
                    } else {
                        // failed!
                    }
                }
        }

        view.findViewById<Button>(R.id.buttonCancelRecover).setOnClickListener {
            mListener?.goToLogin()
        }
        return view
    }

    var mListener: IForgotListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as IForgotListener
    }

    interface IForgotListener {
        fun goToLogin()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ForgotPassword.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                ForgotPassword().apply {
                    arguments = Bundle().apply {

                    }
                }
    }
}