package org.example.backend.service;

import org.example.backend.domain.EnrollForm;
import org.example.backend.domain.Event;

import java.util.List;

public interface EventService {
    boolean saveEvent(Event event);

    boolean deleteEventById(long id);

    boolean updateEvent(Event event);

    Event findEventById(long id);

    List<Event> findAllEvents();

    List<Event> findEventByAuthorId(long authorId);

    boolean saveEnrollForm(EnrollForm enrollForm);

    long getScore(long userId, long eventId);

    boolean saveScore(long userId, long eventId, long score);

    boolean deleteEvent(long eventId);

    boolean appliedByUser(long userId, long eventId);

    List<Event> searchEvent(String keyword);
}
