# java-explore-with-me

**Explore With Me** (англ. «исследуй со мной») — приложение, которое позволит пользователям делиться информацией об интересных событиях и находить компанию для участия в них.

Проект состоит из модулей:

**event-service** - управление событиями, категориями, подборками

**user-service** - управление пользователями

**request-service** - управление заявками на участие

**comment-service** - управление комментариями

**stats-service** - сбор и анализ статистики просмотров событий

**discovery-service** - Eureka для регистрации и обнаружения сервисов

**config-service** - централизованное хранилище конфигураций (Spring Cloud Config)

**gateway-service** - единая точка входа (Spring Cloud Gateway, порт 8080)


## Конфигурация
Конфигурация сервисов находится в:
- `infra/config-server/src/main/resources/config`

Файлы:
- `comment-service.properties`
- `event-service.properties`
- `user-service.properties`
- `request-service.properties`
- `gateway-server.properties`
- `stats-server.properties`

## Внутреннее API
Межсервисное взаимодействие:
- `user-service` -> `event-service` (Feign)
- `request-service` -> `event-service` (Feign)
- `comment-service` -> `event-service` (Feign)
- `event-service` -> `stats-server` (stat-client)

## Внешнее API
Спецификации:
- `ewm-main-service-spec.json`
- `ewm-stats-service-spec.json`
