package ru.sbrf.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.time.LocalDate;

@Value
public class ClientInput {

    @NotBlank
    private String fio;

    @NotNull
    private LocalDate birthday;

    @NotBlank
    private String email;

    @NotBlank
    private String inn;
}
