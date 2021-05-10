package com.example.grubgamble

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment


class MinigameFragment : Fragment() {

    var selectedTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_minigame, container, false)

        val ticTacToe = view.findViewById<ImageButton>(R.id.imageButton1)
        val randGuess = view.findViewById<ImageButton>(R.id.imageButton2)
        val testGame1 = view.findViewById<ImageButton>(R.id.imageButton3)
        val testGame2 = view.findViewById<ImageButton>(R.id.imageButton4)

        ticTacToe.setOnClickListener {
            //go to Tic Tac Toe game
            mListener?.goToTicTacToe()
        }
        randGuess.setOnClickListener {
            //go to Random Guessing game
            mListener?.goToRandomNumberGuess()
        }

        testGame1.setOnClickListener {
            mListener?.goToBingo()
        }

        testGame2.setOnClickListener {
            mListener?.goToRoulette()
        }
        return view
    }
    var mListener: IMiniListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as IMiniListener
    }

    interface IMiniListener {
        fun goToTicTacToe()
        fun goToRandomNumberGuess()
        fun goToBingo()
        fun goToRoulette()
    }
}