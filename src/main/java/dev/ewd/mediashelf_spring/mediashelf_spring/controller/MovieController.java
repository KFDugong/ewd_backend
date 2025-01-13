package dev.ewd.mediashelf_spring.mediashelf_spring.controller;

import dev.ewd.mediashelf_spring.mediashelf_spring.dto.requestDto.GenreFilterRequestDto;
import dev.ewd.mediashelf_spring.mediashelf_spring.dto.requestDto.MovieRequestDTO;
import dev.ewd.mediashelf_spring.mediashelf_spring.dto.updateDto.UpdateMovieDto;
import dev.ewd.mediashelf_spring.mediashelf_spring.model.Movie;
import dev.ewd.mediashelf_spring.mediashelf_spring.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping
    public ResponseEntity<?> getAllMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            return ResponseEntity.ok(movieService.getAllMovies(page, size));
        } catch (RuntimeException e){
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Movie>> searchMoviesByTitle(@RequestParam String title) {
        try {
            List<Movie> movies = movieService.findMoviesByTitle(title);
            return ResponseEntity.ok(movies);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(List.of());
        }
    }

    @PostMapping("/filter")
    public ResponseEntity<Page<Movie>> filterMoviesByGenres(
            @RequestBody GenreFilterRequestDto dto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            List<String> genres = dto.getGenres();
            Page<Movie> movies = movieService.filterMoviesByGenres(genres, page, size);
            return ResponseEntity.ok(movies);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Page.empty());
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(Page.empty());
        }
    }

    @PostMapping
    public ResponseEntity<?> addMovie(
            @RequestPart("movieDto") MovieRequestDTO movieDto,
            @RequestParam("file")MultipartFile file
    ) {
        try{
            Movie movie = movieService.createMovie(movieDto, file);
            return ResponseEntity.status(201).body(movie);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMovie(
            @PathVariable Long id,
            @RequestPart("movieDto") UpdateMovieDto dto,
            @RequestParam(value="file", required = false) MultipartFile file)
    {
        try {
            Movie updateMovie = movieService.updateMovie(id, dto, file);
            return ResponseEntity.ok(updateMovie);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMove(@PathVariable Long id) {
        movieService.deleteMovieById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/genres")
    public ResponseEntity<List<String>> getAllGenres() {
        try {
            List<String> genres = movieService.getAllGenres();
            return ResponseEntity.ok(genres);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(List.of());
        }
    }
}
