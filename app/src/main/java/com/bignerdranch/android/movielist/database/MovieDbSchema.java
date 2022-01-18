package com.bignerdranch.android.movielist.database;


public class MovieDbSchema {
    public static final class MovieTable {
        public static final String NAME = "movies";

        public static final class Cols{
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String WATCHED = "watched";
        }
    }
}
