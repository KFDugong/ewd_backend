package dev.ewd.mediashelf_spring.mediashelf_spring.repository;

import dev.ewd.mediashelf_spring.mediashelf_spring.model.TVSeries;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface TVSeriesRepository extends JpaRepository<TVSeries, Long> {
    List<TVSeries> findByTitleContainingIgnoreCase(String title);
    Optional<TVSeries> findByTitle(String title);
    @NonNull
    Page<TVSeries> findAll(@NonNull Pageable pageable);
    @Query("SELECT m FROM TVSeries m JOIN m.genres g WHERE g in :genres")
    Page<TVSeries> findByGenres(@Param("genres")List<String> genres, Pageable pageable);

    @Query("SELECT DISTINCT g FROM TVSeries tv JOIN tv.genres g")
    List<String> findDistinctGenres();
}
