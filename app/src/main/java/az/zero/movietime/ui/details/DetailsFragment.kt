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
import az.zero.movietime.adapter.ShowAdapter
import az.zero.movietime.data.Show
import az.zero.movietime.databinding.FragmentDetailsBinding
import az.zero.movietime.utils.exhaustive
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {
    private val viewModel: DetailsViewModel by viewModels()
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var similarAdapter: ShowAdapter
    private lateinit var relatedAdapter: ShowAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)

        similarAdapter = ShowAdapter()
        relatedAdapter = ShowAdapter()
        collectEvents()

        val show = viewModel.show

        if (show != null) {
            populateViews(binding, show)
            setUpSimilarRV(binding)
            setUpRelatedRV(binding)

            viewModel.getSimilarShows(show.id)
            viewModel.getRecommendedShows(show.id)

            observeData()
        }

    }

    private fun setUpSimilarRV(binding: FragmentDetailsBinding) {
        binding.rvSimilar.apply {
            adapter = similarAdapter
        }

        similarAdapter.setOnShowClickListener { movie ->
            viewModel.showItemClicked(movie)
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

        relatedAdapter.setOnShowClickListener { movie ->
            viewModel.showItemClicked(movie)
        }

        relatedAdapter.addLoadStateListener { loadState ->
            val showOrHide = relatedAdapter.itemCount >= 1
            binding.rvRelated.isVisible = showOrHide
            binding.tvRelated.isVisible = showOrHide
        }

    }


    private fun observeData() {
        viewModel.similarMovies.observe(viewLifecycleOwner) { shows ->
            similarAdapter.submitData(viewLifecycleOwner.lifecycle, shows)
        }

        viewModel.relatedMovies.observe(viewLifecycleOwner) { show ->
            relatedAdapter.submitData(viewLifecycleOwner.lifecycle, show)
        }
    }

    private fun populateViews(binding: FragmentDetailsBinding, show: Show) {

        binding.apply {
            tvTitle.text = show.showTitle
            tvDetailsReleaseDate.text = show.getTheYearOfReleaseDateYear()
            tvDetailsRatingNumber.text = "${show.voteAverageWithOneDecimalPlace}/10"
            tvOverView.text = show.descriptionOrEmpty

            tvReleaseDate.text = show.fullReleaseDate
            tvIsAdult.text = if (show.adult) "Yes" else "No"
            tvVotesNumber.text = "${show.vote_count}"

            if (show.title == null) {
                tvAdult.visibility = View.GONE
                tvIsAdult.visibility = View.GONE
            }
            Glide.with(requireContext()).load(show.showPoster).into(ivSmallPosterImage)
            Glide.with(requireContext()).load(show.showBackPoster).into(ivBackPosterImage)
        }
    }

    private fun collectEvents() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.showEvent.collect { event ->
                when (event) {
                    is DetailsFragmentEvents.NavigateToDetailsFragmentWithShow -> {
                        val action = NavGraphDirections.actionGlobalDetailsFragment(
                            event.show,
                            event.show.showTitle,
                            event.showType
                        )
                        findNavController().navigate(action)
                    }
                }.exhaustive
            }
        }
    }

}