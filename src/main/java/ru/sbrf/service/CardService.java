package ru.sbrf.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sbrf.dto.CardOutput;
import ru.sbrf.entity.CardEntity;
import ru.sbrf.entity.ClientEntity;
import ru.sbrf.enums.CardStatus;
import ru.sbrf.exceptions.NotFoundException;
import ru.sbrf.mapper.CardMapper;
import ru.sbrf.repository.CardRepository;
import ru.sbrf.repository.ClientRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardGenerator cardGenerator;
    private final ClientRepository clientRepository;
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    public String createCard(int idClient) {
        ClientEntity clientEntity = clientRepository.findById(idClient)
                .orElseThrow(() -> new NotFoundException("Client with id = " + idClient + " not found"));
        String cardNumber = cardGenerator.generateNewCardNumber();
        CardEntity cardEntity = new CardEntity();
        cardEntity.setCardNumber(cardNumber);
        cardEntity.setClientEntity(clientEntity);
        cardEntity.setDateGranted(LocalDateTime.now());
        cardEntity.setDateExpired(LocalDateTime.now().plusDays(2L));
        cardEntity.setStatus(CardStatus.ACTIVE);
        cardRepository.save(cardEntity);
        return cardNumber;
    }

    public void closeCard(int idCard) {
        CardEntity cardEntity = cardRepository.findById(idCard)
                .orElseThrow(() -> new NotFoundException("Card with id = " + idCard + " not found"));
        cardEntity.setStatus(CardStatus.CLOSED);
        cardRepository.save(cardEntity);
    }

    public List<CardOutput> getAll() {
        List<CardEntity> all = cardRepository.findAll();
        return cardMapper.toCardOutputList(all);
    }

}
