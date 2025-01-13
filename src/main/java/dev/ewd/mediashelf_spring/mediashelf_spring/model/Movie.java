package dev.ewd.mediashelf_spring.mediashelf_spring.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private int releaseYear;
    private double rating;

    @ElementCollection
    private List<String> genres;

    private int duration;
    private String director;

    @ElementCollection
    @Column(name ="cast_of_actors")
    private List<String> castOfActors;

    private String productionStudio;
    private String posterUrl;

    @ManyToMany
    private List<MediaCollection> collections;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Movie() {}

    public Movie(
            Long id,
            String title,
            String description,
            int releaseYear,
            double rating,
            List<String> genres,
            int duration,
            String director,
            List<String> castOfActors,
            String productionStudio,
            String posterUrl,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.rating = rating;
        this.genres = genres;
        this.duration = duration;
        this.director = director;
        this.castOfActors = castOfActors;
        this.productionStudio = productionStudio;
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

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public List<String> getCastOfActors() {
        return castOfActors;
    }

    public void setCastOfActors(List<String> castOfActors) {
        this.castOfActors = castOfActors;
    }

    public String getProductionStudio() {
        return productionStudio;
    }

    public void setProductionStudio(String productionStudio) {
        this.productionStudio = productionStudio;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
