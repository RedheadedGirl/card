package ru.sbrf.service;

import ru.sbrf.dto.ClientInput;
import ru.sbrf.dto.ClientOutput;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    List<ClientOutput> getAll();

    Optional<Short> createClient(ClientInput input);
}
