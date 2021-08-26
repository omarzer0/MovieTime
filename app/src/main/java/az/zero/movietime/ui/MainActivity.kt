package az.zero.movietime.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import az.zero.movietime.NavGraphDirections
import az.zero.movietime.R
import az.zero.movietime.databinding.ActivityMainBinding
import az.zero.movietime.ui.home.HomeFragment
import az.zero.movietime.utils.MethodToCall
import az.zero.movietime.utils.ShowType
import az.zero.movietime.utils.changeTitleTextStyle
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var appBarrConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Locale.setDefault(Locale("en", "US"))

        setSupportActionBar(binding.toolbar)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        appBarrConfiguration = AppBarConfiguration(setOf(R.id.homeFragment), binding.drawerLayout)

        setupActionBarWithNavController(navController, appBarrConfiguration)
        binding.navDrawerSlider.setupWithNavController(navController)

        binding.navDrawerSlider.setCheckedItem(R.id.movie_popular_menu_item)
        binding.navDrawerSlider.setNavigationItemSelectedListener { menuItem ->
            val showType: ShowType
            val methodToCall: MethodToCall
            when (menuItem.itemId) {
                R.id.movie_upcoming_menu_item -> {
                    showType = ShowType.MOVIE
                    methodToCall = MethodToCall.UPCOMING
                }

                R.id.movie_trending_menu_item -> {
                    showType = ShowType.MOVIE
                    methodToCall = MethodToCall.TRENDING
                }

                R.id.movie_top_rated_menu_item -> {
                    showType = ShowType.MOVIE
                    methodToCall = MethodToCall.TOP_RATED
                }

                // ---------------------------------------------
                R.id.tv_airing_today_menu_item -> {
                    showType = ShowType.TV
                    methodToCall = MethodToCall.AIRING_TODAY
                }

                R.id.tv_trending_menu_item -> {
                    showType = ShowType.TV
                    methodToCall = MethodToCall.TRENDING
                }

                R.id.tv_top_rated_menu_item -> {
                    showType = ShowType.TV
                    methodToCall = MethodToCall.TOP_RATED
                }

                R.id.tv_popular_menu_item -> {
                    showType = ShowType.TV
                    methodToCall = MethodToCall.GET_POPULAR
                }


                else -> {
                    showType = ShowType.MOVIE
                    methodToCall = MethodToCall.GET_POPULAR
                }
            }

            val action = NavGraphDirections.actionGlobalHomeFragment(
                showType,
                methodToCall
            )
            navController.navigate(action)

            menuItem.isChecked = true
            binding.drawerLayout.close()
            true
        }


        val menu = binding.navDrawerSlider.menu
        val movieItemTitle = menu.findItem(R.id.movie_menu_item_title)
        movieItemTitle.title = changeTitleTextStyle(movieItemTitle, this, R.style.NavigationView)

        val tvItemTitle = menu.findItem(R.id.tv_menu_item_title)
        tvItemTitle.title = changeTitleTextStyle(tvItemTitle, this, R.style.NavigationView)

    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarrConfiguration) || super.onSupportNavigateUp()
    }
}