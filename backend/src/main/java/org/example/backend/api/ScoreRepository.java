package org.example.backend.api;

import org.example.backend.domain.Score;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, Long>{
    // 可能存在多条记录，所以返回 List，调用方自行处理
    List<Score> findAllByUserIdAndEventId(long userId, long eventId);
}
