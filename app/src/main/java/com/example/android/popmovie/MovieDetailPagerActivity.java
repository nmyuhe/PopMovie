package com.example.android.popmovie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;

import com.example.android.popmovie.bean.Movie;

import java.lang.annotation.Annotation;
import java.util.ArrayList;

public class MovieDetailPagerActivity extends FragmentActivity implements ActionBar.DisplayOptions{
    private static final String MOVIE_LIST = "movieList";
    private static final String MOVIE_ID = "movieId";

    private MovieDetailPagerAdapter mMovieDetailPagerAdapter;
    private ViewPager mViewPager;

    private ArrayList<Movie> mMovieList;
    private int currentMovieId;

    public static Intent newIntent(Context context, ArrayList<Movie> movies, int movieId) {
        Intent intent = new Intent(context, MovieDetailPagerActivity.class);
        intent.putExtra(MOVIE_ID, movieId);
        intent.putParcelableArrayListExtra(MOVIE_LIST, movies);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail_pager);
        currentMovieId = getIntent().getIntExtra(MOVIE_ID,0);
        mMovieList = getIntent().getParcelableArrayListExtra(MOVIE_LIST);
        mMovieDetailPagerAdapter = new MovieDetailPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager_view);
        mViewPager.setAdapter(mMovieDetailPagerAdapter);
        for (int i = 0; i < mMovieList.size(); i++) {
            if (mMovieList.get(i).getId()==currentMovieId) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    public Class<? extends Annotation> annotationType() {

        return null;
    }

    private class MovieDetailPagerAdapter extends FragmentStatePagerAdapter {

        public MovieDetailPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Movie movie = mMovieList.get(position);
            return MovieDetailPagerFragment.newInstance(movie);
        }

        @Override
        public int getCount() {
            return mMovieList.size();
        }
    }
}