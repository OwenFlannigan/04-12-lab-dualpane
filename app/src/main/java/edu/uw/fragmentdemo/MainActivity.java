package edu.uw.fragmentdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements MoviesFragment.OnMovieSelectedListener {

    private static final String TAG = "MainActivity";
    private static boolean dualPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if((FrameLayout) findViewById(R.id.container_right) != null &&
                ((FrameLayout) findViewById(R.id.container_right)).getVisibility() == View.VISIBLE) {
            dualPane = true;
        } else {
            dualPane = false;
        }


        MoviesFragment fragment = (MoviesFragment)getSupportFragmentManager().findFragmentByTag("MoviesFragment");
        if(fragment == null) {
            fragment = new MoviesFragment();
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if(!dualPane) {
            //ft.replace(R.id.container, fragment, "MoviesFragment");
            ft.remove(fragment);
            ft.add(R.id.container, fragment, "MoviesFragment");
        } else {
            //ft.replace(R.id.container_left, fragment, "MoviesFragment");
            ft.remove(fragment);
            ft.add(R.id.container_left, fragment, "MoviesFragment");
        }
        ft.commit();
    }


    //respond to search button clicking
    public void handleSearchClick(View v){
        Log.v(TAG, "Button handled");

        EditText text = (EditText)findViewById(R.id.txtSearch);
        String searchTerm = text.getText().toString();

        MoviesFragment fragment = (MoviesFragment)getSupportFragmentManager().findFragmentByTag("MoviesFragment");

        if(fragment == null){
            fragment = new MoviesFragment();
            if(!dualPane) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, fragment, "MoviesFragment")
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_left, fragment, "MoviesFragment")
                        .commit();
            }

        }

        fragment.fetchData(searchTerm);
    }

    @Override
    public void movieSelected(Movie movie) {


        Bundle bundle = new Bundle();
        bundle.putString("title", movie.toString());
        bundle.putString("imdb", movie.imdbId);

        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(bundle);

        if(!dualPane) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, detailFragment, "DetailsFragment")
                    .addToBackStack(null)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_right, detailFragment, "DetailsFragment")
                    .addToBackStack(null)
                    .commit();
        }

    }
}
