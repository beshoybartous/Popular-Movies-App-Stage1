package com.example.myapplication.UI;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.Adapter.DetailAdapter;
import com.example.myapplication.Models.MoviesModel;
import com.example.myapplication.R;

public class DetailActivity extends AppCompatActivity {
    private final static String SELECTED_MOVIES="SELECTED_MOVIES";
    RecyclerView moviesDetailRecycler;
    DetailAdapter detailAdapter;
    MoviesModel.ResultsBean selectedMovie;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.detail_activity );
        moviesDetailRecycler=findViewById( R.id.rv_movie_info );
        @SuppressLint("WrongConstant") RecyclerView.LayoutManager layoutManager=new LinearLayoutManager( this,LinearLayoutManager.VERTICAL,false );
        moviesDetailRecycler.setLayoutManager( layoutManager );
        detailAdapter =new DetailAdapter(DetailActivity.this);
        selectedMovie=(MoviesModel.ResultsBean) getIntent().getSerializableExtra( SELECTED_MOVIES );
        detailAdapter.setOnlineMovie( selectedMovie );
        moviesDetailRecycler.setAdapter( detailAdapter );

    }
}
