package az.zero.movietime.ui.search

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
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
import az.zero.movietime.databinding.FragmentSearchBinding
import az.zero.movietime.ui.adapter.ShowAdapter
import az.zero.movietime.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchView: SearchView
    private val viewModel: SearchViewModel by viewModels()
    private val showAdapter = ShowAdapter(onShowClick = { viewModel.showItemClicked(it) })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)


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
                buttonRetry.isVisible =
                    loadState.source.refresh is LoadState.Error
                textViewError.isVisible =
                    loadState.source.refresh is LoadState.Error

                if (loadState.source.refresh is LoadState.NotLoading
                    && loadState.append.endOfPaginationReached
                    && showAdapter.itemCount < 1
                ) {
                    rvShows.isVisible = false
                }
            }
        }

        setHasOptionsMenu(true)
        collectEvents()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_fragment_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView

        searchItem.expandActionView()
        val pendingQuery = viewModel.getCurrentSearchQuery()
        if (pendingQuery != null && pendingQuery.isNotEmpty() && pendingQuery != START_SEARCH_QUERY) {
            searchView.setQuery(pendingQuery, false)
        }

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean = true
            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                findNavController().navigateUp()
                return true
            }
        })

        searchView.onQueryTextChanged { text ->
            viewModel.updateSearchQuery(
                if (text == "") START_SEARCH_QUERY
                else text
            )
        }
    }

    private fun collectEvents() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.showEvent.collect { event ->
                when (event) {
                    is SearchFragmentEvents.NavigateToDetailsFragmentWithShow -> {
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

    override fun onDestroyView() {
        super.onDestroyView()
        searchView.setOnQueryTextListener(null)
    }
}