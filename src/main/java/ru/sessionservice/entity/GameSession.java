package ru.sessionservice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import ru.sessionservice.enums.Status;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

/**
 * Сессия пользователя. Хранится в Redis.
 */
@Data
@RedisHash("GameSession")
public class GameSession implements Serializable {
    @Id
    private UUID sessionId; // Идентификатор сессии

    private Set<UUID> userIdList; // Идентификатор пользователя

    private String pin;

    private Status statusSession;

//    private long lastActive; // Время последней активности (timestamp)

//    private UUID gameSessionId; // Идентификатор игровой сессии (если есть)
}
