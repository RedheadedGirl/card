package ru.sbrf.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
@Table(name = "cards")
public class CardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private short id;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "date_granted")
    private LocalDateTime dateGranted;

    @Column(name = "date_expired")
    private LocalDateTime dateExpired;

    @ManyToOne
    @JoinColumn(name = "id_client", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ClientEntity clientEntity;
}
