package ru.sbrf.service.db;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.sbrf.dto.ClientInput;
import ru.sbrf.dto.ClientOutput;
import ru.sbrf.entity.ClientEntity;
import ru.sbrf.exceptions.AlreadyInUseException;
import ru.sbrf.mapper.ClientMapper;
import ru.sbrf.repository.ClientRepository;
import ru.sbrf.service.ClientService;

import java.util.List;
import java.util.Optional;

@Service
@Profile("db")
@RequiredArgsConstructor
public class ClientDbService implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    public List<ClientOutput> getAll() {
        return clientMapper.toClientOutputList(clientRepository.findAll());
    }

    @Override
    public Optional<Short> createClient(ClientInput input) {
        Optional<ClientEntity> existingClient = clientRepository.findByInn(input.getInn());
        if (existingClient.isPresent()) { // если клиент был заведен ранее, вернем его id
            return Optional.of(existingClient.get().getId());
        }

        if (clientRepository.existsByEmail(input.getEmail())) { // если кто-то уже использовал такой email, то exception
            throw new AlreadyInUseException("Email " + input.getEmail() + " is already in use");
        }

        ClientEntity newEntity = clientMapper.toNewEntity(input);
        clientRepository.save(newEntity);
        return Optional.empty();
    }
}
