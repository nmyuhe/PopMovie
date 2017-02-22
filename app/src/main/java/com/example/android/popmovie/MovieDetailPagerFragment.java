package com.example.android.popmovie;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popmovie.bean.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;


public class MovieDetailPagerFragment extends Fragment {
    private static final String MOVIE_DATA = "movie_data";
    private Movie mMovie;

    private static final String ID = "mId";
    private static final String TITLE = "mTitle";


    private ArrayList<Movie> mMovieList;

    public static MovieDetailPagerFragment newInstance(Movie movie) {
        Bundle data = new Bundle();
        data.putParcelable(MOVIE_DATA, movie);
        MovieDetailPagerFragment fragment = new MovieDetailPagerFragment();
        fragment.setArguments(data);
        return fragment;
    }

    public MovieDetailPagerFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail_pager, container, false);
        mMovie = getArguments().getParcelable(MOVIE_DATA);
        ((TextView) rootView.findViewById(R.id.movie_title)).setText(mMovie.getTitle());
        ((TextView) rootView.findViewById(R.id.release_date)).setText(mMovie.getRelease_date().substring(0,4));
        Picasso.with(getActivity())
                .load("https://image.tmdb.org/t/p/w185"+ mMovie.getPoster_path())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .into((ImageView) rootView.findViewById(R.id.movie_image), new Callback() {
                    @Override
                    public void onSuccess() {
//                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(getActivity(), R.string.posterDownError, Toast.LENGTH_SHORT).show();
                    }
                });
        ((TextView) rootView.findViewById(R.id.vote_average)).setText(mMovie.getVote_average()+"/10");
        ((TextView) rootView.findViewById(R.id.overview)).setText(mMovie.getOverview());


        return rootView;
    }

}
