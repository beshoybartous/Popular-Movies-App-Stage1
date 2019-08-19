package com.example.myapplication.ModelView;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.myapplication.Models.MoviesModel;
import com.example.myapplication.Repository.MoviesRepository;


public class MoviesViewModel extends AndroidViewModel {

    private MoviesRepository moviesRepository;
    //list of movies online and database
    private MutableLiveData<MoviesModel> OnlineMovies;


    public MoviesViewModel( Application application) {
        super( application );
        moviesRepository=new MoviesRepository( application );
    }
    //get movie from api
    public MoviesModel.ResultsBean getMovieOnline(int id){
        return moviesRepository.getMovieOnline( id );

    }
    //fetch list of movies from api
    public MutableLiveData<MoviesModel> fetchMoviesOnline(String Category)
    {
        OnlineMovies=moviesRepository.fetchMoviesOnline(Category);
        return OnlineMovies;
    }
    }
