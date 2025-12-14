package org.example.backend.api;

import org.example.backend.domain.EnrollForm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollFormRepository extends JpaRepository<EnrollForm, Long> {
    List<EnrollForm> findEnrollFormByUserIdAndFormEnrollment_Event_Id(long userId, long eventId);
    List<EnrollForm> findByFormEnrollment(org.example.backend.domain.FormEnrollment formEnrollment);
}
