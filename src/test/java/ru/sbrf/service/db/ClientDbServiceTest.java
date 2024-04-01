package ru.sbrf.service.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sbrf.dto.ClientInput;
import ru.sbrf.dto.ClientOutput;
import ru.sbrf.entity.ClientEntity;
import ru.sbrf.exceptions.AlreadyInUseException;
import ru.sbrf.mapper.ClientMapper;
import ru.sbrf.repository.ClientRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientDbServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    private ClientDbService clientService;

    @BeforeEach
    void setUp() {
        clientService = new ClientDbService(clientRepository, clientMapper);
    }

    @Test
    void givenPageable_whenGetAll_thenDo() {
        List<ClientEntity> clientEntities = List.of(new ClientEntity());
        List<ClientOutput> clientOutputs = List.of(new ClientOutput((short) 1, "fio", LocalDate.now(),
                "email", "inn"));
        when(clientRepository.findAll()).thenReturn(clientEntities);
        when(clientMapper.toClientOutputList(clientEntities)).thenReturn(clientOutputs);

        List<ClientOutput> all = clientService.getAll();

        assertEquals(1, all.size());
        assertEquals(clientOutputs.getFirst(), all.getFirst());
    }

    @Nested
    class CreateClient {

        @Test
        void givenInput_whenCreateClientAndClientExistsByInn_thenReturnId() {
            ClientEntity entity = createEntity();
            ClientInput input = createInput();
            when(clientRepository.findByInn(input.getInn())).thenReturn(Optional.of(entity));

            Optional<Short> client = clientService.createClient(input);
            assertEquals(entity.getId(), client.get());
        }

        @Test
        void givenInput_whenCreateClientAndExistsByEmail_thenThrowAlreadyInUse() {
            ClientInput input = createInput();
            when(clientRepository.findByInn(input.getInn())).thenReturn(Optional.empty());
            when(clientRepository.existsByEmail(input.getEmail())).thenReturn(true);

            AlreadyInUseException alreadyInUseException = assertThrows(AlreadyInUseException.class,
                    () -> clientService.createClient(input));
            assertEquals("Email email is already in use", alreadyInUseException.getMessage());
        }

        @Test
        void givenInput_whenCreateClient_thenReturn() {
            ClientEntity entity = createEntity();
            ClientInput input = createInput();
            when(clientRepository.findByInn(input.getInn())).thenReturn(Optional.empty());
            when(clientRepository.existsByEmail(input.getEmail())).thenReturn(false);
            when(clientMapper.toNewEntity(input)).thenReturn(entity);

            Optional<Short> client = clientService.createClient(input);

            verify(clientRepository).save(entity);
            assertTrue(client.isEmpty());
        }

        private ClientInput createInput() {
            return new ClientInput("fio", LocalDate.now(), "email", "inn");
        }

        private ClientEntity createEntity() {
            ClientEntity clientEntity = new ClientEntity();
            clientEntity.setId((short) 20);
            return clientEntity;
        }

    }
}