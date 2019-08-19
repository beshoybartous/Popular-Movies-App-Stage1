package com.example.myapplication.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.Adapter.MovieAdapter;
import com.example.myapplication.ModelView.MoviesViewModel;
import com.example.myapplication.Models.MoviesModel;
import com.example.myapplication.R;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListiner{
    private static final String Popular_Movies="Popular Movies";
    private final static String POPULAR_CATEGORY="popular";
    private final static String TOP_RATED_CATEGORY="top_rated";
    private final static String NO_CONNECTION="Please connect to the Internet";
    private final static String SPINNER_POSITION="SPINNER_POSITION";
    private final static String MOVIES_LIST="MOVIES_LIST";
    private final static String RECYCLER_VIEW_POSITION="RECYCLER_VIEW_POSITION";
    private final static String SELECTED_MOVIES="SELECTED_MOVIES";

    //spinner for sort
    private Spinner spinner;
    private int spinnerPostionIdex = -1;
    private MenuItem sortSpinner = null;
    private ArrayAdapter<CharSequence> spinnerAdapter;

    //ViewModel and Movies Adapter and RecyclerView
    private MoviesViewModel moviesViewModel;
    private MovieAdapter moviesAdapter;
    private RecyclerView moviesRv;

    // list of movies from api
    List<MoviesModel.ResultsBean> moviesList;
    MoviesModel mMoviesModel;

    //boolean to check network state and broad cast receiver
    boolean noConetion;

    NetworkBroadCastReceiver networkBroadCastReceiver=new NetworkBroadCastReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        moviesList=new ArrayList<>(  );

        //init spinner adapter
        spinnerAdapter= ArrayAdapter.createFromResource( MainActivity.this,R.array.sort_items,android.R.layout.simple_spinner_item );
        spinnerAdapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );

        //find views and set recycler view and set orientation size
        moviesRv =(RecyclerView) findViewById( R.id.rv_movies );
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            moviesRv.setLayoutManager( new GridLayoutManager( MainActivity.this,2 ) );
        }
        else{
            moviesRv.setLayoutManager( new GridLayoutManager( MainActivity.this,4 ) );
        }

        //set ViewModel
        moviesViewModel= ViewModelProviders.of( MainActivity.this ).get( MoviesViewModel.class );

        //init Movie Adapter
        moviesAdapter=new MovieAdapter(MainActivity.this,MainActivity.this);
        //restore data if orientation changed
        if(savedInstanceState==null) {
            //get movies from api
            fetchData( POPULAR_CATEGORY );
        }
        else{
            mMoviesModel= (MoviesModel) savedInstanceState.getSerializable( MOVIES_LIST);

            if(mMoviesModel!=null) {

                moviesAdapter.setMovies(  mMoviesModel.getResults() );
                moviesAdapter.notifyDataSetChanged();
                moviesRv.setAdapter( moviesAdapter );
            }
            moviesRv.getLayoutManager().scrollToPosition(savedInstanceState.getInt(RECYCLER_VIEW_POSITION));
            spinnerPostionIdex = savedInstanceState.getInt( SPINNER_POSITION, -1 );

        }
        moviesRv.setAdapter(moviesAdapter);

    }
//fetch movies list from api and set adapter
    private void fetchData(String Category){
        moviesViewModel.fetchMoviesOnline(Category).observe( this, new Observer<MoviesModel>() {
            @Override
            public void onChanged(MoviesModel moviesModel) {
                mMoviesModel=moviesModel;
                moviesList=  moviesModel.getResults();
                moviesAdapter.setMovies( moviesList );
                moviesAdapter.notifyDataSetChanged();
            }

        } );
        moviesRv.smoothScrollToPosition(0);
    }
    @Override
    public void onListItemClick(int clickedItemIndex) {
        final Intent detailActivity=new Intent( MainActivity.this,DetailActivity.class );
        if(noConetion==false){
        MoviesModel.ResultsBean movie =moviesViewModel.getMovieOnline( clickedItemIndex );
        detailActivity.putExtra(SELECTED_MOVIES,  movie);
        startActivity(detailActivity);
        }
        else{
            Toast.makeText( MainActivity.this,NO_CONNECTION,Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate( R.menu.sort_menu,menu );
        sortSpinner = menu.findItem( R.id.sort_spinner);
        View spinnerView = sortSpinner.getActionView();
        if (spinnerView instanceof Spinner)
        {
            spinner = (Spinner) spinnerView;
            spinner.setAdapter(spinnerAdapter);
            //set spinner text if orientation changed
            if(spinnerPostionIdex!=-1){
                spinner.setSelection( spinnerPostionIdex );
            }
            spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    if(noConetion==false){
                        //check the type of selected sort
                        if(adapterView.getItemAtPosition( position ).equals( Popular_Movies )){
                            //if the selected sort from spinner not as the same sort before selections get new data
                            if(position!=spinnerPostionIdex) {
                                fetchData( POPULAR_CATEGORY );

                            }
                            //save the selected sort position to be used later when selecting again
                            spinnerPostionIdex=position;
                        }
                        else{
                            if(position!=spinnerPostionIdex) {
                                fetchData( TOP_RATED_CATEGORY );
                            }
                            spinnerPostionIdex=position;
                        }

                    }
                    else{
                        Toast.makeText( MainActivity.this,NO_CONNECTION,Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            } );

        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter=new IntentFilter( ConnectivityManager.CONNECTIVITY_ACTION );
        registerReceiver( networkBroadCastReceiver,filter );
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver( networkBroadCastReceiver );
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(MOVIES_LIST,  mMoviesModel );
        outState.putInt( SPINNER_POSITION,spinner.getSelectedItemPosition() );
        GridLayoutManager layoutManager = ((GridLayoutManager)moviesRv.getLayoutManager());
        int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
        outState.putInt( RECYCLER_VIEW_POSITION,firstVisiblePosition );
        super.onSaveInstanceState( outState );

    }

    private class NetworkBroadCastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(ConnectivityManager.CONNECTIVITY_ACTION.equals( intent.getAction() )){
                noConetion=intent.getBooleanExtra(
                        ConnectivityManager.EXTRA_NO_CONNECTIVITY,false
                );
            }
        }
    }
}
