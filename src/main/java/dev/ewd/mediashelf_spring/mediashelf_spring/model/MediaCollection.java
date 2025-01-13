package dev.ewd.mediashelf_spring.mediashelf_spring.model;

import dev.ewd.mediashelf_spring.mediashelf_spring.model.enums.MediaCollectionType;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class MediaCollection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private MediaCollectionType type;

    private Long ownerId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(
            name = "collection_movies",
            joinColumns = @JoinColumn(name = "collection_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"),
            uniqueConstraints = {
                    @UniqueConstraint(columnNames = {"collection_id", "movie_id"})
            }
    )
    private List<Movie> movies = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "collection_tv_series",
            joinColumns = @JoinColumn(name = "collection_id"),
            inverseJoinColumns = @JoinColumn(name = "tv_series_id"),
            uniqueConstraints = {
                    @UniqueConstraint(columnNames = {"collection_id", "tv_series_id"})
            }
    )
    private List<TVSeries> tvSeries = new ArrayList<>();

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

    public MediaCollection() {}

    public MediaCollection(
            String name,
            String description,
            MediaCollectionType type,
            Long ownerId,
            List<Movie> movies,
            List<TVSeries> tvSeries
    ) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.ownerId = ownerId;
        this.movies = movies;
        this.tvSeries = tvSeries;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MediaCollectionType getType() {
        return type;
    }

    public void setType(MediaCollectionType type) {
        this.type = type;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    public List<TVSeries> getTvSeries() {
        return tvSeries;
    }

    public void setTvSeries(List<TVSeries> tvSeries) {
        this.tvSeries = tvSeries;
    }

    @Override
    public String toString() {
        return "MediaCollection{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", ownerId=" + ownerId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", movies=" + movies +
                ", tvSeries=" + tvSeries +
                '}';
    }
}
