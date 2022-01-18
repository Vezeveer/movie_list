package com.bignerdranch.android.movielist;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

public class FragmentMovie extends Fragment {
    private ModelMovie mMovie;
    private EditText mTitleField;
    private static final String ARG_MOVIE_ID = "movie_id";
    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;
    Button mDateButton;
    Button mAddButton;
    Button mDeleteButton;

    public static FragmentMovie newInstance(UUID movieId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_MOVIE_ID, movieId);
        FragmentMovie fragment = new FragmentMovie();
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentMovie(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID movieId = (UUID) getArguments().getSerializable(ARG_MOVIE_ID);
        mMovie = ControllerMovie.get(getActivity()).getMovie(movieId);
    }

    @Override
    public void onPause(){
        super.onPause();

        ControllerMovie.get(getActivity())
                .updateMovie(mMovie);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie,
                container, false);

        mTitleField = v.findViewById(R.id.edit_text_title);
        mTitleField.setText(mMovie.getmTitle());
        mDateButton = v.findViewById(R.id.edit_text_date);
        mAddButton = v.findViewById(R.id.btn_add_movie);
        mDeleteButton = v.findViewById(R.id.btn_delete_movie);
        CheckBox mCheckBox = v.findViewById(R.id.checkbox_watched);
        mCheckBox.setChecked(mMovie.ismWatched());

        updateDate();

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentDatePicker dialog = FragmentDatePicker
                        .newInstance(mMovie.getmDate());


                dialog.setTargetFragment(FragmentMovie.this, REQUEST_DATE);

                dialog.show(manager, DIALOG_DATE);
            }
        });

        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMovie.setmTitle(mTitleField.getText().toString());
                mMovie.setmWatched(mCheckBox.isChecked());

                goToMain();
                Toast.makeText(getActivity(),
                        mTitleField.getText() + " - Added/updated",
                        Toast.LENGTH_SHORT).show();
            }
        });

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            int i;
            @Override
            public void onClick(View view) {
                i = ControllerMovie.get(getActivity())
                        .deleteMovie(mMovie.getmId());

                if(i>0){
                    Toast.makeText(getActivity(),
                            mTitleField.getText() + " - Deleted!",
                            Toast.LENGTH_SHORT).show();
                    goToMain();
                }else{
                    Toast.makeText(getActivity(),
                            mTitleField.getText() + " - Error. Can't delete!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(getActivity(),
                            mTitleField.getText() + " - Watched!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(),
                            mTitleField.getText() + " - Not Watched",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        }

        if(requestCode == REQUEST_DATE){
            Date date = (Date) data
                    .getSerializableExtra(FragmentDatePicker.EXTRA_DATE);
            mMovie.setmDate(date);

            updateDate();
        }
    }



    private void updateDate() {
        mDateButton.setText(DateFormat.getDateInstance(DateFormat.LONG).format(mMovie.getmDate()));
    }

    private void goToMain(){
        Intent intent = new Intent(getActivity(), ActivityMain.class);
        startActivity(intent);
    }
}
