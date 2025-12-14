package org.example.backend.service;

import org.example.backend.domain.Post;

import java.util.List;

public interface PostService {
    boolean savePost(Post post);

    boolean deletePostById(long id);

    Post findPostById(long id);

    List<Post> findAllPosts();

    List<Post> findPostsByUserId(long userId);

    boolean deletePost(long postId);

    List<Post> searchPost(String keyword);
}
