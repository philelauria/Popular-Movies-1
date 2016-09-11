package codes.pelauria.popular_movies_1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieDetailActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MovieDetailFragment())
                    .commit();
        }
    }

    @SuppressLint("ValidFragment")
    public static class MovieDetailFragment extends Fragment {
        public MovieDetailFragment() {
            setHasOptionsMenu(false);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final String LOG_TAG = MovieDetailFragment.class.getSimpleName();
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            Intent intent = getActivity().getIntent();
            Spanned releaseDate = null;
            Movie movie;
            ImageView posterView;
            Spanned voteAverage;
            Spanned overView;



            if (intent != null && intent.hasExtra("movie")) {
                movie = Parcels.unwrap(intent.getParcelableExtra("movie"));

                try {
                    releaseDate = Html.fromHtml(getString(R.string.release_date) + convertDateFormat(movie.getReleaseDate()));
                } catch (ParseException e) {
                    Log.v(LOG_TAG, "Date Conversion Error: " + e);
                }

                if (releaseDate == null) {
                    releaseDate = Html.fromHtml(getString(R.string.release_date) + movie.getReleaseDate());
                }

                voteAverage = Html.fromHtml(getString(R.string.average_rating) +  String.valueOf(movie.getVoteAverage()) + "/10");
                posterView = (ImageView) rootView.findViewById(R.id.posterView);
                overView = Html.fromHtml(getString(R.string.overview) + movie.getOverview());

                Picasso.with(getActivity()).load(movie.getPosterUrl()).into(posterView);
                ((TextView) rootView.findViewById(R.id.detail_text)).setText(movie.getTitle());
                ((TextView) rootView.findViewById(R.id.release_date)).setText(releaseDate);
                ((TextView) rootView.findViewById(R.id.average_rating)).setText(voteAverage);
                ((TextView) rootView.findViewById(R.id.overview)).setText(overView);
            }

            return rootView;
        }

        private String convertDateFormat(String dateStr) throws ParseException {
            SimpleDateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd");
            fromFormat.setLenient(false);
            SimpleDateFormat toFormat = new SimpleDateFormat("MM/dd/yyyy");
            toFormat.setLenient(false);
            Date date = fromFormat.parse(dateStr);
            return toFormat.format(date);
        }
    }
}