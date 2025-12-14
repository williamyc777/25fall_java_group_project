package org.example.backend.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.util.List;

@Setter
@Getter
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String postTitle;

    @Column(columnDefinition = "TEXT")
    private String postContent;

    @ManyToOne
    private Event event;

    @ManyToOne
    private AbstractUser user;

    @ManyToMany
    private List<AbstractUser> likeUsers;

    private int postCollectAmount;

    @OneToMany(mappedBy = "post")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<EventComment> comments;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Post) {
            return this.id == ((Post) obj).getId();
        } else {
            return false;
        }
    }
}
