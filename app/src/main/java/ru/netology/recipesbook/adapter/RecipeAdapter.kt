package ru.netology.recipesbook.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import ru.netology.recipesbook.R
import ru.netology.recipesbook.databinding.RecipeBinding
import ru.netology.recipesbook.dto.Categories
import ru.netology.recipesbook.dto.Recipe
import ru.netology.recipesbook.dto.Utils


internal class RecipeAdapter(
    private val interactionListener: RecipeInteractionListener
) : ListAdapter<Recipe, RecipeAdapter.ViewHolder>(DiffCallback) {

    private var list = mutableListOf<Recipe>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecipeBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setData(list: MutableList<Recipe>?) {
        this.list = list!!
        submitList(list)
    }

    class ViewHolder(
        private val binding: RecipeBinding,
        private val listener: RecipeInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var recipe: Recipe

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.recipeCardOptionsMaterialButton).apply {
                inflate(R.menu.recipe_options)
                setOnMenuItemClickListener { option ->
                    when (option.itemId) {
                        R.id.remove -> {
                            listener.onRemoveClicked(recipe.id)
                            true
                        }
                        R.id.edit -> {
                            listener.onEditClicked(recipe.id)
                            true
                        }
                        else -> {
                            false
                        }
                    }
                }
            }
        }

        init {

            binding.favoriteRecipeCardMaterialButton.setOnClickListener {
                listener.onFavoriteClicked(recipe)
            }

            binding.recipeLikeButton.setOnClickListener {
                listener.onLikeClicked(recipe)
            }
            binding.recipeCardOptionsMaterialButton.setOnClickListener {
                popupMenu.show()
            }

            binding.root.setOnClickListener {
                listener.onRecipeClicked(recipe)
            }

        }


        fun bind(recipe: Recipe) {
            this.recipe = recipe
            with(binding) {
                titleRecipeCardTextView.text = recipe.title
                recipeAuthor.text = recipe.author
                recipePublished.text = recipe.published
                recipeCardImageView.setImageURI(Uri.parse(recipe.recipeImg))
                favoriteRecipeCardMaterialButton.isChecked = recipe.favoriteByMe
                setCategories(chipGroup.context, recipe.categories, binding)
                recipeLikeButton.text = Utils.counter(recipe.likes)
                recipeLikeButton.isChecked = recipe.likedByMe
            }
        }
    }


    internal object DiffCallback : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe) =
            oldItem.id == newItem.id


        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe) =
            oldItem == newItem

    }
}

private fun setCategories(
    context: Context,
    categories: MutableSet<Categories>,
    binding: RecipeBinding
) {
    val chipGroup = binding.chipGroup
    chipGroup.removeAllViews()
    categories.forEach { category ->
        val categoryName = category.categoryName
        val chip = Chip(context)
        chip.text = categoryName
        chip.isCloseIconVisible = false
        chipGroup.addView(chip)
    }
}

