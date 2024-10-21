package ru.sessionservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
public class GameSessionRequest {
    private UUID id;
    private Set<UUID> players;
}
