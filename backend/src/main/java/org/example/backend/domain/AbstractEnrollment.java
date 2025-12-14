package org.example.backend.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;


@Entity
public class AbstractEnrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne(mappedBy = "abstractEnrollment")
    private Event event;
    @ManyToMany
    @JoinTable(name = "enrollment_user",
            joinColumns = {@JoinColumn(name = "enrollment_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private List<User> participants;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
