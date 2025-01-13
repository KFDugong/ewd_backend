package dev.ewd.mediashelf_spring.mediashelf_spring.dto.createDto;

import dev.ewd.mediashelf_spring.mediashelf_spring.model.enums.MediaCollectionType;

public record CreateMediaCollectionDto(
        String name,
        String description,
        MediaCollectionType type
) {}