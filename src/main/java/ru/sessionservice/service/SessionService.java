package ru.sessionservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sessionservice.dto.AuthResponse;
import ru.sessionservice.entity.GameSession;
import ru.sessionservice.repository.GameSessionRepository;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

import static ru.sessionservice.enums.Status.CANCELLED;
import static ru.sessionservice.enums.Status.WAITING_FOR_PLAYERS;

/**
 * Сервис для управления сессиями пользователей.
 */
@Service
public class SessionService {
    @Autowired
    private GameSessionRepository gameSessionRepository;

    @Autowired
    private WebClient webClient;

    /**
     * Создает новую сессию пользователя.
     */
    public GameSession createSession() {
        GameSession session = new GameSession();
        session.setPin(generatePinInGameSession());
        session.setStatusSession(WAITING_FOR_PLAYERS);
        session.setUserIdList(new HashSet<>());
        return gameSessionRepository.save(session);
    }

    /**
     * Получает информацию о сессии.
     */
    public Optional<GameSession> getSession(UUID sessionId) {
        return gameSessionRepository.findById(sessionId);
    }

    /**
     * Добавляем игрок в сессию.
     */
    public GameSession addUserInSession(String pin, String userId) {
        for (GameSession gameSession : gameSessionRepository.findAll()) {
            if (gameSession.getStatusSession().equals(WAITING_FOR_PLAYERS) && gameSession.getPin().equals(pin)) {
                if (gameSession.getUserIdList() == null) {
                    gameSession.setUserIdList(new HashSet<>());
                }
                gameSession.getUserIdList().add(UUID.fromString(userId));
                System.out.println(gameSessionRepository.save(gameSession));
                return gameSession;
            }
        }
        throw new NoSuchElementException("Session not found");
    }

    /**
     * Завершает сессию пользователя.
     */
    public void deleteSession(UUID sessionId) {
        GameSession gameSession = gameSessionRepository.findById(sessionId)
                .orElseThrow(() -> new NoSuchElementException("Сессия не найдена"));
        gameSession.setStatusSession(CANCELLED);
        gameSessionRepository.save(gameSession);
    }

    public String generatePinInGameSession() {
        Random random = new Random();
        String pin;
        do {
            pin = String.format("%04d", random.nextInt(10000));
        } while (gameSessionRepository.findGameSessionByPinAndStatusSession(pin, WAITING_FOR_PLAYERS).isPresent());
        return pin;
    }

    public void startSession(UUID sessionId) {
        System.out.println("!!"+sessionId);
//        GameSession session  = gameSessionRepository.findById(sessionId).get();
//        GameSessionRequest gameSessionRequest = new GameSessionRequest();
//        gameSessionRequest.setId(session.getSessionId());
//        System.out.println(session.getUserIdList());// Устанавливаем ID сессии
//        gameSessionRequest.setPlayers(session.getUserIdList()); // Устанавливаем ID сессии

        AuthResponse authResponse = authenticate("test", "root");
        String jwtToken = authResponse.getType() + " " + authResponse.getToken();
//        System.out.println(gameSessionRequest);
        webClient.post()
                .uri("http://localhost:8080/games") // URL микросервиса профиля
                .header("Authorization", jwtToken) // Добавляем JWT токен в заголовок
                .bodyValue(sessionId)
                .retrieve()
                .bodyToMono(Void.class)
                .block(); // Блокирующий вызов
    }

    public AuthResponse authenticate(String username, String password) {
        Map<String, String> authRequest = new HashMap<>();
        authRequest.put("username", username);
        authRequest.put("password", password);

        // Выполняем POST-запрос для получения токена
        return webClient.post()
                .uri("http://localhost:8080/auth/login") // Endpoint аутентификации
                .bodyValue(authRequest) // Передаем учетные данные в теле запроса
                .retrieve()
                .bodyToMono(AuthResponse.class)
                .block(); // Получаем синхронный ответ
    }
}
