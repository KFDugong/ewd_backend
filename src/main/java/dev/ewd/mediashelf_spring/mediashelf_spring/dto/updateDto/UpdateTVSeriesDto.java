package dev.ewd.mediashelf_spring.mediashelf_spring.dto.updateDto;

import jakarta.validation.constraints.*;

import java.util.List;

public record UpdateTVSeriesDto(
        @NotBlank String title,
        String description,
        @NotNull @Min(1900) Integer startYear,
        @NotNull Integer endYear,
        @NotNull @Min(0) @Max(10) Double rating,
        @NotEmpty List<String> genres,
        @NotNull @Min(1) Integer numberOfSeasons,
        @NotNull @Min(1) Integer episodesPerSeason,
        String creator,
        List<String> castOfActors,
        String network,
        Boolean isOngoing,
        String posterUrl
) {}