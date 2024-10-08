package ru.sessionservice.enums;

import lombok.Getter;

@Getter
public enum Status {
    WAITING_FOR_PLAYERS,   // Ожидание других игроков для начала игры
    IN_PROGRESS,           // Игра в процессе
    PAUSED,                // Игра на паузе
    COMPLETED,             // Игра завершена
    CANCELLED,             // Игра отменена
    ERROR                  // Произошла ошибка
}
