package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.dto.Utils

class PostRepositoryInMemoryImpl : PostRepository {

    private var nextId = 1L
    private val posts
        get() = checkNotNull(data.value) {

        }

    private val data = MutableLiveData(
        List(100) { index ->
            Post(
                id = index + 1L,
                author = "Нетология. Университет интернет-профессий будущего ",
                content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
                published = "21 мая в 18:36",
                likedByMe = false,
                likes = 999,
                shares = 993,
                sharedByMe = false,
                views = 5,
                viewedBy = false,
            )
        }
    )

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        data.value = posts.map {
            if (it.id != id) {
                it
            } else {
                it.copy(
                    likedByMe = !it.likedByMe,
                    likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
                )
            }
        }

    }

    override fun toShareById(id: Long) {
        data.value = posts.map {
            if (it.id != id) {
                it
            } else {
                it.copy(shares = it.shares + 1)
            }
        }
    }

    override fun toViewById(id: Long) {
        data.value = posts.map {
            if (it.id != id) {
                it
            } else {
                it.copy(views = it.views + 1)
            }
        }

    }

    override fun deleteById(id: Long) {
        data.value = posts.filter { it.id != id }

    }

    override fun addPost(post: Post) {
        if (post.id == 0L) {
            data.value = listOf(
                post.copy(
                    id = nextId++,
                    author = "Test Author",
                    published = Utils.addLocalDataTime(),
                    likedByMe = false,
                    likes = 0,
                    shares = 0,
                    views = 0
                )
            ) + posts
        }

        data.value = posts.map {
            if (it.id != post.id) it else it.copy(content = post.content)
        }

    }
}
