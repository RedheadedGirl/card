package ru.sbrf.service.memory;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.sbrf.dto.CardOutput;
import ru.sbrf.entity.CardEntity;
import ru.sbrf.entity.ClientEntity;
import ru.sbrf.enums.CardStatus;
import ru.sbrf.mapper.CardMapper;
import ru.sbrf.repository.MemoryStorage;
import ru.sbrf.service.CardGenerator;
import ru.sbrf.service.CardService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Profile("map")
@RequiredArgsConstructor
public class CardMemoryService implements CardService {

    private final CardGenerator cardGenerator;
    private final CardMapper cardMapper;
    private final MemoryStorage memoryStorage;

    @Override
    public String createCard(int idClient) {
        ClientEntity clientEntity = memoryStorage.findClientById(idClient);
        String cardNumber = cardGenerator.generateNewCardNumber();
        CardEntity cardEntity = new CardEntity();
        cardEntity.setCardNumber(cardNumber);
        cardEntity.setClientEntity(clientEntity);
        cardEntity.setDateGranted(LocalDateTime.now());
        cardEntity.setDateExpired(LocalDateTime.now().plusDays(2L));
        cardEntity.setStatus(CardStatus.ACTIVE);
        Optional<Short> maxCardId = memoryStorage.findMaxCardId();
        if (maxCardId.isPresent()) {
            cardEntity.setId((short) (maxCardId.get() + 1));
        } else {
            cardEntity.setId((short) 1);
        }
        memoryStorage.saveCard(clientEntity, cardEntity);
        return cardNumber;
    }

    @Override
    public void closeCard(int idCard) {
        CardEntity cardEntity = memoryStorage.findCardById(idCard);
        cardEntity.setStatus(CardStatus.CLOSED);
    }

    @Override
    public List<CardOutput> getAll() {
        List<CardEntity> all = memoryStorage.getAllCards();
        return cardMapper.toCardOutputList(all);
    }

    @Override
    public List<CardEntity> findAllWhereDateExpiredToday() {
        return memoryStorage.findCardsByDateExpired(LocalDate.now());
    }

}
