package ru.sbrf.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.stereotype.Service;
import ru.sbrf.repository.CardRepository;

import static org.apache.commons.text.CharacterPredicates.DIGITS;

@Service
@RequiredArgsConstructor
public class CardGenerator {

    private final static int CARD_LENGTH = 16;

    private final CardRepository cardRepository;

    public String generateNewCardNumber() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('0', '9')
                .filteredBy(DIGITS)
                .build();

        boolean isBusyNumber = true;
        String cardNumber = "";
        while (isBusyNumber) {
            cardNumber = generator.generate(CARD_LENGTH);
            isBusyNumber = cardRepository.existsByCardNumber(cardNumber);
        }

        return cardNumber;
    }

}
