package ru.sbrf.mapper;

import org.springframework.stereotype.Service;
import ru.sbrf.dto.ClientInput;
import ru.sbrf.dto.ClientOutput;
import ru.sbrf.entity.ClientEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientMapper {

    public List<ClientOutput> toClientOutputList(List<ClientEntity> clients) {
        return clients.stream().map(this::toClientOutput).collect(Collectors.toList());
    }

    public ClientOutput toClientOutput(ClientEntity clientEntity) {
        return new ClientOutput(clientEntity.getId(), clientEntity.getFio(), clientEntity.getBirthday(),
                clientEntity.getEmail(), clientEntity.getInn());
    }

    public ClientEntity toNewEntity(ClientInput input) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setFio(input.getFio());
        clientEntity.setBirthday(input.getBirthday());
        clientEntity.setInn(input.getInn());
        clientEntity.setEmail(input.getEmail());
        return clientEntity;
    }
}
