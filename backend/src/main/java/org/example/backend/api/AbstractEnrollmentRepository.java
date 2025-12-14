package org.example.backend.api;

import org.example.backend.domain.AbstractEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbstractEnrollmentRepository extends JpaRepository<AbstractEnrollment, Long> {
    public AbstractEnrollment findAbstractEnrollmentByEventIdAndParticipantsId(long eventId, long userIid);
}
