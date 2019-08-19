package com.example.myapplication.Network;

import com.example.myapplication.Models.MoviesModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface apiInterface {
    //https://api.themoviedb.org/3/movie/{movie_id}/videos?api_key=7d8c113767e44efb0a4a84fe6da33568&language=en-US
    @GET("/3/movie/{category}")
    Call<MoviesModel> getMoviesJson(
            @Path("category") String category,
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("page") int page
    );




}
