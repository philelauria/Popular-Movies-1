package codes.pelauria.popular_movies_1;


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MovieFragment extends Fragment {
    public static MoviePosterImageAdapter mMovieAdapter;
    public static List<Movie> moviesList;

    public MovieFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mMovieAdapter = new MoviePosterImageAdapter(getActivity());
        GridView gridview = (GridView) rootView.findViewById(R.id.gridView);
        gridview.setAdapter(mMovieAdapter);
        updateMovies();

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Movie movie = moviesList.get(position);
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
                intent.putExtra("movie", Parcels.wrap(movie));
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void updateMovies() {
        FetchMovieTask movieTask = new FetchMovieTask();
        movieTask.execute(R.id.action_sort_popularity);
    }

    public static class FetchMovieTask extends AsyncTask<Integer, Void, List<Movie>> {
        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        private String constructPosterUrl(JSONObject movieJson) {
            String baseUrl = "http://image.tmdb.org/t/p/w500";

            try {
                return (baseUrl +  movieJson.getString("poster_path"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        private List<Movie> getMovieDataFromJson(String movieJsonStr) throws JSONException {
            final String MOVIE_RESULTS = "results";

            JSONObject popMovieJson = new JSONObject(movieJsonStr);
            JSONArray popMovieArray = popMovieJson.getJSONArray(MOVIE_RESULTS);
            List<Movie> listOfMovies = new ArrayList<>();

            Movie movie;
            String title, releaseDate, fullPosterUrl, overview;
            int voteAvg;

            JSONObject movieJson;

            for (int i = 0; i < popMovieArray.length(); i++) {
                movieJson = popMovieArray.getJSONObject(i);
                title = movieJson.getString("title");
                releaseDate = movieJson.getString("release_date");
                fullPosterUrl = constructPosterUrl(movieJson);
                overview = movieJson.getString("overview");
                voteAvg = movieJson.getInt("vote_average");
                movie = new Movie(title, releaseDate, fullPosterUrl, overview, voteAvg);
                listOfMovies.add(movie);
            }

            return listOfMovies;
        }


        @Override
        protected List<Movie> doInBackground(Integer... sortType) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String popMovieJsonStr = null;

            try {
                final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/";
                final String API_KEY_PARAM = "api_key";
                String SORT_TYPE = (sortType[0] == R.id.action_sort_rating) ? "top_rated" : "popular";

                Uri builtUri = Uri.parse(MOVIES_BASE_URL)
                        .buildUpon()
                        .appendEncodedPath(SORT_TYPE)
                        .appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                popMovieJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMovieDataFromJson(popMovieJsonStr);
            } catch (JSONException e) {
                Log.v(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> listOfMovies) {
            if (listOfMovies != null) {
                mMovieAdapter.urls.clear();
                moviesList = listOfMovies;
                for (Movie movieObject : listOfMovies) {
                    mMovieAdapter.urls.add(movieObject.getPosterUrl());
                }

                mMovieAdapter.notifyDataSetChanged();
            }
        }
    }
}
