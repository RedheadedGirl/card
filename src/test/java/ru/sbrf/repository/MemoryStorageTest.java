package ru.sbrf.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.sbrf.entity.CardEntity;
import ru.sbrf.entity.ClientEntity;
import ru.sbrf.exceptions.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MemoryStorageTest {

    private MemoryStorage memoryStorage;

    @BeforeEach
    void setUp() {
        memoryStorage = new MemoryStorage();
    }

    @Test
    void givenClients_whenFindClientById_thenReturn() {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId((short) 11);
        memoryStorage.createClient(clientEntity);

        ClientEntity clientById = memoryStorage.findClientById(11);
        assertEquals(clientEntity, clientById);
    }

    @Test
    void givenNoClients_whenFindClientById_thenThrow() {
        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> memoryStorage.findClientById(11));
        assertEquals("Client with id = 11 not found", notFoundException.getMessage());
    }

    @Test
    void givenClients_whenFindByClientInn_thenReturn() {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId((short) 8);
        clientEntity.setInn("inn");
        memoryStorage.createClient(clientEntity);

        Optional<Short> clientById = memoryStorage.findByClientInn("inn");
        assertEquals((short) 8, clientById.get());
    }

    @Test
    void givenNoClients_whenFindByClientInn_thenReturn() {
        Optional<Short> clientById = memoryStorage.findByClientInn("inn");
        assertTrue(clientById.isEmpty());
    }

    @Test
    void givenClients_whenFindMaxClientId_thenReturn() {
        ClientEntity client1 = new ClientEntity();
        client1.setId((short) 8);
        memoryStorage.createClient(client1);
        ClientEntity client2 = new ClientEntity();
        client2.setId((short) 9);
        memoryStorage.createClient(client2);

        Optional<Short> clientById = memoryStorage.findMaxClientId();
        assertEquals((short) 9, clientById.get());
    }

    @Test
    void givenClients_whenExistsByClientEmail_thenReturnTrue() {
        ClientEntity client = new ClientEntity();
        client.setEmail("email");
        memoryStorage.createClient(client);

        assertTrue(memoryStorage.existsByClientEmail("email"));
    }

    @Test
    void givenNoClients_whenExistsByClientEmail_thenReturnFalse() {
        assertFalse(memoryStorage.existsByClientEmail("email"));
    }

    @Test
    void givenClients_whenGetAllClients_thenReturn() {
        ClientEntity client = new ClientEntity();
        memoryStorage.createClient(client);

        List<ClientEntity> allClients = memoryStorage.getAllClients();
        assertEquals(1, allClients.size());
        assertEquals(client, allClients.getFirst());
    }

    @Test
    void givenClient_whenSaveCard_thenReturn() {
        ClientEntity client = new ClientEntity();
        memoryStorage.createClient(client);

        CardEntity cardEntity = new CardEntity();
        memoryStorage.saveCard(client, cardEntity);

        assertEquals(cardEntity, memoryStorage.getAllCards().getFirst());
    }

    @Test
    void givenCards_whenFindMaxCardId_thenReturn() {
        ClientEntity client = new ClientEntity();
        memoryStorage.createClient(client);
        CardEntity cardEntity1 = new CardEntity();
        cardEntity1.setId((short) 7);
        memoryStorage.saveCard(client, cardEntity1);
        CardEntity cardEntity2 = new CardEntity();
        cardEntity2.setId((short) 8);
        memoryStorage.saveCard(client, cardEntity2);

        assertEquals(cardEntity2.getId(), memoryStorage.findMaxCardId().get());
    }

    @Test
    void givenCards_whenFindCardById_thenReturn() {
        ClientEntity client = new ClientEntity();
        memoryStorage.createClient(client);
        CardEntity card = new CardEntity();
        card.setId((short) 8);
        memoryStorage.saveCard(client, card);

        assertEquals(card, memoryStorage.findCardById(8));
    }

    @Test
    void givenNoCards_whenFindCardById_thenThrow() {
        NotFoundException notFoundException = assertThrows(NotFoundException.class,
                () -> memoryStorage.findCardById(8));
        assertEquals("Card with id = 8 not found", notFoundException.getMessage());
    }

    @Test
    void givenCards_whenGetAllCards_thenReturn() {
        ClientEntity client = new ClientEntity();
        memoryStorage.createClient(client);
        CardEntity cardEntity1 = new CardEntity();
        cardEntity1.setId((short) 7);
        memoryStorage.saveCard(client, cardEntity1);
        CardEntity cardEntity2 = new CardEntity();
        cardEntity2.setId((short) 8);
        memoryStorage.saveCard(client, cardEntity2);

        List<CardEntity> allCards = memoryStorage.getAllCards();
        assertEquals(2, allCards.size());
    }

    @Test
    void givenCards_whenFindCardsByDateExpired_thenReturn() {
        ClientEntity client = new ClientEntity();
        memoryStorage.createClient(client);
        CardEntity cardEntity1 = new CardEntity();
        cardEntity1.setId((short) 7);
        cardEntity1.setDateExpired(LocalDateTime.of(2022, 2, 2, 2, 2));
        memoryStorage.saveCard(client, cardEntity1);
        CardEntity cardEntity2 = new CardEntity();
        cardEntity2.setId((short) 8);
        cardEntity2.setDateExpired(LocalDateTime.of(2023, 2, 2, 2, 2));
        memoryStorage.saveCard(client, cardEntity2);

        List<CardEntity> cardsByDateExpired = memoryStorage.findCardsByDateExpired(LocalDate.of(2023, 2, 2));
        assertEquals(1, cardsByDateExpired.size());
        assertEquals(8, cardsByDateExpired.getFirst().getId());
    }
}