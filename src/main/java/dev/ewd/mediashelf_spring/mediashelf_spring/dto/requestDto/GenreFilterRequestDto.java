package dev.ewd.mediashelf_spring.mediashelf_spring.dto.requestDto;

import java.util.List;

public class GenreFilterRequestDto {

    private List<String> genres;

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
}
