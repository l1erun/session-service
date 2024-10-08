package ru.sessionservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sessionservice.entity.GameSession;
import ru.sessionservice.repository.GameSessionRepository;

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
        for (GameSession gameSession : gameSessionRepository.findAll()){
            if (gameSession.getStatusSession().equals(WAITING_FOR_PLAYERS) && gameSession.getPin().equals(pin)){
                if (gameSession.getUserIdList() == null){gameSession.setUserIdList(new HashSet<>());}
                gameSession.getUserIdList().add(UUID.fromString(userId));
                gameSessionRepository.save(gameSession);
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
                        .orElseThrow(()-> new NoSuchElementException("Сессия не найдена"));
        gameSession.setStatusSession(CANCELLED);
        gameSessionRepository.save(gameSession);
    }

    public String generatePinInGameSession(){
        Random random = new Random();
        String pin;
        do {
            pin = String.format("%04d", random.nextInt(10000));
        } while (gameSessionRepository.findGameSessionByPinAndStatusSession(pin, WAITING_FOR_PLAYERS).isPresent());
        return pin;
    }
}
