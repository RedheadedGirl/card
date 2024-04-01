package ru.sbrf.service;

import ru.sbrf.dto.CardOutput;
import ru.sbrf.entity.CardEntity;

import java.util.List;

public interface CardService {

    String createCard(int idClient);

    void closeCard(int idCard);

    List<CardOutput> getAll();

    List<CardEntity> findAllWhereDateExpiredToday();
}
