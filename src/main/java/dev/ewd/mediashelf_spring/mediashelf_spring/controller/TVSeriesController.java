package dev.ewd.mediashelf_spring.mediashelf_spring.controller;

import dev.ewd.mediashelf_spring.mediashelf_spring.dto.requestDto.GenreFilterRequestDto;
import dev.ewd.mediashelf_spring.mediashelf_spring.dto.requestDto.TVSeriesRequestDTO;
import dev.ewd.mediashelf_spring.mediashelf_spring.dto.updateDto.UpdateTVSeriesDto;
import dev.ewd.mediashelf_spring.mediashelf_spring.model.TVSeries;
import dev.ewd.mediashelf_spring.mediashelf_spring.service.TVSeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/tvseries")
public class TVSeriesController {

    @Autowired
    private TVSeriesService tvSeriesService;

    @GetMapping
    public ResponseEntity<?> getAllTVSeries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        try {
            return ResponseEntity.ok(tvSeriesService.getAllTVSeries(page, size));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<TVSeries>> searchTVSeriesByTitle(@RequestParam String title) {
        try {
            List<TVSeries> tvSeries = tvSeriesService.findTVSeriesByTitle(title);
            return ResponseEntity.ok(tvSeries);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(List.of());
        }
    }

    @PostMapping("/filter")
    public ResponseEntity<?> filterTVSeriesByGenres(
            @RequestBody GenreFilterRequestDto dto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            List<String> genres = dto.getGenres();
            Page<TVSeries> tvSeries = tvSeriesService.filterTVSeriesByGenres(genres, page, size);
            return ResponseEntity.ok(tvSeries);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Page.empty());
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(Page.empty());
        }
    }

    @PostMapping
    public ResponseEntity<?> addTVSeries (
            @RequestPart("tvSeriesDto") TVSeriesRequestDTO seriesRequestDTO,
            @RequestParam("file")MultipartFile file
            ){

        try {
            TVSeries tvSeries = tvSeriesService.createTVSeries(seriesRequestDTO, file);
            return ResponseEntity.status(201).body(tvSeries);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTVSeries(
            @PathVariable Long id,
            @RequestPart("tvSeriesDto") UpdateTVSeriesDto updateTVSeriesDto,
            @RequestParam(value="file", required = false) MultipartFile file
    ) {
        try {
            TVSeries updateTVSeries = tvSeriesService.updateTVSeries(id, updateTVSeriesDto, file);
            return ResponseEntity.ok(updateTVSeries);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTVSeries(@PathVariable Long id){
        tvSeriesService.deleteTVSeriesById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/genres")
    public ResponseEntity<List<String>> getAllGenres() {
        try {
            List<String> genres = tvSeriesService.getAllGenres();
            return ResponseEntity.ok(genres);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(List.of());
        }
    }
}
