# Работа 1. Разработка защищенного REST API с интеграцией в CI/CD

## Назначение
Мини-приложение на Java 17 / Spring Boot 3.5, демонстрирующее безопасную реализацию JWT‑аутентификации и базовую защиту от OWASP Top‑10 (A03: Injection, A07: Identification & Authentication Failures) с автоматическими проверками безопасности в CI.

## Стек и запуск локально
- Java 17, Spring Boot, Spring Security, Spring Data JPA, H2 (in‑memory)
- Безопасность: BCrypt для паролей, JWT (HS256), SpotBugs (SAST), OWASP Dependency‑Check (SCA)
- Запуск:  
  ```bash
  cd lab-1
  export JWT_SIGNING_KEY=<base64-ключ-32-байта>   # по умолчанию встроен тестовый
  ./mvnw spring-boot:run
  ```
  Приложение поднимается на `http://localhost:8080`, БД H2 в памяти.

## API
1) `POST /auth/sign-up` -- регистрация, возвращает JWT  
   тело: `{"username":"alice","password":"secret123"}`  
   ответы: `200 OK { "token": "<jwt>" }`, `409 CONFLICT` при дубликате, `400` при невалидных данных.
2) `POST /auth/login` -- вход, возвращает JWT.  
   тело аналогично sign-up, ошибки: `403` при неверных креденшлах, `400` при невалидных данных.
3) `GET /api/data` -- получить список треков, только с JWT.  
   заголовок: `Authorization: Bearer <jwt>`; ошибки: `401/403` без токена.
4) `POST /api/data` -- добавить трек (третья требуемая операция).  
   тело: `{"name":"Song","author":"Artist","numberOfPlays":0}`; валидация возвращает `400`.

### Быстрые примеры curl
```bash
TOKEN=$(curl -s -X POST http://localhost:8080/auth/sign-up \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","password":"Secret123"}' | jq -r .token)

curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/api/data

curl -X POST http://localhost:8080/api/data \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"name":"Song","author":"Artist","numberOfPlays":1}'
```

## Реализованные меры защиты
- **Broken Authentication**: пароль хэшируется BCrypt, JWT c 24h TTL, проверка токена в `JwtAuthFilter`, стейтлесс-сессии (SessionCreationPolicy.STATELESS).
- **SQL Injection**: используется Spring Data JPA (параметризованные запросы), вручную SQL не конкатенируется.
- **XSS**: входные строки экранируются в `TrackService` (`HtmlUtils.htmlEscape`), ответы -- JSON; заголовки `X-XSS-Protection: 1; mode=block` и `Content-Security-Policy: script-src 'self'`.
- **Валидация входных данных**: Bean Validation (`@NotBlank`, `@Size`, `@PositiveOrZero`) + централизованный `RestExceptionHandler`.
- **Секреты**: JWT ключ берётся из переменной окружения `JWT_SIGNING_KEY`; встроенный тестовый ключ можно заменить перед развёртыванием.
- **Актуальные версии зависимостей**: принудительно подняты Log4j до 2.25.3 и Tomcat до 10.1.47 через свойства `log4j2.version` и `tomcat.version` в `pom.xml` — закрыты последние CVE из отчёта Dependency-Check.

## CI/CD и отчёты
- Workflow: `.github/workflows/ci.yml` -- триггеры `push`/`pull_request`.
- Шаги: checkout -> setup-java 17 -> `./mvnw -B verify` (SpotBugs SAST + OWASP Dependency‑Check SCA).  
- Артефакты отчётов:  
  - `target/spotbugsXml.xml` (SAST)  
  - `target/dependency-check-report.html` (SCA)  
