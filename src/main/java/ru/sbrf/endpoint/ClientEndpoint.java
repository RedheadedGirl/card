package ru.sbrf.endpoint;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.sbrf.dto.ClientInput;
import ru.sbrf.dto.ClientOutput;
import ru.sbrf.service.ClientService;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clients")
public class ClientEndpoint {

    private final ClientService clientService;

    /**
     * Получение списка клиентов
     * @return Список клиентов
     */
    @GetMapping
    public List<ClientOutput> getAll() {
        return clientService.getAll();
    }

    /**
     * Создает клиента
     * @param input данные для создания
     * @return Optional.of(id) если клиент с таким инн был создан ранее, empty в случае созданного с нуля
     */
    @PostMapping
    public Optional<Short> createClient(@Valid @RequestBody ClientInput input) {
        return clientService.createClient(input);
    }
}
