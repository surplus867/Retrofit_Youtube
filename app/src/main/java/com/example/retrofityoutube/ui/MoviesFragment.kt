package com.example.retrofityoutube.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofityoutube.adapter.MoviesAdapter
import com.example.retrofityoutube.databinding.FragmentMoviesBinding
import com.example.retrofityoutube.repository.ApiRepository
import com.example.retrofityoutube.response.MoviesListResponse
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private lateinit var binding: FragmentMoviesBinding

    @Inject
    lateinit var apiRepository: ApiRepository

    @Inject
    lateinit var moviesAdapter: MoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMoviesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            prgBarMovies.visibility = View.GONE
            apiRepository.getPopularMoviesList(1).enqueue(object : Callback<MoviesListResponse> {
                override fun onResponse(
                    call: Call<MoviesListResponse>,
                    response: Response<MoviesListResponse>
                ) {
                    when (response.code()) {
                        200 -> {
                            response.body().let { itBody ->
                                if (itBody?.results!!.isNotEmpty()) {
                                    moviesAdapter.differ.submitList(itBody.results)
                                }
                                rvMovies.apply {
                                    layoutManager = LinearLayoutManager(requireContext())
                                    adapter = moviesAdapter
                                }

                                moviesAdapter.setOnItemClickListener {
                                    val direction = MoviesFragmentDirections.actionMoviesFragmentToMoviesDetailFragment(it.id!!)
                                    findNavController().navigate(direction)
                                }
                            }
                        }
                        404 -> {
                            Toast.makeText(
                                requireContext(),
                                "The resource you requested could not be found.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        401 -> {
                            Toast.makeText(
                                requireContext(),
                                "Invalid API key: You must be granted a valid key.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                override fun onFailure(call: Call<MoviesListResponse>, t: Throwable) {
                    prgBarMovies.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        "OnFailure",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }
    }
}