package ru.practicum.ewm.stats.analyzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.stats.analyzer.model.EventPairId;
import ru.practicum.ewm.stats.analyzer.model.EventSimilarity;
import java.util.List;

public interface EventSimilarityRepository extends JpaRepository<EventSimilarity, EventPairId> {

    @Query("""
            SELECT s
            FROM EventSimilarity s
            WHERE s.id.eventA = :eventId OR s.id.eventB = :eventId
            """)
    List<EventSimilarity> findAllByEvent(@Param("eventId") Long eventId);

    @Query("""
            SELECT s
            FROM EventSimilarity s
            WHERE s.id.eventA IN :eventIds OR s.id.eventB IN :eventIds
            """)
    List<EventSimilarity> findAllByEventsIn(@Param("eventIds") List<Long> eventIds);

    @Query("""
            SELECT s
            FROM EventSimilarity s
            WHERE (s.id.eventA = :eventId AND s.id.eventB IN :otherIds)
               OR (s.id.eventB = :eventId AND s.id.eventA IN :otherIds)
            """)
    List<EventSimilarity> findAllForEventAndOthers(@Param("eventId") Long eventId,
                                                   @Param("otherIds") List<Long> otherIds);
}