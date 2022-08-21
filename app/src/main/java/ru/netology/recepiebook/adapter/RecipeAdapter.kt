package ru.netology.recepiebook.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.recepiebook.R
import ru.netology.recepiebook.databinding.RecipeBinding
import ru.netology.recepiebook.dto.Recipe
import ru.netology.recepiebook.dto.Utils


internal class RecipeAdapter(
    private val interactionListener: RecipeInteractionListener
) : ListAdapter<Recipe, ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecipeBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class ViewHolder(
    private val binding: RecipeBinding,
    private val listener: RecipeInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(recipe: Recipe) {
        binding.apply {
            author.text = recipe.author
            published.text = recipe.published
            content.text = recipe.content
            likeButton.text = Utils.counter(recipe.likes)
            shareButton.text = Utils.counter(recipe.shares)
            likeButton.isChecked = recipe.likedByMe
            favoriteButton.isChecked = recipe.favoriteByMe

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_recipe)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                listener.onRemoveClicked(recipe)
                                true
                            }
                            R.id.edit -> {
                                listener.onEditClicked(recipe)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }
            if (recipe.video != null) videoGroup.visibility = View.VISIBLE
            else videoGroup.visibility = View.GONE

            content.setOnClickListener {
                listener.onRecipeClicked(recipe)
            }
            video.setOnClickListener {
                listener.onVideoClicked(recipe)
            }

            play.setOnClickListener {
                listener.onVideoClicked(recipe)
            }

            likeButton.setOnClickListener {
                listener.onLikeClicked(recipe)
            }

            shareButton.setOnClickListener {
                listener.onShareClicked(recipe)
            }
            favoriteButton.setOnClickListener {
                listener.onFavoriteClicked(recipe)
            }
        }
    }
}


class DiffCallback : DiffUtil.ItemCallback<Recipe>() {
    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe) =
        oldItem.id == newItem.id


    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe) =
        oldItem == newItem

}


