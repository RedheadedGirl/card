package ru.sbrf.endpoint;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.sbrf.dto.CardOutput;
import ru.sbrf.service.CardService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cards")
public class CardEndpoint {

    private final CardService cardService;

    /**
     * Создает карту клиенту
     * @param clientId id клиента, которому создаем карту
     * @return номер созданной карты
     */
    @PostMapping("/new/{clientId}")
    public String createCard(@PathVariable int clientId) {
        return cardService.createCard(clientId);
    }

    /**
     * Закрывает карту клиенту
     * @param cardId id карты, которую необходимо закрыть
     */
    @PostMapping("/close/{cardId}")
    public void closeCard(@PathVariable int cardId) {
        cardService.closeCard(cardId);
    }

    /**
     * Получение списка карт
     * @return список карт
     */
    @GetMapping
    public List<CardOutput> getAll() {
        return cardService.getAll();
    }
}
