package dev.ewd.mediashelf_spring.mediashelf_spring.repository;

import dev.ewd.mediashelf_spring.mediashelf_spring.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByTitleContainingIgnoreCase(String title);
    Optional<Movie> findByTitle(String title);
    @NonNull
    Page<Movie> findAll(@NonNull Pageable pageable);

    @Query("SELECT m FROM Movie m JOIN m.genres g WHERE g in :genres")
    Page<Movie> findByGenres (@Param("genres") List<String> genres, Pageable pageable);

    @Query("SELECT DISTINCT g FROM Movie m JOIN m.genres g")
    List<String> findDistinctGenres();
}
