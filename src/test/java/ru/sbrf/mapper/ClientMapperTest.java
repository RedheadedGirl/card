package ru.sbrf.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.sbrf.dto.ClientInput;
import ru.sbrf.dto.ClientOutput;
import ru.sbrf.entity.ClientEntity;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientMapperTest {

    private ClientMapper clientMapper;

    @BeforeEach
    void setUp() {
        clientMapper = new ClientMapper();
    }

    @Test
    void givenEntities_whenMapToOutputList_thenDo() {
        ClientEntity clientEntity = createEntity();

        List<ClientOutput> outputs = clientMapper.toClientOutputList(List.of(clientEntity));

        assertEquals(1, outputs.size());
        assertThat(outputs.getFirst())
                .extracting(ClientOutput::getId, ClientOutput::getFio, ClientOutput::getBirthday,
                        ClientOutput::getEmail, ClientOutput::getInn)
                .contains(clientEntity.getId(), clientEntity.getFio(), clientEntity.getBirthday(),
                        clientEntity.getEmail(), clientEntity.getInn());
    }

    @Test
    void givenEntity_whenMapToOutput_thenDo() {
        ClientEntity clientEntity = createEntity();

        ClientOutput output = clientMapper.toClientOutput(clientEntity);
        assertThat(output)
                .extracting(ClientOutput::getId, ClientOutput::getFio, ClientOutput::getBirthday,
                        ClientOutput::getEmail, ClientOutput::getInn)
                .contains(clientEntity.getId(), clientEntity.getFio(), clientEntity.getBirthday(),
                        clientEntity.getEmail(), clientEntity.getInn());
    }

    private ClientEntity createEntity() {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId((short) 123);
        clientEntity.setBirthday(LocalDate.now());
        clientEntity.setFio("fio");
        clientEntity.setEmail("email");
        clientEntity.setInn("inn");
        return clientEntity;
    }

    @Test
    void givenInput_whenMapToNewEntity_thenDo() {
        ClientInput input = new ClientInput("fio", LocalDate.now(), "email", "inn");
        ClientEntity entity = clientMapper.toNewEntity(input);
        assertThat(entity)
                .extracting(ClientEntity::getFio, ClientEntity::getBirthday, ClientEntity::getEmail,
                        ClientEntity::getInn)
                .contains(input.getFio(), input.getBirthday(), input.getEmail(), input.getInn());
    }
}