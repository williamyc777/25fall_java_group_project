package org.example.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MessageDto {
    Long Id;
    String type;
    Long to;
    Long from;
    String toName;
    String fromName;
    Boolean read;
    String time;
    String content;

    public MessageDto() {
    }

}
