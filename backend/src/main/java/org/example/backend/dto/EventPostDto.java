package org.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.backend.domain.CountEnrollment;
import org.example.backend.domain.Event;
import org.example.backend.domain.FormEnrollment;

import java.util.List;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventPostDto {
    private Long id; // 用于更新时传递 eventId，创建时可以为 null
    private String title;
    private String name;
    private String enrollmentType;
    private String applyStartTime;
    private String applyEndTime;
    private String startTime;
    private String endTime;
    private String imageUrl;
    private String introduction;
    private String mdText;
    private long limitCount;
    private List<DefinedFormDto> definedForm;

    public EventPostDto(Event event) {
        this.id = event.getId();
        this.title = event.getTitle();
        this.name = event.getName();
        this.applyStartTime = event.getAbstractEnrollment().getStartTime().toString().replace("T", " ");
        this.applyEndTime = event.getAbstractEnrollment().getEndTime().toString().replace("T", " ");
        this.startTime = event.getStartTime().toString().replace("T", " ");
        this.endTime = event.getEndTime().toString().replace("T", " ");
        this.imageUrl = event.getPosterUrl();
        this.introduction = event.getIntroduction();
        this.mdText = event.getText();
        if (event.getAbstractEnrollment() instanceof FormEnrollment) {
            this.enrollmentType = "form";
            this.definedForm = ((FormEnrollment) event.getAbstractEnrollment()).getDefinedFormEntries().stream().map(DefinedFormDto::new).toList();
        } else {
            this.enrollmentType = "count";
            this.limitCount = ((CountEnrollment) event.getAbstractEnrollment()).getCapacity();
        }

    }
}
