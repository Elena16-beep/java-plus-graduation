package ru.practicum.ewm.stats.analyzer.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.stats.analyzer.model.UserEventId;
import ru.practicum.ewm.stats.analyzer.model.UserInteraction;
import java.util.List;

public interface UserInteractionRepository extends JpaRepository<UserInteraction, UserEventId> {

    List<UserInteraction> findByIdUserIdOrderByTimestampDesc(Long userId, Pageable pageable);

    List<UserInteraction> findByIdUserId(Long userId);

    boolean existsByIdUserIdAndIdEventId(Long userId, Long eventId);

    @Query("""
            SELECT u.id.eventId as eventId, sum(u.weight) as weightSum
            FROM UserInteraction u
            WHERE u.id.eventId IN :eventIds
            GROUP BY u.id.eventId
            """)
    List<EventWeightSumProjection> sumWeightsByEventIds(@Param("eventIds") List<Long> eventIds);

    interface EventWeightSumProjection {
        Long getEventId();
        Double getWeightSum();
    }
}