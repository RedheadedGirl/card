package ru.sbrf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.sbrf.entity.CardEntity;
import ru.sbrf.entity.ClientEntity;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduledSender {

    private final CardService cardService;
    private final EmailService emailService;

    @Scheduled(cron = "${cron.notify}")
    public void notifyClientsCardExpired() {
        List<CardEntity> allWhereDateExpiredToday = cardService.findAllWhereDateExpiredToday();
        allWhereDateExpiredToday.forEach(this::generateAndNotifyClient);
    }

    private void generateAndNotifyClient(CardEntity card) {
        ClientEntity client = card.getClientEntity();
        cardService.closeCard(card.getId());
        String oldCardNumber = card.getCardNumber();
        String newCardNumber = cardService.createCard(client.getId());
        String email = client.getEmail();
        String message = String.format("Уважаемый %s! Срок действия карты %s истекает сегодня. " +
                "Вам назначена новая карта %s", client.getFio(), oldCardNumber, newCardNumber);
        emailService.sendSimpleMessage(email, "Замена карты", message);
    }

}
