package dev.ewd.mediashelf_spring.mediashelf_spring.service;

import dev.ewd.mediashelf_spring.mediashelf_spring.dto.requestDto.TVSeriesRequestDTO;
import dev.ewd.mediashelf_spring.mediashelf_spring.dto.updateDto.UpdateTVSeriesDto;
import dev.ewd.mediashelf_spring.mediashelf_spring.model.TVSeries;
import dev.ewd.mediashelf_spring.mediashelf_spring.repository.TVSeriesRepository;
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
public class TVSeriesService {

    @Autowired
    private TVSeriesRepository tvSeriesRepository;

    @Autowired
    private S3Service s3Service;

    public Page<TVSeries> getAllTVSeries(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        return tvSeriesRepository.findAll(pageable);
    }

    private TVSeries getTVSeriesById(Long id){
        return tvSeriesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("TVSeries not found with id: " + id));
    }

    public List<TVSeries> findTVSeriesByTitle (String title){
        List<TVSeries> tvSeries = tvSeriesRepository.findByTitleContainingIgnoreCase(title);
        if(tvSeries.isEmpty()) {
            throw new RuntimeException("No TVSeries found with title containing: " + title);
        }
        return tvSeries;
    }

    public TVSeries createTVSeries(TVSeriesRequestDTO dto, MultipartFile file){
        if(tvSeriesRepository.findByTitle(dto.title()).isPresent())
            throw new RuntimeException("A TVSeries with the title " + dto.title() + "is already in the database");
        try {
            String posterUrl = s3Service.uploadPoster(file, "tvseries");
            TVSeries tvSeries = new TVSeries(
                    dto.title(),
                    dto.description(),
                    dto.startYear(),
                    dto.endYear(),
                    dto.rating(),
                    dto.genres(),
                    dto.numberOfSeasons(),
                    dto.episodesPerSeason(),
                    dto.creator(),
                    dto.castOfActors(),
                    dto.network(),
                    dto.isOngoing(),
                    posterUrl
            );
            return tvSeriesRepository.save(tvSeries);
        } catch(Exception e) {
            throw new RuntimeException("Error while creating the tvseries: " + e.getMessage());
        }
    }

    public TVSeries updateTVSeries(Long id, UpdateTVSeriesDto dto, MultipartFile file) {
        TVSeries existingTVSeries = getTVSeriesById(id);

        if (dto.title() != null && tvSeriesRepository.findByTitle(dto.title())
                .filter(tvSeries -> !tvSeries.getId().equals(id))
                .isPresent()) {
            throw new RuntimeException("A TVSeries with the title '" + dto.title() + "' already exists.");
        }

        if(dto.title() != null) existingTVSeries.setTitle(dto.title());
        if(dto.description() != null) existingTVSeries.setDescription(dto.description());
        if(dto.startYear() != null) existingTVSeries.setStartYear(dto.startYear());
        if(dto.endYear() != null) existingTVSeries.setEndYear(dto.endYear());
        if(dto.rating() != null) existingTVSeries.setRating(dto.rating());
        if(dto.genres() != null) existingTVSeries.setGenres(dto.genres());
        if(dto.numberOfSeasons() != null) existingTVSeries.setNumberOfSeasons(dto.numberOfSeasons());
        if(dto.episodesPerSeason() != null) existingTVSeries.setEpisodesPerSeason(dto.episodesPerSeason());
        if(dto.creator() != null) existingTVSeries.setCreator(dto.creator());
        if(dto.castOfActors() != null) existingTVSeries.setCastOfActors(dto.castOfActors());
        if(dto.network() != null) existingTVSeries.setNetwork(dto.network());
        if(dto.isOngoing() != null) existingTVSeries.setOngoing(dto.isOngoing());
        if(dto.posterUrl() != null) existingTVSeries.setPosterUrl(dto.posterUrl());

        if (file != null && !file.isEmpty()) {
            try {
                String  newPosterUrl = s3Service.uploadPoster(file, "movies");
                existingTVSeries.setPosterUrl(newPosterUrl);
            } catch (IOException e) {
                throw new RuntimeException("Error while uploading the new poster: ", e);
            }
        }
        return tvSeriesRepository.save(existingTVSeries);
    }

    public void deleteTVSeriesById(Long id) {
        TVSeries tvSeries = getTVSeriesById(id);
        tvSeriesRepository.delete(tvSeries);
    }

    public Page<TVSeries> filterTVSeriesByGenres(List<String> genres, int page, int size) {
        if (genres == null || genres.isEmpty())
            throw new IllegalArgumentException("Genres list cannot be null or empty.");
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        return tvSeriesRepository.findByGenres(genres, pageable);
    }

    public List<String> getAllGenres() {
        return tvSeriesRepository.findDistinctGenres();
    }
}
