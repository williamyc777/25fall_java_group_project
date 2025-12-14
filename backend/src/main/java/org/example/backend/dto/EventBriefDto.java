package org.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.backend.domain.AbstractEnrollment;
import org.example.backend.domain.CountEnrollment;
import org.example.backend.domain.Event;
import org.example.backend.domain.FormEnrollment;
import org.example.backend.domain.enums.EventType;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventBriefDto {
    private String title;
    private long eventId;
    private String eventName;
    private String enrollmentType;
    private long authorId;
    private String authorName;
    private LocalDateTime applyStartTime;
    private LocalDateTime applyEndTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private float score;
    private String postUrl;
    private String introduction;

    public EventBriefDto(Event event) {
        this.title = event.getTitle();
        this.eventId = event.getId();
        this.eventName = event.getName();
        // 处理author可能为null的情况
        if (event.getAuthor() != null) {
            this.authorId = event.getAuthor().getId();
            this.authorName = event.getAuthor().getName();
        } else {
            this.authorId = 0;
            this.authorName = "Unknown";
        }
        // 处理abstractEnrollment可能为null的情况
        if (event.getAbstractEnrollment() != null) {
            this.applyStartTime = event.getAbstractEnrollment().getStartTime();
            this.applyEndTime = event.getAbstractEnrollment().getEndTime();
        }
        this.startTime = event.getStartTime();
        this.endTime = event.getEndTime();
        this.score = event.getScore();
        this.postUrl = event.getPosterUrl();
        this.introduction = event.getIntroduction();
        AbstractEnrollment enrollment = event.getAbstractEnrollment();
        if (enrollment instanceof CountEnrollment) {
            this.enrollmentType = "count";
        }
        if (enrollment instanceof FormEnrollment) {
            this.enrollmentType = "form";
        }
    }
}
