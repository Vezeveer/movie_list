package com.bignerdranch.android.movielist;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.List;
import java.util.UUID;

public class ActivityMovieListPager extends AppCompatActivity {

    private static final String EXTRA_MOVIE_ID =
            "com.bignerdranch.android.movielist.movie_id";
    private ViewPager mViewPager;
    private List<ModelMovie> mMovies;

    public static Intent newIntent(Context packageContext, UUID movieId) {
        Intent intent = new Intent(packageContext, ActivityMovieListPager.class);
        intent.putExtra(EXTRA_MOVIE_ID, movieId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_pager);

        UUID movieId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_MOVIE_ID);
        mViewPager = findViewById(R.id.movie_view_pager);

        mMovies = ControllerMovie.get(this).getMovies();

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager){
            @Override
            public Fragment getItem(int position){
                ModelMovie movie = mMovies.get(position);
                return FragmentMovie.newInstance(movie.getmId());
            }

            @Override
            public int getCount(){
                return mMovies.size();
            }
        });

        for(int i = 0; i < mMovies.size(); i++){
            if(mMovies.get(i).getmId().equals(movieId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
