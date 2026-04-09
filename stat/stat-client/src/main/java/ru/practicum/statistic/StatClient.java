package ru.practicum.statistic;

//import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.client.ServiceInstance;
//import org.springframework.cloud.client.discovery.DiscoveryClient;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.http.MediaType;
//import org.springframework.retry.backoff.FixedBackOffPolicy;
//import org.springframework.retry.policy.MaxAttemptsRetryPolicy;
//import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestClient;
//import org.springframework.web.server.ResponseStatusException;
//import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.ewm.stats.proto.ActionTypeProto;
import ru.practicum.ewm.stats.proto.RecommendedEventProto;
//import java.net.URI;
import java.time.Instant;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Deprecated
@Component
@RequiredArgsConstructor
public class StatClient {

//    private static final DateTimeFormatter FORMATTER =
//            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//
//    private final RestClient restClient;
//    private final String appName;
//    private final DiscoveryClient discoveryClient;
//    private final RetryTemplate retryTemplate;
//    private final String statsServiceId;
    private final CollectorClient collectorClient;
    private final AnalyzerClient analyzerClient;

    public void sendUserAction(long userId, long eventId, ActionTypeProto actionType, Instant timestamp) {
        collectorClient.sendUserAction(userId, eventId, actionType, timestamp);
    }

    public void sendUserAction(long userId, long eventId, ActionTypeProto actionType) {
        collectorClient.sendUserAction(userId, eventId, actionType);
    }

    public Stream<RecommendedEventProto> getRecommendationsForUser(long userId, int maxResults) {
        return analyzerClient.getRecommendationsForUser(userId, maxResults);
    }

    public Stream<RecommendedEventProto> getSimilarEvents(long eventId, long userId, int maxResults) {
        return analyzerClient.getSimilarEvents(eventId, userId, maxResults);
    }

    public Stream<RecommendedEventProto> getInteractionsCount(List<Long> eventIds) {
        return analyzerClient.getInteractionsCount(eventIds);
    }

//    public void hit(HttpServletRequest request) {
//        EndpointHitDto dto = EndpointHitDto.builder()
//                .app(appName)
//                .uri(request.getRequestURI())
//                .ip(extractClientIp(request))
//                .timestamp(LocalDateTime.now().format(FORMATTER))
//                .build();
//
//        try {
//            restClient.post()
//                    .uri(makeUri("/hit"))
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .body(dto)
//                    .retrieve()
//                    .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
//                        throw new ResponseStatusException(
//                                res.getStatusCode(),
//                                res.getBody().toString()
//                        );
//                    })
//                    .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
//                        throw new ResponseStatusException(
//                                res.getStatusCode(),
//                                res.getBody().toString()
//                        );
//                    })
//                    .toBodilessEntity();
//        } catch (Exception e) {
//            log.warn("Не удалось отправить hit в сервис статистики", e);
//        }
//    }
//
//    public List<ViewStatsDto> getStatistics(LocalDateTime start,
//                                            LocalDateTime end,
//                                            List<String> uris,
//                                            Boolean unique) {
//
//        String startStr = start.format(FORMATTER);
//        String endStr = end.format(FORMATTER);
//
//        try {
//            URI uri = buildStatsUri(startStr, endStr, uris, unique);
//
//            return restClient.get()
//                    .uri(uri)
//                    .retrieve()
//                    .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
//                        throw new ResponseStatusException(
//                                res.getStatusCode(),
//                                res.getBody().toString()
//                        );
//                    })
//                    .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
//                        throw new ResponseStatusException(
//                                res.getStatusCode(),
//                                res.getBody().toString()
//                        );
//                    })
//                    .body(new ParameterizedTypeReference<>() {});
//        } catch (Exception e) {
//            log.warn("Не удалось получить статистику из сервиса статистики", e);
//            return Collections.emptyList();
//        }
//    }
//
//    private RetryTemplate buildRetryTemplate() {
//        RetryTemplate template = new RetryTemplate();
//
//        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
//        backOffPolicy.setBackOffPeriod(3000L);
//        template.setBackOffPolicy(backOffPolicy);
//
//        MaxAttemptsRetryPolicy retryPolicy = new MaxAttemptsRetryPolicy();
//        retryPolicy.setMaxAttempts(3);
//        template.setRetryPolicy(retryPolicy);
//
//        return template;
//    }
//
//    private ServiceInstance getInstance() {
//        try {
//            return discoveryClient
//                    .getInstances(statsServiceId)
//                    .getFirst();
//        } catch (Exception e) {
//            throw new StatsServerUnavailable(
//                    "Ошибка обнаружения адреса сервиса статистики с id: " + statsServiceId,
//                    e
//            );
//        }
//    }
//
//    private URI buildStatsUri(String start, String end, List<String> uris, Boolean unique) {
//        UriComponentsBuilder builder = UriComponentsBuilder
//                .fromUri(makeUri("/stats"))
//                .queryParam("start", start)
//                .queryParam("end", end)
//                .queryParam("unique", unique);
//
//        if (uris != null && !uris.isEmpty()) {
//            for (String uri : uris) {
//                builder.queryParam("uris", uri);
//            }
//        }
//
//        return builder.build().encode().toUri();
//    }
//
//    private URI makeUri(String path) {
//        ServiceInstance instance = retryTemplate.execute(context -> getInstance());
//
//        return instance.getUri().resolve(path);
//    }
//
//    private String extractClientIp(HttpServletRequest request) {
//        String xForwardedFor = request.getHeader("X-Forwarded-For");
//        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
//            return xForwardedFor.split(",")[0].trim();
//        }
//        return request.getRemoteAddr();
//    }
}
