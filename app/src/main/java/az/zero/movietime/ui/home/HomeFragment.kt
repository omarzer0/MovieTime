package az.zero.movietime.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import az.zero.movietime.NavGraphDirections
import az.zero.movietime.R
import az.zero.movietime.api.ShowLoadStateAdapter
import az.zero.movietime.databinding.FragmentHomeBinding
import az.zero.movietime.ui.adapter.ShowAdapter
import az.zero.movietime.utils.LOADING_ITEM
import az.zero.movietime.utils.NO_INTERNET
import az.zero.movietime.utils.exhaustive
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private val viewModel: HomeFragmentViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private val showAdapter = ShowAdapter(onShowClick = { viewModel.showItemClicked(it) })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        val gridLayoutManager = GridLayoutManager(requireContext(), 4)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int =
                when (showAdapter.getItemViewType(position)) {
                    LOADING_ITEM -> 4
                    NO_INTERNET -> 1
                    else -> -1
                }
        }
        binding.rvShows.apply {
            adapter = showAdapter.withLoadStateFooter(
                footer = ShowLoadStateAdapter { showAdapter.retry() }
            )

            itemAnimator = null
            layoutManager = gridLayoutManager

        }
        binding.buttonRetry.setOnClickListener { showAdapter.retry() }


        viewModel.shows.observe(viewLifecycleOwner) { shows ->
            showAdapter.submitData(viewLifecycleOwner.lifecycle, shows)
        }

        showAdapter.addLoadStateListener { loadState ->
            binding.apply {
                pbProgressBar.isVisible = loadState.source.refresh is LoadState.Loading
                rvShows.isVisible = loadState.source.refresh is LoadState.NotLoading
                buttonRetry.isVisible = loadState.source.refresh is LoadState.Error
                textViewError.isVisible = loadState.source.refresh is LoadState.Error

                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    showAdapter.itemCount < 1
                ) {
                    rvShows.isVisible = false
                }
            }
        }


        collectEvents()
        setHasOptionsMenu(true)
    }

    private fun collectEvents() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.showEvent.collect { event ->
                when (event) {
                    is HomeFragmentEvents.NavigateToDetailsFragmentWithShow -> {
                        val action = NavGraphDirections.actionGlobalDetailsFragment(
                            event.show,
                            event.show.showTitle,
                            event.show.showType
                        )
                        findNavController().navigate(action)
                    }
                }.exhaustive
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_search -> {
            val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
            findNavController().navigate(action)
            true
        }

        else -> super.onOptionsItemSelected(item)
    }
}