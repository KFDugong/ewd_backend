package dev.ewd.mediashelf_spring.mediashelf_spring.service;

import dev.ewd.mediashelf_spring.mediashelf_spring.dto.requestDto.MovieRequestDTO;
import dev.ewd.mediashelf_spring.mediashelf_spring.dto.updateDto.UpdateMovieDto;
import dev.ewd.mediashelf_spring.mediashelf_spring.model.Movie;
import dev.ewd.mediashelf_spring.mediashelf_spring.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private S3Service s3Service;

    public Page<Movie> getAllMovies(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        return movieRepository.findAll(pageable);
    }

    private Movie getMovieById(Long id){
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with ID " + id));
    }

    public List<Movie> findMoviesByTitle (String title) {
        List<Movie> movies =  movieRepository.findByTitleContainingIgnoreCase(title);
        if(movies.isEmpty()) {
            throw new RuntimeException("No movies found with title containing " + title);
        }
        return movies;
    }

    public Page<Movie> filterMoviesByGenres(List<String> genres, int page, int size){
        if(genres == null || genres.isEmpty())
            throw new IllegalArgumentException("Genres list cannot be null or empty.");
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        return movieRepository.findByGenres(genres, pageable);
    }

    public Movie createMovie(MovieRequestDTO dto, MultipartFile file) {
        if(movieRepository.findByTitle(dto.title()).isPresent())
            throw new RuntimeException("A movie with the title " + dto.title() + "is already in the database.");
        try {
            String posterUrl = s3Service.uploadPoster(file, "movies");
            Movie movie = new Movie(
                    null,
                    dto.title(),
                    dto.description(),
                    dto.releaseYear(),
                    dto.rating(),
                    dto.genres(),
                    dto.duration(),
                    dto.director(),
                    dto.castOfActors(),
                    dto.productionStudio(),
                    posterUrl,
                    null
            );
            return movieRepository.save(movie);
        } catch (Exception e) {
            throw new RuntimeException("Error while creating the movie: " + e.getMessage());
        }
    }

    public Movie updateMovie(Long id, UpdateMovieDto dto, MultipartFile file) {
        Movie existingMovie = getMovieById(id); // Check if it exists

        if (dto.title() != null && movieRepository.findByTitle(dto.title())
                .filter(movie -> !movie.getId().equals(id))
                .isPresent()) {
            throw new RuntimeException("A movie with the title '" + dto.title() + "' already exists.");
        }

        if (dto.title() != null) existingMovie.setTitle(dto.title());
        if (dto.description() != null) existingMovie.setDescription(dto.description());
        if (dto.releaseYear() != null) existingMovie.setReleaseYear(dto.releaseYear());
        if (dto.rating() != null) existingMovie.setRating(dto.rating());
        if (dto.genres() != null) existingMovie.setGenres(dto.genres());
        if (dto.duration() != null) existingMovie.setDuration(dto.duration());
        if (dto.director() != null) existingMovie.setDirector(dto.director());
        if (dto.castOfActors() != null) existingMovie.setCastOfActors(dto.castOfActors());
        if (dto.productionStudio() != null) existingMovie.setProductionStudio(dto.productionStudio());
        if (dto.posterUrl() != null) existingMovie.setPosterUrl(dto.posterUrl());

        if (file != null && !file.isEmpty()) {
            try {
                String  newPosterUrl = s3Service.uploadPoster(file, "movies");
                existingMovie.setPosterUrl(newPosterUrl);
            } catch (IOException e) {
                throw new RuntimeException("Error while uploading the new poster: ", e);
            }
        }
        return movieRepository.save(existingMovie);
    }

    public void deleteMovieById(Long id) {
        Movie movie = getMovieById(id);
        movieRepository.delete(movie);
    }

    public List<String> getAllGenres() {
        return movieRepository.findDistinctGenres();
    }
}
