package com.example.android.popmovie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.popmovie.bean.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

/**
 * 电影列表，首页
 */
public class MovieListFragment extends Fragment {
    private static final String GET_TYPE_PREF = "getType";
    private static final String LOG_TAG = MovieListFragment.class.getSimpleName();
    private static final int REQUEST_PREF_CHANGED = 0;
    private boolean mIsPrefChanged;

    private RecyclerView mMovieRecyclerView;
    private List<Movie> mMovieList = new ArrayList<Movie>();

    public MovieListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        if (mMovieList.size() == 0) {
            new FetchMovieTask().execute();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        mMovieRecyclerView = (RecyclerView) view.findViewById(R.id.movie_recycler_view);
        mMovieRecyclerView.setHasFixedSize(true);
        GridLayoutManager grdManager;
        int rows;
        if (getActivity().getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
            rows = 3;
        } else {
            rows = 2;
        }
        grdManager = new GridLayoutManager(getActivity(), rows);
        mMovieRecyclerView.setLayoutManager(grdManager);
        setupAdapter();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movielistmenu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_setting:
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivityForResult(intent,REQUEST_PREF_CHANGED);
                return true;
            case R.id.action_refresh:
                updateUI();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Handle various types of return  values
        if (requestCode == REQUEST_PREF_CHANGED) {
            if (data == null) {
                return;
            }
            mIsPrefChanged = SettingsActivity.wasPrefChanged(data);
            if (mIsPrefChanged) {
                updateUI();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setupAdapter() {

        if (isAdded()) {
            mMovieRecyclerView.setAdapter(new MovieAdapter(mMovieList));
        }
    }

    private void updateUI() {
        FetchMovieTask fetchMovieTask = new FetchMovieTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String getType = prefs.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popular));
        fetchMovieTask.execute(getType);
    }

    private class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Movie mMovie;
        public ImageView mItemImageView;

        public MovieHolder(View itemView) {
            super(itemView);
            mItemImageView = (ImageView) itemView.findViewById(R.id.fragment_movie_image_view);
            itemView.setOnClickListener(this);
        }

        public void bindMovie(Movie movie) {
            mMovie = movie;
        }

        public void bindDrawable(Drawable drawable) {
            mItemImageView.setImageDrawable(drawable);
        }

        @Override
        public void onClick(View v) {
            Intent intent = MovieDetailPagerActivity.newIntent(getActivity(), (ArrayList<Movie>) mMovieList, mMovie.getId());
            startActivity(intent);
        }

    }


    private class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {
        private List<Movie> mMovies;

        public MovieAdapter(List<Movie> movies) {
            mMovies = movies;
        }

        @Override
        public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.movie_item, parent, false);
            return new MovieHolder(view);
        }

        @Override
        public void onBindViewHolder(MovieHolder holder, int position) {
            Movie movie = mMovies.get(position);
            holder.bindMovie(movie);
            Drawable placeholder = getResources().getDrawable(R.drawable.placeholder);
            holder.bindDrawable(placeholder);

            Picasso.with(getActivity())
                    .load("https://image.tmdb.org/t/p/w185" + movie.getPoster_path())
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .into(holder.mItemImageView, new Callback() {
                        @Override
                        public void onSuccess() {
//                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError() {
                            Toast.makeText(getActivity(), "图片下载失败！", Toast.LENGTH_SHORT).show();
                        }
                    });

        }

        @Override
        public int getItemCount() {
            return mMovies.size();
        }
    }

    private class FetchMovieTask extends AsyncTask<String, Void, List<Movie>> {
        @Override
        protected List<Movie> doInBackground(String... params) {
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String getTypePref = sharedPref.getString(GET_TYPE_PREF, "popular");
            try {
                return new FetchMovieInfo(getActivity()).fetchMovies(getTypePref);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return null;
            }

        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            mMovieList = movies;
            if (mMovieList != null) {
                setupAdapter();
            } else {
                Toast.makeText(getActivity(), R.string.fetchError, Toast.LENGTH_SHORT).show();
            }

        }
    }

}
