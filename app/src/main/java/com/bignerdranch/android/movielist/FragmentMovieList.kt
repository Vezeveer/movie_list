package com.bignerdranch.android.movielist;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.List;

public class FragmentMovieList extends Fragment {

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private RecyclerView mMovieRecyclerView;
    private MovieAdapter mAdapter;
    private boolean mSubtitleVisible;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        activity.getSupportActionBar().setIcon(R.drawable.logo_smallest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        mMovieRecyclerView = view.findViewById(R.id.movie_recycler_view);
        mMovieRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(savedInstanceState != null){
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        updateUI();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_movie_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if(mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.new_movie:
                ModelMovie movie = new ModelMovie();
                ControllerMovie.get(getActivity()).addMovie(movie);
                Intent intent = ActivityMovieListPager.newIntent(getActivity(), movie.getmId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            case R.id.update_movie_list:
                getActivity().recreate();
                Toast.makeText(getActivity(),
                        "Updated!",
                        Toast.LENGTH_SHORT).show();
                return true;
            case R.id.about_app:
                Intent intentAbout = new Intent(getActivity(), ActivityAbout.class);
                startActivity(intentAbout);
                return true;
            case R.id.exit_app:
                getActivity().finish();
                System.exit(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    private void updateSubtitle(){
        ControllerMovie controllerMovie = ControllerMovie.get(getActivity());
        int movieCount = controllerMovie.getMovies().size();
        String subtitle = getString(R.string.subtitle_format, movieCount);

        if(!mSubtitleVisible){
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    private void updateUI() {
        ControllerMovie controllerMovie = ControllerMovie.get(getActivity());
        List<ModelMovie> movies = controllerMovie.getMovies();

        if(mAdapter == null){
            mAdapter = new MovieAdapter(movies);
            mMovieRecyclerView.setAdapter(mAdapter);
        }else{
            mAdapter.setMovies(movies);
            mAdapter.notifyDataSetChanged();
        }

        updateSubtitle();
    }

    private class MovieHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ModelMovie mMovie;

        public MovieHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_movie, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = itemView.findViewById(R.id.movie_title);
            mDateTextView = itemView.findViewById(R.id.movie_date);
        }

        public void bind(ModelMovie movie){
            mMovie = movie;
            mTitleTextView.setText(mMovie.getmTitle());
            mDateTextView.setText(DateFormat.getDateInstance(DateFormat.LONG).format(mMovie.getmDate()));
        }

        @Override
        public void onClick(View view) {
            Intent intent = ActivityMovieListPager.newIntent(getActivity(), mMovie.getmId());
            startActivity(intent);
        }
    }

    private class MovieAdapter extends RecyclerView.Adapter<MovieHolder>{

        private List<ModelMovie> mMovies;

        public MovieAdapter(List<ModelMovie> movies){
            mMovies = movies;
        }

        @Override
        public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            return new MovieHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(MovieHolder holder, int position) {
            ModelMovie movie = mMovies.get(position);
            holder.bind(movie);
        }

        @Override
        public int getItemCount() {
            return mMovies.size();
        }

        public void setMovies(List<ModelMovie> movies){
            mMovies = movies;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

}
