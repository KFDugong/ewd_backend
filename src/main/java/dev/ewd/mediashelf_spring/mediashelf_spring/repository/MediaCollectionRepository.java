package dev.ewd.mediashelf_spring.mediashelf_spring.repository;

import dev.ewd.mediashelf_spring.mediashelf_spring.model.MediaCollection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MediaCollectionRepository extends JpaRepository<MediaCollection, Long> {
    List<MediaCollection> findByOwnerId(Long id);
    MediaCollection findByNameAndOwnerId(String name, Long id);
}
