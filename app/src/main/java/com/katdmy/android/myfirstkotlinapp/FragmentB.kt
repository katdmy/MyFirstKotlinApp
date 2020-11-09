package com.katdmy.android.myfirstkotlinapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class FragmentB: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_b, container, false)
    }


    override fun onStart() {
        super.onStart()
        val buttonForwardB = view!!.findViewById<Button>(R.id.button_forward_b)
        buttonForwardB.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.mainLayout, FragmentC())
                    .addToBackStack(null)
                    .commit()
        }

        val buttonBackB = view!!.findViewById<Button>(R.id.button_back_b)
        buttonBackB.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.mainLayout, FragmentA())
                    .addToBackStack(null)
                    .commit()
        }
    }
}