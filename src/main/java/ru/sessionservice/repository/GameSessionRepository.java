package ru.sessionservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sessionservice.entity.GameSession;
import ru.sessionservice.enums.Status;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с сессиями пользователей в Redis.
 */
@Repository
public interface GameSessionRepository extends CrudRepository<GameSession, UUID> {
    Optional<GameSession> findGameSessionByPinAndStatusSession(String pin, Status status);
    GameSession findGameSessionByPin(String pin);
}
