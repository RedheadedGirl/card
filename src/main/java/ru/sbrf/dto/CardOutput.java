package ru.sbrf.dto;

import lombok.Value;
import ru.sbrf.enums.CardStatus;

import java.time.LocalDateTime;

@Value
public class CardOutput {
    private short id;
    private String cardNumber;
    private LocalDateTime dateGranted;
    private LocalDateTime dateExpired;
    private short clientId;
    private CardStatus status;
}
