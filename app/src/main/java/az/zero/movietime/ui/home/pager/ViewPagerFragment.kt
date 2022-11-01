package az.zero.movietime.ui.home.pager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import az.zero.movietime.R
import az.zero.movietime.ui.adapter.ShowAdapter
import az.zero.movietime.databinding.FragmentViewPagerBinding
import az.zero.movietime.ui.home.HomeFragmentViewModel
import az.zero.movietime.utils.TAB_NUMBER
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewPagerFragment : Fragment(R.layout.fragment_view_pager) {
    private val viewModel: HomeFragmentViewModel by viewModels()
    private lateinit var binding: FragmentViewPagerBinding
    private lateinit var showAdapter: ShowAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentViewPagerBinding.bind(view)


    }

    companion object {
        fun newInstance(tabNumber: Int): ViewPagerFragment {
            val args = Bundle()
            args.putInt(TAB_NUMBER, tabNumber)
            val fragment = ViewPagerFragment()
            fragment.arguments = args
            return fragment
        }
    }
}