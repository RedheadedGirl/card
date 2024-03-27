package ru.sbrf.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.sbrf.dto.CardOutput;
import ru.sbrf.entity.CardEntity;
import ru.sbrf.entity.ClientEntity;
import ru.sbrf.enums.CardStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CardMapperTest {

    private CardMapper cardMapper;

    @BeforeEach
    void setUp() {
        cardMapper = new CardMapper();
    }

    @Test
    void givenCardEntitiesList_whenMapToCardOutputList_thenDo() {
        LocalDateTime dateTime = LocalDateTime.now();

        ClientEntity client = new ClientEntity();
        client.setFio("ABC");
        CardEntity card1 = new CardEntity();
        card1.setCardNumber("1234");
        card1.setClientEntity(client);
        card1.setDateGranted(dateTime);
        card1.setDateExpired(dateTime.plusDays(2));
        card1.setStatus(CardStatus.ACTIVE);

        CardEntity card2 = new CardEntity();
        card2.setCardNumber("5678");
        card2.setClientEntity(client);
        card2.setDateGranted(dateTime.plusDays(1));
        card2.setDateExpired(dateTime.plusDays(3));
        card2.setStatus(CardStatus.CLOSED);

        List<CardOutput> cardOutputList = cardMapper.toCardOutputList(List.of(card1, card2));

        assertThat(cardOutputList.getFirst())
                .extracting(CardOutput::getCardNumber, CardOutput::getStatus, CardOutput::getClientId,
                        CardOutput::getDateGranted, CardOutput::getDateExpired)
                .contains(card1.getCardNumber(), card1.getStatus(), card1.getClientEntity().getId(),
                        card1.getDateGranted(), card1.getDateExpired());
        assertThat(cardOutputList.getLast())
                .extracting(CardOutput::getCardNumber, CardOutput::getStatus, CardOutput::getClientId,
                        CardOutput::getDateGranted, CardOutput::getDateExpired)
                .contains(card2.getCardNumber(), card2.getStatus(), card2.getClientEntity().getId(),
                        card2.getDateGranted(), card2.getDateExpired());

    }
}