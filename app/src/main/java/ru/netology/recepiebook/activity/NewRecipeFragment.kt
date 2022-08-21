package ru.netology.recepiebook.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.recepiebook.databinding.FragmentNewRecipeBinding
import ru.netology.recepiebook.dto.Utils

import ru.netology.recepiebook.viewmodel.RecipeViewModel

class NewRecipeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewRecipeBinding.inflate(
            inflater,
            container,
            false
        )
        val viewModel: RecipeViewModel by viewModels(ownerProducer = ::requireParentFragment)

        with(binding) {
            arguments?.textArg?.let(edit::setText)
            edit.requestFocus()
            ok.setOnClickListener {
                if (!edit.text.isNullOrBlank()) {
                    val content = edit.text.toString()
                    viewModel.changeContent(content)
                    viewModel.save()
                }
                findNavController().navigateUp()
            }
        }
        return binding.root
    }

    companion object {
        var Bundle.textArg: String? by Utils.StringArg
    }
}