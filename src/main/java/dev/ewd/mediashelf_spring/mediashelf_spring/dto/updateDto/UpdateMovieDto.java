package dev.ewd.mediashelf_spring.mediashelf_spring.dto.updateDto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UpdateMovieDto(
        @NotBlank(message = "Title cannot be empty") String title,
        String description,
        @Min(1900) Integer releaseYear,
        @Min(0) @Max(10) Double rating,
        List<String> genres,
        @Min(1) Integer duration,
        String director,
        List<String> castOfActors,
        String productionStudio,
        String posterUrl
) {
}
