package com.bignerdranch.android.movielist

import android.os.Bundle
import android.content.Intent
import android.view.LayoutInflater
import android.app.Activity
import android.view.ViewGroup
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import java.text.DateFormat
import java.util.*

class FragmentMovie : Fragment() {
    private var mMovie: ModelMovie? = null
    private var mTitleField: EditText? = null
    var mDateButton: Button? = null
    var mAddButton: Button? = null
    var mDeleteButton: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val movieId = requireArguments().getSerializable(ARG_MOVIE_ID) as UUID?
        mMovie = ControllerMovie.Companion.get(activity)!!.getMovie(movieId)
    }

    override fun onPause() {
        super.onPause()
        ControllerMovie.Companion.get(activity)
            ?.updateMovie(mMovie)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(
            R.layout.fragment_movie,
            container, false
        )
        mTitleField = v.findViewById(R.id.edit_text_title)
        mTitleField?.setText(mMovie!!.getmTitle())
        mDateButton = v.findViewById(R.id.edit_text_date)
        mAddButton = v.findViewById(R.id.btn_add_movie)
        mDeleteButton = v.findViewById(R.id.btn_delete_movie)
        val mCheckBox = v.findViewById<CheckBox>(R.id.checkbox_watched)
        mCheckBox.isChecked = mMovie!!.ismWatched()
        updateDate()
        mDateButton?.setOnClickListener(View.OnClickListener {
            val manager = requireActivity().supportFragmentManager
            val dialog: FragmentDatePicker =
                FragmentDatePicker.Companion.newInstance(mMovie!!.getmDate())
            dialog.setTargetFragment(this@FragmentMovie, REQUEST_DATE)
            dialog.show(manager, DIALOG_DATE)
        })
        mAddButton?.setOnClickListener(View.OnClickListener {
            if(mTitleField?.text?.isEmpty() == true){

            } else {
                mMovie!!.setmTitle(mTitleField?.text.toString())
                mMovie!!.setmWatched(mCheckBox.isChecked)
                goToMain()
                Toast.makeText(
                    activity, mTitleField?.text.toString() + " - Added/updated",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        mDeleteButton?.setOnClickListener(object : View.OnClickListener {
            var i = 0
            var dbSize = ControllerMovie[activity]?.movies?.size
            override fun onClick(view: View) {

                if(dbSize == 0){
                    Toast.makeText(
                        activity, " Database is empty! Nothing to delete" + dbSize,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if(mTitleField?.text?.isEmpty() == true){

                    } else {
                        i = ControllerMovie.Companion[activity]?.deleteMovie(mMovie!!.getmId())!!

                        if (i > 0) {
                            Toast.makeText(
                                activity, mTitleField?.text.toString() +" - Deleted!",
                                Toast.LENGTH_SHORT
                            ).show()
                            goToMain()
                        } else {
                            Toast.makeText(
                                activity, mTitleField?.text.toString() + " - Error. Can't delete!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        })
        mCheckBox.setOnCheckedChangeListener { cb, isChecked ->
            if (isChecked) {
                Toast.makeText(
                    activity, mTitleField?.text.toString() + " - Watched!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    activity, mTitleField?.text.toString() + " - Not Watched",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return v
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_DATE) {
            val date = data
                ?.getSerializableExtra(FragmentDatePicker.Companion.EXTRA_DATE) as Date?
            mMovie!!.setmDate(date)
            updateDate()
        }
    }

    private fun updateDate() {
        mDateButton!!.text = DateFormat.getDateInstance(DateFormat.LONG).format(
            mMovie!!.getmDate()
        )
    }

    private fun goToMain() {
        val intent = Intent(activity, ActivityMain::class.java)
        startActivity(intent)
    }

    companion object {
        private const val ARG_MOVIE_ID = "movie_id"
        private const val DIALOG_DATE = "DialogDate"
        private const val REQUEST_DATE = 0
        fun newInstance(movieId: UUID?): FragmentMovie {
            val args = Bundle()
            args.putSerializable(ARG_MOVIE_ID, movieId)
            val fragment = FragmentMovie()
            fragment.arguments = args
            return fragment
        }
    }
}