package ru.sbrf.service.memory;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.sbrf.dto.ClientInput;
import ru.sbrf.dto.ClientOutput;
import ru.sbrf.entity.ClientEntity;
import ru.sbrf.exceptions.AlreadyInUseException;
import ru.sbrf.mapper.ClientMapper;
import ru.sbrf.repository.MemoryStorage;
import ru.sbrf.service.ClientService;

import java.util.List;
import java.util.Optional;

@Service
@Profile("map")
@RequiredArgsConstructor
public class ClientMemoryService implements ClientService {

    private final ClientMapper clientMapper;
    private final MemoryStorage memoryStorage;

    @Override
    public List<ClientOutput> getAll() {
        return clientMapper.toClientOutputList(memoryStorage.getAllClients());
    }

    @Override
    public Optional<Short> createClient(ClientInput input) {

        Optional<Short> existingClientId = memoryStorage.findByClientInn(input.getInn());
        if (existingClientId.isPresent()) { // если клиент был заведен ранее, вернем его id
            return existingClientId;
        }

        if (memoryStorage.existsByClientEmail(input.getEmail())) { // если кто-то уже использовал такой email, то exception
            throw new AlreadyInUseException("Email " + input.getEmail() + " is already in use");
        }

        ClientEntity newEntity = clientMapper.toNewEntity(input);
        Optional<Short> maxClientId = memoryStorage.findMaxClientId();
        if (maxClientId.isPresent()) {
            newEntity.setId((short) (maxClientId.get() + 1));
        } else {
            newEntity.setId((short) 1);
        }
        memoryStorage.createClient(newEntity);
        return Optional.empty();
    }
}
