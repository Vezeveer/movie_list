package com.bignerdranch.android.movielist

import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import com.bignerdranch.android.movielist.database.MovieDbSchema.MovieTable
import com.bignerdranch.android.movielist.database.MovieCursorWrapper
import com.bignerdranch.android.movielist.database.MovieBaseHelper
import android.content.Context
import java.util.*

class ControllerMovie private constructor(context: Context?) {
    private val mContext: Context
    private val mDatabase: SQLiteDatabase
    fun addMovie(m: ModelMovie?) {
        val values = getContentValues(m)
        mDatabase.insert(MovieTable.NAME, null, values)
    }

    val movies: List<ModelMovie>
        get() {
            val movies: MutableList<ModelMovie> = ArrayList()
            val cursor = queryMovies(null, null)
            try {
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    movies.add(cursor.movie)
                    cursor.moveToNext()
                }
            } finally {
                cursor.close()
            }
            return movies
        }

    fun getMovie(id: UUID?): ModelMovie? {
        val cursor = queryMovies(
            MovieTable.Cols.UUID + " = ?", arrayOf(id.toString())
        )
         try {
            if (cursor.count == 0) {
                return null
            }
            cursor.moveToFirst()
             return cursor.movie

        } finally {
            cursor.close()
        }
    }

    fun deleteMovie(id: UUID?): Int {
        val i: Int
        i = mDatabase.delete(
            MovieTable.NAME,
            MovieTable.Cols.UUID + " = ?", arrayOf(id.toString())
        )
        return i
    }

    fun updateMovie(movie: ModelMovie?) {
        val uuidString = movie!!.getmId().toString()
        val values = getContentValues(movie)
        mDatabase.update(
            MovieTable.NAME, values,
            MovieTable.Cols.UUID + " = ?", arrayOf(uuidString)
        )
    }

    private fun queryMovies(whereClause: String?, whereArgs: Array<String>?): MovieCursorWrapper {
        val cursor = mDatabase.query(
            MovieTable.NAME,
            null,
            whereClause,
            whereArgs,
            null,
            null,
            null
        )
        return MovieCursorWrapper(cursor)
    }

    companion object {
        private var sControllerMovie: ControllerMovie? = null
        operator fun get(context: Context?): ControllerMovie? {
            if (sControllerMovie == null) {
                sControllerMovie = ControllerMovie(context)
            }
            return sControllerMovie
        }

        private fun getContentValues(movie: ModelMovie?): ContentValues {
            val values = ContentValues()
            values.put(MovieTable.Cols.UUID, movie!!.getmId().toString())
            values.put(MovieTable.Cols.TITLE, movie.getmTitle())
            values.put(MovieTable.Cols.DATE, movie.getmDate()!!.time)
            values.put(MovieTable.Cols.WATCHED, if (movie.ismWatched()) 1 else 0)
            return values
        }
    }

    init {
        mContext = context!!.applicationContext
        mDatabase = MovieBaseHelper(mContext)
            .writableDatabase
    }
}