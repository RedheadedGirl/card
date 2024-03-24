package ru.sbrf.dto;

import lombok.Value;

import java.time.LocalDate;

@Value
public class ClientOutput {
    private short id;
    private String fio;
    private LocalDate birthday;
    private String email;
    private String inn;
}
