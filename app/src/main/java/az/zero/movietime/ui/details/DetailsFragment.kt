package az.zero.movietime.ui.details

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import az.zero.movietime.NavGraphDirections
import az.zero.movietime.R
import az.zero.movietime.adapter.MovieAdapter
import az.zero.movietime.data.Movie
import az.zero.movietime.databinding.FragmentDetailsBinding
import az.zero.movietime.utils.exhaustive
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {
    private val viewModel: DetailsViewModel by viewModels()
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var similarAdapter: MovieAdapter
    private lateinit var relatedAdapter: MovieAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)

        similarAdapter = MovieAdapter()
        relatedAdapter = MovieAdapter()
        collectEvents()

        val movie = viewModel.movie

        if (movie != null) {
            populateViews(binding, movie)
            setUpSimilarRV(binding)
            setUpRelatedRV(binding)

            viewModel.getSimilarMovies(movie.id)
            viewModel.getRecommendedMovies(movie.id)

            observeData()
        }

    }

    private fun setUpSimilarRV(binding: FragmentDetailsBinding) {
        binding.rvSimilar.apply {
            adapter = similarAdapter
        }

        similarAdapter.setOnMovieClickListener { movie ->
            viewModel.movieItemClicked(movie)
        }

        similarAdapter.addLoadStateListener { loadState ->
            val showOrHide = similarAdapter.itemCount >= 1
            binding.rvSimilar.isVisible = showOrHide
            binding.tvSimilar.isVisible = showOrHide
        }

    }

    private fun setUpRelatedRV(binding: FragmentDetailsBinding) {

        binding.rvRelated.apply {
            adapter = relatedAdapter
        }

        relatedAdapter.setOnMovieClickListener { movie ->
            viewModel.movieItemClicked(movie)
        }

        relatedAdapter.addLoadStateListener { loadState ->
            val showOrHide = relatedAdapter.itemCount >= 1
            binding.rvRelated.isVisible = showOrHide
            binding.tvRelated.isVisible = showOrHide
        }

    }


    private fun observeData() {
        viewModel.similarMovies.observe(viewLifecycleOwner) { movies ->
            similarAdapter.submitData(viewLifecycleOwner.lifecycle, movies)
        }

        viewModel.relatedMovies.observe(viewLifecycleOwner) { movies ->
            Log.e("TAG", "observeData: $movies")
            relatedAdapter.submitData(viewLifecycleOwner.lifecycle, movies)
        }
    }

    private fun populateViews(binding: FragmentDetailsBinding, movie: Movie) {

        binding.apply {
            tvTitle.text = movie.showTitle
            tvDetailsReleaseDate.text = movie.getTheYearOfReleaseDateYear()
            tvDetailsRatingNumber.text = "${movie.voteAverageWithOneDecimalPlace}/10"
            tvOverView.text = movie.descriptionOrEmpty

            tvReleaseDate.text = movie.fullReleaseDate
            tvIsAdult.text = if (movie.adult) "Yes" else "No"
            tvVotesNumber.text = "${movie.vote_count}"

            if (movie.title == null) {
                tvAdult.visibility = View.GONE
                tvIsAdult.visibility = View.GONE
            }
            Glide.with(requireContext()).load(movie.moviePoster).into(ivSmallPosterImage)
            Glide.with(requireContext()).load(movie.movieBackPoster).into(ivBackPosterImage)
        }
    }

    private fun collectEvents() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.movieEvent.collect { event ->
                when (event) {
                    is DetailsFragmentEvents.NavigateToDetailsFragmentWithMovie -> {
                        val action = NavGraphDirections.actionGlobalDetailsFragment(
                            event.movie,
                            event.movie.showTitle,
                            event.showType
                        )
                        findNavController().navigate(action)
                    }
                }.exhaustive
            }
        }
    }

}