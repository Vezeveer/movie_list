package com.bignerdranch.android.movielist.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bignerdranch.android.movielist.database.MovieDbSchema.MovieTable;

public class MovieBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "movieDBase.db";

    public MovieBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table " + MovieTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                MovieTable.Cols.UUID + ", " +
                MovieTable.Cols.TITLE + ", " +
                MovieTable.Cols.DATE + ", " +
                MovieTable.Cols.WATCHED +
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
