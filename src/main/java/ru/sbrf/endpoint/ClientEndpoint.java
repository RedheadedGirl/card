package ru.sbrf.endpoint;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
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
     * Получение списка клиентов
     * @return Список клиентов
     */
    @GetMapping(value = "/filter", params = {"page", "size"})
    public Page<ClientOutput> getAllPageable(
            @SortDefault(value = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return clientService.getAllPageable(pageable);
    }

    /**
     * Создает клиента
     * @param input данные для создания
     * @return id созданного клиента в случае успеха
     */
    @PostMapping
    public Optional<Short> createClient(@Valid @RequestBody ClientInput input) {
        return clientService.createClient(input);
    }
}
