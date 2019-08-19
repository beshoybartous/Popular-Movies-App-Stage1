package com.example.myapplication.Repository;
import android.app.Application;
import androidx.lifecycle.MutableLiveData;
import com.example.myapplication.Models.MoviesModel;
import com.example.myapplication.Network.RetrofitClient;
import com.example.myapplication.Network.apiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesRepository {
    //api data
    private final static String API_KEY="";
    private final static String LANGUAGE="en-US";
    private static int PAGE=1;
    MutableLiveData<MoviesModel> OnlineMovies=new MutableLiveData<>();


    public MoviesRepository(Application application){
    }
    //get movie from api
    public MoviesModel.ResultsBean getMovieOnline(int id) {
        return OnlineMovies.getValue().getResults().get( id );
    }
    // fetch list of movies from api
    public MutableLiveData<MoviesModel> fetchMoviesOnline(String CATEGORY) {
        /**
         * Calling JSON
         */
        apiInterface api = RetrofitClient.getApiService();

        Call<MoviesModel> call = api.getMoviesJson( CATEGORY, API_KEY, LANGUAGE, PAGE );

        /**
         * Enqueue Callback will be call when get response...
         */

        call.enqueue( new Callback<MoviesModel>() {
            @Override
            public void onResponse(Call<MoviesModel> call, Response<MoviesModel> response) {


                if (response.isSuccessful()) {
                    /**
                     * Got Successfully
                     */

                    MoviesModel results = response.body();
                    OnlineMovies.setValue( results );


                }
            }

            @Override
            public void onFailure(Call<MoviesModel> call, Throwable t) {

            }
        } );
        return OnlineMovies;
    }

}
