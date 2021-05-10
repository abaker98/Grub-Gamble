package com.example.grubgamble

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainFragment : Fragment(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, childFragmentManager)
        val viewPager: ViewPager = view.findViewById(R.id.view_pager)

        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = view.findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myToolbar: Toolbar = view.findViewById(R.id.myToolbar)
        myToolbar.title = "GrubGamble"
        myToolbar.setNavigationOnClickListener {
        //myToolbar.setBackground(R.color.);
        }
        myToolbar.inflateMenu(R.menu.main_menu)
        myToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_logout -> {
                    Firebase.auth.signOut()
                    mListener!!.goToLogin()
                    true
                }
                else -> false
            }
        }
    }

    var mListener: IMainListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = context as IMainListener
    }

    interface IMainListener {
        fun goToLogin()
    }
}