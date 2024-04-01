package ru.sbrf.service.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sbrf.entity.CardEntity;
import ru.sbrf.entity.ClientEntity;
import ru.sbrf.enums.CardStatus;
import ru.sbrf.exceptions.NotFoundException;
import ru.sbrf.mapper.CardMapper;
import ru.sbrf.repository.CardRepository;
import ru.sbrf.repository.ClientRepository;
import ru.sbrf.service.CardGenerator;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardDbServiceTest {

    @Mock
    private CardGenerator cardGenerator;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardMapper cardMapper;

    private CardDbService cardDbService;

    @BeforeEach
    void setUp() {
        cardDbService = new CardDbService(cardGenerator, clientRepository, cardRepository, cardMapper);
    }

    @Nested
    class CreateCard {

        int idClient = 5;
        String cardNumber = "1234567812345678";

        @Test
        void givenIdClient_whenFind_thenThrowNotFound() {
            when(clientRepository.findById(idClient)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> cardDbService.createCard(idClient));
        }

        @Test
        void givenIdClient_whenCreate_thenDo() {
            ClientEntity clientEntity = new ClientEntity();
            clientEntity.setFio("Иванов");

            when(clientRepository.findById(idClient)).thenReturn(Optional.of(clientEntity));
            when(cardGenerator.generateNewCardNumber()).thenReturn(cardNumber);
            String card = cardDbService.createCard(idClient);

            ArgumentCaptor<CardEntity> captor = ArgumentCaptor.forClass(CardEntity.class);
            verify(cardRepository).save(captor.capture());
            CardEntity actual = captor.getValue();
            assertEquals(card, actual.getCardNumber());
            assertEquals(cardNumber, actual.getCardNumber());
            assertEquals(clientEntity.getFio(), actual.getClientEntity().getFio());
            assertEquals(CardStatus.ACTIVE, actual.getStatus());
        }
    }

    @Nested
    class CloseCard {

        int idCard = 7;

        @Test
        void givenCardId_whenCloseCard_thenThrowNotFound() {
            when(cardRepository.findById(idCard)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> cardDbService.closeCard(idCard));
        }

        @Test
        void givenCardId_whenCloseCard_thenDo() {
            when(cardRepository.findById(idCard)).thenReturn(Optional.of(new CardEntity()));

            cardDbService.closeCard(idCard);

            ArgumentCaptor<CardEntity> captor = ArgumentCaptor.forClass(CardEntity.class);
            verify(cardRepository).save(captor.capture());
            CardEntity actual = captor.getValue();
            assertEquals(CardStatus.CLOSED, actual.getStatus());
        }
    }

    @Nested
    class GetAll {

        @Test
        void whenGetAll_thenFindAndMapListOfCards() {
            CardEntity card1 = new CardEntity();
            card1.setId((short) 1);
            CardEntity card2 = new CardEntity();
            card2.setId((short) 2);
            List<CardEntity> cards = List.of(card1, card2);

            when(cardRepository.findAll()).thenReturn(cards);

            cardDbService.getAll();
            verify(cardMapper).toCardOutputList(cards);
        }
    }

}
