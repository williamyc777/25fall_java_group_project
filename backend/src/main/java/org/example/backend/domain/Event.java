package org.example.backend.domain;

import jakarta.persistence.*;
import org.example.backend.domain.enums.EventType;
import org.hibernate.annotations.Cascade;

import java.time.LocalDateTime;
import java.util.List;


@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User author;
    private EventType type;
    private String introduction;

    @Column(columnDefinition = "TEXT")
    private String text;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String posterUrl;
    private float score = 0;
    private long scoreCount = 0;
    @OneToMany(mappedBy = "event")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Post> posts;
    @OneToMany(mappedBy = "event")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<EventComment> comments;
    @ManyToMany(mappedBy = "favouriteEvents")
    private List<User> collectors;

    @OneToOne
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @JoinColumn(name = "abstractEnrollment_id")
    private AbstractEnrollment abstractEnrollment;

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public long getScoreCount() {
        return scoreCount;
    }

    public void setScoreCount(long scoreCount) {
        this.scoreCount = scoreCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }


    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public List<User> getCollectors() {
        return collectors;
    }

    public void setCollectors(List<User> collectors) {
        this.collectors = collectors;
    }

    public AbstractEnrollment getAbstractEnrollment() {
        return abstractEnrollment;
    }

    public void setAbstractEnrollment(AbstractEnrollment abstractEnrollment) {
        this.abstractEnrollment = abstractEnrollment;
    }
}
