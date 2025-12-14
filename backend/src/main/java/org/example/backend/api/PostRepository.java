package org.example.backend.api;

import org.example.backend.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 使用正确的JPA方法命名：根据user的id查询
    List<Post> findByUser_Id(long userId);
    
    // 保持向后兼容
    default List<Post> findPostsByUserId(long userId) {
        return findByUser_Id(userId);
    }

    List<Post> findAllByPostContentContainingIgnoreCaseOrPostTitleContainingIgnoreCase(String text, String title);
}
