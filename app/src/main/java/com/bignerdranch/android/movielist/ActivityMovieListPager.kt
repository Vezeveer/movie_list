package com.bignerdranch.android.movielist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentStatePagerAdapter
import android.content.Intent
import android.content.Context
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_movie_pager.*
import java.util.*

class ActivityMovieListPager : AppCompatActivity() {
    private var mMovies: List<ModelMovie>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_pager)
        val movieId = intent
            .getSerializableExtra(EXTRA_MOVIE_ID) as UUID?
        mMovies = ControllerMovie[this]?.movies
        val fragmentManager = supportFragmentManager
        movie_view_pager.adapter = object : FragmentStatePagerAdapter(fragmentManager) {
            override fun getItem(position: Int): Fragment {
                val movie = mMovies!![position]
                return FragmentMovie.newInstance(movie.getmId())
            }

            override fun getCount(): Int {
                return mMovies!!.size
            }
        }
        for (i in mMovies!!.indices) {
            if (mMovies!![i].getmId() == movieId) {
                movie_view_pager.currentItem = i
                break
            }
        }
    }

    companion object {
        private const val EXTRA_MOVIE_ID = "com.bignerdranch.android.movielist.movie_id"
        fun newIntent(packageContext: Context?, movieId: UUID?): Intent {
            val intent = Intent(packageContext, ActivityMovieListPager::class.java)
            intent.putExtra(EXTRA_MOVIE_ID, movieId)
            return intent
        }
    }
}