package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Film {

    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private int rate = 0;
    private List<Genre> genres = new ArrayList<>();
    private Mpa mpa = new Mpa();

    public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration, int rate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
    }

    public void setGenre(Integer id, String name) {
        Genre genre = new Genre();
        genre.setId(id);
        genre.setName(name);
        genres.add(genre);
    }

    public void setGenreId(Integer id) {
        Genre genre = new Genre();
        genre.setId(id);
        genres.add(genre);
    }


    public void setMpaId(Integer id) {
        mpa.setId(id);
    }

    public void setMpaName(String name) {
        mpa.setName(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return Objects.equals(id, film.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Film{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
