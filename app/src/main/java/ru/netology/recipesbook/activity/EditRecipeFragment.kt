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
import ru.netology.recipesbook.databinding.FragmentRecipeEditBinding
import ru.netology.recipesbook.dto.Categories
import ru.netology.recipesbook.dto.Recipe
import ru.netology.recipesbook.dto.Utils.Companion.hideKeyboard
import ru.netology.recipesbook.viewmodel.RecipeViewModel


class EditRecipeFragment : Fragment() {

    private val viewModel by activityViewModels<RecipeViewModel>()

    private val pickStepImgActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data!!
                requireActivity().contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                viewModel.editStepImg.value = uri.toString()
            }
        }

    private val pickRecipeImgActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data!!
                requireActivity().contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                viewModel.editRecipeImg.value = uri.toString()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentRecipeEditBinding.inflate(layoutInflater, container, false).also { binding ->
            with(binding) {

                val currentEditingRecipe = MutableLiveData<Recipe>()

                currentEditingRecipe.value = viewModel.editingRecipe.value

                currentEditingRecipe.observe(viewLifecycleOwner) { recipe ->
                    render(recipe)
                }

                editRecipeNameEditText.setText(currentEditingRecipe.value?.title)
                editRecipeContentWordEditView.setText(currentEditingRecipe.value?.content)

                val categories = currentEditingRecipe.value?.categories ?: mutableSetOf()

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
                                    currentEditingRecipe.value =
                                        currentEditingRecipe.value?.copy(categories = categories)
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

                var recipeImg: String

                editRecipeImageView.setOnClickListener {
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        type = "image/*"
                    }
                    val imgPickIntent = Intent.createChooser(intent, "Select Image for Recipe...")
                    pickRecipeImgActivityResultLauncher.launch(imgPickIntent)
                }

                viewModel.editRecipeImg.observe(viewLifecycleOwner) { path ->
                    recipeImg = path
                    currentEditingRecipe.value =
                        currentEditingRecipe.value?.copy(recipeImg = recipeImg)
                }

                var steps = currentEditingRecipe.value?.steps ?: mutableMapOf()
                var stepImg = ""

                editRecipeAddImageStepImageButton.setOnClickListener {
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        type = "image/*"
                    }
                    val imgPickIntent = Intent.createChooser(intent, "Select Image for step...")
                    pickStepImgActivityResultLauncher.launch(imgPickIntent)
                }
                viewModel.editStepImg.observe(viewLifecycleOwner) { path ->
                    stepImg = path
                }

                editRecipeAddStepImageButton.setOnClickListener {
                    if (!editRecipeStepsEditText.text.isNullOrEmpty()) {
                        steps[editRecipeStepsEditText.text.toString()] = stepImg.ifEmpty { "" }
                        currentEditingRecipe.value =
                            currentEditingRecipe.value?.copy(steps = steps)
                        stepImg = ""
                        editRecipeStepsEditText.hideKeyboard()
                        editRecipeStepsEditText.text.clear()
                    }
                }

                viewModel.editingRecipe.observe(viewLifecycleOwner) { recipe ->
                    steps = recipe.steps
                    currentEditingRecipe.value = currentEditingRecipe.value?.copy(steps = steps)
                }

                editRecipeSaveButton.setOnClickListener {
                    if (
                        !editRecipeNameEditText.text.isNullOrBlank() &&
                        !editRecipeContentWordEditView.text.isNullOrBlank() &&
                        steps.isNotEmpty()
                    ) {
                        currentEditingRecipe.value = currentEditingRecipe.value?.copy(
                            title = editRecipeNameEditText.text.toString()
                        )
                        currentEditingRecipe.value = currentEditingRecipe.value?.copy(
                            content = editRecipeContentWordEditView.text.toString()
                        )
                        currentEditingRecipe.value?.let { recipe ->
                            viewModel.edit(
                                recipe
                            )
                            findNavController().popBackStack()
                        }
                    } else if (editRecipeNameEditText.text.isNullOrBlank()) {
                        editRecipeNameEditText.requestFocus()
                        editRecipeNameEditText.error =
                            resources.getString(R.string.error_empty_title)
                    } else if (steps.isEmpty()) {
                        editRecipeStepsEditText.requestFocus()
                        editRecipeStepsEditText.error =
                            resources.getString(R.string.error_empty_steps)
                    } else if (editRecipeContentWordEditView.text.isNullOrBlank()) {
                        editRecipeContentWordEditView.requestFocus()
                        editRecipeContentWordEditView.error =
                            resources.getString(R.string.error_empty_discription)
                    }
                }
            }

        }.root
    }


    private fun FragmentRecipeEditBinding.render(recipe: Recipe) {

        editRecipeImageView.setImageURI(Uri.parse(recipe.recipeImg))

        setTags(editRecipeChipGroup.context, recipe.categories, this)

        val stepsAdapter = StepsAdapter(recipe, CALLER_EDIT_RECIPE, viewModel)
        editRecipeStepsList.adapter = stepsAdapter
        stepsAdapter.submitList(recipe.steps.keys.toList())
    }

    companion object {
        const val CALLER_EDIT_RECIPE = "Caller: editRecipe"
    }
}

private fun setTags(
    context: Context,
    categories: MutableSet<Categories>,
    binding: FragmentRecipeEditBinding
) {
    val chipGroup = binding.editRecipeChipGroup
    chipGroup.removeAllViews()
    categories.forEach { category ->
        val categoryName = category.categoryName
        val chip = Chip(context)
        chip.text = categoryName
        chip.isCloseIconVisible = true
        chip.setOnCloseIconClickListener {
            categories.remove(category)
            chipGroup.removeView(chip)
        }
        chipGroup.addView(chip)
    }
}