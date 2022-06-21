package com.bignerdranch.android.movielist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.bignerdranch.android.movielist.R

abstract class ActivitySingleFragment : AppCompatActivity() {
    protected abstract fun createFragment(): Fragment?
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)
        val fm = supportFragmentManager
        var fragment = fm.findFragmentById(R.id.fragment_container)
        if (fragment == null) {
            fragment = createFragment()
            fm.beginTransaction()
                .add(R.id.fragment_container, fragment!!)
                .commit()
        }
    }
}