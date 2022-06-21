package com.bignerdranch.android.movielist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.movielist.database.MovieBaseHelper;
import com.bignerdranch.android.movielist.database.MovieCursorWrapper;
import com.bignerdranch.android.movielist.database.MovieDbSchema.MovieTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ControllerMovie {
    private static ControllerMovie sControllerMovie;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static ControllerMovie get(Context context){
        if (sControllerMovie == null){
            sControllerMovie = new ControllerMovie(context);
        }
        return sControllerMovie;
    }

    private ControllerMovie(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new MovieBaseHelper(mContext)
                .getWritableDatabase();
    }

    public void addMovie(ModelMovie m){
        ContentValues values = getContentValues(m);

        mDatabase.insert(MovieTable.NAME, null, values);
    }

    public List<ModelMovie> getMovies(){
        List<ModelMovie> movies = new ArrayList<>();

        MovieCursorWrapper cursor = queryMovies(null, null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                movies.add(cursor.getMovie());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return movies;
    }

    public ModelMovie getMovie(UUID id){
        MovieCursorWrapper cursor = queryMovies(
                MovieTable.Cols.UUID + " = ?",
                new String[] { id.toString() }
        );

        try {
            if(cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getMovie();
        } finally {
            cursor.close();
        }
    }

    public int deleteMovie(UUID id){
        int i;
        i = mDatabase.delete(MovieTable.NAME,
                MovieTable.Cols.UUID + " = ?",
                new String[] { id.toString() });
        return i;
    }

    public void updateMovie(ModelMovie movie){
        String uuidString = movie.getmId().toString();
        ContentValues values = getContentValues(movie);

        mDatabase.update(MovieTable.NAME, values,
                MovieTable.Cols.UUID + " = ?",
                new String[] { uuidString});
    }

    private MovieCursorWrapper queryMovies(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                MovieTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new MovieCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(ModelMovie movie){
        ContentValues values = new ContentValues();
        values.put(MovieTable.Cols.UUID, movie.getmId().toString());
        values.put(MovieTable.Cols.TITLE, movie.getmTitle());
        values.put(MovieTable.Cols.DATE, movie.getmDate().getTime());
        values.put(MovieTable.Cols.WATCHED, movie.ismWatched() ? 1 : 0);

        return values;
    }
}
