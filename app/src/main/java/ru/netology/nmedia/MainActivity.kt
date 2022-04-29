package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.DrawableRes
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 0L,
            author = "Stas",
            content = "Event",
            published = "30.04.2022",
            likedByMe = false
        )

        binding.render(post)
        binding.like.setOnClickListener {
            post.likedByMe = !post.likedByMe
            val imageResId = if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24
            binding.like.setImageResource(getLikeIconResId(post.likedByMe))
        }
    }

    private fun ActivityMainBinding.render(post: Post) {
        author.text = post.author
        content.text = post.content
        published.text = post.published
        like.setImageResource(getLikeIconResId(post.likedByMe))
    }

    @DrawableRes
    private fun getLikeIconResId(liked: Boolean) =
        if (liked) R.drawable.ic_liked_24 else R.drawable.ic_like_24
}
