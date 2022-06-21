package com.bignerdranch.android.movielist

import java.util.*
import kotlin.jvm.JvmOverloads

class ModelMovie @JvmOverloads constructor(private val mId: UUID = UUID.randomUUID()) {
    private var mTitle: String? = null
    private var mDate: Date?
    private var mWatched = false
    fun getmId(): UUID {
        return mId
    }

    fun getmTitle(): String? {
        return mTitle
    }

    fun setmTitle(mTitle: String?) {
        this.mTitle = mTitle
    }

    fun getmDate(): Date? {
        return mDate
    }

    fun setmDate(mDate: Date?) {
        this.mDate = mDate
    }

    fun ismWatched(): Boolean {
        return mWatched
    }

    fun setmWatched(mWatched: Boolean) {
        this.mWatched = mWatched
    }

    init {
        mDate = Date()
    }
}