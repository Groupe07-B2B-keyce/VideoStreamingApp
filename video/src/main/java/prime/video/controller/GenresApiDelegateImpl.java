package prime.video.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import prime.video.api.GenresApiDelegate;
import prime.video.model.Genre;
import prime.video.repository.GenreRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenresApiDelegateImpl implements GenresApiDelegate {

    private final GenreRepository genreRepository;

    public GenresApiDelegateImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public ResponseEntity<List<Genre>> listGenres() {
        List<Genre> genres = genreRepository.findAll().stream()
                .map(this::mapToModel)
                .collect(Collectors.toList());
        return new ResponseEntity<>(genres, HttpStatus.OK);
    }

    private Genre mapToModel(prime.video.domain.Genre domain) {
        Genre model = new Genre();
        model.setId(domain.getId());
        model.setName(domain.getName());
        model.setSlug(domain.getSlug());
        return model;
    }
}
