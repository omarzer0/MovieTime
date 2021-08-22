package az.zero.movietime.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import az.zero.movietime.data.Movie
import az.zero.movietime.databinding.ItemMovieBinding
import az.zero.movietime.utils.LOADING_ITEM
import az.zero.movietime.utils.NO_INTERNET
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class MovieAdapter : PagingDataAdapter<Movie, MovieAdapter.MovieViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) holder.bind(currentItem)
    }

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(currentItem: Movie) {
            binding.apply {
                tvMovieName.text = currentItem.title
                tvReleaseDate.text = currentItem.getReleaseDateYear()
                tvRatingNumber.text = "${currentItem.vote_average}"
                Glide.with(itemView).load(currentItem.moviePoster)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivMoviePoster)
            }
        }

    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie) =
                oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int): Int {
        // set ViewType
        return if (position == itemCount) LOADING_ITEM else NO_INTERNET
    }

}