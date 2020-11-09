package com.katdmy.android.myfirstkotlinapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class FragmentC: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_c, container, false)
    }

    override fun onStart() {
        super.onStart()
        val buttonBackToAC = view!!.findViewById<Button>(R.id.button_backtoa_c)
        buttonBackToAC.setOnClickListener {
            activity!!.supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.mainLayout, FragmentA())
                    .commit()
        }

        val buttonBackC = view!!.findViewById<Button>(R.id.button_back_c)
        buttonBackC.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.mainLayout, FragmentB())
                    .addToBackStack(null)
                    .commit()
        }
    }
}