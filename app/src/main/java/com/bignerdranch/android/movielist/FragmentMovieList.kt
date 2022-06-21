package com.bignerdranch.android.movielist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.text.DateFormat

class FragmentMovieList : Fragment() {
    private var mMovieRecyclerView: RecyclerView? = null
    private var mAdapter: MovieAdapter? = null
    private var mSubtitleVisible = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        val activity = activity as AppCompatActivity?
        activity!!.supportActionBar!!.setDisplayShowHomeEnabled(true)
        activity.supportActionBar!!.setIcon(R.drawable.logo_smallest)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_list, container, false)
        mMovieRecyclerView = view.findViewById(R.id.movie_recycler_view)
        mMovieRecyclerView?.setLayoutManager(LinearLayoutManager(activity))
        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE)
        }
        updateUI()
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_movie_list, menu)
        val subtitleItem = menu.findItem(R.id.show_subtitle)
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle)
        } else {
            subtitleItem.setTitle(R.string.show_subtitle)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_movie -> {
                val movie = ModelMovie()
                ControllerMovie.Companion.get(activity)!!.addMovie(movie)
                val intent: Intent =
                    ActivityMovieListPager.Companion.newIntent(activity, movie.getmId())
                startActivity(intent)
                true
            }
            R.id.show_subtitle -> {
                mSubtitleVisible = !mSubtitleVisible
                requireActivity().invalidateOptionsMenu()
                updateSubtitle()
                true
            }
            R.id.update_movie_list -> {
                requireActivity().recreate()
                Toast.makeText(
                    activity,
                    "Updated!",
                    Toast.LENGTH_SHORT
                ).show()
                true
            }
            R.id.about_app -> {
                val intentAbout = Intent(activity, ActivityAbout::class.java)
                startActivity(intentAbout)
                true
            }
            R.id.exit_app -> {
                requireActivity().finish()
                System.exit(0)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateSubtitle() {
        val controllerMovie: ControllerMovie? = ControllerMovie.Companion.get(activity)
        val movieCount = controllerMovie?.movies?.size
        var subtitle: String? = getString(R.string.subtitle_format, movieCount)
        if (!mSubtitleVisible) {
            subtitle = null
        }
        val activity = activity as AppCompatActivity?
        activity!!.supportActionBar!!.setSubtitle(subtitle)
    }

    private fun updateUI() {
        val controllerMovie: ControllerMovie? = ControllerMovie.Companion.get(activity)
        val movies = controllerMovie?.movies
        if (mAdapter == null) {
            mAdapter = MovieAdapter(movies)
            mMovieRecyclerView!!.adapter = mAdapter
        } else {
            mAdapter!!.setMovies(movies)
            mAdapter!!.notifyDataSetChanged()
        }
        updateSubtitle()
    }

    private inner class MovieHolder(inflater: LayoutInflater, parent: ViewGroup?) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.list_item_movie, parent, false)),
        View.OnClickListener {
        private val mTitleTextView: TextView
        private val mDateTextView: TextView
        private var mMovie: ModelMovie? = null
        fun bind(movie: ModelMovie?) {
            mMovie = movie
            mTitleTextView.text = mMovie!!.getmTitle()
            mDateTextView.text = DateFormat.getDateInstance(DateFormat.LONG).format(
                mMovie!!.getmDate()
            )
        }

        override fun onClick(view: View) {
            val intent: Intent =
                ActivityMovieListPager.Companion.newIntent(activity, mMovie!!.getmId())
            startActivity(intent)
        }

        init {
            itemView.setOnClickListener(this)
            mTitleTextView = itemView.findViewById(R.id.movie_title)
            mDateTextView = itemView.findViewById(R.id.movie_date)
        }
    }

    private inner class MovieAdapter(private var mMovies: List<ModelMovie?>?) :
        RecyclerView.Adapter<MovieHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
            val layoutInflater = LayoutInflater.from(activity)
            return MovieHolder(layoutInflater, parent)
        }

        override fun onBindViewHolder(holder: MovieHolder, position: Int) {
            val movie = mMovies!![position]
            holder.bind(movie)
        }

        override fun getItemCount(): Int {
            return mMovies!!.size
        }

        fun setMovies(movies: List<ModelMovie?>?) {
            mMovies = movies
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible)
    }

    companion object {
        private const val SAVED_SUBTITLE_VISIBLE = "subtitle"
    }
}