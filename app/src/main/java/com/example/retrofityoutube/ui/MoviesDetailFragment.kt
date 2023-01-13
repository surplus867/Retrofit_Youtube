package com.example.retrofityoutube.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import coil.load
import coil.size.Scale
import com.example.retrofityoutube.R
import com.example.retrofityoutube.databinding.FragmentMoviesBinding
import com.example.retrofityoutube.databinding.FragmentMoviesDetailBinding
import com.example.retrofityoutube.repository.ApiRepository
import com.example.retrofityoutube.response.MovieDetailsResponse
import com.example.retrofityoutube.utils.Constants.POSTER_BASE_URL
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class MoviesDetailFragment : Fragment() {

    private lateinit var binding: FragmentMoviesDetailBinding

    @Inject
    lateinit var apiRepository: ApiRepository

    private val args: MoviesDetailFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMoviesDetailBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = args.movieId
        binding.apply {
            prgBarMovies.visibility =View.INVISIBLE
            apiRepository.getMovieDetails(id).enqueue(object : Callback<MovieDetailsResponse> {
                override fun onResponse(
                    call: Call<MovieDetailsResponse>,
                    response: Response<MovieDetailsResponse>
                ) {
                    prgBarMovies.visibility=View.GONE
                    when(response.code()){
                        200 -> {
                            response.body().let {
                                val moviePoster = POSTER_BASE_URL + it!!.posterPath
                                tvMovieBudget.text =it.budget.toString()
                                tvMovieOverview.text =it.overview
                                tvMovieDateRelease.text=it.releaseDate
                                tvMovieRating.text=it.voteAverage.toString()
                                tvMovieRevenue.text=it.revenue.toString()
                                tvMovieRuntime.text=it.runtime.toString()
                                tvMovieTagLine.text=it.tagline
                                tvMovieTitle.text=it.title

                                imgMovie.load(moviePoster){
                                    crossfade(true)
                                    placeholder(R.drawable.poster_placeholder)
                                    scale(Scale.FILL)
                                }
                                imgMovieBack.load(moviePoster){
                                    crossfade(true)
                                    placeholder(R.drawable.poster_placeholder)
                                    scale(Scale.FILL)
                                }
                            }
                        }
                        401 -> {
                            Toast.makeText(requireContext(), "Invalid API key: You must be granted a valid key.", Toast.LENGTH_SHORT).show()
                        }
                        404 -> {
                            Toast.makeText(requireContext(), "The resource you requested could not be found.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<MovieDetailsResponse>, t: Throwable) {
                    prgBarMovies.visibility=View.GONE
                    Toast.makeText(requireContext(), "OnFailure", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}