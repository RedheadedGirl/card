package ru.sbrf.service.memory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sbrf.dto.ClientInput;
import ru.sbrf.dto.ClientOutput;
import ru.sbrf.entity.ClientEntity;
import ru.sbrf.exceptions.AlreadyInUseException;
import ru.sbrf.mapper.ClientMapper;
import ru.sbrf.repository.MemoryStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientMemoryServiceTest {

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private MemoryStorage memoryStorage;

    private ClientMemoryService clientMemoryService;

    @BeforeEach
    void setUp() {
        clientMemoryService = new ClientMemoryService(clientMapper, memoryStorage);
    }

    @Test
    void givenClients_whenGetAll_thenReturnList() {
        List<ClientEntity> clients = List.of(new ClientEntity());
        when(memoryStorage.getAllClients()).thenReturn(clients);
        ClientOutput output = new ClientOutput((short) 1, "fio", LocalDate.now(), "email", "inn");
        List<ClientOutput> clientOutputs = List.of(output);
        when(clientMapper.toClientOutputList(clients)).thenReturn(clientOutputs);

        assertEquals(clientOutputs, clientMemoryService.getAll());
    }

    @Test
    void givenInputWithExistingInn_whenCreateClient_thenReturnClientId() {
        ClientInput input = new ClientInput("fio", LocalDate.now(), "email", "inn");
        when(memoryStorage.findByClientInn(input.getInn())).thenReturn(Optional.of((short) 3));

        assertEquals((short) 3, clientMemoryService.createClient(input).get());
    }

    @Test
    void givenInputWithExistingEmail_whenCreateClient_thenThrow() {
        ClientInput input = new ClientInput("fio", LocalDate.now(), "email", "inn");
        when(memoryStorage.findByClientInn(input.getInn())).thenReturn(Optional.empty());
        when(memoryStorage.existsByClientEmail(input.getEmail())).thenReturn(true);

        AlreadyInUseException alreadyInUseException = assertThrows(AlreadyInUseException.class,
                () -> clientMemoryService.createClient(input));
        assertEquals("Email email is already in use", alreadyInUseException.getMessage());
    }

    @Test
    void givenInputAndNoMaxId_whenCreateClient_thenDoWithFirstId() {
        ClientInput input = new ClientInput("fio", LocalDate.now(), "email", "inn");
        when(memoryStorage.findByClientInn(input.getInn())).thenReturn(Optional.empty());
        when(memoryStorage.existsByClientEmail(input.getEmail())).thenReturn(false);
        ClientEntity clientEntity = new ClientEntity();
        when(clientMapper.toNewEntity(input)).thenReturn(clientEntity);
        when(memoryStorage.findMaxClientId()).thenReturn(Optional.empty());

        clientMemoryService.createClient(input);

        assertEquals(1, clientEntity.getId());
        verify(memoryStorage).createClient(clientEntity);
    }

    @Test
    void givenInputAndMaxIdExists_whenCreateClient_thenDo() {
        ClientInput input = new ClientInput("fio", LocalDate.now(), "email", "inn");
        when(memoryStorage.findByClientInn(input.getInn())).thenReturn(Optional.empty());
        when(memoryStorage.existsByClientEmail(input.getEmail())).thenReturn(false);
        ClientEntity clientEntity = new ClientEntity();
        when(clientMapper.toNewEntity(input)).thenReturn(clientEntity);
        when(memoryStorage.findMaxClientId()).thenReturn(Optional.of((short) 4));

        clientMemoryService.createClient(input);

        assertEquals(5, clientEntity.getId());
        verify(memoryStorage).createClient(clientEntity);
    }
}