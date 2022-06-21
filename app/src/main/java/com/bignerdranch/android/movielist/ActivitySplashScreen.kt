package com.bignerdranch.android.movielist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler

class ActivitySplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar!!.hide()
        Handler().postDelayed({ //This method will be executed once the timer is over
            // Start your app main activity
            val i = Intent(this@ActivitySplashScreen, ActivityMain::class.java)
            startActivity(i)
            // close this activity
            finish()
        }, 3000)
    }
}