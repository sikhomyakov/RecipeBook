package ru.netology.recipesbook.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import ru.netology.recipesbook.R
import ru.netology.recipesbook.adapter.StepsAdapter
import ru.netology.recipesbook.databinding.FragmentRecipeNewBinding
import ru.netology.recipesbook.dto.Categories
import ru.netology.recipesbook.dto.Recipe
import ru.netology.recipesbook.dto.Utils
import ru.netology.recipesbook.dto.Utils.Companion.hideKeyboard
import ru.netology.recipesbook.viewmodel.RecipeViewModel


class NewRecipeFragment : Fragment() {

    private val viewModel by activityViewModels<RecipeViewModel>()


    private val pickRecipeImgActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data!!
                requireActivity().contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                viewModel.newRecipeImg.value = uri.toString()
            }
        }
    private val pickStepImgActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data!!
                requireActivity().contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                viewModel.newStepImg.value = uri.toString()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentRecipeNewBinding.inflate(layoutInflater, container, false).also { binding ->
            with(binding) {


                val newRecipe = MutableLiveData(
                    Recipe(
                        id = 0,
                        author = "Me",
                        published = Utils.addLocalDataTime(),
                        title = "",
                        content = "",
                        recipeImg = "",
                        steps = mutableMapOf(),
                        categories = mutableSetOf()
                    )
                )

                newRecipe.observe(viewLifecycleOwner) { recipe ->
                    render(recipe)
                }


                val categories = mutableSetOf<Categories>()

                val popupMenu by lazy {
                    PopupMenu(context, binding.addCategoryImageButton).apply {
                        inflate(R.menu.recipe_categories_menu)
                        for (i in 0 until Categories.values().size) {
                            menu.add(0, i, 0, Categories.values()[i].categoryName)
                        }
                        setOnMenuItemClickListener { option ->
                            for (i in 0 until Categories.values().size) {
                                if (option.itemId == i) {
                                    categories.add(Categories.values()[i])
                                    newRecipe.value =
                                        newRecipe.value?.copy(categories = categories)
                                    return@setOnMenuItemClickListener true
                                }
                            }
                            false
                        }
                    }
                }

                addCategoryImageButton.setOnClickListener {
                    popupMenu.show()
                }

                var recipeImg = ""


                viewModel.newRecipeImg.observe(viewLifecycleOwner) { path ->
                    recipeImg = path
                    newRecipe.value =
                        newRecipe.value?.copy(recipeImg = recipeImg)
                }

                newRecipeAddRecipeImageMaterialButton.setOnClickListener {
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        type = "image/*"
                    }
                    val imgPickIntent = Intent.createChooser(intent, "Select Image for recipe...")
                    pickRecipeImgActivityResultLauncher.launch(imgPickIntent)
                }

                var steps = mutableMapOf<String, String>()
                var stepImg = ""

                newRecipeAddImageStepImageButton.setOnClickListener {
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        type = "image/*"
                    }
                    val imgPickIntent = Intent.createChooser(intent, "Select Image for step...")
                    pickStepImgActivityResultLauncher.launch(imgPickIntent)
                }

                viewModel.newStepImg.observe(viewLifecycleOwner) { path ->
                    stepImg = path
                }

                newRecipeAddStepImageButton.setOnClickListener {
                    if (!newRecipeStepsEditText.text.isNullOrEmpty()) {
                        steps[newRecipeStepsEditText.text.toString()] = stepImg.ifEmpty { "" }
                        newRecipe.value =
                            newRecipe.value?.copy(steps = steps)
                        stepImg = ""
                        newRecipeStepsEditText.hideKeyboard()
                        newRecipeStepsEditText.text.clear()
                    }
                }

                viewModel.newRecipe.observe(viewLifecycleOwner) { recipe ->
                    steps = recipe.steps
                    newRecipe.value = newRecipe.value?.copy(steps = recipe.steps)
                }


                newRecipeSaveButton.setOnClickListener {
                    if (recipeImg.isEmpty()) {
                        recipeImg = DEFAULT_IMAGE
                        newRecipe.value =
                            newRecipe.value?.copy(recipeImg = recipeImg)
                    }
                    if (
                        !newRecipeNameEditText.text.isNullOrBlank() &&
                        !newRecipeContentWordEditView.text.isNullOrBlank() &&
                        steps.isNotEmpty()
                    ) {
                        newRecipe.value = newRecipe.value?.copy(
                            title = newRecipeNameEditText.text.toString()
                        )
                        newRecipe.value = newRecipe.value?.copy(
                            content = newRecipeContentWordEditView.text.toString()
                        )
                        newRecipe.value?.let { recipe ->
                            viewModel.create(recipe)
                            findNavController().popBackStack()
                        }
                    } else if (newRecipeNameEditText.text.isNullOrBlank()) {
                        newRecipeNameEditText.requestFocus()
                        newRecipeNameEditText.error =
                            resources.getString(R.string.error_empty_title)
                    } else if (newRecipeContentWordEditView.text.isNullOrBlank()) {
                        newRecipeContentWordEditView.requestFocus()
                        newRecipeContentWordEditView.error =
                            resources.getString(R.string.error_empty_description)
                    } else if (steps.isEmpty()) {
                        newRecipeStepsEditText.requestFocus()
                        newRecipeStepsEditText.error =
                            resources.getString(R.string.error_empty_steps)
                    }
                }
            }

        }.root
    }

    private fun FragmentRecipeNewBinding.render(recipe: Recipe) {

        newRecipeImageView.setImageURI(Uri.parse(recipe.recipeImg))

        setTags(newRecipeChipGroup.context, recipe.categories, this)

        val stepsAdapter = StepsAdapter(recipe, CALLER_NEW_RECIPE, viewModel)
        newRecipeStepsList.adapter = stepsAdapter
        stepsAdapter.submitList(recipe.steps.keys.toList())
    }


    companion object {
        const val DEFAULT_IMAGE =
            "android.resource://ru.netology.recipesbook/drawable/no_img"
        const val CALLER_NEW_RECIPE = "Caller: newRecipe"
    }
}

private fun setTags(
    context: Context,
    categories: MutableSet<Categories>,
    binding: FragmentRecipeNewBinding
) {
    val chipGroup = binding.newRecipeChipGroup

    chipGroup.removeAllViews()

    categories.forEach { category ->
        val tagName = category.categoryName
        val chip = Chip(context)
        chip.text = tagName
        chip.isCloseIconVisible = true

        chip.setOnCloseIconClickListener {
            categories.remove(category)
            chipGroup.removeView(chip)
        }
        chipGroup.addView(chip)
    }
}




