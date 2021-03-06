package codes.pelauria.popular_movies_1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import static codes.pelauria.popular_movies_1.MovieFragment.*;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MovieFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort_rating) {
            updateMoviesFromOptionItemSelected(R.id.action_sort_rating);
        } else {
            updateMoviesFromOptionItemSelected(R.id.action_sort_popularity);
        }

        return true;
    }

    private void updateMoviesFromOptionItemSelected(Integer sortType) {
        FetchMovieTask movieTask = new FetchMovieTask();
        movieTask.execute(sortType);
    }

    // TODO: Maintain scroll position when backing out of MovieDetailActivity

}
