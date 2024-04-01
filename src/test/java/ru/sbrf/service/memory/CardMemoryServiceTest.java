package ru.sbrf.service.memory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sbrf.dto.CardOutput;
import ru.sbrf.entity.CardEntity;
import ru.sbrf.entity.ClientEntity;
import ru.sbrf.enums.CardStatus;
import ru.sbrf.exceptions.NotFoundException;
import ru.sbrf.mapper.CardMapper;
import ru.sbrf.repository.MemoryStorage;
import ru.sbrf.service.CardGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardMemoryServiceTest {

    @Mock
    private CardGenerator cardGenerator;

    @Mock
    private CardMapper cardMapper;

    @Mock
    private MemoryStorage memoryStorage;

    private CardMemoryService cardMemoryService;

    @BeforeEach
    void setUp() {
        cardMemoryService = new CardMemoryService(cardGenerator, cardMapper, memoryStorage);
    }

    @Test
    void givenNoClient_whenCreateCard_thenThrow() {
        doThrow(new NotFoundException("")).when(memoryStorage).findClientById(1);

        assertThrows(NotFoundException.class, () -> cardMemoryService.createCard(1));
    }

    @Test
    void givenClient_whenCreateCardAndNoMaxId_thenReturn() {
        ClientEntity clientEntity = new ClientEntity();
        when(memoryStorage.findClientById(1)).thenReturn(clientEntity);
        String cardNumber = "1234567812345678";
        when(cardGenerator.generateNewCardNumber()).thenReturn(cardNumber);
        when(memoryStorage.findMaxCardId()).thenReturn(Optional.empty());

        cardMemoryService.createCard(1);

        ArgumentCaptor<CardEntity> captor = ArgumentCaptor.forClass(CardEntity.class);
        verify(memoryStorage).saveCard(eq(clientEntity), captor.capture());
        CardEntity card = captor.getValue();
        assertEquals(cardNumber, card.getCardNumber());
        assertEquals(1, card.getId());
        assertEquals(CardStatus.ACTIVE, card.getStatus());
        assertEquals(card.getDateExpired(), card.getDateGranted().plusDays(2L));
    }

    @Test
    void givenClient_whenCreateCardAndMaxId_thenReturn() {
        ClientEntity clientEntity = new ClientEntity();
        when(memoryStorage.findClientById(1)).thenReturn(clientEntity);
        String cardNumber = "1234567812345678";
        when(cardGenerator.generateNewCardNumber()).thenReturn(cardNumber);
        when(memoryStorage.findMaxCardId()).thenReturn(Optional.of((short) 7));

        cardMemoryService.createCard(1);

        ArgumentCaptor<CardEntity> captor = ArgumentCaptor.forClass(CardEntity.class);
        verify(memoryStorage).saveCard(eq(clientEntity), captor.capture());
        CardEntity card = captor.getValue();
        assertEquals(cardNumber, card.getCardNumber());
        assertEquals(8, card.getId());
        assertEquals(CardStatus.ACTIVE, card.getStatus());
        assertEquals(card.getDateExpired(), card.getDateGranted().plusDays(2L));
    }

    @Test
    void givenCardId_whenCloseCard_thenDo() {
        int idCard = 5;
        when(memoryStorage.findCardById(idCard)).thenReturn(new CardEntity());

        cardMemoryService.closeCard(idCard);
    }

    @Test
    void givenCards_whenGetAll_thenReturnList() {
        CardEntity cardEntity = new CardEntity();
        when(memoryStorage.getAllCards()).thenReturn(List.of(cardEntity));
        CardOutput cardOutput = new CardOutput((short) 1, "1234", LocalDateTime.now(), LocalDateTime.now(),
                (short) 1, CardStatus.ACTIVE);
        when(cardMapper.toCardOutputList(List.of(cardEntity))).thenReturn(List.of(cardOutput));

        List<CardOutput> all = cardMemoryService.getAll();
        assertEquals(List.of(cardOutput), all);
    }

    @Test
    void givenCards_whenFindAllWhereDateExpiredToday_thenReturnList() {
        cardMemoryService.findAllWhereDateExpiredToday();
        verify(memoryStorage).findCardsByDateExpired(any(LocalDate.class));
    }
}