package com.bignerdranch.android.movielist

import androidx.fragment.app.Fragment

class ActivityMain : ActivitySingleFragment() {
    override fun createFragment(): Fragment? {
        return FragmentMovieList()
    }
}