package com.example.retrofityoutube.api

import com.example.retrofityoutube.response.MovieDetailsResponse
import com.example.retrofityoutube.response.MoviesListResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {

       @GET("movie/popular")
       fun getPopularMoviesList(@Query("page") page : Int) : Call<MoviesListResponse>

       @GET("movie/{movie_id}")
       fun getMovieDetails(@Path("movie_id") movieId : Int) : Call<MovieDetailsResponse>
    }