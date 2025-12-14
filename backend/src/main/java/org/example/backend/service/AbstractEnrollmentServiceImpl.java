package org.example.backend.service;

import jakarta.persistence.EntityManager;
import org.example.backend.api.AbstractEnrollmentRepository;
import org.example.backend.domain.AbstractEnrollment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AbstractEnrollmentServiceImpl implements AbstractEnrollmentService {
    @Autowired
    AbstractEnrollmentRepository abstractEnrollmentRepository;
    
    @Autowired
    private EntityManager entityManager;

    @Override
    public boolean saveAbstractEnrollment(AbstractEnrollment abstractEnrollment) {
        abstractEnrollmentRepository.save(abstractEnrollment);
        return true;
    }

    @Override
    public boolean deleteAbstractEnrollmentById(long id) {
        abstractEnrollmentRepository.deleteById(id);
        return true;
    }

    @Override
    @Transactional
    public boolean updateAbstractEnrollment(AbstractEnrollment abstractEnrollment) {
        abstractEnrollmentRepository.save(abstractEnrollment);
        // 立即刷新到数据库，确保数据已持久化
        entityManager.flush();
        // 清除EntityManager缓存，确保下次查询时获取最新数据
        entityManager.clear();
        System.out.println("updateAbstractEnrollment - Enrollment ID: " + abstractEnrollment.getId() + " updated");
        return true;
    }

    @Override
    public AbstractEnrollment findAbstractEnrollmentById(long id) {
        return abstractEnrollmentRepository.findById(id).orElse(null);
    }
}
