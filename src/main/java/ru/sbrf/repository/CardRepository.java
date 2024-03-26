package ru.sbrf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sbrf.entity.CardEntity;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, Integer> {

    boolean existsByCardNumber(String cardNumber);
}
