package org.example.backend.service;

import jakarta.persistence.EntityManager;
import org.example.backend.api.PostRepository;
import org.example.backend.domain.Post;
import org.example.backend.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    public boolean savePost(Post post) {
        postRepository.save(post);
        // 立即刷新到数据库，确保数据已持久化
        entityManager.flush();
        // 清除EntityManager中的所有缓存，确保下次查询时获取最新数据
        entityManager.clear();
        return true;
    }

    @Override
    public boolean deletePostById(long id) {
        postRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional
    public Post findPostById(long id) {
        // 清除EntityManager缓存，确保获取最新数据
        entityManager.clear();
        // 使用JPQL查询，使用JOIN FETCH立即加载懒加载的集合
        try {
            Post post = entityManager.createQuery(
                "SELECT p FROM Post p " +
                "LEFT JOIN FETCH p.likeUsers " +
                "LEFT JOIN FETCH p.user " +
                "LEFT JOIN FETCH p.event " +
                "WHERE p.id = :id", Post.class)
                .setParameter("id", id)
                .getSingleResult();
            return post;
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public List<Post> findAllPosts() {
        // 清除EntityManager缓存，确保获取最新数据
        entityManager.clear();
        // 使用JPQL查询，使用JOIN FETCH立即加载懒加载的集合
        return entityManager.createQuery(
            "SELECT DISTINCT p FROM Post p " +
            "LEFT JOIN FETCH p.likeUsers " +
            "LEFT JOIN FETCH p.user " +
            "LEFT JOIN FETCH p.event", Post.class)
            .getResultList();
    }

    @Override
    @Transactional
    public List<Post> findPostsByUserId(long userId) {
        // 清除缓存，确保获取最新数据
        entityManager.clear();
        List<Post> posts = postRepository.findPostsByUserId(userId);
        // 在事务内初始化懒加载属性
        for (Post post : posts) {
            if (post.getUser() != null) {
                post.getUser().getId();
                post.getUser().getName();
            }
            if (post.getLikeUsers() != null) {
                post.getLikeUsers().size(); // 触发懒加载
            }
        }
        return posts;
    }

    @Override
    @Transactional
    public boolean deletePost(long postId) {
        // 先检查帖子是否存在
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            System.out.println("deletePost - Post not found: " + postId);
            return false;
        }
        
        // 1. 先删除所有用户收藏该帖子的关系
        try {
            // 从所有用户的收藏列表中移除该帖子
            List<User> usersWithFavourite = entityManager.createQuery(
                "SELECT DISTINCT u FROM User u JOIN u.favouritePosts fp WHERE fp.id = :postId", User.class)
                .setParameter("postId", postId)
                .getResultList();
            
            for (User user : usersWithFavourite) {
                if (user.getFavouritePosts() != null) {
                    user.getFavouritePosts().removeIf(p -> p != null && p.getId() == postId);
                }
            }
            
            System.out.println("deletePost - Removed favourite relationships for " + usersWithFavourite.size() + " users");
        } catch (Exception e) {
            System.out.println("deletePost - Error removing favourite relationships: " + e.getMessage());
            // 继续删除，即使清理收藏关系失败
        }
        
        // 2. 删除点赞关系（Post.likeUsers 是反向关系，直接清空即可）
        try {
            // Post.likeUsers 是 ManyToMany 关系，直接清空即可
            if (post.getLikeUsers() != null) {
                post.getLikeUsers().clear();
                postRepository.save(post); // 保存以更新关系
            }
            System.out.println("deletePost - Cleared like relationships");
        } catch (Exception e) {
            System.out.println("deletePost - Error removing like relationships: " + e.getMessage());
        }
        
        // 3. 刷新以确保关联关系已删除
        entityManager.flush();
        entityManager.clear();
        
        // 4. 删除帖子本身（级联删除会自动处理 comments）
        postRepository.deleteById(postId);
        
        // 5. 立即刷新到数据库，确保删除操作已提交
        entityManager.flush();
        entityManager.clear();
        
        System.out.println("deletePost - Post deleted successfully: " + postId);
        return true;
    }

    @Override
    public List<Post> searchPost(String keyword) {
        return postRepository.findAllByPostContentContainingIgnoreCaseOrPostTitleContainingIgnoreCase(keyword, keyword);
    }
}
