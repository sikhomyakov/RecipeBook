package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val likeButton = findViewById<ImageButton>(R.id.like)
        likeButton.setOnClickListener {
            println("Like clicked")
        }
    }
}