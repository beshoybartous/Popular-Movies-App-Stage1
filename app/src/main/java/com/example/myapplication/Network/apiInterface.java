package com.example.myapplication.Network;

import com.example.myapplication.Models.MoviesModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface apiInterface {
    
    @GET("/3/movie/{category}")
    Call<MoviesModel> getMoviesJson(
            @Path("category") String category,
            @Query("api_key") String api_key,
            @Query("language") String language,
            @Query("page") int page
    );




}
