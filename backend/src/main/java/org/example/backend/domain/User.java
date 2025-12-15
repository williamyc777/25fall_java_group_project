package org.example.backend.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.util.List;

@Entity
@DiscriminatorValue(value = "User")
public class User extends AbstractUser {
    @OneToOne(mappedBy = "user")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Permission permission;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Event> favouriteEvents;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Post> favouritePosts;

    @OneToMany(mappedBy = "user")
    private List<EnrollForm> enrollForms;

    @ManyToMany(mappedBy = "participants")
    private List<AbstractEnrollment> enrollments;

    @ManyToMany
    @JoinTable(name = "score",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")})
    private List<Event> scoredEvents;

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public List<AbstractEnrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<AbstractEnrollment> enrollments) {
        this.enrollments = enrollments;
    }

    public List<EnrollForm> getEnrollForms() {
        return enrollForms;
    }

    public void setEnrollForms(List<EnrollForm> enrollForms) {
        this.enrollForms = enrollForms;
    }

    public List<Event> getScoredEvents() {
        return scoredEvents;
    }

    public void setScoredEvents(List<Event> scoredEvents) {
        this.scoredEvents = scoredEvents;
    }

    public List<Event> getFavouriteEvents() {
        return favouriteEvents;
    }

    public void setFavouriteEvents(List<Event> favouriteEvents) {
        this.favouriteEvents = favouriteEvents;
    }

    public List<Post> getFavouritePosts() {
        return favouritePosts;
    }

    public void setFavouritePosts(List<Post> favouritePosts) {
        this.favouritePosts = favouritePosts;
    }
}
