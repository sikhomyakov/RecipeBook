package ru.netology.recipesbook.adapter

import ru.netology.recipesbook.dto.Recipe

interface StepInteractionListener {

    fun onDeleteStepButtonClicked(recipe: Recipe, stepKey: String, caller: String)

}