package org.example.backend.service;

import org.example.backend.domain.AbstractEnrollment;

public interface AbstractEnrollmentService {
    boolean saveAbstractEnrollment(AbstractEnrollment abstractEnrollment);

    boolean deleteAbstractEnrollmentById(long id);

    boolean updateAbstractEnrollment(AbstractEnrollment abstractEnrollment);

    AbstractEnrollment findAbstractEnrollmentById(long id);
}
