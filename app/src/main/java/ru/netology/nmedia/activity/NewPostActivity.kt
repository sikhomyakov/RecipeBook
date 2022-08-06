package ru.netology.nmedia.activity

import android.app.Activity

import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityNewPostBinding

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)


        with(binding) {
            edit.setText(intent.extras?.getString(Intent.EXTRA_TEXT))
            edit.requestFocus()
            ok.setOnClickListener {
                val intent = Intent()
                if (edit.text.isNullOrBlank()) {
                    setResult(Activity.RESULT_CANCELED, intent)
                } else {
                    val content = edit.text.toString()
                    intent.putExtra(Intent.EXTRA_TEXT, content)
                    setResult(Activity.RESULT_OK, intent)
                }
                finish()
            }
//        binding.edit.requestFocus()
//        binding.ok.setOnClickListener {
//            onOkButtonClicked(binding.edit.text?.toString())
//        }
//    }

//    private fun onOkButtonClicked(postContent: String?) {
//        val intent = Intent()
//        if (postContent.isNullOrBlank()) {
//            setResult(Activity.RESULT_CANCELED, intent)
//        } else {
//
//            intent.putExtra(POST_CONTENT_EXTRA_KEY, postContent)
//            setResult(Activity.RESULT_OK, intent)
//        }
//    }
//
//    companion object {
//        const val POST_CONTENT_EXTRA_KEY = "postContent"
//    }
//
//    object ResultContract : ActivityResultContract<Unit, String?>() {
//        override fun createIntent(context: Context, input: Unit) =
//            Intent(context, NewPostActivity::class.java)
//
//        override fun parseResult(resultCode: Int, intent: Intent?): String? {
//            if (resultCode != Activity.RESULT_OK) return null
//            intent ?: return null
//            return intent.getStringExtra(POST_CONTENT_EXTRA_KEY)
        }
    }
}