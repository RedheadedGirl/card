package ru.sbrf.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.sbrf.entity.CardEntity;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, Integer> {

    boolean existsByCardNumber(String cardNumber);

    // пусть у нас будет логика, по которой мы раз в день отправляем людям нотификации что их карты expired
    // но крон для демонстрации стоит каждую минуту
    @Query(value = "SELECT * FROM cards WHERE DATE(date_expired) = CURRENT_DATE", nativeQuery = true)
    List<CardEntity> findAllWhereDateExpiredToday();
}
