package az.zero.movietime.ui.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import az.zero.movietime.R
import az.zero.movietime.adapter.MovieAdapter
import az.zero.movietime.api.MovieLoadStateAdapter
import az.zero.movietime.databinding.FragmentHomeBinding
import az.zero.movietime.utils.LOADING_ITEM
import az.zero.movietime.utils.NO_INTERNET
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private val viewModel: HomeFragmentViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var movieAdapter: MovieAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)


        movieAdapter = MovieAdapter()
        val gridLayoutManager = GridLayoutManager(requireContext(), 4)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int =
                when (movieAdapter.getItemViewType(position)) {
                    LOADING_ITEM -> 4
                    NO_INTERNET -> 1
                    else -> -1
                }
        }
        binding.rvMovies.apply {
            adapter = movieAdapter.withLoadStateFooter(
                footer = MovieLoadStateAdapter { movieAdapter.retry() }
            )

            itemAnimator = null
            layoutManager = gridLayoutManager

        }
        binding.buttonRetry.setOnClickListener { movieAdapter.retry() }


        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            movieAdapter.submitData(viewLifecycleOwner.lifecycle, movies)
            Log.e("TAG", "observer called")
        }

        movieAdapter.addLoadStateListener { loadState ->
            binding.apply {
                pbProgressBar.isVisible = loadState.source.refresh is LoadState.Loading
                rvMovies.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error

                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    movieAdapter.itemCount < 1
                ) {
                    rvMovies.isVisible = false
                }
            }
        }

    }
}