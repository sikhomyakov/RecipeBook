package ru.netology.recipesbook.activity


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ru.netology.recipesbook.R
import ru.netology.recipesbook.databinding.ActivityAppBinding


class AppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = binding.fragmentContainer.getFragment<NavHostFragment>().navController
        binding.recipesBottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.recipesFeedFragment || destination.id == R.id.favoriteRecipesFeedFragment) {
                binding.recipesBottomNavigationView.visibility = View.VISIBLE
            } else {
                binding.recipesBottomNavigationView.visibility = View.GONE
            }
        }


    }
}