package com.katdmy.android.myfirstkotlinapp

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ActivityC : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c)

        val buttonBackA = findViewById<Button>(R.id.button_back_a)
        buttonBackA.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        val buttonBackB = findViewById<Button>(R.id.button_back_b)
        buttonBackB.setOnClickListener {
            val intent = Intent(this, ActivityB::class.java)
            startActivity(intent)
        }
    }
}