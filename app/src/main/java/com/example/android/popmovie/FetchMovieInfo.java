package com.example.android.popmovie;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.android.popmovie.bean.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 获取电影信息
 * Created by Happy on 2017/2/7.
 */

public class FetchMovieInfo {
    private static final String LOG_TAG = FetchMovieInfo.class.getSimpleName();
    private Context mContext;
    //电影的查询类型
    private String mGetType;  //popular 和top_rated 两种 (热门电影和高分电影)

    public FetchMovieInfo(Context context) {
        mContext = context;
    }

    private String getUrl() {
        String language = "zh";
        final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/movie/" + mGetType + "?";
        final String LANGUAGE_PARAM = "language";
        final String APIKEY_PARAM = "api_key";

        Uri uri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendQueryParameter(LANGUAGE_PARAM, language)
                .appendQueryParameter(APIKEY_PARAM, BuildConfig.FETCH_MOVIE_API_KEY)
                .build();
        return uri.toString();
    }

    private List<Movie> parseMovies(String movieJsonStr) {
        // 需要解析的Json对象
        final String TMDB_PAGE = "page";
        final String TMDB_RESULTS = "results";
        final String TMDB_TOTAL_RESULTS = "total_results";
        final String TMDB_TOTAL_PAGES = "total_pages";
        List<Movie> moviesList = new ArrayList<>();

        try {
                /* 从Json串中解析出最外层的JSON对象 */
            JSONObject moviesJsonObject = new JSONObject(movieJsonStr);
            //解析出对象数组
            JSONArray movieJsonArray = moviesJsonObject.getJSONArray(TMDB_RESULTS);
            for (int i = 0; i < movieJsonArray.length(); i++) {
                JSONObject movieJsonObj = movieJsonArray.getJSONObject(i);
                Movie movie = new Movie();

                //开始装配对象
                movie.setId(movieJsonObj.getString("id"));
                movie.setPoster_path(movieJsonObj.getString("poster_path"));
                movie.setAdult(movieJsonObj.getBoolean("adult"));
                movie.setOverview(movieJsonObj.getString("overview"));
                movie.setRelease_date(movieJsonObj.getString("release_date"));
                movie.setOriginal_title(movieJsonObj.getString("original_title"));
                movie.setOriginal_language(movieJsonObj.getString("original_language"));
                movie.setTitle(movieJsonObj.getString("title"));
                movie.setBackdrop_path(movieJsonObj.getString("backdrop_path"));
                movie.setPopularity(movieJsonObj.getDouble("popularity"));
                movie.setVote_count(movieJsonObj.getLong("vote_count"));
                movie.setVideo(movieJsonObj.getBoolean("video"));
                movie.setVote_average(movieJsonObj.getDouble("vote_average"));
                moviesList.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return moviesList;
    }

    private boolean isOnline(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nwInfo = connectivityManager.getActiveNetworkInfo();
        return nwInfo != null && nwInfo.isConnected();
    }

    public List<Movie> fetchMovies(String getType) {
        this.mGetType = getType;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        BufferedReader reader = null;
        URL url = null;
        // 从请求响应返回的Json串
        String movieJsonStr = null;

        if ("".equals(getType)) {
            return null;
        }

        //测试网络
        if (!isOnline(mContext)) {
            Toast.makeText(mContext, R.string.connError, Toast.LENGTH_SHORT).show();
            return null;
        }

        //开始获取电影Json
        try {
            url = new URL(getUrl());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setUseCaches(false);
            urlConnection.setDoInput(true);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.connect();

            //将输入流读取到字符串
            inputStream = urlConnection.getInputStream();
            StringBuilder sb = new StringBuilder();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            if (sb.length() == 0) {
                return null;
            }
            movieJsonStr = sb.toString();
        } catch (MalformedURLException me) {
            Log.e(LOG_TAG, me.getMessage());
        } catch (IOException ioe) {
            Log.e(LOG_TAG, ioe.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
                urlConnection = null;
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                    inputStream = null;
                } catch (IOException e) {
                    Log.e(LOG_TAG,e.getMessage());
                }
            }
            if (reader != null) {
                try {
                    reader.close();

                } catch (IOException e) {
                    Log.e(LOG_TAG, e.getMessage());
                }
            }
        } //获取电影Json结束

        return parseMovies(movieJsonStr);
    }


}