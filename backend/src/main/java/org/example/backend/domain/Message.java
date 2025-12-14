package org.example.backend.domain;

import jakarta.persistence.*;
import lombok.Setter;
import lombok.Getter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    private AbstractUser toUser;

    @ManyToOne(fetch = FetchType.LAZY)
    private Event toEvent;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post toPost;

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment toComment;

    @ManyToOne(fetch = FetchType.LAZY)
    private AbstractUser from;

    @Column(name = "is_read")
    private boolean read;

    private LocalDateTime time;

    @Column(columnDefinition = "text")
    private String content;
    public Message() {
    }

    public Message(String type, AbstractUser from, boolean read, LocalDateTime time, String content) {
        this.type = type;
        this.from = from;
        this.read = read;
        this.time = time;
        this.content = content;
    }

    public Message(String type, AbstractUser toUser, AbstractUser from, boolean read, LocalDateTime time, String content) {
        this(type, from, read, time, content);
        this.toUser = toUser;
    }

    public Message(String type, Event toEvent, AbstractUser from, boolean read, LocalDateTime time, String content) {
        this(type, from, read, time, content);
        this.toEvent = toEvent;
    }

    public Message(String type, Post toPost, AbstractUser from, boolean read, LocalDateTime time, String content) {
        this(type, from, read, time, content);
        this.toPost = toPost;
    }

    public Message(String type, Comment toComment, AbstractUser from, boolean read, LocalDateTime time, String content) {
        this(type, from, read, time, content);
        this.toComment = toComment;
    }

}
