package com.bignerdranch.android.movielist.database;


import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.movielist.ModelMovie;
import com.bignerdranch.android.movielist.database.MovieDbSchema.MovieTable;

import java.util.Date;
import java.util.UUID;

public class MovieCursorWrapper extends CursorWrapper {
    public MovieCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public ModelMovie getMovie(){
        String uuidString = getString(getColumnIndex(MovieTable.Cols.UUID));
        String title = getString(getColumnIndex(MovieTable.Cols.TITLE));
        long date = getLong(getColumnIndex(MovieTable.Cols.DATE));
        int hasWatched = getInt(getColumnIndex(MovieTable.Cols.WATCHED));

        ModelMovie movie = new ModelMovie(UUID.fromString(uuidString));
        movie.setmTitle(title);
        movie.setmDate(new Date(date));
        movie.setmWatched(hasWatched !=0);

        return movie;
    }
}
