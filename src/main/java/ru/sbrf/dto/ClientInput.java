package ru.sbrf.dto;

import lombok.Value;

import java.time.LocalDate;

@Value
public class ClientInput { // расставить валидацию
    private short id;
    private String fio;
    private LocalDate birthday;
    private String email;
    private String inn;
}
