package dev.ewd.mediashelf_spring.mediashelf_spring.dto.requestDto;

import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record TVSeriesRequestDTO(
        @NotBlank String title,
        String description,
        @NotNull @Min(1900) int startYear,
        @NotNull int endYear,
        @NotNull @Min(0) @Max(10) double rating,
        @NotEmpty List<String> genres,
        @NotNull @Min(1) int numberOfSeasons,
        @NotNull @Min(1) int episodesPerSeason,
        String creator,
        List<String> castOfActors,
        String network,
        boolean isOngoing,
        @NotNull MultipartFile posterUrl
) {}
