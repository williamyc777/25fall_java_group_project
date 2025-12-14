package org.example.backend.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.backend.domain.AbstractUser;
import org.example.backend.domain.Event;
import org.example.backend.domain.Post;
import org.example.backend.domain.User;

@Setter
@Getter
public class PostDto {
    private long postID;
    private String postLink;
    private String postTitle;
    private String postContent;
    private long postRelevantEventID;
    private int postLikeAmount;
    private int postCollectAmount;
    private int postCommentAmount;
    private String username;
    private Long userID;
    private String userBio;
    private String userAvatar;
    private String eventLink;
    private String eventName;
    private String eventIntroduction;
    private String eventPoster;
    private boolean likeOrNot;
    private boolean collectOrNot;

    public PostDto() {
    }

    public PostDto(Post post) {
        Event event = post.getEvent();
        AbstractUser user = post.getUser();
        this.postID = post.getId();
        this.postLink = String.valueOf(post.getId());
        this.postTitle = post.getPostTitle();
        this.postContent = post.getPostContent();
        // 处理 event 可能为 null 的情况
        if (event != null) {
            this.postRelevantEventID = event.getId();
            this.eventLink = String.valueOf(event.getId());
            this.eventName = event.getName();
            this.eventIntroduction = event.getIntroduction();
            this.eventPoster = event.getPosterUrl();
        } else {
            this.postRelevantEventID = 0;
            this.eventLink = null;
            this.eventName = null;
            this.eventIntroduction = null;
            this.eventPoster = null;
        }
        // 处理 likeUsers 可能为 null 的情况
        if (post.getLikeUsers() != null) {
            this.postLikeAmount = post.getLikeUsers().size();
        } else {
            this.postLikeAmount = 0;
        }
        // 处理 user 可能为 null 的情况
        if (user != null) {
            this.username = user.getUsername();
            this.userID = user.getId();
            this.userBio = user.getBio();
            this.userAvatar = user.getAvatar();
        } else {
            this.username = null;
            this.userID = null;
            this.userBio = null;
            this.userAvatar = null;
        }
    }
}
