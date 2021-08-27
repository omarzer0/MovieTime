package az.zero.movietime.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import az.zero.movietime.data.Show
import az.zero.movietime.databinding.ItemMovieBinding
import az.zero.movietime.utils.LOADING_ITEM
import az.zero.movietime.utils.NO_INTERNET
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class ShowAdapter : PagingDataAdapter<Show, ShowAdapter.ShowViewHolder>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val binding = ItemMovieBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ShowViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) holder.bind(currentItem)
    }

    inner class ShowViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val clickedShow = getItem(bindingAdapterPosition)
                clickedShow?.let { onShowClickListener?.let { it(clickedShow) } }
            }
        }

        fun bind(currentItem: Show) {
            binding.apply {
                tvMovieName.text = currentItem.showTitle
                tvReleaseDate.text = currentItem.getTheYearOfReleaseDateYear()
                tvRatingNumber.text = "${currentItem.voteAverageWithOneDecimalPlace}"
                Glide.with(itemView).load(currentItem.showPoster)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivMoviePoster)
            }
        }

    }

    private var onShowClickListener: ((Show) -> Unit)? = null
    fun setOnShowClickListener(listener: (Show) -> Unit) {
        onShowClickListener = listener
    }

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Show>() {
            override fun areItemsTheSame(oldItem: Show, newItem: Show) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Show, newItem: Show) =
                oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int): Int {
        // set ViewType
        return if (position == itemCount) LOADING_ITEM else NO_INTERNET
    }

}