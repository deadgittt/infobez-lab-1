package ru.atmo.lab1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class TrackDto {
    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "author is required")
    private String author;

    @NotNull(message = "numberOfPlays is required")
    @PositiveOrZero(message = "numberOfPlays must be >= 0")
    private Integer numberOfPlays;
}
