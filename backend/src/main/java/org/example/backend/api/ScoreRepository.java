package org.example.backend.api;

import org.example.backend.domain.Score;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScoreRepository extends JpaRepository<Score, Long>{
    public Score findScoreByUserIdAndEventId(long userId, long eventId);
}
