package ru.sessionservice.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import ru.sessionservice.entity.GameSession;
import ru.sessionservice.service.SessionService;

import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * Контроллер для управления сессиями пользователей.
 */
@RestController
@RequestMapping("/sessions")
public class SessionController {
    @Autowired
    private SessionService sessionService;

    /**
     * Создает новую сессию пользователя.
     */
    @PostMapping
    public GameSession createSession() {
        return sessionService.createSession();
    }

    @PostMapping("/start")
    public void startSession(@Valid @RequestBody GameSession gameSession){
        sessionService.startSession(gameSession);
    }

    /**
     * Получает информацию о сессии.
     */
    @GetMapping("/{sessionId}")
    public GameSession getSession(@PathVariable UUID sessionId) {
        return sessionService.getSession(sessionId)
                .orElseThrow(() -> new NoSuchElementException("Session not found"));
    }

    /**
     * Обновляет время последней активности сессии.
     */
    @PostMapping("/{pin}/addUser/{userId}")
    public GameSession refreshSession(@PathVariable String pin, @PathVariable String userId) {
        return sessionService.addUserInSession(pin, userId);
    }

    /**
     * Завершает сессию пользователя.
     */
    @DeleteMapping("/{sessionId}")
    public void deleteSession(@PathVariable UUID sessionId) {
        sessionService.deleteSession(sessionId);
    }
}
