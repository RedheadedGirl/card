package ru.sbrf.mapper;

import org.springframework.stereotype.Service;
import ru.sbrf.dto.CardOutput;
import ru.sbrf.entity.CardEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardMapper {

    public List<CardOutput> toCardOutputList(List<CardEntity> cardEntityList) {
        return cardEntityList.stream().map(this::toClientOutput).collect(Collectors.toList());
    }

    private CardOutput toClientOutput(CardEntity entity) {
        return new CardOutput(entity.getId(), entity.getCardNumber(), entity.getDateGranted(),
                entity.getDateExpired(), entity.getClientEntity().getId(), entity.getStatus());
    }

}
