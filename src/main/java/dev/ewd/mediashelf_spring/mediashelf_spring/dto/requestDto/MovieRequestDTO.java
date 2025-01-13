package dev.ewd.mediashelf_spring.mediashelf_spring.dto.requestDto;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record MovieRequestDTO(
        @NotBlank String title,
        String description,
        @NotNull @Min(1900) int releaseYear,
        @NotNull @Min(0) @Max(10) double rating,
        @NotEmpty List<String> genres,
        @NotNull @Min(1) int duration,
        String director,
        List<String> castOfActors,
        String productionStudio,
        @NotNull MultipartFile posterUrl
) {
}
