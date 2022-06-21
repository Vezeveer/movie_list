package com.bignerdranch.android.movielist;


import androidx.fragment.app.Fragment;

public class ActivityMain extends ActivitySingleFragment {

    @Override
    protected Fragment createFragment(){
        return new FragmentMovieList();
    }
}
