package dev.ewd.mediashelf_spring.mediashelf_spring.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
public class TVSeries {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private int startYear;
    private int endYear;
    private double rating;

    @ElementCollection
    private List<String> genres;

    private int numberOfSeasons;
    private int episodesPerSeason;
    private String creator;

    @ElementCollection
    @Column(name ="cast_of_actors")
    private List<String> castOfActors;

    private String network;
    private boolean isOngoing;

    @ManyToMany
    private List<MediaCollection> collections = new ArrayList<>();

    private String posterUrl;
    LocalDateTime createdAt;

    public TVSeries() {
    }

    public TVSeries(
            String title,
            String description,
            int startYear,
            int endYear,
            double rating,
            List<String> genres,
            int numberOfSeasons,
            int episodesPerSeason,
            String creator,
            List<String> castOfActors,
            String network,
            boolean isOngoing,
            String posterUrl
    ) {
        this.title = title;
        this.description = description;
        this.startYear = startYear;
        this.endYear = endYear;
        this.rating = rating;
        this.genres = genres;
        this.numberOfSeasons = numberOfSeasons;
        this.episodesPerSeason = episodesPerSeason;
        this.creator = creator;
        this.castOfActors = castOfActors;
        this.network = network;
        this.isOngoing = isOngoing;
        this.posterUrl = posterUrl;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public void setNumberOfSeasons(int numberOfSeasons) {
        this.numberOfSeasons = numberOfSeasons;
    }

    public int getEpisodesPerSeason() {
        return episodesPerSeason;
    }

    public void setEpisodesPerSeason(int episodesPerSeason) {
        this.episodesPerSeason = episodesPerSeason;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public List<String> getCastOfActors() {
        return castOfActors;
    }

    public void setCastOfActors(List<String> castOfActors) {
        this.castOfActors = castOfActors;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public boolean isOngoing() {
        return isOngoing;
    }

    public void setOngoing(boolean ongoing) {
        isOngoing = ongoing;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public List<MediaCollection> getCollections() {
        return collections;
    }

    public void setCollections(List<MediaCollection> collections) {
        this.collections = collections;
    }
}
