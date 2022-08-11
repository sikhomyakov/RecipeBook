package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.util.StringArg
import ru.netology.nmedia.viewmodel.PostViewModel

class PostFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostBinding.inflate(
            inflater,
            container,
            false
        )
        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)


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
        var Bundle.textArg: String? by StringArg
    }
}