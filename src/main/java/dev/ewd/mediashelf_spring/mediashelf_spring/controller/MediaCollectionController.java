package dev.ewd.mediashelf_spring.mediashelf_spring.controller;

import dev.ewd.mediashelf_spring.mediashelf_spring.dto.createDto.CreateMediaCollectionDto;
import dev.ewd.mediashelf_spring.mediashelf_spring.dto.requestDto.AddMovieToCollectionRequestDto;
import dev.ewd.mediashelf_spring.mediashelf_spring.dto.requestDto.AddTVSeriesToCollectionRequestDto;
import dev.ewd.mediashelf_spring.mediashelf_spring.dto.requestDto.EditCollectionDescriptionRequestDto;
import dev.ewd.mediashelf_spring.mediashelf_spring.model.MediaCollection;
import dev.ewd.mediashelf_spring.mediashelf_spring.service.MediaCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collections")
public class MediaCollectionController {

    @Autowired
    private MediaCollectionService mediaCollectionService;

    @PostMapping
    public ResponseEntity<?> createMediaCollection(@RequestBody CreateMediaCollectionDto dto) {
        try {
            MediaCollection createdCollection = mediaCollectionService.createMediaCollection(dto.name(), dto.description(), dto.type(), null);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCollection);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error happened: " + e);
        }
    }

    @GetMapping
    public ResponseEntity<?> getCollectionsByOwner() {
        List<MediaCollection> collections = mediaCollectionService.getAllCollectionsByOwner();
        if (collections.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No media collections found.");
        }
        return ResponseEntity.ok(collections);
    }

    @PostMapping("/{collectionName}/movies")
    public ResponseEntity<?> addMovieToCollection(@PathVariable String collectionName, @RequestBody AddMovieToCollectionRequestDto dto) {
        try {
            MediaCollection updatedCollection = mediaCollectionService.addMovieToCollection(collectionName, dto.movieId());
            return ResponseEntity.ok(updatedCollection);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PostMapping("/{collectionName}/tvseries")
    public ResponseEntity<?> addTVSeriesToCollection(@PathVariable String collectionName, @RequestBody AddTVSeriesToCollectionRequestDto dto) {
        try {
            MediaCollection updatedCollection = mediaCollectionService.addTVSeriesToCollection(collectionName, dto.tvSeriesId());
            return ResponseEntity.ok(updatedCollection);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{collectionName}/movies/{movieId}")
    public ResponseEntity<?> removeMovieFromCollection(@PathVariable String collectionName, @PathVariable Long movieId) {
        try {
            MediaCollection updatedCollection = mediaCollectionService.removeMovieFromCollection(collectionName, movieId);
            return ResponseEntity.ok(updatedCollection);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{collectionName}/tvseries/{tvSeriesId}")
    public ResponseEntity<?> removeTVSeriesFromCollection(@PathVariable String collectionName, @PathVariable Long tvSeriesId) {
        try {
            MediaCollection updatedCollection = mediaCollectionService.removeTVSeriesFromCollection(collectionName, tvSeriesId);
            return ResponseEntity.ok(updatedCollection);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @DeleteMapping("/{collectionName}")
    public ResponseEntity<?> deleteCollection(@PathVariable String collectionName) {
        try {
            mediaCollectionService.deleteCollection(collectionName);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PutMapping("/{collectionName}/description")
    public ResponseEntity<?> editDescription(@PathVariable String collectionName, @RequestBody EditCollectionDescriptionRequestDto dto) {
        try {
            MediaCollection updatedCollection = mediaCollectionService.editDescription(collectionName, dto.description());
            return ResponseEntity.ok(updatedCollection);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    @PutMapping("/{collectionName}/flush")
    public ResponseEntity<?> flushCollection(@PathVariable String collectionName) {
        try {
            MediaCollection flushedCollection = mediaCollectionService.flushCollection(collectionName);
            return ResponseEntity.ok(flushedCollection);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
}
