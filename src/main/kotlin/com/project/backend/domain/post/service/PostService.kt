package com.project.backend.domain.post.service

import com.project.backend.domain.post.dto.PostNavItem
import com.project.backend.domain.post.dto.PostNavigationResponse
import com.project.backend.domain.post.entity.Post
import com.project.backend.domain.post.repository.PostRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PostService(private val postRepository: PostRepository) {

    fun getAllPosts(): List<Post> = postRepository.findAll().sortedByDescending { it.createdAt }

    fun getPostById(id: Long): Post? = postRepository.findById(id).orElse(null)

    fun createPost(title: String, content: String, author: String): Post {
        val post = Post(
            title = title,
            content = content,
            author = author,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        return postRepository.save(post)
    }

    fun updatePost(id: Long, title: String, content: String, author: String): Post? {
        val post = postRepository.findById(id).orElse(null) ?: return null
        post.title = title
        post.content = content
        post.author = author
        post.updatedAt = LocalDateTime.now()
        return postRepository.save(post)
    }

    fun deletePost(id: Long): Boolean {
        return if (postRepository.existsById(id)) {
            postRepository.deleteById(id)
            true
        } else {
            false
        }
    }

    fun getPostNavigation(postId: Long): PostNavigationResponse {
        if (!postRepository.existsById(postId)) {
            throw PostNotFoundException("게시글을 찾을 수 없습니다")
        }

        val prevPost = postRepository.findPrevPost(postId)
        val nextPost = postRepository.findNextPost(postId)

        return PostNavigationResponse(
            prev = prevPost?.let { PostNavItem(id = it.id, title = it.title, createdAt = it.createdAt) },
            next = nextPost?.let { PostNavItem(id = it.id, title = it.title, createdAt = it.createdAt) }
        )
    }
}

class PostNotFoundException(message: String) : RuntimeException(message)
