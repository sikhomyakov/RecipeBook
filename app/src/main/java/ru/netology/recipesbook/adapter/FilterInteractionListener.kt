package ru.netology.recipesbook.adapter

import androidx.lifecycle.MutableLiveData
import ru.netology.recipesbook.dto.Categories


interface FilterInteractionListener {

    fun onCheckClicked(category: Categories)

    fun onUncheckClicked(category: Categories)

    val filterCheckboxUpdate: MutableLiveData<Boolean>

}