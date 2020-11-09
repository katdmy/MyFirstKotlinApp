package com.katdmy.android.myfirstkotlinapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class FragmentA: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_a, container, false)
    }

    override fun onStart() {
        super.onStart()
        val buttonForwardA = view!!.findViewById<Button>(R.id.button_forward_a)
        buttonForwardA.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction()
                    .replace(R.id.mainLayout, FragmentB())
                    .addToBackStack(null)
                    .commit()
        }
    }
}