package dev.ewd.mediashelf_spring.mediashelf_spring.service;

import dev.ewd.mediashelf_spring.mediashelf_spring.exception.ResourceNotFoundException;
import dev.ewd.mediashelf_spring.mediashelf_spring.model.MediaCollection;
import dev.ewd.mediashelf_spring.mediashelf_spring.model.Movie;
import dev.ewd.mediashelf_spring.mediashelf_spring.model.TVSeries;
import dev.ewd.mediashelf_spring.mediashelf_spring.model.enums.MediaCollectionType;
import dev.ewd.mediashelf_spring.mediashelf_spring.repository.MediaCollectionRepository;
import dev.ewd.mediashelf_spring.mediashelf_spring.repository.MovieRepository;
import dev.ewd.mediashelf_spring.mediashelf_spring.repository.TVSeriesRepository;
import dev.ewd.mediashelf_spring.mediashelf_spring.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MediaCollectionService {

    @Autowired
    private MediaCollectionRepository mediaCollectionRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TVSeriesRepository tvSeriesRepository;

    public MediaCollection createMediaCollection(String name, String description, MediaCollectionType type, Long ownerId) {
        if(ownerId == null) {
            ownerId = AuthUtil.getCurrentUserId();
        }
        MediaCollection existingCollection = mediaCollectionRepository.findByNameAndOwnerId(name, ownerId);
        if (existingCollection != null) {
            throw new IllegalArgumentException("A collection with the name '" + name + "' already exists.");
        }
        if (ownerId == null && (type == MediaCollectionType.WATCHED || type == MediaCollectionType.TO_WATCH)) {
            throw new RuntimeException("Default collections cannot be created manually.");
        }
        MediaCollection collection = new MediaCollection();
        collection.setName(name);
        collection.setDescription(description);
        collection.setOwnerId(ownerId);
        collection.setType(type);
        return mediaCollectionRepository.save(collection);
    }

    public List<MediaCollection> getAllCollectionsByOwner() {
        Long ownerId = AuthUtil.getCurrentUserId();
        List<MediaCollection> collections = mediaCollectionRepository.findByOwnerId(ownerId);
        if (collections.isEmpty()) {
            throw new ResourceNotFoundException("No media collections found for owner ID: " + ownerId);
        }
        return collections;
    }

    public MediaCollection addMovieToCollection(String collectionName, Long movieId) {
        Long ownerId = AuthUtil.getCurrentUserId();
        if (movieId == null) {
            throw new IllegalArgumentException("Movie cannot be null.");
        }
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + movieId));
        MediaCollection collection = mediaCollectionRepository.findByNameAndOwnerId(collectionName, ownerId);
        boolean alreadyInCollection = collection.getMovies().stream()
                        .anyMatch(existingMovies -> existingMovies.getId().equals(movieId));
        if (alreadyInCollection)
            throw new IllegalArgumentException("Movie with the ID " + movieId + "is already in the collection.");
        collection.getMovies().add(movie);
        return mediaCollectionRepository.save(collection);
    }

    public MediaCollection addTVSeriesToCollection(String collectionName, Long tvSeriesId) {
        Long ownerId = AuthUtil.getCurrentUserId();
        if (tvSeriesId == null) {
            throw new IllegalArgumentException("TVSeries cannot be null.");
        }
        TVSeries tvSerie = tvSeriesRepository.findById(tvSeriesId)
                .orElseThrow(() -> new ResourceNotFoundException("TVSeries not found with id: " + tvSeriesId));
        MediaCollection collection = mediaCollectionRepository.findByNameAndOwnerId(collectionName, ownerId);

        boolean alreadyInCollection = collection.getTvSeries().stream()
                .anyMatch(existingTVSeries -> existingTVSeries.getId().equals(tvSeriesId));
        if (alreadyInCollection) {
            throw new IllegalStateException("TVSeries with ID " + tvSeriesId + " is already in the collection.");
        }
        collection.getTvSeries().add(tvSerie);
        return mediaCollectionRepository.save(collection);
    }

    public MediaCollection removeMovieFromCollection(String collectionName , Long movieId) {
        Long ownerId = AuthUtil.getCurrentUserId();
        MediaCollection collection = mediaCollectionRepository.findByNameAndOwnerId(collectionName, ownerId);

        boolean removed = collection.getMovies().removeIf(movie -> movie.getId().equals(movieId));
        if (!removed) {
            throw new ResourceNotFoundException("Movie with ID " + movieId + " not found in collection.");
        }

        return mediaCollectionRepository.save(collection);
    }

    public MediaCollection removeTVSeriesFromCollection(String collectionName, Long tvSeriesId) {
        Long ownerId = AuthUtil.getCurrentUserId();
        MediaCollection collection = mediaCollectionRepository.findByNameAndOwnerId(collectionName, ownerId);

        boolean removed = collection.getTvSeries().removeIf(tvSeries -> tvSeries.getId().equals(tvSeriesId));
        if (!removed) {
            throw new ResourceNotFoundException("TVSeries with ID " + tvSeriesId + " not found in collection.");
        }

        return mediaCollectionRepository.save(collection);
    }

    public void deleteCollection(String name) {
        Long ownerId = AuthUtil.getCurrentUserId();
        System.out.println(ownerId);
        System.out.println(name);
        MediaCollection collection = mediaCollectionRepository.findByNameAndOwnerId(name, ownerId);
        System.out.println(collection);
        if (collection.getType() == MediaCollectionType.WATCHED || collection.getType() == MediaCollectionType.TO_WATCH) {
            throw new RuntimeException("Default collections cannot be deleted.");
        }
        mediaCollectionRepository.delete(collection);
    }

    public MediaCollection getMediaCollectionById(Long id) {
        return mediaCollectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MediaCollection not found with ID: " + id));
    }

    public MediaCollection editDescription(String collectionName, String description) {
        if (description == null) {
            throw new IllegalArgumentException("Description cannot be null or empty.");
        }
        Long ownerId = AuthUtil.getCurrentUserId();
        MediaCollection collection = mediaCollectionRepository.findByNameAndOwnerId(collectionName, ownerId);
        collection.setDescription(description);
        return mediaCollectionRepository.save(collection);
    }

    public MediaCollection flushCollection(String collectionName) {
        Long ownerId = AuthUtil.getCurrentUserId();
        MediaCollection collection = mediaCollectionRepository.findByNameAndOwnerId(collectionName, ownerId);
        collection.getMovies().clear();
        collection.getTvSeries().clear();
        return mediaCollectionRepository.save(collection);
    }
}
