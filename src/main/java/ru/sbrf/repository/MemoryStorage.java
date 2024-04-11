package ru.sbrf.repository;

import org.springframework.stereotype.Repository;
import ru.sbrf.entity.CardEntity;
import ru.sbrf.entity.ClientEntity;
import ru.sbrf.enums.CardStatus;
import ru.sbrf.exceptions.NotFoundException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class MemoryStorage {

    private Set<ClientEntity> clients = new HashSet<>();
    private HashMap<ClientEntity, List<CardEntity>> map = new HashMap<>();

    public void createClient(ClientEntity client) {
        clients.add(client);
    }

    public ClientEntity findClientById(int idClient) {
        return clients.stream().filter(clientEntity -> clientEntity.getId() == idClient).findFirst()
                .orElseThrow(() -> new NotFoundException("Client with id = " + idClient + " not found"));
    }

    public Optional<Short> findByClientInn(String inn) {
        Optional<ClientEntity> optionalClient = clients.stream()
                .filter(clientEntity -> inn.equals(clientEntity.getInn())).findFirst();
        return optionalClient.map(ClientEntity::getId);
    }

    public Optional<Short> findMaxClientId() {
        Optional<ClientEntity> optionalClient = clients.stream()
                .max(Comparator.comparing(ClientEntity::getId));
        return optionalClient.map(ClientEntity::getId);
    }

    public boolean existsByClientEmail(String email) {
        return clients.stream()
                .anyMatch(clientEntity -> email.equals(clientEntity.getEmail()));
    }

    public List<ClientEntity> getAllClients() {
        return clients.stream().toList();
    }

    public void saveCard(ClientEntity client, CardEntity card) {
        map.computeIfPresent(client, (client1, cardEntities) -> {
            // a не будет ли тут проблем когда мы обновили статус карте и закидываем как обновленную? проверить!
            ArrayList<CardEntity> entities = new ArrayList<>(cardEntities);
            entities.add(card);
            return entities;
        });
        map.computeIfAbsent(client, k -> List.of(card));
    }

    public Optional<Short> findMaxCardId() {
        Optional<CardEntity> optionalCard = map.values().stream()
                .flatMap(Collection::stream)
                .max(Comparator.comparing(CardEntity::getId));
        return optionalCard.map(CardEntity::getId);
    }

    public CardEntity findCardById(int idCard) {
        return map.values().stream()
                .flatMap(Collection::stream)
                .filter(card -> card.getId() == idCard)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Card with id = " + idCard + " not found"));
    }

    public List<CardEntity> getAllCards() {
        return map.values().stream().flatMap(Collection::stream).toList();
    }

    public List<CardEntity> findCardsByDateExpired(LocalDate localDate) {
        return map.values().stream()
                .flatMap(Collection::stream)
                .filter(card -> localDate.equals(card.getDateExpired().toLocalDate())
                        && card.getStatus() == CardStatus.ACTIVE)
                .collect(Collectors.toList());
    }

}
